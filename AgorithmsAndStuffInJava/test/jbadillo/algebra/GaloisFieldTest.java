package jbadillo.algebra;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Test;

public class GaloisFieldTest {

	@Test
	public void testGF16() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// validate generation of the 16 elements
		// alpha^0 = 1
		assertEquals(0b0001, gf16.alphaTo(0));
		// alpha^1 = X
		assertEquals(0b0010, gf16.alphaTo(1));
		// alpha^2 = X^2
		assertEquals(0b0100, gf16.alphaTo(2));
		assertEquals(0b1000, gf16.alphaTo(3));
		assertEquals(0b0011, gf16.alphaTo(4));
		assertEquals(0b0110, gf16.alphaTo(5));
		assertEquals(0b1100, gf16.alphaTo(6));
		// alpha ^ 7 = X^3 + X + 1
		assertEquals(0b1011, gf16.alphaTo(7));
		assertEquals(0b0101, gf16.alphaTo(8));
		assertEquals(0b1010, gf16.alphaTo(9));
		assertEquals(0b0111, gf16.alphaTo(10));
		assertEquals(0b1110, gf16.alphaTo(11));
		assertEquals(0b1111, gf16.alphaTo(12));
		assertEquals(0b1101, gf16.alphaTo(13));
		assertEquals(0b1001, gf16.alphaTo(14));
		assertEquals(0b0001, gf16.alphaTo(15)); // alpha^0

	}

	@Test
	public void testGF64() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);
		// validate the generation of all 64 elements
		Set<Integer> set = new HashSet<>();
		gf64.alphaTo(56);
		for (int i = 0; i < gf64.getN(); i++) {
			int element = gf64.alphaTo(i);
			System.out.println(i + ": " + element);
			assertFalse("Element produced twice: " + element, set.contains(element));
			set.add(gf64.alphaTo(i));
		}
	}

	@Test
	public void testGF16Modulo() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// X^4 mod Fx = X + 1
		assertEquals(0b0011, gf16.mod(0b10000));
		// alpha ^ 4 = alpha * alpha ^ 3 mod Fx
		assertEquals(gf16.alphaTo(4), gf16.mod(gf16.alphaTo(3) << 1));
		// alpha ^ 7 = alpha * alpha^6 mod Fx
		assertEquals(gf16.alphaTo(7), gf16.mod(gf16.alphaTo(6) << 1));

		// alpha^i = X ^ i mod fx
		for (int i = 0; i < 31; i++)
			assertEquals(gf16.alphaTo(i), gf16.mod(0b1 << i));

		// zero
		assertEquals(0, gf16.mod(0));
	}

	@Test
	public void testGF64Modulo() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);

		// alpha^i = X ^ i mod fx
		for (int i = 0; i < 31; i++)
			assertEquals(gf64.alphaTo(i), gf64.mod(0b1 << i));

		// zero
		assertEquals(0, gf64.mod(0));
	}

	@Test
	public void testGF16Sum() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// (X^3+X) + (X^2 + X + 1) = X^3 + X^2 + 1
		assertEquals(0b1101, gf16.add(0b1010, 0b0111));

		// alpha^7 + alpha^10 = alpha^6 (cyclic table)
		assertEquals(gf16.alphaTo(6), gf16.add(gf16.alphaTo(7), gf16.alphaTo(10)));
		for (int i = 0; i < gf16.getN(); i++)
			assertEquals(0, gf16.add(gf16.alphaTo(i), gf16.alphaTo(i)));
	}

	@Test
	public void testGF64Sum() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);
		// (X^6+X) + (1) = 0
		assertEquals(0b0, gf64.add(0b1000010, 0b000001));

		// (X^6 + X) + (X + 1) = X^6 + 1 (mod Fx) = X
		assertEquals(0b0000010, gf64.add(0b1000010, 0b0000011));

		for (int i = 0; i < gf64.getN(); i++)
			assertEquals(0, gf64.add(gf64.alphaTo(i), gf64.alphaTo(i)));
	}

	@Test
	public void testGF16Mult() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// all multiplications
		// alpha^i * alpha^j = alpha^(i+j mod n) mod Fx
		for (int i = 0; i < 50; i++)
			for (int j = 0; j < 50; j++)
				assertEquals(gf16.alphaTo(i + j), gf16.prod(gf16.alphaTo(i), gf16.alphaTo(j)));

		for (int i = 0; i < 50; i++) {
			assertEquals(0, gf16.prod(0, gf16.alphaTo(i)));
			assertEquals(0, gf16.prod(gf16.alphaTo(i), 0));
		}
	}

	@Test
	public void testGF64Mult() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);

		// all multiplications
		// alpha^i * alpha^j = alpha^(i+j mod n) mod Fx
		for (int i = 0; i < gf64.getN(); i++)
			for (int j = 0; j < gf64.getN(); j++)
				assertEquals(gf64.alphaTo(i + j), gf64.prod(gf64.alphaTo(i), gf64.alphaTo(j)));

		for (int i = 0; i < gf64.getN(); i++) {
			assertEquals(0, gf64.prod(0, gf64.alphaTo(i)));
			assertEquals(0, gf64.prod(gf64.alphaTo(i), 0));
		}
	}

	@Test
	public void testGF16Div() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// all multiplications
		// alpha^i / alpha^j = alpha^(i-j mod n) mod Fx
		for (int i = 0; i < 50; i++)
			for (int j = 0; j < 50; j++)
				assertEquals(gf16.alphaTo(i - j), gf16.div(gf16.alphaTo(i), gf16.alphaTo(j)));

		for (int i = 0; i < 50; i++) {
			assertEquals(0, gf16.prod(0, gf16.alphaTo(i)));
			assertEquals(0, gf16.prod(gf16.alphaTo(i), 0));
		}
	}

	@Test
	public void testGF64Div() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);

		// all multiplications
		// alpha^i / alpha^j = alpha^(i-j mod n) mod Fx
		for (int i = 0; i < gf64.getN(); i++)
			for (int j = 0; j < gf64.getN(); j++)
				assertEquals(gf64.alphaTo(i - j), gf64.div(gf64.alphaTo(i), gf64.alphaTo(j)));

		for (int i = 0; i < gf64.getN(); i++) {
			assertEquals(0, gf64.prod(0, gf64.alphaTo(i)));
			assertEquals(0, gf64.prod(gf64.alphaTo(i), 0));
		}
	}

	@Test
	public void testGF16Pow() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// some powers
		for (int i = 1; i < 50; i++)
			for (int j = 1; j < 50; j++)
				assertEquals(gf16.alphaTo(i * j), gf16.pow(gf16.alphaTo(i), j));
		// zero power
		for (int i = 0; i < 50; i++)
			assertEquals(1, gf16.pow(gf16.alphaTo(i), 0));
		// zero
		assertEquals(0, gf16.pow(0, 10));
		assertEquals(0, gf16.pow(0b10011, 10));

	}

	@Test
	public void testGF64Pow() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);

		// some powers
		for (int i = 1; i < gf64.getN(); i++)
			for (int j = 1; j < gf64.getN(); j++)
				assertEquals(gf64.alphaTo(i * j), gf64.pow(gf64.alphaTo(i), j));
		// zero power
		for (int i = 0; i < gf64.getN(); i++)
			assertEquals(1, gf64.pow(gf64.alphaTo(i), 0));
		// zero
		assertEquals(0, gf64.pow(0, 10));
		assertEquals(0, gf64.pow(0b1000011, 10));

	}

	@Test
	public void testGF16Inv() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// all inverses
		// alpha^i * alpha^j = alpha^(i+j mod n) mod Fx
		assertEquals(gf16.alphaTo(0), gf16.alphaTo(15));
		assertEquals(gf16.alphaTo(0), gf16.inv(gf16.alphaTo(0)));
		assertEquals(gf16.alphaTo(0), gf16.inv(gf16.alphaTo(15)));

		for (int i = 1; i < 50; i++)
			assertEquals("Failed on case: " + i, 1, gf16.prod(gf16.alphaTo(i), gf16.inv(gf16.alphaTo(i))));
	}
	
	@Test
	public void testGF64Inv() {
		// GF(2^6) = GF(64), Fx = X^6 + X + 1
		GaloisField gf64 = new GaloisField(6, 0b1000011);

		// all inverses
		// alpha^i * alpha^j = alpha^(i+j mod n) mod Fx
		assertEquals(gf64.alphaTo(0), gf64.alphaTo(63));
		assertEquals(gf64.alphaTo(0), gf64.inv(gf64.alphaTo(0)));
		assertEquals(gf64.alphaTo(0), gf64.inv(gf64.alphaTo(63)));

		// alpha ^ i * inv(alpha^i) = 1
		for (int i = 1; i < gf64.getN(); i++)
			assertEquals("Failed on case: " + i, 1, gf64.prod(gf64.alphaTo(i), gf64.inv(gf64.alphaTo(i))));
	}

	@Test
	public void testGF16Eval() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);

		// Px = 1 (constant value)
		int[] P = new int[] { gf16.alphaTo(0) };
		assertEquals(1, gf16.eval(P, gf16.alphaTo(1)));
		assertEquals(1, gf16.eval(P, gf16.alphaTo(2)));

		// Px = X (identity)
		P = new int[] { 0, gf16.alphaTo(0) };
		for (int i = 0; i < 16; i++)
			assertEquals(gf16.alphaTo(i), gf16.eval(P, gf16.alphaTo(i)));

		// 0.8, 11.7, 8.5, 10.4, 4.3, 3.2, 8.1, 12.0
		// = 1* X^8 + alpha^11 * X^7 + alpha^8 * X^5 + ...
		P = new int[] { gf16.alphaTo(12), gf16.alphaTo(8), gf16.alphaTo(3), gf16.alphaTo(4), gf16.alphaTo(10),
				gf16.alphaTo(8), 0, gf16.alphaTo(11), gf16.alphaTo(0) };

		assertEquals(1, gf16.eval(P, gf16.alphaTo(1)));
		assertEquals(1, gf16.eval(P, gf16.alphaTo(2)));
		assertEquals(gf16.alphaTo(5), gf16.eval(P, gf16.alphaTo(3)));
		assertEquals(1, gf16.eval(P, gf16.alphaTo(4)));
		assertEquals(0, gf16.eval(P, gf16.alphaTo(5)));
		assertEquals(gf16.alphaTo(10), gf16.eval(P, gf16.alphaTo(6)));

	}

	@Test
	public void testGF16PolynomialMod() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, t = 6

		// easy case = aplha^5 * X (order 1)
		int[] px = { 0, gf16.alphaTo(11) };
		int[] dx = new int[] { gf16.alphaTo(6), gf16.alphaTo(9), gf16.alphaTo(6), gf16.alphaTo(4), gf16.alphaTo(14),
				gf16.alphaTo(10), gf16.alphaTo(0) };

		// answer vs expected answer
		int[] r = gf16.mod(px, dx);
		int[] rexp = { 0, gf16.alphaTo(11) };
		assertArrayEquals(rexp, r);

		// easy case 2: similar to the gx
		px = new int[] { gf16.alphaTo(1), // one instead of 6
				gf16.alphaTo(9), gf16.alphaTo(6), gf16.alphaTo(4), gf16.alphaTo(14), gf16.alphaTo(10),
				gf16.alphaTo(0) };
		r = gf16.mod(px, dx);
		rexp = new int[] { gf16.add(gf16.alphaTo(6), gf16.alphaTo(1)) };
		assertArrayEquals(rexp, r);

		// a polynomial Px = alpha^11 * X^7 (order 7)
		px = new int[] { 0, 0, 0, 0, 0, 0, 0, gf16.alphaTo(11) };
		r = gf16.mod(px, dx);

		// expected result: 8.5, 10.4, 4.3, 14.2, 8.1, 12.0
		// = alpha^8*X^5 + alpha^10*X^4 + alpha^4*X^3 + alpha^14*X^2 +alpha^8*X
		// +alpha^12
		rexp = new int[] { gf16.alphaTo(12), gf16.alphaTo(8), gf16.alphaTo(14), gf16.alphaTo(4), gf16.alphaTo(10),
				gf16.alphaTo(8) };
		assertArrayEquals(rexp, r);
	}

	@Test
	public void testRSCode16PolynomialDiv() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, t = 6

		// easy case = aplha^5 * X (order 1)
		int[] px = { 0, gf16.alphaTo(11) };
		int[] dx = new int[] { gf16.alphaTo(6), gf16.alphaTo(9), gf16.alphaTo(6), gf16.alphaTo(4), gf16.alphaTo(14),
				gf16.alphaTo(10), gf16.alphaTo(0) };

		// answer vs expected answer
		int[] q = gf16.div(px, dx);
		int[] dExp = { 0 }; // zero
		assertArrayEquals(dExp, q);

		// easy case 2: similar to the gx
		px = new int[] { gf16.alphaTo(1), // one instead of 6
				gf16.alphaTo(9), gf16.alphaTo(6), gf16.alphaTo(4), gf16.alphaTo(14), gf16.alphaTo(10),
				gf16.alphaTo(0) };

		q = gf16.div(px, dx);
		dExp = new int[] { 1 }; // one (same degree)
		assertArrayEquals(dExp, q);

		// a polynomial Px = alpha^11 * X^7 (order 7)
		px = new int[] { 0, 0, 0, 0, 0, 0, 0, gf16.alphaTo(11) };
		q = gf16.div(px, dx);

		// expected result: 11.1, 6.0
		dExp = new int[] { gf16.alphaTo(6), gf16.alphaTo(11) };
		assertArrayEquals(dExp, q);
	}

	@Test
	public void testGF16PolynomialDivModProd() {
		// Test all in one
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, t = 6
		Random rand = new Random(System.currentTimeMillis());

		/**
		 * q [6, 12, 4, 6, 9, 13] d [12, 1, 14, 14, 13] r [9, 4, 5, 13] p [7,
		 * 13, 14, 1, 1, 6, 9, 5, 5, 14]
		 */

		// dividend
		int px[] = IntStream.range(0, 10).map(i -> rand.nextInt(gf16.getN())).toArray();
		// divisor
		int dx[] = IntStream.range(0, 5).map(i -> rand.nextInt(gf16.getN())).toArray();
		// quotient
		int qx[] = gf16.div(px, dx);
		// residue
		int rx[] = gf16.mod(px, dx);

		// q*d + r = p
		int[] p2x = gf16.add(gf16.prod(qx, dx), rx);

		int m = GaloisField.order(p2x.length);
		assertEquals(GaloisField.order(px), GaloisField.order(p2x));
		for (int i = 0; i < m; i++)
			assertEquals(px[i], p2x[i]);
	}

	@Test
	public void testOrder() {

		assertEquals(0, GaloisField.order(0b0));
		assertEquals(0, GaloisField.order(0b1));
		assertEquals(1, GaloisField.order(0b10));
		assertEquals(1, GaloisField.order(0b11));
		assertEquals(2, GaloisField.order(0b110));
		assertEquals(2, GaloisField.order(0b100));
		assertEquals(3, GaloisField.order(0b1000));
		assertEquals(4, GaloisField.order(0b10110));
		assertEquals(5, GaloisField.order(0b100110));
		assertEquals(6, GaloisField.order(0b1001010));
		assertEquals(7, GaloisField.order(0b10011000));
		assertEquals(8, GaloisField.order(0b100110010));
		assertEquals(9, GaloisField.order(0b1010110010));
		assertEquals(10, GaloisField.order(0b10110110010));
		assertEquals(12, GaloisField.order(0b1010110110010));

	}

	@Test
	public void testLeftShift() {
		int bitFlag = 0b11111111111111111111111111111111 << 4;
		int exp = 0b11111111111111111111111111110000;
		assertEquals(exp, bitFlag);
	}

}
