import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


public class Deque <Item> implements Iterable<Item> {
	private int N_ = 0;
	private Node<Item> root_;
	private Node<Item> last_;
	
	
	public Deque() {
		N_ = 0;
	}
	
	public boolean isEmpty() {return 0 == N_;}
	public int     size()    {return N_;}
	
	public void addLast (Item item) {
		if (isEmpty()) {
			last_ = root_ = new Node<Item>(item);
		}
		else {
			final Node<Item> p = new Node<Item>(item);
		
			p.prev_ = last_;
			last_.next_ = p;
			last_ = p;
		}
		
		++N_;
	}
	
	public void addFirst (Item item) {
		final Node<Item> p = new Node<Item>(item);
		
		if (isEmpty()) {
			last_ = root_ = p;
		}
		else {
			p.next_ = root_;
			root_.prev_ = p;
			root_ = p;
		}
		
		++N_;
	}
	
	public Item removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		final Node<Item> p = root_;
		if (N_ > 1) {
			root_ = root_.next_;
			root_.prev_ = null; 
		}
		else {
			root_ = last_ = null;
		}
		--N_;
		return p.item_;
	}
	
	public Item removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		final Node<Item> p = last_;
		
		if (N_ > 1) {
			last_ = last_.prev_;
			last_.next_ = null;
		}
		else {
			root_ = last_ = null;
		}
		--N_;
		return p.item_;
	}
	
	
	@Override
	public Iterator<Item> iterator() {
		return new Iterable<Item>(root_);
	}
	
	private static class Iterable<T> implements Iterator<T> {
		private Node<T> p_;
		
		public Iterable(Node<T> p) {p_ = p;}

		@Override
		public boolean hasNext() {
			return null != p_;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final T t = p_.item_;
			p_ = p_.next_;
			return t;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private static class Node<T> {
		T item_;
		Node<T> next_, prev_;
		
		Node (T item) {
			if (null == item) {
				throw new NullPointerException();
			}
			item_ = item;
			prev_ = next_ = null;
		}
	}
	
	public static void main(String[] args) {
		final Deque <Integer> dQ = new Deque <Integer>();
		
		dQ.addLast(5);
		dQ.addLast(6);
		dQ.addLast(7);
		dQ.addLast(8);
		dQ.addLast(9);
		dQ.addLast(10);
		
		dQ.addFirst(4);
		dQ.addFirst(3);
		dQ.addFirst(2);
		dQ.addFirst(1);
		dQ.addFirst(0);
		
		for (final Integer x: dQ) {
			System.out.print (x + " ");
		}
		
		System.out.println("\nSize: " + dQ.size());
		
		final Random r = new Random();
		for (boolean x = r.nextBoolean(); !dQ.isEmpty(); x = r.nextBoolean()) {
			if (x) {
				System.out.println("Removed from begining: " + dQ.removeFirst());
			}
			else {
				System.out.println("Removed from back: " + dQ.removeLast());
			}
			System.out.print("Reamaining items: ");
			for (final Integer y: dQ) {
				System.out.print (y + " ");
			}
			System.out.println("\nSize: " + dQ.size() + "\n");
		}
		
		
	}

}
