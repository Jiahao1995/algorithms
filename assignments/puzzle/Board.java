package puzzle;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private final int N;

    public Board(int[][] tiles) {
        this.tiles = copyOf(tiles);
        N = tiles.length;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                builder.append(String.format("%2d ", tiles[i][j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != i * N + j + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int row = (tiles[i][j] - 1) / N;
                int col = (tiles[i][j] - 1) % N;
                manhattan += Math.abs(row - i) + Math.abs(col - j);
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1) {
                    return tiles[i][j] == 0;
                }
                if (tiles[i][j] != i * N + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int row = 0;
        int col = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        List<Board> list = new ArrayList<>();
        if (row > 0) {
            int[][] copy = copyOf(tiles);
            swap(copy, row, col, row - 1, col);
            list.add(new Board(copy));
        }
        if (row < N - 1) {
            int[][] copy = copyOf(tiles);
            swap(copy, row, col, row + 1, col);
            list.add(new Board(copy));
        }
        if (col > 0) {
            int[][] copy = copyOf(tiles);
            swap(copy, row, col, row, col - 1);
            list.add(new Board(copy));
        }
        if (col < N - 1) {
            int[][] copy = copyOf(tiles);
            swap(copy, row, col, row, col + 1);
            list.add(new Board(copy));
        }
        return list;
    }

    public Board twin() {
        int[][] copy = copyOf(tiles);
        if (copy[0][0] != 0 && copy[0][1] != 0) {
            swap(copy, 0, 0, 0, 1);
        }
        else {
            swap(copy, 1, 0, 1, 1);
        }
        return new Board(copy);
    }

    private static int[][] copyOf(int[][] tiles) {
        int row = tiles.length;
        int col = tiles[0].length;
        int[][] copy = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    private static void swap(int[][] tiles, int i1, int j1, int i2, int j2) {
        int temp = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = temp;
    }

    public static void main(String[] args) {
        int[][] tiles = new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };
        Board board = new Board(tiles);
        StdOut.print(board.isGoal());
    }
}
