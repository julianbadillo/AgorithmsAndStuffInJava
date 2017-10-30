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
	
	private int errorsFound;
	
	private int[] errorLocations;
	
	public RSCode(GaloisField gf, int k) {
		this.gf = gf;
		this.k = k;
		this.n = gf.getN();
		this.t = (n - k) / 2;
		setGeneratorPolynomial();
	}
	
	private int[] gx;
	/**
	 * @return Generator polynomial, n - k + 1 order
	 */
	public int[] getGx() {
		return gx;
	}
	
	/**
	 * @return max errors corrected
	 */
	public int getT() {
		return t;
	}
	
	/**
	 * @return block size
	 */
	public int getN() {
		return n;
	}
	
	/**
	 * @return message size
	 */
	public int getK() {
		return k;
	}
	
	/**
	 * @return location of errors found
	 */
	public int[] getErrorLocations() {
		return errorLocations;
	}
	
	/**
	 * @return number of errors found
	 */
	public int getErrorsFound() {
		return errorsFound;
	}
	
	private void setGeneratorPolynomial(){
		// set generator, a polynomial with roots alpha^i
		// multiply (x + alpha^i) for i [1, n - k]
		// in array representation Alpha^i + alpha^0 *X^1
		gx = new int[]{ gf.alphaTo(1) , 0b1};
		for (int i = 2; i <= n - k; i++)
			gx = gf.prod(gx, new int[]{gf.alphaTo(i), 0b1});
	}
	
	/**
	 * Generates a codeword Mx + Ck
	 * @param Mx - k-length message of symbols in GF
	 * @return A codeword n-length
	 */
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
	
	
	/**
	 * Extracts the message from the codeword, correcting
	 * errors if found
	 * @param Rx - an n-length codeword
	 * @return The k-length message.
	 */
	public int[] decode(int[] Rx){
		
		// calculate syndromes- components
		int Sx[] = new int[n-k];
		for (int i = 0; i < Sx.length; i++) 
			Sx[i] = gf.eval(Rx, gf.alphaTo(i+1));
		
		// if all syndromes are zero, return the value (last k positions)
		if(Arrays.stream(Sx).allMatch(i -> i == 0))
			return Arrays.copyOfRange(Rx, n - k, n);
		
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
		while (GaloisField.order(r) >= t) {
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
		
		// actual # of errors detected
		int T = o.length -1;
		
		// reciprocal polynomial
		int [] or = new int[T + 1];
		for (int i = 0; i <= T; i++)
			or[i] = o[T - i];
		
		// roots of or
		int [] z = new int[T];
		int [] x = new int[T];
		// evaluate or(alpha^i) on all field elements 
		for (int i = 0, j = 0; i < this.n && j < T; i++)
			if(gf.eval(or, gf.alphaTo(i)) == 0){
				// keep value and location
				z[j] = gf.alphaTo(i);
				x[j] = i;
				j++;
			}

		// solve the linear system
		// Si = SUM(Yj * Zj^i, for j in [1,T]), for i in [1, T] 
		int[][] A = new int[T][T+1];
		for (int i = 0; i < T; i++){
			for (int j = 0; j < T; j++) 
				// yj * Zj^i 
				A[i][j] = gf.pow(z[j], i+1);
			A[i][T] = Sx[i];
		}
		int[] y = solve(A);
		
		// noise estimate
		int [] Ex = new int[n];
		for (int i = 0; i < T; i++)
			Ex[x[i]] = y[i];
		
		// closest code-word
		int[] Cx = gf.add(Rx, Ex);
		// extract data
		int[] Mx = Arrays.copyOfRange(Cx, n - k, n);
		this.errorsFound = T;
		this.errorLocations = x;
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
	
	/**
	 * Solve linear system
	 * @param M
	 * @return
	 */
	protected int[] solve(int[][] M) {
		// elimination
		int rows = M.length;
		int cols = M.length + 1;
		for (int i = 0; i < rows; i++) {
			// divide all row by M[i,i] 
			int a = M[i][i];
			// pick any other row that has no zero on col i
			if(a == 0){
				int k;
				for (k = 0; k < rows && M[k][i] != 0; k++);
				if(k == rows)
					throw new RSCodeException("Non solvable system");
				
				// add it to row i
				for (int j = i; j < cols; j++)
					M[i][j] = gf.add(M[i][j], M[k][j]);
				a = M[i][i];
			}
			
			for (int j = i; j < cols; j++)
				M[i][j] = gf.div(M[i][j], a);
				
			
			// eliminate values on other rows of column i
			for (int k = 0; k < rows; k++) 
				if(i != k){
					int b = M[k][i];
					// add to row k, b times row i
					for (int j = i; j < cols; j++)
						M[k][j] = gf.add(M[k][j], gf.prod(b, M[i][j]));						
				}
		}
		
		// verify identity
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < rows; j++)
				if((i == j) && (M[i][j] != 1) ||
						(i != j) && (M[i][j] != 0))
					throw new RSCodeException("Non identity - not solvable system (" + i + "," + j + ")" + M[i][j]);
		
		// copy solution
		int[] x = new int[rows];
		for (int i = 0; i < rows; i++)
			x[i] = M[i][cols-1];
		return x;
	}
}

class RSCodeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public RSCodeException(String msg) {
		super(msg);
	}
}
