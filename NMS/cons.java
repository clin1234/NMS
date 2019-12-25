package NMS;

import static java.lang.Character.toLowerCase;
import static java.lang.System.out;
import static java.lang.System.err;

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
            + "d: Display the current board." + nl + "f: Forfeit; uncover the entire board." + nl
            + "v: Display copyright and version." + nl + nl + "Gameplay:" + nl
            + "Grid coordinates start at 0,0 at the top left of the board." + nl
            + "2,2: Uncovers cell 2,2, which is the third cell from the top and the third cell from the left." + nl
            + "f_1,9: Flags cell 1,9. Note that \"_\" indicates any number of whitespace. Using this option on an already flagged cell unflags it."
            + nl;
    static game g;

    public static void main(String[] a) {
        if (a.length > 1) {
            err.print("Usage: cons [vh]" + nl + "Options:" + nl + nl + "h: Prints help message." + nl
                    + "v: Prints version and license information.");
            exit(1);
        } else if (a.length == 1) {
            switch (a[0].charAt(0)) {
            case 'h':
                out.print(help_text);
                break;
            case 'v':
                out.println(game.version + nl);
                out.println(info.copyright + nl);
                out.print(info.notice);
                break;
            }
        } else {
            cons lol = new cons();
            lol.greet();
            lol.gloop();
        }
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
            int r = ThreadLocalRandom.current().nextInt(1, 20), c = ThreadLocalRandom.current().nextInt(1, 20),
                    m = ThreadLocalRandom.current().nextInt(1, r * c);
            g = new game(r, c, m);
            break;
        case 'm':
            int[] grid_attr = new int[3];

            // XXX: assumes user is sensible enough...
            out.print("Rows? ");
            int tmp = input.nextInt();
            if (is_more_than_0(tmp) == true)
                grid_attr[0] = tmp;
            out.print("Columns? ");
            tmp = input.nextInt();
            if (is_more_than_0(tmp) == true)
                grid_attr[1] = tmp;
            // boolean unchecked = true;
            while (true) {
                out.print("Mines? ");
                grid_attr[2] = input.nextInt();
                if (is_more_than_0(grid_attr[2]) && (grid_attr[2] < grid_attr[0] * grid_attr[1])) {
                    g = new game(grid_attr[0], grid_attr[1], grid_attr[2]);
                    break;
                }
            }
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
            if (g.allmines_revealed() == false) {
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
                        if (g.grid[x][y].isCovered() && g.check_coord(x, y)) {
                            if (g.grid[x][y].isFlagged())
                                g.grid[x][y].setFlagged(false);
                            else
                                g.grid[x][y].setFlagged(true);
                        }
                    } else {
                        coor = res.split(",");
                        x = Short.parseShort(coor[0]);
                        y = Short.parseShort(coor[1]);
                        if (g.check_coord(x, y)) {
                            if (g.grid[x][y].isCovered()) {
                                if (g.isuncovering_success(x, y) == false) {
                                    g.game_in_progress = false;
                                    display_board();
                                    restart();
                                }
                            }
                        }
                    }
                } else if (res.length() == 1)
                    disp_opt(res.charAt(0));
            } else {
                out.println("All mines revealed.");
                restart();
            }
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
            out.println(game.version);
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
        out.print(nl + g.toString());
    }

    private void restart() {
        out.print("Restart? [y/n] ");
        char res = input.next().charAt(0);
        if (toLowerCase(res) == 'n')
            exit(0);
        else {
            greet();
        }
    }

    private boolean is_more_than_0(int val) {
        if (val > 0)
            return true;
        else {
            err.println("Invalid value " + val);
            return false;
        }

    }
}