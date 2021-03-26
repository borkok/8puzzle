import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;

public class Solver {
    private final Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solution = PuzzleSolver.of(initial).solution();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves() >= 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solution : null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private static class PuzzleSolver {
        private boolean unsolvable;
        private final BoardPriorityQueue priorityQueue;
        private final BoardPriorityQueue twinPriorityQueue;
        private Stack<Board> solution;

        public static PuzzleSolver of(Board initial) {
            return new PuzzleSolver(initial);
        }

        private PuzzleSolver(Board initial) {
            priorityQueue = new BoardPriorityQueue(initial);
            twinPriorityQueue = new BoardPriorityQueue(initial.twin());

            while (makeMove()) {
            }

            if (unsolvable) {
                clearSolution();
            }
            else {
                putMovesIntoSolution();
            }
        }

        public Stack<Board> solution() {
            return solution;
        }

        /**
         * @return true if should make another move, false if should stop
         */
        private boolean makeMove() {
            if (!makeMoveOnBoard()) return false;
            return makeMoveOnTwin();
        }

        private boolean makeMoveOnBoard() {
            if (achievedGoal()) return false;
            priorityQueue.putNeighborsAndRemoveMin();
            return true;
        }

        private boolean achievedGoal() {
            return priorityQueue.isMinAGoal();
        }

        private boolean makeMoveOnTwin() {
            if (provedUnsolvable()) {
                unsolvable = true;
                return false;
            }
            twinPriorityQueue.putNeighborsAndRemoveMin();
            return true;
        }

        private boolean provedUnsolvable() {
            return twinPriorityQueue.isMinAGoal();
        }

        private void clearSolution() {
            solution = new Stack<>();
        }

        private void putMovesIntoSolution() {
            solution = priorityQueue.traverseBoardsFromMin();
        }
    }

    private static class BoardPriorityQueue {
        private final MinPQ<SearchNode> queue;

        private BoardPriorityQueue(Board board) {
            queue = new MinPQ<>();
            queue.insert(new SearchNode(0, SearchNode.EMPTY, board));
        }

        private Board getMin() {
            return queue.min().board;
        }

        private boolean isMinAGoal() {
            return getMin().isGoal();
        }

        private void putNeighborsAndRemoveMin() {
            SearchNode min = queue.delMin();

            Iterable<Board> neighbors = min.board.neighbors();
            for (Board neighbor : neighbors) {
                if (min.hasNoPrevious() || !min.previousBoardEquals(neighbor)) {
                    SearchNode node = new SearchNode(min.movesSoFar + 1, min, neighbor);
                    queue.insert(node);
                }
            }
        }

        public Stack<Board> traverseBoardsFromMin() {
            Stack<Board> result = new Stack<>();
            SearchNode searchNode = queue.delMin();
            while (true) {
                result.push(searchNode.board);
                if (searchNode.hasNoPrevious()) {
                    break;
                }
                searchNode = searchNode.previous;
            }
            return result;
        }
    }

    /**
     * We define a search node of the game to be a board,
     * the number of moves made to reach the board, and the previous search node.
     */
    private static class SearchNode implements Comparable<SearchNode> {
        public static final SearchNode EMPTY = null;
        private final int movesSoFar;
        private final SearchNode previous;
        private final Board board;

        public SearchNode(int movesSoFar, SearchNode previous, Board board) {
            this.movesSoFar = movesSoFar;
            this.previous = previous;
            this.board = board;
        }

        public int compareTo(SearchNode other) {
            int manhattanPriority = this.manhattanPriority() - other.manhattanPriority();
            if (manhattanPriority == 0) {
                return this.board.hamming() - other.board.hamming();
            }
            return manhattanPriority;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchNode that = (SearchNode) o;
            return movesSoFar == that.movesSoFar &&
                    Objects.equals(previous, that.previous) &&
                    Objects.equals(board, that.board);
        }

        public int hashCode() {
            return Objects.hash(movesSoFar, previous, board);
        }

        //The Manhattan manhattanPriority function is the Manhattan distance of a board
        // plus the number of moves made so far to get to the search node.
        private int manhattanPriority() {
            return board.manhattan() + movesSoFar;
        }

        public boolean hasNoPrevious() {
            return previous == EMPTY;
        }

        private boolean previousBoardEquals(Board neighbor) {
            return previous.board.equals(neighbor);
        }
    }
}