package GUI;

import Shapes.Shape;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisKeyListener extends KeyAdapter {

    private Board board;

    public TetrisKeyListener(Board b){
        board = b;
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(!board.isStarted() || board.getCurPiece().getShape() == Shape.Tetrominoes.NoShape)
            return;

        int keyCode = ke.getKeyCode();

        if(keyCode == 'p' || keyCode == 'P')
            board.pause();

        if(board.isPaused()) return;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                board.tryMoveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                board.tryMoveRight();
                break;
            case KeyEvent.VK_DOWN:
                board.tryRotateRight();
                break;
            case KeyEvent.VK_UP:
                board.tryRotateLeft();
                break;
            case KeyEvent.VK_SPACE:
                board.dropDown();
                break;
            case 'd':
            case 'D':
                board.oneLineDown();
                break;
        }

    }


}
