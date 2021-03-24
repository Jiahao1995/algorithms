package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int N;
    private final WeightedQuickUnionUF toTopBottom;
    private final WeightedQuickUnionUF toTop;
    private final boolean[] sites;
    private int count;

    public Percolation(int n) {
        N = n;
        toTopBottom = new WeightedQuickUnionUF(N * N + 2);
        toTop = new WeightedQuickUnionUF(N * N + 1);
        sites = new boolean[N * N + 1];

        for (int i = 1; i <= N; i++) {
            toTopBottom.union(0, i);
            toTopBottom.union(N * N + 1, N * (N - 1) + i);
            toTop.union(0, i);
        }
    }

    public void open(int row, int col) {
        int index = indexOf(row, col);
        sites[index] = true;
        if (row > 1 && isOpen(row - 1, col)) {
            int up = indexOf(row - 1, col);
            toTopBottom.union(index, up);
            toTop.union(index, up);
        }
        if (row < N && isOpen(row + 1, col)) {
            int down = indexOf(row + 1, col);
            toTopBottom.union(index, down);
            toTop.union(index, down);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            int left = indexOf(row, col - 1);
            toTopBottom.union(index, left);
            toTop.union(index, left);
        }
        if (col < N && isOpen(row, col + 1)) {
            int right = indexOf(row, col + 1);
            toTopBottom.union(index, right);
            toTop.union(index, right);
        }
        count++;
    }

    public boolean isOpen(int row, int col) {
        return sites[indexOf(row, col)];
    }

    public boolean isFull(int row, int col) {
        return sites[indexOf(row ,col)] && toTop.find(0) == toTop.find(indexOf(row, col));
    }

    public int numberOfOpenSites() {
        return count;
    }

    public boolean percolates() {
        return toTopBottom.find(0) == toTopBottom.find(N * N + 1);
    }

    private int indexOf(int row, int col) {
        return (row - 1) * N + col;
    }

    public static void main(String[] args) {
    }

}
