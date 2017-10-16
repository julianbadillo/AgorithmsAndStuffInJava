package jbadillo.numbers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class MultiplicativeGroupTest {

	@Test
	public void testMultiplicativeGroupNonPrimitiveRoot() throws MultiplicativeGroupException{
		
		MultiplicativeGroup mg = new MultiplicativeGroup(valueOf(8), valueOf(3));
		
		assertEquals(valueOf(4), mg.getPhi());
		//assertEquals(valueOf(2), mg.getOrder());
		
		assertFalse(mg.isPrimitiveRoot());
		assertEquals(valueOf(3), mg.generate(valueOf(1)));
		assertEquals(valueOf(1), mg.generate(valueOf(2)));
		
		mg = new MultiplicativeGroup(valueOf(56), valueOf(13));
		assertEquals(valueOf(24), mg.getPhi());
		//assertEquals(valueOf(2), mg.getOrder());
		
		assertEquals(valueOf(13), mg.generate(valueOf(1)));
		assertEquals(valueOf(1), mg.generate(valueOf(2)));
		assertEquals(valueOf(13), mg.generate(valueOf(3)));
		
		assertFalse(mg.isPrimitiveRoot());
		
		mg = new MultiplicativeGroup(valueOf(56), valueOf(3));
		assertEquals(valueOf(24), mg.getPhi());
		//assertEquals(valueOf(6), mg.getOrder());
		
		assertEquals(valueOf(3), mg.generate(valueOf(1)));
		assertEquals(valueOf(9), mg.generate(valueOf(2)));
		assertEquals(valueOf(27), mg.generate(valueOf(3)));
		assertEquals(valueOf(1), mg.generate(valueOf(6)));
		
		assertFalse(mg.isPrimitiveRoot());
	}
	
	@Test
	public void testMultiplicativeGroupBitsOnlySmall(){
		MultiplicativeGroup mg = new MultiplicativeGroup(32);
		assertEquals(mg.getPhi(), mg.getOrder());
		assertEquals(ONE, mg.generate(mg.getOrder()));
	}
	
	@Test
	public void testMultiplicativeGroupBitsOnlyMiddle(){
		MultiplicativeGroup mg = new MultiplicativeGroup(512);
		assertEquals(mg.getPhi(), mg.getOrder());
		assertEquals(ONE, mg.generate(mg.getOrder()));
	}
	
	
	@Test
	public void testMultiplicativeGroupPrimitiveRoot() throws MultiplicativeGroupException{
		
		MultiplicativeGroup mg = new MultiplicativeGroup(valueOf(13), valueOf(2));
		assertEquals(valueOf(12), mg.getPhi());
		
		assertTrue(mg.isPrimitiveRoot());
		assertEquals(valueOf(2), mg.generate(valueOf(1)));
		assertEquals(valueOf(4), mg.generate(valueOf(2)));
		assertEquals(valueOf(8), mg.generate(valueOf(3)));
		assertEquals(valueOf(1), mg.generate(valueOf(12)));
		
		mg = new MultiplicativeGroup(valueOf(71), valueOf(28));
		assertEquals(valueOf(70), mg.getPhi());
		
		assertTrue(mg.isPrimitiveRoot());
		assertEquals(valueOf(28), mg.generate(valueOf(1)));
		assertEquals(valueOf(1), mg.generate(valueOf(70)));
		
	}
	
	
	@Test
	public void testMultiplicatievGroupOnlyModulo() throws MultiplicativeGroupException{
		MultiplicativeGroup mg = new MultiplicativeGroup(valueOf(7));
		assertEquals(valueOf(6), mg.getPhi());
		assertTrue("Generator is " + mg.getG(), mg.getG().intValue() == 3 
											|| mg.getG().intValue() == 5);
		
		mg = new MultiplicativeGroup(valueOf(62));
		int [] primRoots = {3, 11, 13, 17, 21, 43, 53, 55};
		assertEquals(30, mg.getPhi().intValue());
		int g = mg.getG().intValue();
		assertTrue("Generator is " + mg.getG(), Arrays.stream(primRoots)
											.anyMatch(i -> i == g));

	}
	
	@Test
	public void testFactorizeSmall() throws MultiplicativeGroupException {
		BigInteger n = valueOf(15);
		Map<BigInteger, Integer> factors = MultiplicativeGroup.factorize(n);

		assertEquals(2, factors.size());
		assertTrue(factors.containsKey(valueOf(5)));
		assertTrue(factors.containsKey(valueOf(3)));
		assertEquals(1, (int) factors.get(valueOf(5)));
		assertEquals(1, (int) factors.get(valueOf(3)));

		n = valueOf(64);
		factors = MultiplicativeGroup.factorize(n);

		assertEquals(1, factors.size());
		assertTrue(factors.containsKey(valueOf(2)));
		assertEquals(6, (int) factors.get(valueOf(2)));

		n = valueOf(13 * 13 * 7 * 11 * 11 * 11);
		factors = MultiplicativeGroup.factorize(n);

		assertEquals(3, factors.size());
		assertEquals(3, (int) factors.get(valueOf(11)));
		assertEquals(1, (int) factors.get(valueOf(7)));
		assertEquals(2, (int) factors.get(valueOf(13)));

	}

	@Test
	public void testFactorizeMid() throws MultiplicativeGroupException {
		BigInteger n = valueOf(40577 * 40577);
		Map<BigInteger, Integer> factors = MultiplicativeGroup.factorize(n);

		assertEquals(1, factors.size());
		assertEquals(2, (int) factors.get(valueOf(40577)));

		n = valueOf(32609 * 32839);
		factors = MultiplicativeGroup.factorize(n);

		assertEquals(2, factors.size());
		assertEquals(1, (int) factors.get(valueOf(32609)));
		assertEquals(1, (int) factors.get(valueOf(32839)));

	}

	@Test
	public void testTotientSmall() throws MultiplicativeGroupException {
		int[] totients = { 0, 1, 1, 2, 2, 4, 2, 6, 4, 6, 4, 10, 4, 12, 6, 8, 8, 16, 6, 18, 8, 12, 10, 22, 8, 20, 12, 18,
				12, 28, 8, 30, 16, 20, 16, 24, 12, 36, 18, 24, 16, 40, 12, 42, 20, 24, 22, 46, 16, 42, 20, 32, 24, 52,
				18, 40, 24, 36, 28, 58, 16, 60, 30, 36, 32, 48, 20, 66, 32, 44, 24, 70, 24, 72, 36, 40, 36, 60, 24, 78,
				32, 54, 40, 82, 24, 64, 42, 56, 40, 88, 24, 72, 44, 60, 46, 72, 32, 96, 42, 60 };
		for (int i = 1; i < totients.length; i++) {
			assertEquals("Error on " + i, valueOf(totients[i]), MultiplicativeGroup.totient(valueOf(i)));
		}
	}

	@Test
	public void testTotientMid() throws MultiplicativeGroupException {
		BigInteger n = valueOf(40577 * 40577);
		BigInteger totient = MultiplicativeGroup.totient(n);
		assertEquals(valueOf((40577 - 1) * 40577), totient);

		n = valueOf(32609 * 32839);
		totient = MultiplicativeGroup.totient(n);
		assertEquals(valueOf((32609 - 1) * (32839 - 1)), totient);
		
	}
	
	

}
