import net.jcip.annotations.Immutable;

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
        return 0;
    }

    // number of tiles out of place
    public int hamming() {
        return 0;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
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
