// import edu.princeton.cs.algs4.QuickFindUF;
// import edu.princeton.cs.algs4.QuickUnionUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // TODO: Add instance variables here.
    boolean[][] grid;
    WeightedQuickUnionUF uf;
    int numOpen = 0;

    /* Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {
        // TODO: YOUR CODE HERE
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        int index = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
    }

    /* Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        // TODO: YOUR CODE HERE
        int N = grid[row].length;
        if (!valid(row, col)) {
            throw new IndexOutOfBoundsException();
        } else if (isOpen(row, col)) {
            return;
        } else {
            grid[row][col] = true;
            numOpen += 1;
            int p = xyTo1D(row, col);

            if (row == 0) {
                uf.union(p, N * N);
                return;
            }

            if (row == N - 1) {
                uf.union(p, N * N + 1);
                return;
            }

            if (isOpen(row - 1, col)) {
                //union two points
                int q = xyTo1D(row - 1, col);
                uf.union(p, q);
            }

            if (isOpen(row + 1, col)) {
                //union
                int q = xyTo1D(row - 1, col);
                uf.union(p, q);
            }

            if (isOpen(row, col - 1)) {
                //union
                int q = xyTo1D(row - 1, col);
                uf.union(p, q);
            }

            if (isOpen(row, col + 1)) {
                //union
                int q = xyTo1D(row - 1, col);
                uf.union(p, q);
            }
            return;
        }
    }

    /* Returns true if the site at (row, col) is open. */
    public boolean isOpen(int row, int col) {
        // TODO: YOUR CODE HERE
        if (valid(row, col)) {
            return grid[row][col];
        } else {
            return valid(row, col);
        }
    }

    /* Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        // TODO: YOUR CODE HERE
        if (valid(row, col)) {
            //use the connected method
            int N = grid[row].length;
            int p = xyTo1D(row, col);
            return uf.connected(p, N * N);
        }
        return false;
    }

    /* Returns the number of open sites. */
    public int numberOfOpenSites() {
        // TODO: YOUR CODE HERE
        return numOpen;
    }

    /* Returns true if the system percolates. */
    public boolean percolates() {
        // TODO: YOUR CODE HERE
        int N = grid[0].length;
        return uf.connected(N * N, N * N + 1);
    }

    /* Converts row and column coordinates into a number. This will be helpful
       when trying to tie in the disjoint sets into our NxN grid of sites. */
    private int xyTo1D(int row, int col) {
        // TODO: YOUR CODE HERE
        return row * grid[row].length + col + 1;
    }
    /* Returns true if (row, col) site exists in the NxN grid of sites.
       Otherwise, return false. */
    private boolean valid(int row, int col) {
        // TODO: YOUR CODE HERE
        return row >= 0 && row <= grid[0].length && col >= 0 && col <= grid[0].length;
    }
}

//virtual top site: number is size * size
//virtual bottom site: number is size * size + 1
//open method, if grid in top row open, union it to top site (same for bottom)

