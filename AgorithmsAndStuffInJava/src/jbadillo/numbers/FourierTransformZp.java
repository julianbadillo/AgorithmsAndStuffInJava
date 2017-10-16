package jbadillo.numbers;

import static jbadillo.numbers.LongMath.modInv;
import static jbadillo.numbers.LongMath.modPow;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.management.RuntimeErrorException;

/**
 * A Fourier Transform based on a Zp group.
 * @author jbadillo
 *
 */
public class FourierTransformZp {

	private int n, p, alpha;
	private int[][] forward;
	private int[][] inverse;

	/**
	 * From given parameters, Alpha is a nth primitive root of unity (in Z
	 * modulo p) p must be prime.
	 * 
	 * @param n
	 * @param p
	 * @param alph
	 */
	public FourierTransformZp(int n, int p, int alpha) {
		this.n = n;
		this.p = p;
		this.alpha = alpha;
		initializeMatrices();
	}

	/**
	 * Makes a fourier transform with a capacity greater or equal
	 * to minN
	 * @param minN
	 */
	public FourierTransformZp(int minN) {
		try {
			// if we have a prime number in the list
			if (minN < PrimeNumbers._16BIT_PRIMES[PrimeNumbers._16BIT_PRIMES.length - 1]){	
				this.p = getGreaterOrEqualPrime(minN - 1);
				this.n = this.p - 1;
			}
			// build a multiplicative group to a generator
			MultiplicativeGroup mg = new MultiplicativeGroup(BigInteger.valueOf(this.p));
			this.alpha = mg.getG().intValue();
			initializeMatrices();
		} catch (MultiplicativeGroupException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param n
	 * @return a prime that is >= than n
	 */
	private int getGreaterOrEqualPrime(int n){
		ArrayList<int[]> primeArrs = new ArrayList<>();
		primeArrs.add(PrimeNumbers._8BIT_PRIMES);
		primeArrs.add(PrimeNumbers._9BIT_PRIMES);
		primeArrs.add(PrimeNumbers._10BIT_PRIMES);
		primeArrs.add(PrimeNumbers._11BIT_PRIMES);
		primeArrs.add(PrimeNumbers._12BIT_PRIMES);
		primeArrs.add(PrimeNumbers._13BIT_PRIMES);
		primeArrs.add(PrimeNumbers._14BIT_PRIMES);
		primeArrs.add(PrimeNumbers._15BIT_PRIMES);
		primeArrs.add(PrimeNumbers._16BIT_PRIMES);
		int primes[] = new int[primeArrs.stream()
		                       .mapToInt(arr -> arr.length)
		                       .sum()];
		int i = 0;
		for(int[] arr: primeArrs)
			for (int prime: arr) 
				primes[i++] = prime;
		
		for (int j = 0; j < primes.length - 1; j++)
			if (primes[j] >= primes[j + 1])
				throw new RuntimeErrorException(null, "Not sorted " + j + ": " + primes[j] + ", " + primes[j + 1]);
		
		// search and get a prime than is great or equal 
		int index = Arrays.binarySearch(primes, n);
		if(index < 0) index = - index;
		while(primes[index] <= n)
			index++;
		return primes[index];
	}

	/**
	 * @return The Prime modulus
	 */
	public int getP() {
		return p;
	}

	/**
	 * @return N - capacity
	 */
	public int getN() {
		return n;
	}

	
	private void initializeMatrices() {
		// set coef values
		// f = alpha ^ k*j
		forward = new int[n][n];
		for (int k = 0; k < n; k++)
			for (int j = 0; j < n; j++)
				forward[k][j] = (int) modPow(this.alpha, k * j, this.p);

		inverse = new int[n][n];
		int ninv = (int) modInv(n, this.p), ainv = (int) modInv(alpha, this.p);
		// make positive
		if (ninv < 0)
			ninv += this.p;
		if (ainv < 0)
			ainv += this.p;

		// finv = (1/n)alpha ^(-j*k)
		for (int k = 0; k < n; k++)
			for (int j = 0; j < n; j++)
				inverse[k][j] = ninv * (int) modPow(ainv, k * j, this.p) % this.p;
	}

	/**
	 * Applies forward transform
	 * @param v
	 * @return
	 * @throws FourierTransformZpException
	 */
	public int[] transform(int[] v) throws FourierTransformZpException {
		if (v.length != n)
			throw new FourierTransformZpException("Array size needs to be " + n);
		int f[] = new int[n];
		// X = x * forward
		for (int k = 0; k < n; k++) {
			for (int j = 0; j < n; j++){
				f[k] += v[j] * forward[k][j];
				f[k] %= p;
			}
		}
		return f;
	}

	/**
	 * Applies inverse transform
	 * @param f
	 * @return
	 * @throws FourierTransformZpException
	 */
	public int[] inverse(int[] f) throws FourierTransformZpException {
		if (f.length != n)
			throw new FourierTransformZpException("Array size needs to be " + n);
		int v[] = new int[n];
		// x = X * inverse
		for (int j = 0; j < n; j++) {
			for (int k = 0; k < n; k++){
				v[j] += f[k] * inverse[j][k];
				v[j] %= p;
			}
		}
		return v;
	}
}

class FourierTransformZpException extends Exception {
	private static final long serialVersionUID = 1L;

	public FourierTransformZpException(String msg) {
		super(msg);
	}
}
