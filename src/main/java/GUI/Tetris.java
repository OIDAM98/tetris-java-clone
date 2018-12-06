package GUI;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private JLabel statusBar;
    private SidePanel s;
    private Board b;

    public Tetris() {
        statusBar = new JLabel("0");
        setLayout(new BorderLayout());
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

    public SidePanel getSidePanel(){
        return s;
    }

    public Board getBoard(){
        return b;
    }

}
