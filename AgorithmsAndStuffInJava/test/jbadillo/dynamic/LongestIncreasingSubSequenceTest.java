package jbadillo.dynamic;

import static jbadillo.dynamic.LongestIncreasingSubSequence.longestIncSubSec;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class LongestIncreasingSubSequenceTest {

	@Test
	public void longestIncSubSecTestShort() {
		assertArrayEquals(new int[]{1,2,3,4,5,6,7,8,9}, longestIncSubSec(new int[]{1,2,3,4,5,6,7,8,9}));
		assertArrayEquals(new int[]{2,3,6,9}, longestIncSubSec(new int[]{5,2,8,6,3,6,9,5}));
		assertArrayEquals(new int[]{2,3,6,9}, longestIncSubSec(new int[]{7,2,8,6,3,6,9,5}));
	}
	
	@Test
	public void longestIncSubSecTestRandom() {
		Random rand = new Random(System.currentTimeMillis());
		
		int [] arr = new int[1000];
		int t, j;
		for (int i = 0; i < arr.length; i++) {
			j = rand.nextInt(arr.length);
			// swap
			t = arr[j];
			arr[j] = arr[i];
			arr[i] = t;
		}
		
		
		int [] inc = longestIncSubSec(arr);
		// verify that it is contained
		assertTrue(inc.length <= arr.length);
		int i;
		for (i = 0, j = 0; i < inc.length && j < arr.length; i++) {
			for(; j<arr.length; j++)
				if(inc[i] == arr[j])
					break;
		}
		assertEquals(inc.length, i);
		
	}

}
