import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *
 */
public class PercolationStats {

  private double[] thresholdData;

  private double mean;
  private double stddev;
  private double confidenceLo, confidenceHi;

  private final int expCount;

  /** Perform T independent computational experiments on an N-by-N grid */
  public PercolationStats(int N, int T) {

    // check input
    if (N <= 0 || T <= 0) {
      throw new IllegalArgumentException("N = " + N + " T = " + T);
    }

    expCount = T;

    // performs T independent computational experiments
    thresholdData = new double[expCount];
    for (int i = 0; i < expCount; i++) {
      thresholdData[i] = estimTreshold(N);
    }

    mean = StdStats.mean(thresholdData);
    stddev = StdStats.stddev(thresholdData);

    double x = 1.96 * stddev / Math.sqrt(expCount);
    confidenceLo = mean - x;
    confidenceHi = mean + x;
  }

  private static double estimTreshold(int N) {
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

    return ((double) k) / (N * N);
  }

  /** Sample mean of percolation threshold */
  public double mean() {
    return mean;
  }

  /** Sample standard deviation of percolation threshold */
  public double stddev() {
    return stddev;
  }

  /** Returns lower bound of the 95% confidence interval */
  public double confidenceLo() {
    return confidenceLo;
  }

  /** Returns upper bound of the 95% confidence interval */
  public double confidenceHi() {
    return confidenceHi;
  }

  public static void main(String[] args) {

    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(n, t);

    StdOut.printf("%-24s = %1.12f\n", "mean", stats.mean());
    StdOut.printf("%-24s = %1.12f\n", "stddev", stats.stddev());
    StdOut.printf("%-24s = %1.12f, %1.12f\n",
            "95% confidence interval", stats.confidenceLo(),
            stats.confidenceHi());
  }
}
