package GUI;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    public NextPiece next;
    private JLabel level;

    public SidePanel(NextPiece n, Tetris parent){
        next = n;
        setPreferredSize(new Dimension(100, parent.getHeight()));
        setLayout(new FlowLayout());
        generatePanel();
    }

    private void generatePanel(){
        //add(next);


    }

    public NextPiece getNextPiece(){
        return next;
    }

}
