import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N_ ;
	private int idx_;
	private Item[] items_;
	
	@SuppressWarnings("unchecked")
	public RandomizedQueue () {
		N_ = 0;
		idx_ = 0;
		items_ = (Item[])new Object[8];
	}
	
	public boolean isEmpty() {return 0 == N_;}
	public int     size()    {return N_;}
	
	public void enqueue (Item item) {
		if (null == item) {
			throw new NullPointerException();
		}
		
		if (idx_ >= items_.length) {
			resize (items_.length << 1);
		}
		
		items_[idx_++] = item;
		++N_;
	}
	
	public Item sample() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		return items_[choose()];
	}
	
	public Item dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		int choosen = choose();
		final Item item = items_[choosen];
		items_[choosen] = null;
		
		--N_;
		if (N_ <= (items_.length >> 2)) {
			resize (items_.length >> 1);
		}
		
		return item;
	}
	
	private int choose() {
		int i;
		
		do {
			i = StdRandom.uniform(items_.length);
		} while (null == items_[i]);
		
		return i;
	}

	private void resize(int newSize) {
		if (0 == newSize) return;
		
		@SuppressWarnings("unchecked")
		Item[] newArray = (Item[])new Object[newSize];
		
		for (int i = 0, j = 0; i < items_.length; ++i) {
			if (null != items_[i]) {
				newArray[j++] = items_[i];
			}
		}

		idx_ = N_;
		items_ = newArray;
	}

	@Override
	public Iterator<Item> iterator() {
		@SuppressWarnings("unchecked")
		final Item[] sequence = (Item[])new Object[N_];
		
		for (int i = 0, j = 0; i < items_.length; ++i) {
			if (null != items_[i]) {
				sequence[j++] = items_[i];
				
				final int newJ = StdRandom.uniform(j);
				
				final Item swap = sequence[j - 1];
				sequence[j - 1] = sequence[newJ];
				sequence[newJ] = swap;
			}
		}
		
		return new Iterator<Item>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < sequence.length;
			}

			@Override
			public Item next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				
				return sequence[i++];
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	public static void main(String[] args) {
		final RandomizedQueue<Integer> rq = new RandomizedQueue<>();
		
		final Random random = new Random();
		
		for (int i = 0; i < 20; ++i) {
			if (random.nextBoolean()) {
				System.out.println("enqueue");
				rq.enqueue(i);
			}
			else {
				try {
					System.out.println("dequeue");
					rq.dequeue();
				}
				catch (Exception e) {
					
				}
			}
		}
	
		
		
		for (int i = 0; i < 10; ++i) {
			rq.enqueue(i);
		}
		
		System.out.println ("Size: " + rq.size());
		for (int i = 0; i < 10; ++i) {
			System.out.println("Sample number: " + i + ": " + rq.sample());
		}
		
		System.out.println("\n");
		for (int i = 0; i < 10; ++i) {
			System.out.print("Sequence number: " + i + ":  " );
			for (final Integer x: rq) {
				System.out.print(x + " ");
			}
			System.out.println();
		}
		
		while (!rq.isEmpty()) {
			System.out.println("Sample: " + rq.dequeue() + " Size: " + rq.size());
		}
	}

}
