package jbadillo.dynamic;

import static org.junit.Assert.*;

import org.junit.Test;

public class EditionDistanceTest {

	@Test
	public void testEditionDistanceAddAndChange() {
		String s1 = "snowy";
		String s2 = "sunny";
		
		assertEquals(3, EditionDistance.editionDistance(s1,s2));
		
		s1 ="exponential";
		s2 ="polynomial";
		assertEquals(6, EditionDistance.editionDistance(s1,s2));
		
		s1 ="hola";
		s2 ="hello";
		assertEquals(3, EditionDistance.editionDistance(s1,s2));
	}
	
	@Test
	public void testEditionDistanceOnlyAdd() {
		String s1 = "";
		String s2 = "sunny";
		
		assertEquals(5, EditionDistance.editionDistance(s1,s2));
		
		s1 ="exponential";
		s2 ="";
		assertEquals(11, EditionDistance.editionDistance(s1,s2));
		
		s1 ="abcdefgh";
		s2 ="ah";
		assertEquals(6, EditionDistance.editionDistance(s1,s2));
		
		s1 ="abcdefgh";
		s2 ="de";
		assertEquals(6, EditionDistance.editionDistance(s1,s2));
		
		s1 ="abcdefgh";
		s2 ="cf";
		assertEquals(6, EditionDistance.editionDistance(s1,s2));
	}
	
	@Test
	public void testEditionDistanceOnlyChange() {
		String s1 = "abcde";
		String s2 = "12345";
		
		assertEquals(5, EditionDistance.editionDistance(s1,s2));
		
		s1 ="exponential";
		s2 ="bcdfghjkmqr";
		assertEquals(11, EditionDistance.editionDistance(s1,s2));
		
		s1 ="baltimore";
		s2 ="balfimore";
		assertEquals(1, EditionDistance.editionDistance(s1,s2));
		
		s1 ="baltimore";
		s2 ="dalfimuri";
		assertEquals(4, EditionDistance.editionDistance(s1,s2));
		
		s1 ="abcdf";
		s2 ="1bcde";
		assertEquals(2, EditionDistance.editionDistance(s1,s2));
	}

}
