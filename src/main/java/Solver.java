import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;

public class Solver {
    private Stack<Board> solution;
    private final BoardPriorityQueue priorityQueue;
    private final BoardPriorityQueue twinPriorityQueue;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        clearSolution();
        priorityQueue = new BoardPriorityQueue(initial);
        twinPriorityQueue = new BoardPriorityQueue(initial.twin());

        while (makeMove()) ;
    }

    /**
     * @return true if should make another move, false if should stop
     */
    private boolean makeMove() {
        if (!makeMoveOnBoard()) return false;
        return makeMoveOnTwin();
    }

    private boolean makeMoveOnBoard() {
        solution.push(priorityQueue.getMin());
        if (achievedGoal()) return false;
        priorityQueue.putNeighborsAndRemoveMin();
        return true;
    }

    private boolean achievedGoal() {
        return priorityQueue.isMinAGoal();
    }

    private boolean makeMoveOnTwin() {
        if (provedUnsolvable()) {
            clearSolution();
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

        public boolean isPreviousOfMinEqualTo(Board neighbor) {
            if (queue.min().hasNoPrevious()) return false;
            return queue.min().previousBoardEquals(neighbor);
        }

        public void insert(Board neighbor) {
            queue.insert(
                    new SearchNode(queue.min().movesSoFar + 1, queue.min(), neighbor)
            );
        }

        public void removeMin() {
            queue.delMin();
        }

        private void putNeighborsAndRemoveMin() {
            Iterable<Board> neighbors = getMin().neighbors();
            for (Board neighbor : neighbors) {
                if (!isPreviousOfMinEqualTo(neighbor)) {
                    insert(neighbor);
                }
            }
            removeMin();
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
            return this.priority() - other.priority();
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

        //The Manhattan priority function is the Manhattan distance of a board
        // plus the number of moves made so far to get to the search node.
        private int priority() {
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