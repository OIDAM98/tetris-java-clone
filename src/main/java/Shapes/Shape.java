package Shapes;

import Constants.TetrominoesColors;
import Constants.TetrominoesShapes;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Shape {

    private Tetrominoes pieceShape;
    private int[][] coords;

    public enum Tetrominoes {
        NoShape(TetrominoesShapes.NOSHAPE, TetrominoesColors.NOSHAPE),
        ZShape(TetrominoesShapes.ZSHAPE, TetrominoesColors.ZSHAPE),
        SHape(TetrominoesShapes.SSHAPE, TetrominoesColors.SSHAPE),
        LineShape(TetrominoesShapes.LINESHAPE, TetrominoesColors.LINESHAPE),
        TShape(TetrominoesShapes.TSHAPE, TetrominoesColors.TSHAPE),
        SquareShape(TetrominoesShapes.SQUARESHAPE, TetrominoesColors.SQUARESHAPE),
        LShape(TetrominoesShapes.LSHAPE, TetrominoesColors.LSHAPE),
        MirroredLShape(TetrominoesShapes.MIRRORLSHAPE, TetrominoesColors.MIRRORLSHAPE);

        public int[][] coords;
        public Color color;

        Tetrominoes(int[][] coords, Color c){
            this.coords = coords;
            color = c;
        }
    }

    public Shape(){
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }

    public void setShape(Tetrominoes shape){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 2; j++){
                coords[i][j] = shape.coords[i][j];
            }
        }

        pieceShape = shape;

    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int getX(int index) {
        return coords[index][0];
    }

    public int getY(int index) {
        return coords[index][1];
    }

    public Tetrominoes getShape(){
        return pieceShape;
    }

    public void setRandomShape(){
        Random gen = new Random();
        int x = gen.nextInt(7) + 1;
        Tetrominoes[] values = Tetrominoes.values();
        setShape(values[x]);
    }

    public int minX() {
        return Arrays.stream(coords) //Get Stream of Coords
                .map(arr -> arr[0]) //get X values of Coords
                .mapToInt(v -> v) //Convert to IntStream
                .min() //Call "min" value of Stream (Returns Optional)
                .getAsInt(); //Return as int (Unpacks Optional)
    }

    public int minY(){
        return Arrays.stream(coords) //Get Stream of Coords
                .map(arr -> arr[1]) //Get Y values of Coords
                .mapToInt(v -> v) //Convert to IntStream
                .min() //Call "min" value of Stream (Returns Optional)
                .getAsInt(); //Return as int (Unpacks Optional)

    }

    public Shape rotateLeft() {
        if(pieceShape == Tetrominoes.SquareShape) return this;
        Shape result = new Shape();

        result.pieceShape = pieceShape;

        for(int i = 0; i < 4; i++){
            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }

        return result;

    }

    public Shape rotateRight() {
        if(pieceShape == Tetrominoes.SquareShape) return this; //No need to rotate square

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++){
            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }

        return result;

    }

}

