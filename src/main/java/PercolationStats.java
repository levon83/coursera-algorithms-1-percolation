import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Perform numerous tests and calculations related to {@link Percolation} problem.
 */
public class PercolationStats {

    /**
     * Value of 95% confidence.
     */
    private static final double CONFIDENCE_95 = 1.96;

    /**
     * Fraction of open sites per trial.
     */
    private final double[] fractionOfOpenSites;

    /**
     * Sample mean of percolation threshold.
     */
    private final double m;

    /**
     * Sample standard deviation of percolation threshold.
     */
    private final double s;

    /**
     * Perform trials independent experiments on an n-by-n grid.
     *
     * @param n      the size of the grid
     * @param trials the number of independent experiments to perform
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The values fo 'n' and 'trials' must be positive.");
        }

        this.fractionOfOpenSites = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }

            this.fractionOfOpenSites[t] = p.numberOfOpenSites() / Math.pow(n, 2);
        }

        this.m = StdStats.mean(this.fractionOfOpenSites);
        this.s = StdStats.stddev(this.fractionOfOpenSites);
    }

    /**
     * Sample mean of percolation threshold.
     *
     * @return the calculated value as described
     */
    public double mean() {
        return this.m;
    }

    /**
     * Sample standard deviation of percolation threshold.
     *
     * @return the calculated value as described
     */
    public double stddev() {
        return this.s;
    }

    /**
     * Low  endpoint of 95% confidence interval.
     *
     * @return the calculated value as described
     */
    public double confidenceLo() {
        return this.m - CONFIDENCE_95 * this.s / Math.sqrt(this.fractionOfOpenSites.length);
    }

    /**
     * High endpoint of 95% confidence interval.
     *
     * @return the calculated value as described
     */
    public double confidenceHi() {
        return this.m + CONFIDENCE_95 * this.s / Math.sqrt(this.fractionOfOpenSites.length);
    }

    /**
     * Intended for simulations.
     *
     * @param args input arguments of the program
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}
