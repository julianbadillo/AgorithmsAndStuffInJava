package jbadillo.numbers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;


/**
 * An implementation of a multiplicative group over n
 * Zn*
 * It's an Abelian Group.
 * If n is 1,2,4 or, P^k or 2*p^k it's a cyclic group
 * 
 * @author jbadillo
 *
 */
public class MultiplicativeGroup {

	/**
	 * Number above this threshold may not be factorized
	 */
	public static final int FACTORIZATION_THRESHOLD = 32;
	private BigInteger n;
	private BigInteger order;
	private BigInteger phi;
	private Random rand = new Random(System.currentTimeMillis());
	private BigInteger g;
	private boolean primitiveRoot;
	
	/**
	 * Makes a Multiplicative group from standard parameters already produced
	 * @param n
	 * @param g
	 * @param order
	 * @throws MultiplicativeGroupException 
	 */
	public MultiplicativeGroup(BigInteger n, BigInteger g, BigInteger order) throws MultiplicativeGroupException {
		this.n = n;
		this.g = g;
		this.order = order;
		if(!n.gcd(g).equals(ONE))
			throw new MultiplicativeGroupException("g doesn't belong to the group - has common divisors with n");
	}
	
	/**
	 * Makes a multiplicative group with modulus an generator,
	 * Calculates the orde
	 * @param n
	 * @param g
	 * @throws MultiplicativeGroupException
	 */
	public MultiplicativeGroup(BigInteger n, BigInteger g) throws MultiplicativeGroupException{
		this.n = n;
		this.g = g;
		
		if(!n.gcd(g).equals(ONE))
			throw new MultiplicativeGroupException("g doesn't belong to the group - has common divisors with n");
		
		// factorize n
		Map<BigInteger, Integer> factors = factorize(this.n);
		this.phi = totientFormula(factors);
		
		// totient factors
		Map<BigInteger, Integer> phiFactors = factorize(this.phi);
		
		// calculate Charmicael's function
		BigInteger minDiv = this.phi;
		for (BigInteger factor : phiFactors.keySet()) {
			// g ^ p (mod n)  
			if(this.g.modPow(factor, this.n).equals(ONE) && factor.compareTo(minDiv) < 0)
				minDiv = factor;
			// g ^ (phi(n)/p) (mod n)
			BigInteger div = this.phi.divide(factor);
			if (this.g.modPow(div, this.n).equals(ONE) && div.compareTo(minDiv) < 0)
				minDiv = div;
		}
		this.order = minDiv;
		if (minDiv.equals(this.phi))
			this.primitiveRoot = true;
	}
	
	private BigInteger q;
	/**
	 * Generates a cyclic multiplicative group of the given size
	 * It uses a Sophie Germain prime so the order is easy to calculate.
	 * Only to generate new groups (is slow by default)
	 * @param bits
	 */
	public MultiplicativeGroup(int bits){
		// generating a Sophie Germain prime
		this.q = BigInteger.probablePrime(bits-1, rand);
		// n = 2*q + 1
		this.n = this.q.shiftLeft(1).add(ONE);
		// keep picking
		while(!this.n.isProbablePrime(200)){
			q = BigInteger.probablePrime(bits-1, rand);
			this.n = q.shiftLeft(1).add(ONE);
		}
		// order = phi is easy to calculate
		this.order = n.subtract(ONE);
		this.phi = this.order;
		
		// set a generator knowing the structure of the prime
		// since n = 2*q + 1, hence 2 and q are the only factors of phi(n)
		LinkedList<BigInteger> factors = new LinkedList<>();
		factors.add(valueOf(2l));
		factors.add(q);
		
		this.g = new BigInteger(bits, rand).mod(n);
		// if any of the two factors g^f = 1 (mod n), pick another one
		while(factors.stream().anyMatch(f -> this.g.modPow(f, n).equals(ONE)))
			this.g = new BigInteger(bits, rand).mod(n);
	}
	
