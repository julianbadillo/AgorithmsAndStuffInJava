package jbadillo.algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class RSCodeTest {

	@Test
	public void testRSCodeGF16() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, k = 9
		RSCode rsc16 = new RSCode(gf16, 9);

		assertEquals(15, rsc16.getN());
		assertEquals(9, rsc16.getK());
		assertEquals(3, rsc16.getT());
	}

	@Test
	public void testRSCodeGF16Generator() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, k = 9
		RSCode rsc16 = new RSCode(gf16, 9);

		// verify generator
		int[] gen = rsc16.getGx();
		// expected generator: 0.6, 10.5, 14.4, 4.3, 6.2, 9.1, 6.0
		// = alpha^6 + alpha^9*X + alpha^6*x^2 + alpha^4*x^3 + alpha^14*x^4 +
		// alpha^10*x^5 + alpha^0 * X^6

		// compressed format in inverse oder (least order first)
		int[] genT = new int[] { gf16.alphaTo(6), gf16.alphaTo(9), gf16.alphaTo(6), gf16.alphaTo(4), gf16.alphaTo(14),
				gf16.alphaTo(10), gf16.alphaTo(0) };
		assertArrayEquals(genT, gen);
	}


	@Test
	public void testRSCodeGF16Encode() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, t = 6
		RSCode rsc16 = new RSCode(gf16, 9);

		// easy case = alpha^11 * X (order 1)
		int[] Mx = new int[] { 0, gf16.alphaTo(11) };
		int[] C = rsc16.encode(Mx);

		// expected result: 11.7, 8.5, 10.4, 4.3, 14.2, 8.1, 12.0
		// = alpha^11 * X^7 + alpha^8*X^5 + alpha^10*X^4 + alpha^4*X^3 +
		// alpha^14*X^2 +alpha^8*X +alpha^12
		int[] CT = new int[] { gf16.alphaTo(12), gf16.alphaTo(8), gf16.alphaTo(14), gf16.alphaTo(4), gf16.alphaTo(10),
				gf16.alphaTo(8), 0, gf16.alphaTo(11), 0, 0, 0, 0, 0, 0, 0 };
		assertArrayEquals(CT, C);
	}

	
	@Test
	public void testRSCodeGF16DecodeOx() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// n = 15, t = 6
		RSCode rsc16 = new RSCode(gf16, 9);

		// 0.8, 11.7, 8.5, 10.4, 4.3, 3.2, 8.1, 12.0
		int[] Rx = { gf16.alphaTo(12),
				gf16.alphaTo(8), gf16.alphaTo(3),
				gf16.alphaTo(4), gf16.alphaTo(10),
				gf16.alphaTo(8), 0,
				gf16.alphaTo(11), 1};
		int[] Mx = rsc16.decode(Rx);

		// expected result: alpha^11 * X (order 1)
		int[] Mxt = new int[] {0, gf16.alphaTo(11), 0, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(Mxt, Mx);
	}
	
}
