package GUI;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    private JLabel level;
    private JLabel score;
    private JLabel lines;
    private JLabel image;

    private final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 32);
    private final Font STATE_FONT = new Font("Arial", Font.BOLD, 30);
    private final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 36);
    private final Font CONFIG_FONT = new Font("Arial", Font.PLAIN, 19);

    private final Color BACKGROUND_COLOR = new Color(49, 62, 129);
    private final Color BUTTON_COLOR = new Color(99,48,122);

    private final String[] CONFIGS = new String[]
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
        setPreferredSize(new Dimension(425, parent.getHeight()));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        b = parent.getBoard();

        setBackground(BACKGROUND_COLOR);
        generatePanel();
    }

    private void generatePanel(){
        ImageIcon img = new ImageIcon(ClassLoader.getSystemClassLoader().getResource("tetris2.png"));
        Image imgRes = img.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        image = new JLabel();
        image.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        image.setIcon(new ImageIcon(imgRes));
        add(image);

        add(Box.createVerticalGlue());

        JLabel temp;

        temp = new JLabel("LEVEL");
        temp.setFont(LABEL_FONT);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        level = new JLabel(String.valueOf(b.getCurrentLevel()));
        level.setFont(STATE_FONT);
        level.setForeground(Color.WHITE);
        level.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(level);

        add(Box.createVerticalGlue());

        temp = new JLabel("SCORE");
        temp.setFont(LABEL_FONT);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        score = new JLabel(String.valueOf(b.getCurrentScore()));
        score.setFont(STATE_FONT);
        score.setForeground(Color.WHITE);
        score.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(score);

        add(Box.createVerticalGlue());

        temp = new JLabel("LINES CLEARED");
        temp.setFont(LABEL_FONT);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        lines = new JLabel(String.valueOf(b.getNumLinesRemoved()));
        lines.setFont(STATE_FONT);
        lines.setForeground(Color.WHITE);
        lines.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(lines);

        start = new JButton("Start Game!");
        start.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        start.setFont(BUTTON_FONT);
        start.addActionListener(e -> {
            if(!b.isStarted()){
                b.start();
            }
        });
        start.setBackground(BUTTON_COLOR);
        start.setForeground(Color.WHITE);
        start.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        start.setFocusable(false);

        pause = new JButton("Pause Game!");
        pause.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        pause.setFont(BUTTON_FONT);
        pause.addActionListener(e -> b.pause() );
        pause.setBackground(BUTTON_COLOR);
        pause.setForeground(Color.WHITE);
        pause.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        pause.setFocusable(false);

        end = new JButton("Exit Game!");
        end.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        end.setFont(BUTTON_FONT);
        end.addActionListener( e -> System.exit(0) );
        end.setBackground(BUTTON_COLOR);
        end.setForeground(Color.WHITE);
        end.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        end.setFocusable(false);

        add(Box.createVerticalGlue());

        add(start);
        add(pause);
        add(end);

        add(Box.createVerticalGlue());

        temp = new JLabel("BUTTONS:");
        temp.setFont(LABEL_FONT);
        temp.setForeground(Color.WHITE);
        temp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(temp);

        JPanel map = new JPanel();
        map.setLayout(new BoxLayout(map, BoxLayout.Y_AXIS));
        map.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        map.setBackground(BACKGROUND_COLOR);
        map.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        map.setFocusable(false);
        for(String conf : CONFIGS){
            JLabel con = new JLabel(conf);
            con.setFont(CONFIG_FONT);
            con.setForeground(Color.WHITE);
            map.add(con);
        }

        add(map);

        add(Box.createVerticalGlue());

    }

    public synchronized void updateLevel(){
        level.setText(String.valueOf(b.getCurrentLevel()));
    }

    public synchronized void updateCurrentScore(){
        score.setText(String.valueOf(b.getCurrentScore()));
    }

    public synchronized void updateLines(){
        lines.setText(String.valueOf(b.getNumLinesRemoved()));
    }

}
