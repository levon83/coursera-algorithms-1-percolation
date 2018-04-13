import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Run Monte Carlo simulation to estimate percolation threshold.
 */
public class Percolation {

    /**
     * Maximal number of sites.
     */
    private final int maxSites;

    /**
     * Grid size is n-by-n.
     */
    private final int gridSize;

    /**
     * Union-Find data structure to maintain open sites.
     */
    private final WeightedQuickUnionUF openSites;

    /**
     * Union-Find data structure to maintain full sites.
     */
    private final WeightedQuickUnionUF fullSites;

    /**
     * Tracks open sites.
     */
    private boolean[] openedSites;

    /**
     * Number of open sites.
     */
    private int openSitesCount;

    /**
     * Initialize n-by-n grid.
     *
     * @param gridSize the number of rows and columns in the grid
     */
    public Percolation(int gridSize) {
        if (gridSize < 1) {
            throw new IllegalArgumentException("Invalid grid size.");
        }

        this.gridSize = gridSize;
        this.maxSites = this.gridSize * this.gridSize + 2;
        this.openSites = new WeightedQuickUnionUF(this.maxSites);
        this.fullSites = new WeightedQuickUnionUF(this.maxSites - 1);
        this.openedSites = new boolean[this.maxSites];
        this.openSitesCount = 0;

        setSiteOpen(0);
        setSiteOpen(this.maxSites - 1);
    }

    /**
     * Open a site and connect it to its neighbors.
     *
     * @param row row index
     * @param col column index
     */
    public void open(int row, int col) {
        validateInput(row);
        validateInput(col);

        int currentIndex = indexFrom2D(row, col);

        if (!getSiteOpen(currentIndex)) {
            setSiteOpen(currentIndex);
            this.openSitesCount++;

            int leftIndex = leftSiteIndex(row, col);
            int aboveIndex = aboveSiteIndex(row, col);
            int rightIndex = rightSiteIndex(row, col);
            int belowIndex = belowSiteIndex(row, col);

            if (leftIndex != -1 && getSiteOpen(leftIndex)) {
                this.openSites.union(currentIndex, leftIndex);
                this.fullSites.union(currentIndex, leftIndex);
            }

            if (aboveIndex != -1 && getSiteOpen(aboveIndex)) {
                this.openSites.union(currentIndex, aboveIndex);
                this.fullSites.union(currentIndex, aboveIndex);
            } else if (aboveIndex == -1) {
                this.openSites.union(currentIndex, 0);
                this.fullSites.union(currentIndex, 0);
            }

            if (rightIndex != -1 && getSiteOpen(rightIndex)) {
                this.openSites.union(currentIndex, rightIndex);
                this.fullSites.union(currentIndex, rightIndex);
            }

            if (belowIndex != -1 && getSiteOpen(belowIndex)) {
                this.openSites.union(currentIndex, belowIndex);
                this.fullSites.union(currentIndex, belowIndex);
            } else if (belowIndex == -1) {
                this.openSites.union(currentIndex, this.maxSites - 1);
            }
        }
    }

    /**
     * Check if the site is open.
     *
     * @param row row index
     * @param col column index
     * @return true if open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        validateInput(row);
        validateInput(col);

        int currentIndex = indexFrom2D(row, col);

        return getSiteOpen(currentIndex);
    }

    /**
     * Check if the site is full.
     *
     * @param row row index
     * @param col column index
     * @return true if full, false otherwise
     */
    public boolean isFull(int row, int col) {
        validateInput(row);
        validateInput(col);

        int currentIndex = indexFrom2D(row, col);

        return this.fullSites.connected(0, currentIndex);
    }

    /**
     * Get the number of open sites.
     *
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return this.openSitesCount;
    }

    /**
     * Whether the model percolates.
     *
     * @return true if it percolates, false otherwise
     */
    public boolean percolates() {
        return this.openSites.connected(0, this.maxSites - 1);
    }

    /**
     * Validate input index to be within bounds.
     *
     * @param i the index to validate
     */
    private void validateInput(int i) {
        if (i < 1 || i > this.gridSize) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
    }

    /**
     * Convert 2D index to 1D unique index.
     *
     * @param row row index
     * @param col column index
     * @return unique index derived from both inputs
     */
    private int indexFrom2D(int row, int col) {
        return this.gridSize * (row - 1) + col;
    }

    /**
     * Get the 1D index of the left site.
     *
     * @param row row index of the current site
     * @param col column index of the current site
     * @return the 1D index of the neighbor
     */
    private int leftSiteIndex(int row, int col) {
        return col > 1 ? indexFrom2D(row, col - 1) : -1;
    }

    /**
     * Get the 1D index of the above site.
     *
     * @param row row index of the current site
     * @param col column index of the current site
     * @return the 1D index of the neighbor
     */
    private int aboveSiteIndex(int row, int col) {
        return row > 1 ? indexFrom2D(row - 1, col) : -1;
    }

    /**
     * Get the 1D index of the right site.
     *
     * @param row row index of the current site
     * @param col column index of the current site
     * @return the 1D index of the neighbor
     */
    private int rightSiteIndex(int row, int col) {
        return col < this.gridSize ? indexFrom2D(row, col + 1) : -1;
    }

    /**
     * Get the 1D index of the below site.
     *
     * @param row row index of the current site
     * @param col column index of the current site
     * @return the 1D index of the neighbor
     */
    private int belowSiteIndex(int row, int col) {
        return row < this.gridSize ? indexFrom2D(row + 1, col) : -1;
    }

    /**
     * Check if the site is open by its 1D index.
     *
     * @param i 1D index of the site
     * @return true if open, false otherwise
     */
    private boolean getSiteOpen(int i) {
        return this.openedSites[i];
    }

    /**
     * Set site to be open.
     *
     * @param i 1D index of the site
     */
    private void setSiteOpen(int i) {
        this.openedSites[i] = true;
    }

    /**
     * Intended for testing.
     *
     * @param args input arguments of the program
     */
    public static void main(String[] args) {
        System.out.println("Testing...");

        Percolation p = new Percolation(2);
        assert !p.percolates();
        p.open(1, 1);
        assert p.isOpen(1, 1);
        assert p.isFull(1, 1);
        assert p.numberOfOpenSites() == 1;
        assert !p.percolates();
        assert !p.isOpen(2, 2);
        p.open(2, 2);
        assert p.isOpen(2, 2);
        assert !p.isFull(2, 2);
        assert p.numberOfOpenSites() == 2;
        assert !p.percolates();
        assert !p.isOpen(2, 1);
        p.open(2, 1);
        assert p.isOpen(2, 1);
        assert p.isFull(2, 1);
        assert p.numberOfOpenSites() == 3;
        assert p.percolates();

        System.out.println("OK!");
    }

}
