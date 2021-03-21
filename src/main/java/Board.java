import net.jcip.annotations.Immutable;

/*
You may assume that the constructor receives an n-by-n array containing the n2 integers
between 0 and n2 − 1, where 0 represents the blank square.
You may also assume that 2 ≤ n < 128.
 */
@Immutable
public class Board {
    private final int[][] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] inTiles) {
        validateN(inTiles.length);
        dimension = inTiles.length;
        tiles = deepCopy(inTiles);
    }

    private int[][] deepCopy(int[][] inTiles) {
        int[][] ints = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            if (inTiles[i].length != dimension) {
                throw new IllegalArgumentException();
            }
            System.arraycopy(inTiles[i], 0, ints[i], 0, dimension);
        }
        return ints;
    }

    private void validateN(int n) {
        if (n < 2 || n > 128) {
            throw new IllegalArgumentException();
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = dimension;
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    //The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
    public int hamming() {
        return 0;
    }

    //The Manhattan distance between a board and the goal board is the sum of the Manhattan distances
    //(sum of the vertical and horizontal distance) from the tiles to their goal positions.
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension * dimension - 1; i++) {
            int row = i / dimension;
            int col = i % dimension;
            if (tiles[row][col] != i+1) {
                return false;
            }
        }
        return isLastTileBlank();
    }

    private boolean isLastTileBlank() {
        return getLastTile() == 0;
    }

    private int getLastTile() {
        return tiles[dimension - 1][dimension - 1];
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {}
}
