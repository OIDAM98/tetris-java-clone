package Constants;

public class TetrominoesShapes {

    public final static int[][] NOSHAPE = new int[][] {{0,0}, {0,0}, {0,0}, {0,0} };
    public final static int[][] ZSHAPE = new int[][] {{0,-1}, {0,0}, {-1,0}, {-1,1}};
    public final static int[][] SSHAPE = new int[][] {{0,-1}, {0,0}, {1,0}, {1,1}};
    public final static int[][] LINESHAPE = new int[][] {{0,-1}, {0,0}, {0,1}, {0,2}};
    public final static int[][] TSHAPE = new int[][] {{-1,0}, {0,0}, {1,0} , {0,1}};
    public final static int[][] SQUARESHAPE = new int[][] {{0,0}, {1,0}, {0,1}, {1,1}};
    public final static int[][] LSHAPE = new int[][] {{-1,-1}, {0,-1}, {0,0}, {0,1}};
    public final static int[][] MIRRORLSHAPE = new int[][] {{1,-1}, {0,-1}, {0,0}, {0,1}};

}
