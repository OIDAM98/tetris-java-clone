package GUI;

import javax.swing.*;

import Constants.BoardSize;
import Shapes.Shape;
import Shapes.Shape.Tetrominoes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusBar;
    private Shapes.Shape curPiece;
    private Tetrominoes[] board;

    public Board(Tetris parent) {
        setFocusable(true);
        curPiece = new Shapes.Shape();
        timer = new Timer(400, this);
        statusBar = parent.getStatusBar();
        board = new Tetrominoes[BoardSize.WIDTH * BoardSize.HEIGHT];
        addKeyListener(new TetrisKeyListener());
    }

    public int squareWidht() {
        return (int) getSize().getWidth() / BoardSize.WIDTH;
    }

    public int squareHeight() {
        return (int) getSize().getHeight() / BoardSize.HEIGHT;
    }

    public Tetrominoes shapeAt(int x, int y) {
        return board[y * BoardSize.WIDTH + x];
    }

    private void clearBoard() {
        for(int i = 0; i < BoardSize.HEIGHT * BoardSize.WIDTH; i++){
            board[i] = Tetrominoes.NoShape;
        }
    }

    private void pieceDropped() {
        for(int i = 0; i < 4; i++){
            int x = curX + curPiece.getX(i);
            int y = curY - curPiece.getY(i);
            board[y * BoardSize.WIDTH + x] = curPiece.getShape();
        }

        removeFullLines();

        if(!isFallingFinished){
            newPiece();
        }

    }

    public void newPiece() {
        curPiece.setRandomShape();
        curX = BoardSize.WIDTH / 2 + 1;
        curY = BoardSize.HEIGHT - 1 + curPiece.minY();

        if(!tryMove(curPiece, curX, curY - 1)){
            curPiece.setShape(Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            statusBar.setText("Game Over!");
        }

    }

    private void oneLineDown() {
        if(!tryMove(curPiece, curX, curY - 1)){
            pieceDropped();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if(isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        }
        else {
            oneLineDown();
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoes shape){
        Color color = shape.color;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidht() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidht() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidht() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidht() - 1, y + squareHeight() - 1, x + squareWidht() - 1, y + 1);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardSize.HEIGHT * squareHeight();

        for(int i = 0; i < BoardSize.HEIGHT; i++){
            for(int j = 0; j < BoardSize.WIDTH; j++){
                Tetrominoes shape = shapeAt(j, BoardSize.HEIGHT - i - 1);

                if(shape != Tetrominoes.NoShape){
                    drawSquare(g, j * squareWidht(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        if(curPiece.getShape() != Tetrominoes.NoShape) {
            for(int i = 0; i < 4; i++){
                int x = curX + curPiece.getX(i);
                int y = curY - curPiece.getY(i);
                drawSquare(g, x * squareWidht(), boardTop + (BoardSize.HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
            }
        }
    }

    public void start(){
        if(isPaused) return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();
        newPiece();
        timer.start();
    }

    public void pause(){
        if(!isStarted) return;

        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
            statusBar.setText("Paused");
        }
        else {
            timer.start();
            statusBar.setText(String.valueOf(numLinesRemoved));
        }

        repaint();
    }

    private boolean tryMove(Shape piece, int newX, int newY){
        for(int i = 0; i < 4; i++){
            int x = newX + piece.getX(i);
            int y = newY - piece.getY(i);

            if(x < 0 || x >= BoardSize.WIDTH ||
                y < 0 || y >= BoardSize.HEIGHT) //If move is out of bounds
                return false;

            if(shapeAt(x , y) != Tetrominoes.NoShape) //If move hits another Tetrominoe
                return false;
        }

        curPiece = piece;
        curX = newX;
        curY = newY;

        repaint();

        return true;

    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BoardSize.HEIGHT - 1; i >= 0; --i){
            boolean lineIsFull = true;

            for(int j = 0; j < BoardSize.WIDTH; j++) {
                if(shapeAt(j, i) == Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if(lineIsFull) {
                ++numFullLines;

                for(int k = i; k < BoardSize.HEIGHT - 1; k++){
                    for(int j = 0; j < BoardSize.WIDTH; j++) {
                        board[k * BoardSize.WIDTH + j] = shapeAt(j, k + 1);
                    }
                }
            }

            if(numFullLines > 0){
                numLinesRemoved += numFullLines;
                statusBar.setText(String.valueOf(numLinesRemoved));
                isFallingFinished = true;
                curPiece.setShape(Tetrominoes.NoShape);
                repaint();
            }

        }

    }

    private void dropDown() {
        int newY = curY;

        while (newY > 0) {
            if(!tryMove(curPiece, curX, newY - 1)) break;

            --newY;
        }

        pieceDropped();

    }

    class TetrisKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            if(!isStarted || curPiece.getShape() == Tetrominoes.NoShape)
                return;

            int keyCode = ke.getKeyCode();

            if(keyCode == 'p' || keyCode == 'P')
                pause();

            if(isPaused) return;

            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case 'd':
                case 'D':
                    oneLineDown();
                    break;
            }

        }


    }

}
