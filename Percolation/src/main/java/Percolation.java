//import edu.princeton.cs.algs4.QuickFindUF;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Random;

/**
 * Created by icordonm on 24/04/2017.
 */
public class Percolation {

    private boolean[] grid;
    private int n = 0;
    private int sizeGridTable = 0;
    private int lastIndexGridArray = 0;
    private int numOpenSites = 0;
    //private QuickFindUF qf;
    private WeightedQuickUnionUF wquf;

    private static final boolean BLOCKED = false;
    private static final boolean OPEN = true;

    //Auxiliary function to convert row-column coordenates to array index of grid
    private int getIndexGridArray(int row, int col) {
        return (row - 1) * n + col;
    }

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        //Exception if n <= 0
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");

        this.sizeGridTable = n * n;
        this.lastIndexGridArray = sizeGridTable + 1;
        this.n = n;
        grid = new boolean[lastIndexGridArray+1];

        //Initialize each site to closed
        grid[0]=OPEN;
        for (int i = 1; i < lastIndexGridArray; i++) {
            grid[i] = BLOCKED;
        }
        grid[lastIndexGridArray]=OPEN;

        //qf = new QuickFindUF(lastIndexGridArray + 1);
        wquf = new WeightedQuickUnionUF(lastIndexGridArray + 1);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int idx = getIndexGridArray(row, col);
        if (idx <= 0 || idx >= lastIndexGridArray) throw new IndexOutOfBoundsException();

        if (!isOpen(row, col)) {
            //System.out.println("(" + row + "," + col + ")");
            grid[idx] = OPEN;
            numOpenSites++;
            //If row = 0 then connect to virtual start
            if (row == 1) {
                //qf.union(0, idx);
                wquf.union(0, idx);
            }

            //connect with neiborhoods
     /*
            if (row - 1 >= 1 && isOpen(row - 1, col)) qf.union(getIndexGridArray(row - 1, col), idx);
            else if (row + 1 <= n && isOpen(row + 1, col)) qf.union(getIndexGridArray(row + 1, col), idx);
            else if (col - 1 >= 1 && isOpen(row, col - 1)) qf.union(getIndexGridArray(row, col - 1), idx);
            else if (col + 1 <= n && isOpen(row, col + 1)) qf.union(getIndexGridArray(row, col + 1), idx);
    */
            if (row - 1 >= 1 && isOpen(row - 1, col)) wquf.union(getIndexGridArray(row - 1, col), idx);
            if (row + 1 <= n && isOpen(row + 1, col)) wquf.union(getIndexGridArray(row + 1, col), idx);
            if (col - 1 >= 1 && isOpen(row, col - 1)) wquf.union(getIndexGridArray(row, col - 1), idx);
            if (col + 1 <= n && isOpen(row, col + 1)) wquf.union(getIndexGridArray(row, col + 1), idx);


            //if (row == n && isFull(row, col)) {
            if (row == n) {
                //System.out.println("Conecto por abajo a (" + row + "," + col + ")");
                //qf.union(idx, lastIndexGridArray);
                wquf.union(lastIndexGridArray, idx);
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[getIndexGridArray(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        //return qf.connected(0, getIndexGridArray(row, col));
        return wquf.connected(0, getIndexGridArray(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        //return qf.connected(0, lastIndexGridArray);
        return wquf.connected(0, lastIndexGridArray);
    }


    // test client (optional)
    public static void main(String[] args) {

        final int N = 3;
        // Call costructor
        Percolation percolation = new Percolation(N);


        while (!percolation.percolates()) {
            Random r = new Random();
            int Low = 1;
            int High = N + 1;
            int row = r.nextInt(High - Low) + Low;
            int col = r.nextInt(High - Low) + Low;
            percolation.open(row, col);
            //System.out.println("(" + row + "," + col + ")");
            //if (percolation.isFull(row, col)) System.out.println("IsFULL");
        }



        System.out.println("Numer of opened sites " + percolation.numberOfOpenSites());
    }

}
