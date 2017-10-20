package jbadillo.algebra;

/**
 * An implementation of a extension binary Galois Field GF(2^m) as described in
 * https://ntrs.nasa.gov/search.jsp?R=19900019023
 * Elements correspond to binary polynomials in bit representation
 * p(x) = in*X^n + ... + i1 * X + i0 * 1
 * The most significative bit being the coefficient of the highest order term
 * of the polynomial.
 * @author jbadillo
 *
 */
public class GaloisField {

	/*
	 * n = 2^m - 1
	 */
	int n;
	
	/*
	 * The primitive polynomial. F(x) such that:
	 * F(x) divides the polynomial X^n + 1
	 * F(x) does NOT divide any polynomial X^i + 1 (i < n)
	 * It is represented in binary, so if
	 * F(x) = in*X^n + ... + i1 * X + i0 * 1
	 * Then in ... i1 i0 is the binary representation
	 * of this value, with the most significative bit
	 * being the coeficient of the highest order term
	 * of the polynomial.
	 * Fx = in*2^n + ... + i1 *2 + i0 * 1
	 */
	int Fx;
	
	/*
	 * Order of the primitive polynomial.
	 * The Field has 2^m elements
	 */
	int m;
	int bitFlag;
	
	public GaloisField(int m, int Fx){
		this.m = m;
		this.Fx = Fx;
		this.n = (1 << m) - 1;
		// TODO exception if Fx has more than m bits
		// TODO exception if m > 31
		generateElements();
	}
	/**
	 * alpha[i] = alpha^i, the value (binary representation of the polynomial)
	 */
	private int [] alpha;
	/**
	 * exp[alpha ^ i] = i, logarithm of the value (given the binary representation, tells you the exponent)
	 */
	private int [] exp;
	
	private void generateElements(){
		// generation one by one
		// TODO should be this done? or arithmetic on the fly
		this.alpha = new int[n + 1];
		this.exp = new int[n + 1];
		this.alpha[0] = 0b1; // alpha ^ 0 = 1
		this.exp[0b1] = 0;
		this.alpha[1] = 0b10; // alpha^1 = X
		this.exp[0b10] = 1;
		
		bitFlag = 0b11111111111111111111111111111111 << this.m;
		// generate the other elements
		for (int i = 2; i < alpha.length - 1; i++){
			// a^i = a * a^(i-1) = X * alpha^(i-1) mod Fx
			alpha[i] = alpha[i - 1] << 1;
			// bit flag on, do modulo Fx
			while((bitFlag & alpha[i]) != 0)
				alpha[i] ^= Fx;
			exp[alpha[i]] = i;
		}
	}
	
	public int getN() {
		return n;
	}
	public int getM() {
		return m;
	}
	
	/**
	 * @param exp
	 * @return alpha ^ (exp mod n)
	 */
	public int alphaTo(int exp){
		return this.alpha[exp % n];
	}
	
	/**
	 * Same as alphaTo but generating bitwise
	 *  = O(bits^2) complexity, more efficient if modulo
	 *  is more efficient
	 * @param exp
	 * @return alpha ^ (exp mod n)
	 */
	public int generate(int exp){
		exp %= n;
		// X^exp mod Fx
		return mod(0b1 << exp);
	}
	/**
	 * Add to elements of the field
	 * @param pol1: binary representation of the polynomial
	 * @param pol2: binary representation of the polynomial
	 * @return the binary representation of the addition
	 */
	public int add(int pol1, int pol2){
		// modulo FX
		pol1 = mod(pol1);
		pol2 = mod(pol2);
		
		// polynomial addition = xor the coeficients one by one
		int r = pol1 ^ pol2; 
		return r;
	}
	
	public int prod(int pol1, int pol2){
		// modulo FX
		pol1 = mod(pol1);
		pol2 = mod(pol2);
		
		// look for the exp
		int i1 = exp[pol1], i2 = exp[pol2];
		// alpha^i1 + alpha^i2 = alpha^(i1+i2)s
		return alpha[(i1 + i2)%n];
	}
	
	public int bitProd(int pol1, int pol2){
		// modulo FX
		pol1 = mod(pol1);
		pol2 = mod(pol2);
		// do the bit product O(bits^2)
		int prod = 0;
		while(pol2 != 0)
		{
			// last bit is 1
			if((pol2 & 1) != 0)
				prod ^= pol1;
			pol2 >>= 1;
			pol1 <<= 1;
		}
		return mod(prod);
	}
	
	/**
	 * Calculates residue of dividing
	 * pol in Fx
	 * @param pol binary representation
	 * @return
	 */
	public int mod(int pol){
		// while order >= m
		// if this is efficient, can generate any element / power on the fly
		int order = order(pol);
		int mod = Fx << (order - m);
		// O(bits^2) = can be reduced to O(bits)
		while(order >= m){
			// substract Fx*X^(order-m) from polynomial 
			// thus eliminating the highest order term
			pol ^= mod;
			// recalculate order
			order = order(pol);
			mod = Fx << (order - m);	
		}
		return pol;
	}
	
	/**
	 * The order of the polynomial representation
	 * i.e. the index (starting from 0) of the most significative
	 * bit that is 1
	 * @param pol binary representation
	 * @return m
	 */
	public static int order(int pol){
		int maxm = 0;
		int bit = 1;
		// O(bits)
		for(int m = 0; bit != 0; m++, bit = bit << 1)
			if((bit & pol) != 0)
				maxm = m;
		return maxm;
	}
}

