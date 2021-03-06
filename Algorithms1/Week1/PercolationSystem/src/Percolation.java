import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Percolation {
	private final int N_;
	private final byte[] board_;
	private final byte[] lastRow_;
	private final WeightedQuickUnionUF union_;
	
	private boolean isPercolating_;
	
	private static int[] dx = new int[] {-1, 0, 0, 1};
	private static int[] dy = new int[] {0, -1, 1, 0};
	
	public Percolation (int N) {
		if (N <= 0) {
			throw new IllegalArgumentException();
		}
		
		N_ = N;
		isPercolating_ = false;
		board_ = new byte[N * N];
		lastRow_  = new byte[N * N];
		union_ = new WeightedQuickUnionUF(N_ * N_ + 1);
	}
	
	public void open (int i, int j) {
		check(i, j);
		
		final int idx = getIdx (i, j);
		
		set (board_, idx);
		if (1 == i) {
			union_.union(0, idx);
		}
		
		if (N_ == i) {
			set (lastRow_, idx);
		}
		
		for (int k = 0; k < dx.length; ++k) {
			int newI = i + dx[k];
			int newJ = j + dy[k];
			
			if (newI <= 0 || newJ <= 0 || newI > N_ || newJ > N_) {
				continue;
			}
			
			int newIdx = getIdx (newI, newJ);
			
			if (isSet (board_, newIdx)) {
				final int root = union_.find(newIdx);
				
				union_.union(idx, newIdx);
				
				copy (lastRow_, idx, newIdx);
				copy (lastRow_, idx, root);
			}
		}
		
		for (int k = 0; k < dx.length; ++k) {
			int newI = i + dx[k];
			int newJ = j + dy[k];
			
			if (newI <= 0 || newJ <= 0 || newI > N_ || newJ > N_) {
				continue;
			}
			
			int newIdx = getIdx (newI, newJ);
			
			if (isSet (board_, newIdx)) {
				int root = union_.find(idx);
				
				copy (lastRow_, idx, newIdx);
				copy (lastRow_, idx, root);
				
				isPercolating_ = isPercolating_ || 
						(union_.connected(0, newIdx) && isSet(lastRow_, newIdx)) ||
						(union_.connected(0, root) && isSet(lastRow_, root));
			}
		}
		
		isPercolating_ = isPercolating_ || (union_.connected(0, idx) && isSet(lastRow_, idx));
	}
	
	public boolean isOpen(int i, int j) {
		check(i, j);
		return isSet(board_, getIdx(i, j));
	}
	
	public boolean isFull(int i, int j) {
		check(i, j);
		return union_.connected(0, getIdx(i, j));
	}
	
	public boolean percolates() {
		return isPercolating_;
	}
	
	private void check (int i, int j) {
		if (i <= 0 || j <= 0 || i > N_ || j > N_) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private int getIdx (int i, int j) {return (i - 1) * N_ + j;}
	
	private static void set (final byte[] v, int idx) {v[idx >> 3] |= 1 << (idx & 7);}
	private static boolean isSet(final byte[] v, int idx) {return 0 != (v[idx >> 3] & (1 << (idx & 7)));}
	private static byte get (final byte[] v, int idx) {return isSet(v, idx) ? (byte)1 : (byte)0;}

	private static void copy (final byte[] v, int toIdx, int fromIdx) {
		v[toIdx >> 3] |= get(v, fromIdx) << (toIdx & 7);
		v[fromIdx >> 3] |= get(v, toIdx) << (fromIdx & 7);
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		@SuppressWarnings("resource")
		final Scanner s = new Scanner(new File(args[0]));
		
		int N = s.nextInt();
		final Percolation p = new Percolation(N);
		
		while (s.hasNext()) {
			int i = s.nextInt();
			int j = s.nextInt();
			
			p.open(i, j);
			System.out.println("(" + i + ", " + j + "): " + p.isOpen(i, j) + " " + p.isFull(i, j) + " " + p.percolates());
		}
		
		System.out.println(p.percolates());
		System.out.println("========================= post analysis ===========================");
		for (int i = 1; i <= N; ++i) {
			for (int j = 1; j <= N; ++j) {
				System.out.println("(" + i + ", " + j + "): " + p.isOpen(i, j) + " " + p.isFull(i, j));
			}
		}
		System.out.println(p.percolates());
	}
}