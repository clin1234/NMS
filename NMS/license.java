package NMS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class info
{
    static final String nl = System.lineSeparator();
    static final String copyright = "Copyright (C) 2020 Charlie Lin";
    static final String notice = 
            "NMS is free software: you can redistribute it and/or modify"+nl+
            "it under the terms of the GNU General Public License as published by"+nl+
            "the Free Software Foundation, either version 3 of the License, or"+nl+
            "(at your option) any later version."+nl+
            nl+
            "This program is distributed in the hope that it will be useful,"+nl+
            "but WITHOUT ANY WARRANTY; without even the implied warranty of"+nl+
            "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the"+nl+
            "GNU General Public License for more details."+nl+
            nl+
            "You should have received a copy of the GNU General Public License"+nl+
            "along with this program.  If not, see <https://www.gnu.org/licenses/>."+nl+
            nl
    ;
}

public class license {
    public static void main(final String[] a) {
        EventQueue.invokeLater(() -> {
            final frame frame = new frame();
            // frame.setLayout(new BoxLayout());
            frame.setTitle("License");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(500, 600));
            // frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}

class frame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 3127333301832710246L;

    private JScrollPane license_text() {

        URL url;
        try {
            url = new URL("https://www.gnu.org/licenses/gpl-3.0-standalone.html");
            try (Scanner s = new Scanner(url.openStream())) {
                final JTextArea license = new JTextArea(s.toString(), 8, 26);
                final JScrollPane l = new JScrollPane(license);
                return l;
            } catch (final IOException e) {

                e.printStackTrace();
            }
        } catch (final MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return null;
    }

    public frame() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalBox());

        final JTextField copyright = new JTextField(info.copyright);
        copyright.setEditable(false);

        final var notice = new JTextArea(info.notice);
        notice.setEditable(false);
        notice.setLineWrap(true);
        notice.setWrapStyleWord(true);

        panel.add(copyright);
        panel.add(notice);
        panel.validate();
        panel.setVisible(true);
    }
}