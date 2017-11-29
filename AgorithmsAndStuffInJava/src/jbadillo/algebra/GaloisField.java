package jbadillo.algebra;

import java.util.Arrays;

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
	private int n;
	
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
	private int Fx;
	
	/*
	 * Order of the primitive polynomial.
	 * The Field has 2^m elements
	 */
	private int m;
	private int bitFlag;
	
	public static final int PRE_CALCULATE_THRESHOLD = 1024;
	public static final int POLY_DEGREE_THRESHOLD = 30;
	
	public GaloisField(int m, int Fx){
		this.m = m;
		if(m > POLY_DEGREE_THRESHOLD)
			throw new GaloisFieldException("Polynomial degree larger than "+POLY_DEGREE_THRESHOLD+" is not supported");
		
		this.Fx = Fx;
		this.n = (1 << m) - 1;
		
		///*
		if(n < PRE_CALCULATE_THRESHOLD)		
			generateElements();//*/
	}
	/**
	 * alpha[i] = alpha^i, the value (binary representation of the polynomial)
	 */
	private int [] alpha;
	
	/**
	 * exp[alpha ^ i] = i, logarithm of the value (given the binary representation, tells you the exponent)
	 * only preloaded if not using much memory
	 */
	private int [] exp;
	
	private void generateElements(){
		
		// if below threshold, all elements can be pre-calculated and stored on memory
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
	
	/**
	 * 2^m - 1
	 * @return
	 */
	public int getN() {
		return n;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getM() {
		return m;
	}
	
	/**
	 * O(bits) if not preloaded
	 * @param exp
	 * @return alpha ^ (exp mod n)
	 */
	public int alphaTo(int exp){
		exp %= n;
		if(exp < 0)
			exp += n;
		// if preloaded
		if(alpha != null)
			return this.alpha[exp];
		// X^exp mod Fx
		// O(bits^2) complexity
		if(exp <= POLY_DEGREE_THRESHOLD)
			return mod(1 << exp);
		
		// break exponent in parts to avoid overflowing
		// X ^ exp = X ^ T(exp / T) * X^(exp % T) mod FX
		int e1 = mod(1 << POLY_DEGREE_THRESHOLD);
		int e2 = mod(1 << (exp % POLY_DEGREE_THRESHOLD));
		int r = e2;
		for (int i = 0; i < exp / POLY_DEGREE_THRESHOLD; i++) 
			r = prod(r, e1);
		return r;	
	}
	
	/**
	 * Add two elements of the field, 
	 * O(bits)
	 * @param pol1
	 * @param pol2
	 * @return pol1 + pol2
	 */
	public int add(int pol1, int pol2){
		// polynomial addition = xor the coefficients one by one
		int r = pol1 ^ pol2;
		// modulo FX
		return mod(r);
	}
	
	/**
	 * Multiplies two elements of the field
	 * O(bits^2) if not preloaded
	 * @param pol1
	 * @param pol2
	 * @return pol1 * pol2
	 */
	public int prod(int pol1, int pol2){
		if(pol1 == 0 || pol2 == 0)
			return 0;
		// modulo FX
		pol1 = mod(pol1);
		pol2 = mod(pol2);
		
		// if pre-loaded we look at exponents
		if(alpha != null){
			int i1 = exp[pol1], i2 = exp[pol2];
			// alpha^i1 + alpha^i2 = alpha^(i1+i2)s
			return alpha[(i1 + i2)%n];
		}
		// if not, do bitwise multiplication
		// O(bits^2)
		int prod = 0;
		while(pol2 != 0)
		{
			// last bit is 1
			if((pol2 & 1) != 0)
				prod ^= pol1;
			// pol1 = 2*pol1 mod Fx (to avoid overflowing)
			pol1 = mod(pol1<<1);
			// pol2 = pol2 / 2
			pol2 >>= 1;
		}
		return mod(prod);
	}
	
	/**
	 * Exponentiates
	 * @param base
	 * @param exp
	 * @return base ^ exp (mod fx)
	 */
	public int pow(int base, int exp){
		base = mod(base);
		if(base == 0)
			return 0;
		exp %= n;
		if(exp < 0)
			exp += n;
		// one
		if(exp == 0)
			return alphaTo(0);
		
		// if pre-loaded
		if(alpha != null){
			// get exponent
			int exp1 = this.exp[base];
			// alpha ^ exp + exp1
			return alpha[(exp * exp1) % n];
		}
		// exponentiation by squaring
		int res = 1;
		while(exp > 0){
			// last bit equals zero
			if(exp % 2 == 1)
				res = prod(res, base);
			base = prod(base, base);
			exp >>= 1;
		}
		return res;
	}
	
	
	/**
	 * Multiplicative inverse of an element
	 * @param pol
	 * @return multiplicative inverse (pol') st. pol * pol' = alpha^0 = 1
	 */
	public int inv(int pol){
		// modulo
		pol = mod(pol);
		if(pol == 0)
			throw new GaloisFieldException("Divided by zero.");
		
		// if pre-loaded, get exponent and find -exp (mod n)
		if(alpha != null){
			int i = (n - exp[pol]) % n;
			return alpha[i];
		}
		
		// calculate on the fly
		// pol ^ -1 = pol ^ (n - 1) mod Fx
		return pow(pol, n - 1);
	}
	
	/**
	 * Divides two elements
	 * @param pol1
	 * @param pol2
	 * @return pol1 / pol2 = pol1 * pol2' (pol2' being the multiplicative inverse of pol2)
	 */
	public int div(int pol1, int pol2){
		if(pol2 == 0)
			throw new GaloisFieldException("Divided by zero.");
		// modulo FX
		pol1 = mod(pol1);
		pol2 = mod(pol2);
		
		return prod(pol1, inv(pol2));
	}
	
	/**
	 * Calculates residue of dividing pol in Fx (the lowest representation
	 * of an element). i.e. Converts any polynomial of degree equal or higher
	 * than m into a degree lower than m.
	 * @param pol binary representation
	 * @return pol mod Fx
	 */
	public int mod(int pol){
		// while order >= m
		int order = order(pol);
		int subs = Fx << (order - m);
		// O(bits^2) = can be reduced to O(bits)
		while(order >= m){
			// substract Fx*X^(order-m) from polynomial 
			// thus eliminating the highest order term
			pol ^= subs;
			// recalculate order
			order = order(pol);
			subs = Fx << (order - m);	
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
	
	/**
	 * Add two polynomials of GF terms in array representation— 
	 * Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * All Pi's are elements of the field.
	 * @param px1
	 * @param px2
	 * @return a resulting polynomial
	 */
	public int[] add(int[] px1, int[] px2){
		int size = Math.min(n, Math.max(px1.length, px2.length));
		int[] res = new int[size];
		// add terms of the same power - if i = j (mod n), then X^i = X^j 
		for (int i = 0; i < px1.length; i++)
			res[i % n] = px1[i];
		for (int i = 0; i < px2.length; i++)
			res[i % n] = add(res[i % n], px2[i]);
		return res;
	}
	
	/**
	 * Multiplies two polynomials of GF terms in array representation— 
	 * Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * All Pi's are elements of the field.* @param pol1
	 * @param px2
	 * @return a resulting polynomial
	 */
	public int[] prod(int[] px1, int[] px2){
		// polynomial multiplication
		// order of result = order 1 + order 2
		int size = Math.min(n, px1.length + px2.length - 1);
		int res[] = new int[size];
		
		for (int i = 0; i < px1.length; i++) 
			for (int j = 0; j < px2.length; j++) 
				// add terms of the same power - if i = j (mod n), then X^i = X^j 
				// res[i + j] += pol1[i] * pol2[j]
				res[(i + j) % n] = add(res[(i + j) % n], prod(px1[i], px2[j]));
		return res;
	}
	
	
	/**
	 * Calculates division residue of two polynomials of GF terms in array representation— 
	 * Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * All Pi's are elements of the field. 
	 * @param px
	 * @param dx
	 * @return a resulting polynomial
	 */
	public int[] mod(int[] px, int [] dx){
		int [] rx = Arrays.copyOf(px, px.length);
		int n = order(dx);
		int m;
		// break when order of pol < order of div
		while((m = order(rx)) >= n){
			// get the highest order element of pol and divide between highest order element of div
			int d = div(rx[m], dx[n]);
			// multiply gx to equal the highest order element
			// yx = d * X^(m - n)* div = has order m
			int[] yx = prodXToN(dx, d, m - n);

			// add to cancel out
			rx = add(rx, yx);
		}
		
		// trim to highest order
		rx = Arrays.copyOf(rx, m + 1);
		return rx;
	}
	
	/**
	 * Calculates division of two polynomials of GF terms in array representation— 
	 * Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * All Pi's are elements of the field. O(n*m) worst case 
	 * @param px
	 * @param dx
	 * @return a resulting polynomial
	 */
	protected int[] div(int[] px, int [] dx){
		int [] rx = Arrays.copyOf(px, px.length);
		int quot[] = new int[rx.length];
		int n = order(dx);
		int m;
		// break when order of pol < order of div
		while((m = order(rx)) >= n){
			// get the highest order element of pol and divide between highest order element of div
			quot[m - n] = div(rx[m], dx[n]);
			// multiply gx to equal the highest order element
			// yx = d * X^(m - n)* div = has order m
			int[] yx = prodXToN(dx, quot[m - n], m - n);

			// add to cancel out
			rx = add(rx, yx);
		}
		
		// trim to highest order
		quot = Arrays.copyOf(quot, order(quot) + 1);
		return quot;
	}
	
	/**
	 * Multiplies a polynomial by a single term:
	 * meaning it shifts all elements to a higher order (right)
	 * and multiplies by the coefficient
	 * Px = P0 + P1 * x + P2*x^2 ... (lowest order first)
	 * All Pi's are elements of the field. O(n*m) worst case
	 * O(px.lenght + exp)
	 * @param px Px
	 * @param coef
	 * @param exp
	 * @return coef*X^exp*Px
	 */
	public int[] prodXToN(int[] px, int coef, int exp){
		int size = Math.min(n, px.length + exp);
		int[] yx = new int[size];
		for (int i = 0; i < px.length; i++)
			yx[(exp + i) % n] = add(yx[(exp + i) % n], prod(coef, px[i]));
		return yx;
	}
	
	/**
	 * Evaluates the polynomial on the given value on the 
	 * Field
	 * @param px
	 * @param value belongs to the field
	 * @return px[0]*value^0 + px[1]*value^1 ... 
	 */
	public int eval(int [] px, int value){
		/*
		int r = 0;
		for (int i = 0; i < px.length; i++)
			r = add(r, prod(px[i], pow(value, i)));
		return r; //*/
		// decomposing polynomial as
		// ax^3 + bx^2 + c + d =  x(x(ax + b) + c) + d
		///*
		int r = px[px.length-1];
		for (int i = px.length-2; i >= 0; i--)
			//r = r*x + p[i]
			r = add(prod(r, value), px[i]);
		
		return r;
		//*/
	}
	
	public static int order(int Px[]){
		// the last non-zero position
		int m = Px.length - 1;
		while(Px[m] == 0 && m > 0) m--;
		return m;
	}
	
	/**
	 * Interpolates the minimum degree polynomial
	 * that contains all given points in the field
	 * @param points a two column matrix with  x,y coordinates of points
	 * @return the polynomial
	 */
	public int[] interpolate(int points[][]){
		// split x and y
		int n = points.length;
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = points[i][0];
			y[i] = points[i][1];
		}
		// set the linear system
		int[][] M = new int[n][n + 1];
		for (int i = 0; i < n; i++) {
			for (int j = 0, pow = alphaTo(0); j < n; j++, pow = prod(pow, x[i])) 
				M[i][j] = pow;
			M[i][n] = y[i];
		}
		
		return solve(M);
	}
	
	/**
	 * Solve linear system on , using Gauss-Jordan
	 * reduction O(N^3)
	 * A * xT = b
	 * @param M = A | b coefficients and results matrix
	 * @return x
	 */
	protected int[] solve(int[][] M) {
		// elimination
		int rows = M.length;
		int cols = M.length + 1;
		for (int i = 0; i < rows; i++) {
			// divide all row by M[i,i] 
			int a = M[i][i];
			if(a == 0){
				// pick any other row that has no zero on col i
				int k;
				for (k = 0; k < rows && M[k][i] != 0; k++);
				if(k == rows)
					throw new GaloisFieldException("Non solvable system");
				
				// add it to row i
				for (int j = i; j < cols; j++)
					M[i][j] = add(M[i][j], M[k][j]);
				a = M[i][i];
			}
			
			for (int j = i; j < cols; j++)
				M[i][j] = div(M[i][j], a);
				
			// eliminate values on other rows of column i
			for (int k = 0; k < rows; k++) 
				if(i != k){
					int b = M[k][i];
					// add to row k, b times row i
					for (int j = i; j < cols; j++)
						M[k][j] = add(M[k][j], prod(b, M[i][j]));						
				}
		}
		
		// verify identity
		for (int i = 0; i < rows; i++) 
			for (int j = 0; j < rows; j++)
				if((i == j) && (M[i][j] != 1) ||
						(i != j) && (M[i][j] != 0))
					throw new GaloisFieldException("Non identity - not solvable system (" + i + "," + j + ")" + M[i][j]);
		
		// copy solution
		int[] x = new int[rows];
		for (int i = 0; i < rows; i++)
			x[i] = M[i][cols-1];
		return x;
	}
	
}


class GaloisFieldException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public GaloisFieldException(String msg) {
		super(msg);
	}
}

