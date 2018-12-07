package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Class that contains a Tetris Board and a Side Panel that
 * displays the game information and the controls required
 * to play.
 *
 * Has added sound effects to show the user that a certain
 * movement is made, like rotating the current Tetromino
 * or clearing one or many lines, and has added information
 * that displays the current number of lines that have been
 * cleared and the current score that the player has achieved.
 *
 *The game generates emotions with the usage of the Sounds,
 * Time, Score, and Threats challenges.
 * The game tests the player by incrementing the level depending on
 * the number of lines that it has cleared, and displays the current
 * Score that he/she has achieved, challenging his/her reaction time
 * skills. The sounds are used to indicate lines have been cleared and
 * indicate a reward for the user, giving motivation. The threats on this
 * game are represented by the uncertainties of placing a Tetromino on
 * a certain spot and how this choice can affect further into the game.
 */

public class Tetris extends JFrame {

    private JLabel statusBar;
    private SidePanel s;
    private Board b;

    public Tetris() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Get current screen size

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
        setSize(1000, screenSize.height - 50);
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
