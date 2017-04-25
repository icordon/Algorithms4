import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * Created by icordonm on 25/04/2017.
 */
public class PercolationStats {

    private double[] openedProportion;
    private int trials;
    private double meanValue;
    private double stddevValue;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        if (trials <= 0) throw  new IllegalArgumentException("trial must be > 0");

        this.trials = trials;
        openedProportion = new double[trials];
        for (int i=0; i<trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()){
                Random r = new Random();
                int Low = 1;
                int High = n+1;
                int row = r.nextInt(High - Low) + Low;
                int col = r.nextInt(High - Low) + Low;
                percolation.open(row, col);
                //System.out.println("(" + row + "," + col + ")");
                //if (percolation.isFull(row, col)) System.out.println("IsFULL");
            }
            openedProportion[i] = percolation.numberOfOpenSites()/((double)n*n);
            System.out.println("Opened proportion : " + openedProportion[i]);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        double sum = 0;
        for (double val : openedProportion){
            sum += val;
        }
        meanValue = sum / trials;
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        double sum = 0;
        for (double val : openedProportion){
            sum += (val - meanValue);
        }
        stddevValue = sum / (trials - 1);
        return  stddevValue;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return meanValue - (1.96*sqrt(stddevValue)/sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return meanValue + (1.96*sqrt(stddevValue)/sqrt(trials));
    }

    // test client (described below)
    public static void main(String[] args){
        PercolationStats percolationStats = new PercolationStats(200, 100);
        System.out.println("mean                   = " + percolationStats.mean());
        System.out.println("stddev                 = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}