package NMS;

import static java.lang.Character.toLowerCase;
import static java.lang.System.out;

import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

import static NMS.info.*;

public class cons {
    private static final String nl = System.lineSeparator();
    static boolean multi = true, net = true;
    static Scanner input = new Scanner(System.in);
    final static String help_text = "h: Display this help message." + nl + "q: Quit the game." + nl
            + "d: Display the current board." + nl
            + "f: Forfeit; uncover the entire board." + nl
            + "v: Display copyright and version." + nl + nl + "Gameplay:" + nl
            + "Grid coordinates start at 0,0 at the top left of the board." + nl
            + "2,2: Uncovers cell 2,2, which is the third cell from the top and the third cell from the left." + nl
            + "f_1,9: Flags cell 1,9. Note that \"_\" indicates any number of whitespace." + nl;
    game g;

    public static void main(String[] a) {
        cons lol = new cons();
        lol.greet();
        lol.gloop();
    }

    public void greet() {
        out.print("Networked? [y/n] ");
        char[] responses = new char[4];
        responses[0] = input.next().charAt(0);
        out.print("Single or multiplayer? [s/m] ");
        responses[1] = input.next().charAt(0);
        out.print("Generate random grid or set manually? [r/m] ");
        responses[2] = input.next().charAt(0);
        switch (toLowerCase(responses[2])) {
        case 'r':
            g = new game(ThreadLocalRandom.current().nextInt(20), ThreadLocalRandom.current().nextInt(20),
                    ThreadLocalRandom.current().nextInt());
            break;
        case 'm':
            int[] grid_attr = new int[3];

            // XXX: assumes user is sensible enough...
            out.println("Rows? ");
            grid_attr[0] = input.nextInt();
            out.println("Columns? ");
            grid_attr[1] = input.nextInt();
            //boolean unchecked = true;
            while (true) {
                out.println("Mines? ");
                grid_attr[2] = input.nextInt();
                if (grid_attr[2] > 0 && (grid_attr[2] < grid_attr[0] * grid_attr[1]))
                    break;
            }
            g = new game(grid_attr[0], grid_attr[1], grid_attr[2]);
            break;
        default:
            g.game_in_progress = false;
            exit(0);
        }

        switch (toLowerCase(responses[0])) {
        case 'y':
            break; // No networking code yet
        case 'n':
            net = false;
            break;
        default:
            g.game_in_progress = false;
            exit(0);
        }
        switch (toLowerCase(responses[1])) {
        case 's':
            multi = false;
            break;
        case 'm':
            break;
        default:
            g.game_in_progress = false;
            exit(0);
        }
    }

    public void gloop() {
        Timer t = new Timer();
        String[] coor = new String[2];
        while (g.game_in_progress) {
            out.println();
            out.print("? ");
            String res = input.nextLine();
            if (res.length() > 1) {
                short x, y;
                if (res.charAt(0) == 'f') {
                    String[] tok = res.split("\\s+");
                    coor = tok[1].split(",");
                    x = Short.parseShort(coor[0]);
                    y = Short.parseShort(coor[1]);
                    if (g.grid[x][y].isCovered())
                        g.grid[x][y].setFlagged(true);
                } else {
                    coor = res.split(",");
                    x = Short.parseShort(coor[0]);
                    y = Short.parseShort(coor[1]);
                    if (g.grid[x][y].isCovered()) {
                        g.grid[x][y].setCovered(false);
                        if (g.grid[x][y].isMined()){
                            g.game_in_progress = false;
                            display_board();
                            restart();
                        }
                    }
                }
            } else if (res.length() == 1)
                disp_opt(res.charAt(0));
        }
    }

    private void disp_opt(char opt) {
        switch (toLowerCase(opt)) {
        case 'h':
            out.print(help_text);
            break;
        case 'q':
            g.game_in_progress = false;
            exit(0);
            break;
        case 'd':
            display_board();
            break;
        case 'v':
            out.println("Version " + game.version);
            out.println();
            out.println(copyright);
            out.println();
            out.print(notice);
            break;
        case 'f':
            g.game_in_progress = false;
            display_board();
            restart();
            break;
        }
    }

    private void display_board() {
        out.print(nl+g.toString());
    }

    private void restart()
    {
        out.print("Restart? [y/n] ");
            char res = input.next().charAt(0);
            if (toLowerCase(res) == 'n')
                exit(0);
            else
            {
                g.game_in_progress = false;
            }
    }
}