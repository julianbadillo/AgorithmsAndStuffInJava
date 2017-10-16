package jbadillo.numbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigInteger;

import org.junit.Test;

public class IntegerModuloPrimeTest {

	@Test
	public void testNthRoot() throws IntegerModuloPrimeException, MultiplicativeGroupException {
		MultiplicativeGroup mg = new MultiplicativeGroup(8);
		//MultiplicativeGroup mg = new MultiplicativeGroup(BigInteger.valueOf(5), BigInteger.valueOf(2), BigInteger.valueOf(4));
		// generate a multiplicative group
		// the generator is a primitive n-th root of unity
		int alpha = mg.getG().intValue();
		int p = mg.getN().intValue();
		int n = mg.getOrder().intValue(), r;
		
		// test that is a root of unity
		// alpha ^ n = 1 (mod p)
		r = (int) LongMath.modPow(alpha, n, p);
		assertEquals(1, r);
		
		// no other power is equals to 1
		for (int i = 1; i < n; i++) {
			r = (int) LongMath.modPow(alpha, i, p);
			assertNotEquals(1, r);
		}
		System.out.println(p + ", " + alpha);
		// sum is equals to zero, 1 <= k < n
		for (int k = 1; k < n; k++) {
			int s = 0;
			// sum ( alpha ^(j*k) )
			for (int j = 0; j < n; j++)
			{
				s += (int)LongMath.modPow(alpha, j*k, p);
				s %= p;
				//System.out.println(LongMath.modPow(alpha, j*k, p));
			}
			assertEquals(0, s);
		}
	}
	
	@Test
	public void testNthRoot2() throws IntegerModuloPrimeException, MultiplicativeGroupException {
		// get from a prime
		MultiplicativeGroup mg = new MultiplicativeGroup(BigInteger.valueOf(1021));
		
		// generate a multiplicative group
		// the generator is a primitive n-th root of unity
		int alpha = mg.getG().intValue();
		int p = mg.getN().intValue();
		int n = mg.getOrder().intValue(), r;
		
		// test that is a root of unity
		// alpha ^ n = 1 (mod p)
		r = (int) LongMath.modPow(alpha, n, p);
		assertEquals(1, r);
		
		// no other power is equals to 1
		for (int i = 1; i < n; i++) {
			r = (int) LongMath.modPow(alpha, i, p);
			assertNotEquals(1, r);
		}
		System.out.println(p + ", " + alpha);
		// sum is equals to zero, 1 <= k < n
		for (int k = 1; k < n; k++) {
			int s = 0;
			// sum ( alpha ^(j*k) )
			for (int j = 0; j < n; j++)
			{
				s += (int)LongMath.modPow(alpha, j*k, p);
				s %= p;
				//System.out.println(LongMath.modPow(alpha, j*k, p));
			}
			assertEquals(0, s);
		}
	}

}