	/**
	 * If Cyclic - equals to Phi
	 * @return
	 */
	public BigInteger getOrder() {
		return order;
	}
	
	/**
	 * Generator
	 * @return
	 */
	public BigInteger getG() {
		return g;
	}
	
	/**
	 * Modulus
	 * @return
	 */
	public BigInteger getN() {
		return n;
	}
	
	/**
	 * Phi(n) = Euler Totient's formula
	 * @return
	 */
	public BigInteger getPhi() {
		return phi;
	}
	
	/**
	 * @param a
	 * @return true if a is in the group
	 */
	public boolean inGroup(BigInteger a){
		return a.gcd(this.n).equals(ONE);
	}
	
	/**
	 * 
	 * @param e
	 * @return g ^ e (mod n)
	 */
	public BigInteger generate(BigInteger e){
		return g.modPow(e, n);
	}
	
	/**
	 * A random generated element
	 * @return
	 */
	public BigInteger getRandom(){
		BigInteger e = new BigInteger(order.bitLength(), rand);
		return generate(e);
	}
	
	/**
	 * @param a
	 * @return Multiplicative inverse of a, s.t. a * inv(a) = 1 (mod n)
	 */
	public BigInteger inv(BigInteger a){
		return a.modInverse(n);
	}
	
	/**
	 * @return true if g is a primitive root
	 */
	public boolean isPrimitiveRoot() {
		return primitiveRoot;
	}

	/**
	 * 
	 * @param n
	 * @return map of (prime factor, exponent) s.t. n = prod p ^ k
	 * @throws MultiplicativeGroupException
	 */
	public static Map<BigInteger, Integer> factorize(BigInteger n) throws MultiplicativeGroupException{
		// if it's probable prime, easy case
		if(n.isProbablePrime(200)){
			TreeMap<BigInteger, Integer> factors = new TreeMap<>();
			factors.put(n, 1);
			return factors;
		}
		
		if(n.bitCount() > FACTORIZATION_THRESHOLD)
			throw new MultiplicativeGroupException("Modulus n is too big to factorize");
		
		TreeMap<BigInteger, Integer> factors = new TreeMap<>();
		
		// all primes up to 15 bit
		LinkedList<Integer> primes = new LinkedList<>();
		for(int i : PrimeNumbers._8BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._9BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._10BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._11BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._12BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._13BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._14BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._15BIT_PRIMES) primes.add(i);
		for(int i : PrimeNumbers._16BIT_PRIMES) primes.add(i);
		
		Iterator<Integer> ite = primes.iterator();
		while(n.compareTo(ONE) > 0){
			BigInteger prime = valueOf(ite.next());
			int exp = 0;
			// while n % prime == 0
			while(n.mod(prime).equals(ZERO)){
				exp ++;
				n = n.divide(prime);
			}
			// keep the prime and power
			if(exp > 0)
				factors.put(prime, exp);
		}
		return factors;
	}
	
	/**
	 * @param n
	 * @return Phi (Euler's totient function) of n
	 * @throws MultiplicativeGroupException
	 */
	public static BigInteger totient(BigInteger n) throws MultiplicativeGroupException{
		if(n.isProbablePrime(200))
			return n.subtract(ONE);
		return totientFormula(factorize(n));
	}
	
	private static BigInteger totientFormula(Map<BigInteger, Integer> factors){
		// Euler's formula
		BigInteger phi = ONE;
		for (Entry<BigInteger, Integer> fact : factors.entrySet()) {
			// phi *= (p - 1)
			phi = phi.multiply(fact.getKey().subtract(ONE));
			// phi *= p ^ (k-1)
			if(fact.getValue() > 0)
				phi = phi.multiply(fact.getKey().pow(fact.getValue() - 1));
		}
		return phi;
	}
	
	public static class MultiplicativeGroupException extends Exception{
		private static final long serialVersionUID = 1L;

		public MultiplicativeGroupException(String msg) {
			super(msg);
		}
	}
}
