package jbadillo.daq;

import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

public class MedianTest {

	@Test
	public void testMedianSorted() {
		int [] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		int median = Median.median(arr);
		assertEquals(5, median);
	}
	
	@Test
	public void testMedianInverse() {
		int[] arr = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
		int median = Median.median(arr);
		assertEquals(5, median);
	}
	
	@Test
	public void testMedianScramble() {
		int[] arr = { 9, 0, 7, 6, 3, 4, 2, 1, 5, 8 };
		int median = Median.median(arr);
		assertEquals(5, median);
	}
	
	@Test
	public void testMedianAllEqual() {
		int[] arr = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		int median = Median.median(arr);
		assertEquals(5, median);
	}
	
	
	@Test
	public void testMedianScrambleBigger() {
		int[] arr = IntStream.range(0, 1000).toArray();
		Random r = new Random();
		// random permutations
		for(int i=0; i<arr.length; i++)
		{
			int j = r.nextInt(arr.length);
			int t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
		
		int median = Median.median(arr);
		assertEquals(500, median);
	}
	
	
	@Test
	public void testKselectSorted() {
		int [] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		for (int k = 0; k < arr.length; k++) {
			int r = Median.kselect(arr, k);
			assertEquals(k, r);	
		}		
	}
	
	@Test
	public void testKselectInverse() {
		int[] arr = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
		for (int k = 0; k < arr.length; k++) {
			int r = Median.kselect(arr, k);
			assertEquals(k, r);	
		}		
	}
	
	@Test
	public void testKselectScramble() {
		int[] arr = { 9, 0, 7, 6, 3, 4, 2, 1, 5, 8 };
		for (int k = 0; k < arr.length; k++) {
			int r = Median.kselect(arr, k);
			assertEquals(k, r);	
		}		
	}
	
	@Test
	public void testKselectAllEqual() {
		int[] arr = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		for (int k = 0; k < arr.length; k++) {
			int r = Median.kselect(arr, k);
			assertEquals(5, r);	
		}
	}
	
	@Test
	public void testKselectScrambleBigger() {
		int[] arr = IntStream.range(0, 1000).toArray();
		Random rand = new Random();
		// random permutations
		for(int i=0; i<arr.length; i++)
		{
			int j = rand.nextInt(arr.length);
			int t = arr[i];
			arr[i] = arr[j];
			arr[j] = t;
		}
		
		for (int k = 0; k < arr.length; k++) {
			int r = Median.kselect(arr, k);
			assertEquals(k, r);	
		}
	}
}
