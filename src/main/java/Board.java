import net.jcip.annotations.Immutable;

import java.util.Arrays;

/*
You may assume that the constructor receives an n-by-n array containing the n2 integers
between 0 and n2 − 1, where 0 represents the blank square.
You may also assume that 2 ≤ n < 128.
 */
@Immutable
public class Board {
    private final SmallintMatrix matrix;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] inTiles) {
        validateN(inTiles.length);
        matrix = new SmallintMatrix(inTiles);
    }

    private Board(SmallintMatrix newMatrix) {
        matrix = newMatrix;
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
        return 0;
    }

    //The Manhattan distance between a board and the goal board is the sum of the Manhattan distances
    //(sum of the vertical and horizontal distance) from the tiles to their goal positions.
    public int manhattan() {
        return 0;
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
        return null;
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

    // unit testing (not graded)
    public static void main(String[] args) {}


    /*****************************
     * SMALL INT MATRIX
     *****************************/
    private static class SmallintMatrix {
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

        private int getOneBeforeLast() {
            return charArray[count() - 2];
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

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SmallintMatrix that = (SmallintMatrix) o;
            return Arrays.equals(charArray, that.charArray);
        }

        public int hashCode() {
            return Arrays.hashCode(charArray);
        }
    }
}
