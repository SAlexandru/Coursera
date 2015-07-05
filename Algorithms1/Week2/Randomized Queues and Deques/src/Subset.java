public class Subset {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		
		if (k <= 0 || StdIn.isEmpty()) {
			return;
		}
		
		
		final String[] strings = new String[k];
		final Deque<String> deque = new Deque<String>();
		
		strings[0] = StdIn.readString();
		for (int i = 1; i < k && !StdIn.isEmpty(); ++i) {
			int j = StdRandom.uniform(i + 1);
			
			strings[i] = strings[j];
			strings[j] = StdIn.readString();
			deque.addFirst(strings[i]);
		}
		
		deque.addFirst("");
		
		for (int i = k; !StdIn.isEmpty(); ++i) {
			final int j = StdRandom.uniform(i + 1);
			final String s = StdIn.readString();
			if (j < k) {
				strings[j] = s;
			}
		}
		
		for (final String s: strings) {
			System.out.println(s);
		}
	}
}
