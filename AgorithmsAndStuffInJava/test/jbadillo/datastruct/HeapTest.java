package jbadillo.datastruct;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

import jbadillo.datastruct.Heap;

public class HeapTest {

	@Test
	public void testPushOnlySorted() {
		int[] a = {0,1,2,3,4,5,6,7,8,9};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++) {
			h.push(a[i]);
			assertEquals(0, (int)h.peekMin());
		}
	}

	@Test
	public void testPushOnlyScrambled() {
		int[] a = {6,7,8,9,0,1,2,3,4,5,11};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++) {
			h.push(a[i]);
		}
		assertEquals(0, (int)h.peekMin());
	}
	
	@Test
	public void testPushOnlyScrambled2() {
		int[] a = {11,10,9,8,7,5,3,2,1,0};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++) {
			h.push(a[i]);
		}
		assertEquals(0, (int)h.peekMin());
	}
	
	@Test
	public void testPopSorted() {
		int[] a = {0,1,2,3,4,5,6,7,8,9};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		for (int i = 0; i < a.length; i++)
			assertEquals(i, (int)h.pop());
	}
	
	@Test
	public void testPop2() {
		int[] a = {6,7,8,9,0,1,2,3,4,5};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		for (int i = 0; i < a.length; i++)
			assertEquals(i, (int)h.pop());
	}
	
	@Test
	public void testPopScrambled() {
		int[] a = {5,8,3,6,2,1,0,7,9,4};
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		for (int i = 0; i < a.length; i++)
			assertEquals(i, (int)h.pop());
	}
	@Test
	public void testFullRepetition() {
		int[] a = {4,3,5,2,3,1,9,1,4,4,1};
		int[] order = Arrays.copyOf(a, a.length);
		Arrays.sort(order);
		
		Heap<Integer> h = new Heap<>();
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		for (int i = 0; i < a.length; i++)
			assertEquals(order[i], (int)h.pop());
	}
	
	@Test
	public void testFullRandom(){
		Random rand = new Random(System.currentTimeMillis());
		int[] a = IntStream.range(0, 10000).toArray();
		// do random permutations
		int t, j = rand.nextInt(a.length);
		for (int i = 0; i < a.length; i++, j = rand.nextInt(a.length)) {
			t = a[i];
			a[i] = a[j];
			a[j] = t;
		}
		
		Heap<Integer> h = new Heap<>();
		
		// put in heap
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		// pop them all
		for (int i = 0; i < a.length; i++)
			assertEquals(i, (int)h.pop());
	}
	
	@Test
	public void testFullInverse(){
		Random rand = new Random(System.currentTimeMillis());
		int[] a = IntStream.range(0, 10000).toArray();
		// do random permutations
		int t, j = rand.nextInt(a.length);
		for (int i = 0; i < a.length; i++, j = rand.nextInt(a.length)) {
			t = a[i];
			a[i] = a[j];
			a[j] = t;
		}
		// inverse comparator
		Heap<Integer> h = new Heap<>((o1, o2) -> o2.compareTo(o1));
		
		// put in heap
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		// pop them all
		for (int i = 0; i < a.length; i++)
			assertEquals(a.length - i - 1, (int)h.pop());
	}
	
	@Test
	public void testStringsSorted(){
		String[] a = {"A", "AA", "AB", "BA", "BAA", "CDA"};
		
		Heap<String> h = new Heap<>();
		
		// put in heap
		for (int i = 0; i < a.length; i++)
			h.push(a[i]);
		// pop them all
		for (int i = 0; i < a.length; i++)
			assertEquals(a[i], h.pop());
	}
}
