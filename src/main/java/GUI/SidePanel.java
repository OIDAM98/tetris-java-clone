package GUI;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    private JLabel level;
    private JLabel score;
    private JLabel lines;

    private final Font lFont = new Font("Arial", Font.PLAIN, 32);
    private final Font sFont = new Font("Arial", Font.BOLD, 30);
    private final Font bFont = new Font("Arial", Font.PLAIN, 36);
    private final Font cFont = new Font("Arial", Font.PLAIN, 19);

    private final Color backColor = new Color(49, 62, 129);
    private final Color butColor = new Color(99,48,122);

    private final String[] configs = new String[]
            {
                    "LEFT Arrow -> Move Left",
                    "RIGHT Arrow -> Move Right",
                    "UP Arrow -> Rotate ClockWise",
                    "DOWN Arrow -> Rotate CounterClockWise",
                    "P -> Pause Game",
                    "D -> Soft Drop",
                    "SPACE -> Hard Drop"
            };

    private JButton start;
    private JButton pause;
    private JButton end;

    private Board b;

    public SidePanel( Tetris parent){
        setPreferredSize(new Dimension(400, parent.getHeight()));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        b = parent.getBoard();
        setBackground(backColor);
        generatePanel();
    }

    private void generatePanel(){
        JLabel temp;

        temp = new JLabel("LEVEL");
        temp.setFont(lFont);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        level = new JLabel(String.valueOf(b.getCurrentLevel()));
        level.setFont(sFont);
        level.setForeground(Color.WHITE);
        level.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(level);

        add(Box.createVerticalGlue());

        temp = new JLabel("SCORE");
        temp.setFont(lFont);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        score = new JLabel(String.valueOf(b.getCurrentScore()));
        score.setFont(sFont);
        score.setForeground(Color.WHITE);
        score.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(score);

        add(Box.createVerticalGlue());

        temp = new JLabel("LINES CLEARED");
        temp.setFont(lFont);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        lines = new JLabel(String.valueOf(b.getNumLinesRemoved()));
        lines.setFont(sFont);
        lines.setForeground(Color.WHITE);
        lines.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(lines);

        start = new JButton("Start Game!");
        start.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        start.setFont(bFont);
        start.addActionListener(e -> {
            if(!b.isStarted()){
                b.start();
            }
        });
        start.setBackground(butColor);
        start.setForeground(Color.WHITE);
        start.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        start.setFocusable(false);

        pause = new JButton("Pause Game!");
        pause.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        pause.setFont(bFont);
        pause.addActionListener(e -> b.pause() );
        pause.setBackground(butColor);
        pause.setForeground(Color.WHITE);
        pause.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        pause.setFocusable(false);

        end = new JButton("Exit Game!");
        end.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        end.setFont(bFont);
        end.addActionListener( e -> System.exit(0) );
        end.setBackground(butColor);
        end.setForeground(Color.WHITE);
        end.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        end.setFocusable(false);

        add(Box.createVerticalGlue());

        add(start);
        add(pause);
        add(end);

        add(Box.createVerticalGlue());

        temp = new JLabel("BUTTONS:");
        temp.setFont(lFont);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        JPanel map = new JPanel();
        map.setLayout(new BoxLayout(map, BoxLayout.Y_AXIS));
        map.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        map.setBackground(backColor);
        map.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        map.setFocusable(false);
        for(String conf : configs){
            JLabel con = new JLabel(conf);
            con.setFont(cFont);
            con.setForeground(Color.WHITE);
            map.add(con);
        }

        add(map);

        add(Box.createVerticalGlue());

    }

    public void updateLevel(){
        level.setText(String.valueOf(b.getCurrentLevel()));
    }

    public void updateCurrentScore(){
        score.setText(String.valueOf(b.getCurrentScore()));
    }

    public void updateLines(){
        lines.setText(String.valueOf(b.getNumLinesRemoved()));
    }

    public void updateStatus(){

    }

}
