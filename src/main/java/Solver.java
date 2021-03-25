import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/*
When considering the neighbors of a search node, don’t enqueue a neighbor
if its board is the same as the board of the previous search node in the game tree
 */
public class Solver {
    private final Stack<Board> solution;
    private final BoardPriorityQueue boardPriorityQueue;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solution = new Stack<>();
        boardPriorityQueue = new BoardPriorityQueue(initial);

        while(true) {
            SearchNode min = boardPriorityQueue.queue.delMin();
            solution.push(min.board);
            if (min.board.isGoal()) {
                break;
            }
            Iterable<Board> neighbors = min.board.neighbors();
            for (Board neighbor : neighbors) {
                if (min.previous != null && neighbor.equals(min.previous.board)) {
                    continue;
                }
                boardPriorityQueue.queue.insert(new SearchNode(min.movesSoFar + 1, min, neighbor));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves() >= 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution.size()-1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? solution : null;
    }

    // test client (see below)
    public static void main(String[] args){
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
            queue.insert(new SearchNode(0,SearchNode.EMPTY,board));
        }
    }

    /**
     * We define a search node of the game to be a board,
     * the number of moves made to reach the board, and the previous search node.
     */
    private static class SearchNode implements Comparable<SearchNode> {
        public static SearchNode EMPTY = null;
        private final int movesSoFar;
        private final SearchNode previous;
        private final Board board;

        public SearchNode(int movesSoFar, SearchNode previous, Board board) {
            this.movesSoFar = movesSoFar;
            this.previous = previous;
            this.board = board;
        }

        public int getMovesSoFar() {
            return movesSoFar;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public Board getBoard() {
            return board;
        }

        public int compareTo(SearchNode other) {
            return this.priority() - other.priority();
        }

        //The Manhattan priority function is the Manhattan distance of a board
        // plus the number of moves made so far to get to the search node.
        private int priority() {
            return board.manhattan() + movesSoFar;
        }
    }
}