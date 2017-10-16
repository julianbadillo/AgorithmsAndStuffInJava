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
	
	public static long modInv(long a, long m){
		return new EuclidAlgorithm(a, m).s_old;
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
	long s, s_old;// where mod inverse is
	long r, r_old;// where the GCD is
	long t, t_old;
	long q;
	long gcd;
	/***
	 * Solves the equation
	 * a*s + b*t = gcd(a,b)
	 * @param a
	 * @param b
	 */
	public EuclidAlgorithm(long a, long b) {
		this.a = a;
		this.b = b;
		run();
	}
	
	
	private void run(){
		s_old = 1;
		s = 0;
		
		r_old = a;
		r = b;
		
		t_old = 0;
		t = 1;
		
		long temp;
		while(r != 0){
			q = r_old / r;
			temp = r;
			r = r_old - q*r;
			r_old = temp;
			
			temp = s;
			s = s_old - q*s;
			s_old = temp;
			
			temp = t;
			t = t_old - q*t;
			t_old = temp;
		}
		gcd = r_old;
	}
}
