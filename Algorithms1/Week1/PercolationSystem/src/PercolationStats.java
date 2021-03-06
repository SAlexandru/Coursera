

public class PercolationStats {
	private double mean_ ;
	private double stddev_;
	private double confidenceLo_;
	private double confidenceHi_;
	
	public PercolationStats (int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException();
		}
		
		final double[] points = new double[T];
		for (int k = 0; k < T; ++k) {
			final Percolation p = new Percolation(N);
		
			int count = 0;
			final boolean[][] was = new boolean[N][N];
			while (!p.percolates()) {
				int i;
				int j;
				
				do {
					i = StdRandom.uniform(N);
					j = StdRandom.uniform(N);
				} while (was[i][j]);
				
				was[i][j] = true;
				p.open(i + 1, j + 1);
				++count;
			}
			
			points[k] = (1.0 * count) / (1.0 * N * N);
		}
		
		mean_ = StdStats.mean(points);
		stddev_ = StdStats.stddev(points);
		
		confidenceLo_ = mean_ - (1.96 * stddev_) / (Math.sqrt(T));
		confidenceHi_ = mean_ + (1.96 * stddev_) / (Math.sqrt(T));
		
	}
	
	public double mean() {return mean_;}
	
	public double stddev() {return stddev_;}
	
	public double confidenceLo() {return confidenceLo_;}
	
	public double confidenceHi() {return confidenceHi_;}

	public static void main(String[] args) {
		final PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		
		System.out.println("mean                    = " + stats.mean());
		System.out.println("stddev                  = " + stats.stddev());
		System.out.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
	}
}
