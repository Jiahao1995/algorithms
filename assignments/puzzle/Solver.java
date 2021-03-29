package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final static boolean IS_HAMMING = false;
    private boolean solvable;
    private final Stack<Board> solution;
    private int minMoves;
    private final MinPQ<Node> rawPQ;
    private final MinPQ<Node> twinPQ;

    private class Node implements Comparable<Node> {
        Board board;
        boolean isTwin;
        Node prev;
        int moves;

        Node(Board board, boolean isTwin) {
            this.board = board;
            this.isTwin = isTwin;
        }

        Node(Board board, boolean isTwin, Node prev, int moves) {
            this.board = board;
            this.isTwin = isTwin;
            this.prev = prev;
            this.moves = moves;
        }

        private int priority() {
            if (IS_HAMMING) {
                return moves + board.hamming();
            }
            else {
                return moves + board.manhattan();
            }
        }

        @Override
        public int compareTo(Node that) {
            if (this.priority() == that.priority()) {
                return Integer.compare(this.board.manhattan(), that.board.manhattan());
            }
            return Integer.compare(this.priority(), that.priority());
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        solvable = false;
        solution = new Stack<>();
        rawPQ = new MinPQ<>();
        rawPQ.insert(new Node(initial, false));
        twinPQ = new MinPQ<>();
        twinPQ.insert(new Node(initial.twin(), true));
        solve();
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return minMoves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        return solution;
    }

    private void solve() {
        while (true) {
            Node rawNode = rawPQ.delMin();
            if (rawNode.board.isGoal()) {
                solvable = true;
                minMoves = rawNode.moves;
                Node current = rawNode;
                while (current != null) {
                    solution.push(current.board);
                    current = current.prev;
                }
                break;
            }
            Node twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) {
                solvable = false;
                break;
            }
            Iterable<Board> rawNeighbors = rawNode.board.neighbors();
            for (Board neighbor : rawNeighbors) {
                if (rawNode.prev != null && neighbor.equals(rawNode.prev.board)) {
                    continue;
                }
                rawPQ.insert(new Node(neighbor, false, rawNode, rawNode.moves + 1));
            }
            Iterable<Board> twinNeighbors = twinNode.board.neighbors();
            for (Board neighbor : twinNeighbors) {
                if (twinNode.prev != null && neighbor.equals(twinNode.prev.board)) {
                    continue;
                }
                twinPQ.insert(new Node(neighbor, true, twinNode, twinNode.moves + 1));
            }
            StdOut.print(rawPQ.size());
        }
    }

    public static void main(String[] args) {
        In in = new In(StdIn.readString());
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
}
