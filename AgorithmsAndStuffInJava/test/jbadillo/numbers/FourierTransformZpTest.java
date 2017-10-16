package jbadillo.numbers;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class FourierTransformZpTest {

	
	@Test
	public void testTransformMinN() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(1000);
		assertTrue(ftzp.getN() >= 1000);
		
		ftzp = new FourierTransformZp(2000);
		assertTrue(ftzp.getN() >= 2000);
		
		ftzp = new FourierTransformZp(500);
		assertTrue(ftzp.getN() >= 500);
		
		ftzp = new FourierTransformZp(700);
		assertTrue(ftzp.getN() >= 700);
	}
	
	@Test
	public void testTransformMinNMiddle() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(2000);
		int[] x = new int[ftzp.getN()];
		int[] X = new int[ftzp.getN()];
		int[] x2 = new int[ftzp.getN()];
		
		// random
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < x.length; i++) 
			x[i] = rand.nextInt(ftzp.getP());
		X = ftzp.transform(x);
		x2 = ftzp.inverse(X);
		assertArrayEquals(x, x2);
	}
	
	@Test
	public void testTransformSmall() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(4, 5, 2);
		
		int[] x = {1,1,1,1};
		
		int[] X = ftzp.transform(x);
		assertArrayEquals(new int[]{4, 0, 0, 0}, X);
	}
	
	@Test
	public void testInverseSmall() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(4, 5, 2);
		int[] X = {1,1,1,1};
		int[] x = ftzp.inverse(X);
		assertArrayEquals(new int[]{1, 0, 0, 0}, x);
	}
	
	@Test
	public void testFullTransformSmall() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(4, 5, 2);
		int[] x = {1,1,1,1};
		int[] X = ftzp.transform(x);
		int[] x2 = ftzp.inverse(X);	
		assertArrayEquals(x, x2);
		
		// random
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < x.length; i++) 
			x[i] = rand.nextInt(ftzp.getP());
		X = ftzp.transform(x);
		x2 = ftzp.inverse(X);
		assertArrayEquals(x, x2);
	}
	
	@Test
	public void testFullTransformMiddle() throws FourierTransformZpException {
		FourierTransformZp ftzp = new FourierTransformZp(126, 127, 3);
		int[] x = new int[ftzp.getN()];
		int[] X = new int[ftzp.getN()];
		int[] x2 = new int[ftzp.getN()];
		
		// random
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < x.length; i++) 
			x[i] = rand.nextInt(ftzp.getP());
		X = ftzp.transform(x);
		x2 = ftzp.inverse(X);
		assertArrayEquals(x, x2);
	}
}
