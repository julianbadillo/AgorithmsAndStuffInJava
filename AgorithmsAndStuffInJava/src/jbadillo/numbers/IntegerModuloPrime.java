package jbadillo.numbers;

/**
 * A finite field of all integers modulo prime
 * @author jbadillo
 *
 */
public class IntegerModuloPrime {
	
	private int p;
	private int alpha;
	
	public IntegerModuloPrime(int p) throws IntegerModuloPrimeException {
		this.p = p;
		if(!LongMath.isProbablePrime(p,200))
			throw new IntegerModuloPrimeException("Not a prime number");
	}

	public long getP() {
		return p;
	}
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getAlpha() {
		return alpha;
	}
}

class IntegerModuloPrimeException extends Exception{
	private static final long serialVersionUID = 1L;
	public IntegerModuloPrimeException(String msg) {
		super(msg);
	}
}