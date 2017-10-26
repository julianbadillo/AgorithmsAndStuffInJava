package jbadillo.numbers;

import java.util.Random;

public class LongMath {

	public static boolean isProbablePrime(long n, int certainty) {
		Random rand = new Random(System.currentTimeMillis());
		long a;
		// primality test
		for (int i = 0; i < certainty; i++) {
			a = rand.nextLong() % n;
			a = a < 0 ? a + n : a;
			// a ^ phi(n) = 1 (mod n)
			// if prime, phi(n) = n - 1
			if(gcd(n, a) != 1)
				continue;
			if (modPow(a, n - 1, n) != 1)
				return false;
		}
		return true;
	}

	public static long modPow(long b, long e, long mod) {
		long pow = b;
		long res = 1;
		while (e > 0) {
			if (e % 2 == 1)
				res = res * pow % mod;
			pow = pow * pow % mod;
			e = e >> 1;
		}
		return res;
	}
	
	public static long gcd(long a, long b){
		return new EuclidAlgorithm(a, b).gcd;
	}
	
	/**
	 * Module inverse. Assumes a,m relative primes
	 * @param a
	 * @param m
	 * @return a', s.t. a * a' = 1 (mod m)
	 */
	public static long modInv(long a, long m){
		// if a,m relative primes, gcd(a, m) = 1
		// a*s + m*t = gcd(a, m) = 1 can be solved using
		// euclid algorithm.
		return new EuclidAlgorithm(a, m).s;
	}

}

/**
 * An implementation of extended Euclid's algorithm
 * for long.
 * @author jbadillo
 *
 */
class EuclidAlgorithm{
	long a;
	long b;
	// where mod inverse is
	long s;
	// where the GCD is
	long r;
	long t;
	long gcd;
	
	/***
	 * Solves the equation 
	 * a*s + b*t = gcd(a,b) = r
	 * 
	 * @param a
	 * @param b
	 */
	public EuclidAlgorithm(long a, long b) {
		this.a = a;
		this.b = b;
		run();
	}
	
	
	private void run(){
		long s_old = 1;
		s = 0;
		
		long r_old = a;
		r = b;
		
		long t_old = 0;
		t = 1;
		
		long temp, q;
		while(r!= 0 && r_old % r != 0){
			q = r_old / r;
			temp = r;
			// r = r_old mod r 
			r = r_old - q*r;
			r_old = temp;
			
			// substract same quotient to s and t
			temp = s;
			s = s_old - q*s;
			s_old = temp;
			
			temp = t;
			t = t_old - q*t;
			t_old = temp;
		}
		gcd = r;
	}
}
