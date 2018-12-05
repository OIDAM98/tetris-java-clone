package GUI;

import Shapes.Shape;

import javax.swing.*;
import java.awt.*;

public class NextPiece extends JPanel {

    private Shape next;
    private Board board;

    public NextPiece(){
        setPreferredSize(new Dimension(50, 50));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public void setNext(Shape n){
        next = n;
        repaint();
    }

    public void setBoard(Board b){
        board = b;
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoes shape){
        Color color = shape.color;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, board.squareWidth() - 2, board.squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + board.squareHeight() - 1, x, y);
        g.drawLine(x, y, x + board.squareWidth() - 1, y);
        g.setColor(Color.BLACK);
        g.drawLine(x + 1, y + board.squareHeight() - 1, x + board.squareWidth() - 1, y + board.squareHeight() - 1);
        g.drawLine(x + board.squareWidth() - 1, y + board.squareHeight() - 1, x + board.squareWidth() - 1, y + 1);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        int top = (int) getSize().getHeight();

        for(int i = 0; i < (int) getSize().getHeight(); i++){
            for(int j = 0; j < (int) getSize().getWidth(); j++) {
                drawSquare(g, j * board.squareWidth(), top + i * board.squareHeight(), next.getShape());
            }
        }
    }

}
