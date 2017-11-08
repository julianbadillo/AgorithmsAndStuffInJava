package jbadillo.numbers;
import static jbadillo.numbers.LongMath.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

public class LongMathTest {

	@Test
	public void testModProd(){
		// test less than 16 bit
		Random rand = new Random(System.currentTimeMillis());
		long mod = PrimeNumbers._16BIT_PRIMES[0];
		for (int i = 0; i < 1000; i++) {
			long a = rand.nextInt(1 << 17); // 16 bit
			long b = rand.nextInt(1 << 17); // 16 bit
			assertEquals(a * b % mod, modProd(a, b, mod));
		}
		
		// overflowing integers
		for (int i = 0; i < 1000; i++) {
			long a = rand.nextLong() % 0x10000000l; // 28 bit
			a = a < 0? -a: a;
			long b = rand.nextLong() % 0x10000000l; // 28 bit
			b = b < 0? -b: b;
			assertEquals(a * b % mod, modProd(a, b, mod));
		}
		
		// close to overflow long
		// 32 bit x 32 bit but elimination at the beginning (small modulo)
		assertEquals(0l, modProd(0xFAFCABC1l, 0xFAFCABC2l, 2l));
		// 32 bit x 32 bit with 32 bit modulo
		assertEquals(0x8145E059l, modProd(0xFAFCABC1l, 0xFAFCABC1l, 0xFFFFFFFFl));
		// 60 bit x 60 bit with 32 bit modulo
		assertEquals(0x7B4EF92Dl, modProd(0x1234567890ABCDEFl, 0x87654321ABCDEFl, 0xFFFFFFFFl));
		// 60 bit x 60 bit with 60 bit modulo
		assertEquals(298165650705736920l, modProd(0x1234567890ABCDEFl, 0x87654321ABCDEFl, 0xFFFFFFFFFFFFFFFl));
	}
	
	@Test
	public void testModPow(){
		
		assertEquals(2, modPow(2, 1, 10000));
		assertEquals(4, modPow(2, 2, 10000));
		assertEquals(8, modPow(2, 3, 10000));
		assertEquals(16, modPow(2, 4, 10000));
		assertEquals(32, modPow(2, 5, 10000));
		assertEquals(64, modPow(2, 6, 10000));
		assertEquals(128, modPow(2, 7, 10000));
		
		assertEquals(3, modPow(3, 1, 10000));
		assertEquals(9, modPow(3, 2, 10000));
		assertEquals(27, modPow(3, 3, 10000));
		assertEquals(81, modPow(3, 4, 10000));
		assertEquals(243, modPow(3, 5, 10000));
		assertEquals(729, modPow(3, 6, 10000));		
	}

	
	
	@Test
	public void testModPowBig(){
		int bits = 62;
		BigInteger a = new BigInteger(bits, new Random());
		BigInteger e = new BigInteger(bits, new Random());
		BigInteger m = new BigInteger(bits, new Random());
	
		for (int i = 0; i < 100; i++) {
			BigInteger r = a.modPow(e, m);
			assertEquals(r.longValue(), modPow(a.longValue(), e.longValue(), m.longValue()));
		}
		
	}
	
	@Test
	public void testIsProbablePrime(){
		for(int prime: PrimeNumbers._8BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		
		for(int prime: PrimeNumbers._9BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		
		for(int prime: PrimeNumbers._10BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._11BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._12BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._13BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._14BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._15BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
		for(int prime: PrimeNumbers._16BIT_PRIMES)
			assertTrue("Wrong in "+prime,isProbablePrime(prime, 200));
	}
	
	@Test
	public void testIsProbablePrimeFalse(){
		assertFalse(isProbablePrime(4, 200));
		assertFalse(isProbablePrime(6, 200));
		assertFalse(isProbablePrime(8, 200));
		assertFalse(isProbablePrime(9, 200));
		assertFalse(isProbablePrime(32, 200));
		assertFalse(isProbablePrime(48, 200));
		assertFalse(isProbablePrime(261, 200));
		assertFalse(isProbablePrime(1232, 200));
		assertFalse(isProbablePrime(4432, 200));
		assertFalse(isProbablePrime(23*17*51, 200));
		assertFalse(isProbablePrime(32770, 200));
		for(int prime: PrimeNumbers._16BIT_PRIMES)
			assertFalse("Wrong in "+(prime+1), isProbablePrime(prime+1, 200));
	}
}
