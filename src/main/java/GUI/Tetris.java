package GUI;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {

    private JLabel statusBar;
    private SidePanel s;

    public Tetris() {
        statusBar = new JLabel("0");
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        Board board = new Board(this);
        //NextPiece n = new NextPiece();
        //s = new SidePanel(n);
        add(board, BorderLayout.CENTER);
        //add(s, BorderLayout.EAST);
        board.start();
        setSize(500, 700);
        setTitle("Tetris!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JLabel getStatusBar(){
        return statusBar;
    }

    public SidePanel getSidePanel(){
        return s;
    }

}
