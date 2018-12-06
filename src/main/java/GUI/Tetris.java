package GUI;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private JLabel statusBar;
    private SidePanel s;
    private Board b;

    public Tetris() {
        statusBar = new JLabel("");
        statusBar.setForeground(Color.WHITE);
        statusBar.setFont(new Font("Arial", Font.BOLD, 30));

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(49, 62, 129));


        add(statusBar, BorderLayout.SOUTH);

        Board board = new Board(this);
        b = board;

        SidePanel side = new SidePanel(this);
        s = side;

        b.setConfigs(s);

        add(b, BorderLayout.CENTER);
        add(s, BorderLayout.EAST);

        b.start();
        setSize(1000, 900);
        setResizable(false);
        setTitle("Tetris!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar(){
        return statusBar;
    }

    public Board getBoard(){
        return b;
    }

}
