package NMS;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class menu {
    public JMenuBar makemenu() {
        JMenuBar bar;
        bar = new JMenuBar();

        var file = new JMenu("File");
        var exit = new JMenuItem("Exit", 'x');
        file.add(exit);

        var help_menu = new JMenu("Help");
        var about = new JMenuItem("About",'A');
        var help = new JMenuItem("Instructions");
        help.setAccelerator(KeyStroke.getKeyStroke('H'));
        help_menu.add(about);
        help_menu.add(help);

        var game = new JMenu("Game");
        JMenu opt = new JMenu("New Game");
        opt.setMnemonic('N');
        var sin = new JMenuItem("Single",'S');
        var mul = new JMenuItem("Multiplayer",'M');
        opt.add(sin);
        opt.add(mul);
        var save_game = new JMenuItem("Save Game",'S');
        game.add(opt);
        game.add(save_game);

        bar.add(file);
        bar.add(game);
        bar.add(help_menu);

        return bar;
    }

}