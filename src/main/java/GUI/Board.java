package GUI;

import javax.swing.*;

import Constants.BoardSize;
import Constants.LevelSpeeds;
import Shapes.Shape;
import Shapes.Shape.Tetrominoes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    private Tetris parent;
    private Timer timer;

    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;

    private int numLinesRemoved = 0;
    private int currentLevel = 1;

    private int curX = 0;
    private int curY = 0;

    private JLabel statusBar;

    private Shape curPiece;
    private Tetrominoes[] board;

    public Board(Tetris p) {
        setFocusable(true);

        this.parent = p;
        statusBar = parent.getStatusBar();

        curPiece = new Shape();

        timer = new Timer(LevelSpeeds.getLevel(currentLevel), this);
        board = new Tetrominoes[BoardSize.WIDTH * BoardSize.HEIGHT];

        addKeyListener(new TetrisKeyListener(this));

        setBackground(Color.CYAN.darker());
    }

    //Getters
    public boolean isPaused(){
        return isPaused;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public Shape getCurPiece(){
        return curPiece;
    }

    //Altering State Methods

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

    /**
     * Increases current level and updates Timer delay correspondingly
     */
    private void updateLevel(){
        currentLevel++; //Increases current level
        int delay = LevelSpeeds.getLevel(currentLevel); //Gets from LevelSpeeds (Constant) the delay according to the level
        timer.setDelay(delay); //Updates delay
    }

    public int squareWidth() {
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

    public void actionPerformed(ActionEvent ae) {
        if(isFallingFinished) {
            isFallingFinished = false;
            newPiece(); //Creates new piece to drop
        }
        else {
            oneLineDown(); //Drops down current Tetromino one line
        }
    }


    public void drawSquare(Graphics g, int x, int y, Tetrominoes shape){
        Color color = shape.color;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        //Paints small gradient between the Tetromino and its border
        g.setColor(color.brighter()); //Makes soft
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        //Paints border of tetromino
        g.setColor(Color.BLACK);
        g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardSize.HEIGHT * squareHeight();

        //Starts scanning whole board to paint
        for(int i = 0; i < BoardSize.HEIGHT; i++){
            for(int j = 0; j < BoardSize.WIDTH; j++){
                Tetrominoes shape = shapeAt(j, BoardSize.HEIGHT - i - 1); //Gets current shape to draw

                if(shape != Tetrominoes.NoShape){ //Only paints if there's a Tetromino
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        //Paint current Tetromino on Top

        if(curPiece.getShape() != Tetrominoes.NoShape) { //Only paints if it has a defined shape
            for(int i = 0; i < 4; i++){
                int x = curX + curPiece.getX(i);
                int y = curY - curPiece.getY(i);
                drawSquare(g, x * squareWidth(), boardTop + (BoardSize.HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
            }
        }
    }

    //Logic Methods

    public void tryMoveLeft(){
        tryMove(curPiece, curX - 1, curY);
    }

    public void tryMoveRight(){
        tryMove(curPiece, curX + 1, curY);
    }

    public void tryRotateLeft(){
        tryMove(curPiece.rotateLeft(), curX, curY);
    }

    public void tryRotateRight(){
        tryMove(curPiece.rotateRight(), curX, curY);
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

    public void dropDown() {
        int newY = curY;

        while (newY > 0) {
            if(!tryMove(curPiece, curX, newY - 1)) break;

            --newY;
        }

        pieceDropped();

    }

    public void oneLineDown() {
        if(!tryMove(curPiece, curX, curY - 1)){
            pieceDropped();
        }
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BoardSize.HEIGHT - 1; i >= 0; --i){
            boolean lineIsFull = true;

            for(int j = 0; j < BoardSize.WIDTH; ++j) {
                if(shapeAt(j, i) == Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if(lineIsFull) {
                ++numFullLines;

                for(int k = i; k < BoardSize.HEIGHT - 1; ++k){
                    for(int j = 0; j < BoardSize.WIDTH; ++j) {
                        board[k * BoardSize.WIDTH + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if(numFullLines > 0){
            numLinesRemoved += numFullLines;
            statusBar.setText(String.valueOf(numLinesRemoved));
            if(numLinesRemoved % 10 == 0){
                updateLevel();
            }
            isFallingFinished = true;
            curPiece.setShape(Tetrominoes.NoShape);
            repaint();
        }

    }

}
