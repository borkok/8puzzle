import edu.princeton.cs.algs4.Stack;
import net.jcip.annotations.Immutable;

import java.util.Arrays;
import java.util.Objects;

/*
You may assume that the constructor receives an n-by-n array containing the n2 integers
between 0 and n2 − 1, where 0 represents the blank square.
You may also assume that 2 ≤ n < 128.

To avoid recomputing the Manhattan priority of a search node from scratch each time
during various priority queue operations, pre-compute its value when you construct
the search node; save it in an instance variable; and return the saved value as needed.
 */
@Immutable
public class Board {
    public static final int BLANK = 0;
    private final SmallintMatrix matrix;

    private final int hamming;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] inTiles) {
        validateN(inTiles.length);
        matrix = new SmallintMatrix(inTiles);

        hamming = calculateHamming();
        manhattan = calculateManhattan();
    }

    private Board(SmallintMatrix newMatrix) {
        matrix = newMatrix;

        hamming = calculateHamming();
        manhattan = calculateManhattan();
    }

    private void validateN(int n) {
        if (n < 2 || n > 128) {
            throw new IllegalArgumentException();
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", matrix.get(i,j)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return matrix.getDimension();
    }

    //The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
    public int hamming() {
        return hamming;
    }

    private int calculateHamming() {
        return matrix.countNonConsecutiveButLast();
    }

    //The Manhattan distance between a board and the goal board is the sum of the Manhattan distances
    //(sum of the vertical and horizontal distance) from the tiles to their goal positions.
    public int manhattan() {
        return manhattan;
    }

    private int calculateManhattan() {
        int distance = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int number = matrix.get(row, col);
                if (number == BLANK)  continue;
                distance += matrix.indexToRowCol(number - 1)
                                   .distanceTo(RowCol.of(row, col));
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return matrix.isConsecutiveButLast() && isLastTileBlank();
    }

    private boolean isLastTileBlank() {
        return matrix.getLast() == 0;
    }

    // does this board equal y?
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return matrix.equals(board.matrix);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        RowCol blankRowCol = matrix.findBlank();
        return createStackWithoutEmpty(
                matrix.exchange(blankRowCol, blankRowCol.up()),
                matrix.exchange(blankRowCol, blankRowCol.left()),
                matrix.exchange(blankRowCol, blankRowCol.down(dimension())),
                matrix.exchange(blankRowCol, blankRowCol.right(dimension()))
        );
    }

    private Stack<Board> createStackWithoutEmpty(SmallintMatrix... matrices) {
        Stack<Board> stack = new Stack<>();
        for (SmallintMatrix sm : matrices) {
            if (sm.isEmpty()) continue;
            stack.push(new Board(sm));
        }
        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return new Board(makeTwinMatrix());
    }

    private SmallintMatrix makeTwinMatrix() {
        if (matrix.getFirst() != 0 && matrix.getSecond() != 0) {
            return matrix.exchangeFirstWithSecond();
        }
        return matrix.exchangeLastWithOneBefore();
    }


    public static void main(String[] args) {
        // unit testing (not graded)
    }


    /*****************************
     * SMALL INT MATRIX
     *****************************/
    private static class SmallintMatrix {
        public static final SmallintMatrix EMPTY = new SmallintMatrix(new char[0], 0);
        private final char[] charArray;
        private final int dimension;

        private SmallintMatrix(int[][] ints) {
            if (ints.length == 0) {
                throw new IllegalArgumentException();
            }
            dimension = ints.length;
            charArray = new char[dimension*dimension];
            copyToCharArray(ints);
        }

        private SmallintMatrix(char[] chars, int dim) {
            charArray = chars;
            dimension = dim;
        }

        private boolean isEmpty() {
            return dimension==0;
        }

        private void copyToCharArray(int[][] ints) {
            for (int row = 0; row < dimension; row++) {
                if (ints[row].length != dimension) {
                    throw new IllegalArgumentException();
                }
                for (int col = 0; col < dimension; col++) {
                    charArray[row*dimension + col] = (char) ints[row][col];
                }
            }
        }

        private int getDimension() {
            return dimension;
        }

        private int getFirst() {
            return charArray[0];
        }

        private int getSecond() {
            return charArray[1];
        }

        private int getLast() {
            return charArray[count() - 1];
        }

        //0-based
        private int get(int row, int col) {
            int index = row * dimension + col;
            return charArray[index];
        }

        private int count() {
            return charArray.length;
        }

        private boolean isConsecutiveButLast() {
            for (int i = 0; i < count() - 1; i++) {
                if (charArray[i] != i+1) {
                    return false;
                }
            }
            return true;
        }

        private int countNonConsecutiveButLast() {
            int count = 0;
            for (int i = 0; i < count() - 1; i++) {
                if (charArray[i] != i+1) {
                    count++;
                }
            }
            return count;
        }

        private SmallintMatrix exchangeFirstWithSecond() {
            return exchangeAt(0,1);
        }

        private SmallintMatrix exchangeLastWithOneBefore() {
            return exchangeAt(count()-2,count()-1);
        }

        private SmallintMatrix exchangeAt(int i, int j) {
            char[] copy = new char[count()];
            System.arraycopy(charArray, 0, copy, 0, count());
            char c = copy[i];
            copy[i] = copy[j];
            copy[j] = c;
            return new SmallintMatrix(copy, dimension);
        }

        // 0-based
        private RowCol indexToRowCol(int index) {
            int row = index / dimension;
            int col = index % dimension;
            return RowCol.of(row, col);
        }

        private RowCol findBlank() {
            for (int index = 0; index < count(); index++) {
                if (charArray[index] == BLANK) {
                    return indexToRowCol(index);
                }
            }
            return RowCol.EMPTY;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SmallintMatrix that = (SmallintMatrix) o;
            return Arrays.equals(charArray, that.charArray);
        }

        public int hashCode() {
            return Arrays.hashCode(charArray);
        }

        public SmallintMatrix exchange(RowCol rowCol, RowCol otherRowCol) {
            if (rowCol.isEmpty() || otherRowCol.isEmpty()) {
                return EMPTY;
            }
            return exchangeAt(
                    rowCol.toIndex(dimension),
                    otherRowCol.toIndex(dimension)
            );
        }
    }

    /**********************************
     * ROW COL 0-based
     ******************************/
    private static class RowCol {
        public static final RowCol EMPTY = new RowCol(-1, -1);
        final int row;
        final int col;

        private static RowCol of(int row, int col) {
            return new RowCol(row, col);
        }

        private RowCol(int row, int col) {
            this.row = row;
            this.col = col;
        }

        private int distanceTo(RowCol otherRowCol) {
            return Math.abs(otherRowCol.row - row) + Math.abs(otherRowCol.col - col);
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowCol rowCol = (RowCol) o;
            return row == rowCol.row &&
                    col == rowCol.col;
        }

        public int hashCode() {
            return Objects.hash(row, col);
        }

        private RowCol up() {
            if (row == 0) {
                return EMPTY;
            }
            return RowCol.of(row-1, col);
        }

        private RowCol left() {
            if (col == 0) {
                return EMPTY;
            }
            return RowCol.of(row, col-1);
        }

        private RowCol down(int dimension) {
            if (row >= dimension-1) {
                return EMPTY;
            }
            return RowCol.of(row+1, col);
        }

        private RowCol right(int dimension) {
            if (col >= dimension-1) {
                return EMPTY;
            }
            return RowCol.of(row, col+1);
        }

        public int toIndex(int dimension) {
            return row * dimension + col;
        }

        public boolean isEmpty() {
            return this.equals(EMPTY);
        }
    }
}
