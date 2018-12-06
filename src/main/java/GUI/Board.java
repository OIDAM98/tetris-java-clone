package GUI;

import javax.swing.*;

import Constants.BoardSize;
import Constants.LevelSpeeds;
import Constants.Sounds;
import Shapes.Shape;
import Shapes.Shape.Tetrominoes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.IntStream;

public class Board extends JPanel implements ActionListener {

    private Tetris parent;
    private Timer timer;

    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;

    private int numLinesRemoved = 0;
    private int currentLevel = 1;
    private int currentScore = 0;

    private int curX = 0;
    private int curY = 0;

    private JLabel statusBar;

    private Shape curPiece;
    private Tetrominoes[] board;
    private SidePanel configs;

    public Board(Tetris p) {
        setFocusable(true);

        this.parent = p;
        statusBar = parent.getStatusBar();
        statusBar.setBackground(new Color(49, 62, 129));

        curPiece = new Shape();

        timer = new Timer(LevelSpeeds.getLevel(currentLevel), this);
        board = new Tetrominoes[BoardSize.WIDTH * BoardSize.HEIGHT];

        addKeyListener(new TetrisKeyListener(this));

        setBackground(Color.DARK_GRAY);
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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNumLinesRemoved(){
        return numLinesRemoved;
    }

    public int getCurrentScore(){
        return currentScore;
    }

    //Setters

    public void setConfigs(SidePanel configs) {
        this.configs = configs;
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
        Sounds.playStart();

        statusBar.setText("");

        configs.updateLines();
        configs.updateCurrentScore();
        configs.updateLevel();
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
            statusBar.setText("");
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
        configs.updateLevel();
    }

    /**
     * Updates the current score depending on the lines cleared
     * Formula:
     *  1.- score = 40 * currentLevel
     *  2.- score = 100 * currentLevel
     *  3.- score = 300 * currentLevel
     *  4.- score = 1200 * currentLevel
     * @param n number of lines removed
     */

    private void updateScore(int n){
        int score = 0;
        switch (n){
            case 1:
                score = 40 * currentLevel;
                break;
            case 2:
                score = 100 * currentLevel;
                break;
            case 3:
                score = 300 * currentLevel;
                break;
            case 4:
                score = 1200 * currentLevel;
                break;

        }

        currentScore += score; //Updates score
        configs.updateCurrentScore(); //Visually updates score

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

    /**
     * Creates new Tetromino at the center of the Board to start falling down
     */

    public void newPiece() {
        curPiece.setRandomShape();
        curX = BoardSize.WIDTH / 2 + 1;
        curY = BoardSize.HEIGHT - 1 + curPiece.minY();

        /*
            Checks for Game Over:
            Only GameOver if Tetromino cannot be moved one line down
            when it appears. => Board Full!
         */
        if(!tryMove(curPiece, curX, curY - 1)){
            curPiece.setShape(Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
            statusBar.setText("Game Over!");
            Sounds.playOver();
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


    /**
     * Draws part of a Tetromino, a square, depending on its Shape and its x,y position on the board
     * @param g Graphics component where to paint
     * @param x Coordinate X of Tetromino
     * @param y Coordinate Y of Tetromino
     * @param shape Shape of Tetromino
     */
    public void drawSquare(Graphics g, int x, int y, Tetrominoes shape){
        Color color = shape.color;
        g.setColor(color);
        //Make rectangle of Tetromino
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        //Paints small gradient between the Tetromino and its border
        g.setColor(color.brighter()); //Makes soft
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        //Paints border of tetromino
        g.setColor(color.darker());
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
        Sounds.playRotate();
    }

    public void tryRotateRight(){
        tryMove(curPiece.rotateRight(), curX, curY);
        Sounds.playRotate();
    }

    /**
     * Tries to move a specified Tetromino to a new x,y position.
     * @param piece Tetromino to move
     * @param newX Coordinate X to move to
     * @param newY Coordinate Y to move to
     * @return returns if the move is possibe to make
     */

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

    /**
     * Moves current Tetromino one line below
     */

    public void oneLineDown() {
        if(!tryMove(curPiece, curX, curY - 1)){
            pieceDropped();
        }
    }

    /**
     * Removes full lines made in the Board.
     *  Scans the Board to find them, deletes them and overlaps
     *  current shape with the one found above.
     */

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

        //Plays sound depending on the number of lines removed

        if(numFullLines > 0){

            switch (numFullLines) {
                case 1:
                    Sounds.playClearLine();
                    break;
                case 2:
                    Sounds.playClearTwo();
                    break;
                case 3:
                    Sounds.playClearThree();
                    break;
                case 4:
                    Sounds.playClearTetris();
                    break;
                default:
                    break;
            }

            updateScore(numFullLines);

            //Makes a range  of 1's for numberOfLinesCleared
            int[] range = IntStream.range(0, numFullLines).map(v -> 1).toArray();


            //Sums values of range to numberOfLinesRemoved
            for(int sum : range){
                numLinesRemoved += sum;
                if(numLinesRemoved % 10 == 0){ //Check if numOfLinesRemoved has reached a multiple of 10 to increase Level
                    updateLevel();
                }
            }

            configs.updateLines();

            isFallingFinished = true;

            curPiece.setShape(Tetrominoes.NoShape);

            repaint();
        }

    }

}
