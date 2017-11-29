package jbadillo.datastruct;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Simple implementation of a heap/priority queue using an array tree.
 * Keeps the minimum value at the head.
 * - Get Min = O(1)
 * - Insert = O(log n)
 * - Remove min = O(log n)
 * @author jbadillo
 *
 */
public class Heap<T extends Comparable<T>> {

	private ArrayList<T> heap = new ArrayList<>();
	private Comparator<? super T> comp;
	
	public Heap(){
		// default comparator
		this((o1, o2) -> ((Comparable<T>)o1).compareTo(o2));
	}
	
	public Heap(Comparator<? super T> comp){
		this.comp = comp;
	}
	
	/**
	 * Adds a new value, keeping the minimum at the head
	 * @param x
	 */
	public void push(T x){
		// add at the end
		heap.add(x);
		// rebalance
		int child = heap.size()-1, parent = (child - 1)/2;
		
		// a[parent] > x
		while(child > 0 && comp.compare(heap.get(parent), x) > 0){
			// move parent to child, and move x to child
			heap.set(child, heap.get(parent));
			heap.set(parent, x);
			child = parent;
			parent = (child - 1) / 2;
		}
	}
	
	/**
	 * @return the min value, doesn't modify the heap
	 */
	public T peekMin(){
		return heap.get(0);
	}
	
	/**
	 * 
	 * @return the min value, and removes it from the heap.
	 */
	public T pop(){
		if(heap.size() == 1)
			return heap.remove(0);
		T m = heap.get(0);
		
		// add last element to the root
		T x = heap.remove(heap.size()-1);
		
		// rebalance
		int parent = 0;
		int child = 2 * parent + 1;
		while(child < heap.size()){
			// min of two children a[child] > a[child + 1]
			if(child < heap.size() - 1 && comp.compare(heap.get(child), heap.get(child + 1)) > 0)
				child++;
			// x < a[child] this is the correct place to keep it balanced
			if(comp.compare(x, heap.get(child)) < 0)
				break;
			// move the smallest child up
			heap.set(parent, heap.get(child));
			
			parent = child;
			child = 2*parent + 1;
		}
		heap.set(parent, x);
		return m;
	}

}
