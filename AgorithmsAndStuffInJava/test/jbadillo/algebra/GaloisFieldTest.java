package jbadillo.algebra;

import static org.junit.Assert.*;

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
	public void testGF16Modulo() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		// X^4 mod Fx = X + 1
		assertEquals(0b0011, gf16.mod(0b10000));
		// alpha ^ 4 = alpha * alpha ^ 3 mod Fx
		assertEquals(gf16.alphaTo(4), gf16.mod(gf16.alphaTo(3) << 1));
		// alpha ^ 7 = alpha * alpha^6 mod Fx
		assertEquals(gf16.alphaTo(7), gf16.mod(gf16.alphaTo(6) << 1 ));
		
		// alpha^i = X ^ i mod fx
		for (int i = 0; i < 31; i++) 
			assertEquals(gf16.alphaTo(i), gf16.mod(0b1 << i));
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
	public void testGF16Mult() {
		// A GF(16) with Fx = X^4 + X + 1
		GaloisField gf16 = new GaloisField(4, 0b10011);
		
		// all multiplications
		// alpha^i * alpha^j = alpha^(i+j mod n) mod Fx
		for (int i = 0; i < 50; i++) 
			for (int j = 0; j < 50; j++) 
				assertEquals(gf16.alphaTo(i+j), gf16.prod(gf16.alphaTo(i), gf16.alphaTo(j)));
	}
	
	
	@Test
	public void testOrder(){
		
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
	public void testLeftShift(){
		int bitFlag = 0b11111111111111111111111111111111 << 4;
		int exp = 0b11111111111111111111111111110000;
		assertEquals(exp, bitFlag);
	}

}
