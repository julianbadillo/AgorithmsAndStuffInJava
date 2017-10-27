package jbadillo.algebra;

import java.util.Arrays;

/**
 * An implementation of Reed-Solomon Code
 * Based on GF(2^m)
 * @author jbadillo
 *
 */
public class RSCode {

	private GaloisField gf;
	private int t;
	/**
	 * Size of the whole block (encoded)
	 */
	private int n;
	/**
	 * Size of the message
	 */
	private int k;
	
	
	public RSCode(GaloisField gf, int k) {
		this.gf = gf;
		this.k = k;
		this.n = gf.getN();
		this.t = (n - k) / 2;
		setGeneratorPolynomial();
	}
	
	/**
	 * Generator polynomial, n - k + 1 order
	 */
	private int[] gx;
	
	public int[] getGx() {
		return gx;
	}
	
	public int getT() {
		return t;
	}
	
	public int getN() {
		return n;
	}
	
	public int getK() {
		return k;
	}
	
	private void setGeneratorPolynomial(){
		// set generator, a polynomial with roots alpha^i
		// multiply (x + alpha^i) for i [1, n - k]
		// in array representation Alpha^i + alpha^0 *X^1
		gx = new int[]{ gf.alphaTo(1) , 0b1};
		for (int i = 2; i <= n - k; i++)
			gx = gf.prod(gx, new int[]{gf.alphaTo(i), 0b1});
	}
	
	public int[] encode(int[] Mx){
		if(Mx.length > k)
			throw new RSCodeException("Mx should be equal or lower than " + k);
		
		// get codeword
		// CKx = X^(n-k) * Mx (mod gx)
		Mx = gf.prodXToN(Mx, 1, n - k);
		int[] CKx = mod(Mx);
		
		int[] Cx = new int[n];
		for (int i = 0; i < Mx.length; i++)
			Cx[i] = Mx[i];
		
		// add message at the end (higher degree)
		for (int i = 0; i < CKx.length; i++)
			Cx[i] = CKx[i];
		return Cx;
	}
	
	
	
	public int[] decode(int[] Rx){
		
		// calculate syndromes- components
		int Sx[] = new int[n-k];
		for (int i = 0; i < Sx.length; i++) 
			Sx[i] = gf.eval(Rx, gf.alphaTo(i+1));
		
		// TODO if all syndromes are zero, skip ahead
		
		// Sx as a polynomial

		// solve the equation
		// Sx * ox + X^(2t)*B(x) = r(x) s.t. degree of r(x) <= t
		// using Euclid's Algorithm
		// starting with
		// Sx * 0 + X^(2t) * 1 = X^(2t)
		// Sx * 1 + X^(2t) * 0 = Sx
		int [] r_old = new int[2*this.t+1]; // max errors accepted
		r_old[2*this.t] = 1;
		
		int[] r = Sx;
		
		int [] o_old = new int[]{0};
		int [] o = new int[]{1};
		int [] q, temp;
		
		// stop when degree of r(x) <= t
		// O(n^2*log n) worst case
		while (GaloisField.order(r) > t) {
			q = gf.div(r_old, r);
			temp = r;
			// r = r_old - q*r = r_old mod r
			r = gf.mod(r_old, r);
			r_old = temp;
			
			temp = o;
			// o = o_old - q*o
			o = gf.add(o_old, gf.prod(q, o));
			o_old = temp;
		}
		
		// reciprocal polynomial
		int [] or = new int[o.length];
		for (int i = 0; i < o.length; i++)
			or[i] = o[o.length-i-1];
		
		// roots of or
		int [] zi = new int[or.length-1];
		int [] xi = new int[or.length-1];
		// evaluate or(alpha^i) on all field elements 
		for (int i = 0, j = 0; i < this.n && j < zi.length; i++)
			if(gf.eval(or, gf.alphaTo(i)) == 0){
				// keep value and location
				zi[j] = gf.alphaTo(i);
				xi[j] = i;
				j++;
			}
		
		// now get the values
		int [] yi = new int[zi.length];
		for (int i = 0; i < xi.length; i++) {
			int num = gf.eval(r, gf.inv(zi[i]));
			int den = 1;
			// all roots of or, different than i
			for (int j = 0; j < yi.length; j++)
				if(j != i)
					den = gf.prod(den, gf.add(zi[i], zi[j]));
			yi[i] = gf.div(num, den);
		}
		
		// noise estimate
		int [] Ex = new int[n];
		for (int i = 0; i < yi.length; i++)
			Ex[xi[i]] = yi[i];
		
		// closest code-word
		int[] Cx = gf.add(Rx, Ex);
		// extract data
		int[] Mx = Arrays.copyOfRange(Cx, n - k, n);
		return Mx;
	}
	
	/**
	 * Computes polynomial, modulo gx
	 * @param Px polynomial with coefficients in GF in array representation 
	 * s.t.  Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * @return resulting polynomial with order less than n - k
	 */
	public int[] mod(int [] Px){
		return gf.mod(Px, this.gx);
	}
	
}

class RSCodeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public RSCodeException(String msg) {
		super(msg);
	}
}
