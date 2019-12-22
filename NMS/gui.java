package NMS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class gui {

    public static void main(String[] args) {
        new gui();
    }

    public gui() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch(ClassNotFoundException | InstantiationException |
            IllegalAccessException | UnsupportedLookAndFeelException e)
            {

            }

            NMS.menu m = new NMS.menu();

            JFrame frame = new JFrame("NMS");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setJMenuBar(m.makemenu());
            frame.setLayout(new BorderLayout());
            frame.add(new TestPane());
            //frame.add(new stat());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    };
}

/*class stat extends JPanel
{
    
    private static final long serialVersionUID = 1L;

    public stat()
    {

    JPanel panel = new JPanel();
    JTextField stat = new JTextField("Number of mines");
    stat.setEditable(false);
    panel.add(stat);
    }
}*/

class TestPane extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TestPane() {
        var grid = new GridBagLayout();
        setLayout(grid);

        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < 5+2; row++) {
            for (int col = 0; col < 5+2; col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPane cellPane = new CellPane();
                Border border;
                if (row < 4+2) {
                    if (col < 4+2) {
                        border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                    }
                } else {
                    if (col < 4+2) {
                        border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                    } else {
                        border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                    }
                }
                cellPane.setBorder(border);
                add(cellPane, gbc);
            }
        }
    }
}

class CellPane extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Color defaultBackground;

    public CellPane() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                defaultBackground = getBackground();
                setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackground);
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
}