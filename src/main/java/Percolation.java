import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Program to estimate the value of the percolation threshold
 * via Monte Carlo simulation.
 *
 * @author Alex N. Oreshkevich
 */
public class Percolation {

  private final WeightedQuickUnionUF alg;
  private final boolean[] sites;
  private final int dim;
  private final int virtualTopId, virtualBottomId;

  /**
   * Create N-by-N grid, with all sites blocked.
   *
   * @param N grid dimension
   */
  public Percolation(int N) {
    dim = N;
    int size = N * N;
    alg = new WeightedQuickUnionUF(size + 2); // 2 means virtual top/bottom
    sites = new boolean[size];
    virtualTopId = size;
    virtualBottomId = size + 1;
  }

  private int toInternalInd(int i, int j) {
    return dim * (i - 1) + j - 1;
  }

  private void checkBounds(int i, int j) {
    if (i < 1 || i > dim || j < 1 || j > dim) {
      throw new IndexOutOfBoundsException("(" + i + ", " + j + ").");
    }
  }

  /**
   * Open site if it is not already.
   *
   * @param i row
   * @param j column
   */
  public void open(int i, int j) {

    checkBounds(i, j);

    if (isOpen(i, j)) {
      return;
    }

    int currentInd = toInternalInd(i, j);
    sites[currentInd] = true;

    // --------------------------------------------------
    // connect to virtual top/bottom
    if (i == 1) { // first row
      alg.union(currentInd, virtualTopId);
    }
    // not else-if because of N=1 support
    if (i == dim) {
      alg.union(currentInd, virtualBottomId);
    }
    // --------------------------------------------------

    // --------------------------------------------------
    // top
    if (i > 1 && isOpen(i - 1, j)) {
      alg.union(toInternalInd(i - 1, j), currentInd);
    }

    // left
    if (j > 1 && isOpen(i, j - 1)) {
      alg.union(toInternalInd(i, j - 1), currentInd);
    }

    // right
    if (j != dim && isOpen(i, j + 1)) {
        alg.union(toInternalInd(i, j + 1), currentInd);
    }

    // bottom
    if (i != dim && isOpen(i + 1, j)) {
        alg.union(toInternalInd(i + 1, j), currentInd);
    }
    // --------------------------------------------------
  }

  // is site (row i, column j) open?
  public boolean isOpen(int i, int j) {
    checkBounds(i, j);
    return sites[toInternalInd(i, j)];
  }

  // is site (row i, column j) full?
  public boolean isFull(int i, int j) {

    checkBounds(i, j);

    int currentInd = toInternalInd(i, j);
    return sites[currentInd] && alg.connected(currentInd, virtualTopId);
  }

  // does the system percolate?
  public boolean percolates() {
    return alg.connected(virtualTopId, virtualBottomId);
  }

  public static void main(String[] args) {
    for (int N = 1; N < 2049; N *= 2) {

      StdRandom.setSeed(System.currentTimeMillis());
      Stopwatch watch = new Stopwatch();
      Percolation perc = new Percolation(N);
      int k = 0;

      while (true) {
        int i = StdRandom.uniform(1, N + 1);
        int j = StdRandom.uniform(1, N + 1);
        if (perc.isOpen(i, j)) {
          continue;
        }
        perc.open(i, j);
        k++;
        if (perc.percolates()) {
          break;
        }
      }

      double p = ((double) k) / (N * N);

      System.out.printf("[N] = " + N + " System percolates. p = "
              + p + ". Elapsed time = %3.3f (ms).\n",
              watch.elapsedTime() * 1000);
    }
  }
}