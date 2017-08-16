import static org.junit.Assert.*;

import org.junit.Test;

public class SuffixTreeTest {

	@Test
	public void testJustBuild1() {
		String word = "xabxa";
		//xabxa$
		//012345
		SuffixTree st = new SuffixTree(word);
		System.out.println(st.toString());		
	}

	@Test
	public void testJustBuild2() {
		String word = "xab";
		//xab$
		//012
		SuffixTree st = new SuffixTree(word);
		System.out.println(st.toString());		
	}
	
	@Test
	public void testIsSuffix() {
		String word = "xabxa";
		SuffixTree st = new SuffixTree(word);
		assertTrue(st.isSuffix(""));
		assertTrue(st.isSuffix("a"));		
		assertTrue(st.isSuffix("xa"));
		assertTrue(st.isSuffix("bxa"));
		assertTrue(st.isSuffix("abxa"));
		assertTrue(st.isSuffix("xabxa"));
	}
	
	@Test
	public void testIsSuffixNegative() {
		String word = "xabxa";
		SuffixTree st = new SuffixTree(word);
		assertFalse(st.isSuffix("x"));
		assertFalse(st.isSuffix("b"));		
		assertFalse(st.isSuffix("ax"));
		assertFalse(st.isSuffix("bx"));
		assertFalse(st.isSuffix("abx"));
		assertFalse(st.isSuffix("xabx"));
	}
	
	@Test
	public void testSearchSuffix() {
		String word = "xabxa";
		SuffixTree st = new SuffixTree(word);
		assertEquals(0, st.searchSuffix("xabxa"));
		assertEquals(1, st.searchSuffix("abxa"));
		assertEquals(2, st.searchSuffix("bxa"));
		assertEquals(3, st.searchSuffix("xa"));
		assertEquals(4, st.searchSuffix("a"));
		assertEquals(5, st.searchSuffix(""));
	}
	
	/*
	@Test
	public void testIndexOf() {
		String word = "xabxa";
		SuffixTree2 st = new SuffixTree2(word);
		assertEquals(0, st.indexOf("x"));
		assertEquals(0, st.indexOf("xa"));
		assertEquals(0, st.indexOf("xab"));
		assertEquals(0, st.indexOf("xabxa"));
		assertEquals(1, st.indexOf("a"));
		assertEquals(1, st.indexOf("ab"));
		assertEquals(1, st.indexOf("abx"));
		assertEquals(1, st.indexOf("abxa"));
		assertEquals(2, st.indexOf("b"));
		assertEquals(2, st.indexOf("bx"));
		assertEquals(2, st.indexOf("bxa"));
		assertEquals(0, st.indexOf(""));
	}
	
	@Test
	public void testLastIndexOf() {
		String word = "xabxa";
		SuffixTree2 st = new SuffixTree2(word);
		assertEquals(0, st.lastIndexOf("xabxa"));
		assertEquals(1, st.lastIndexOf("abxa"));
		assertEquals(2, st.lastIndexOf("b"));
		assertEquals(2, st.lastIndexOf("bx"));
		assertEquals(2, st.lastIndexOf("bxa"));
		assertEquals(3, st.lastIndexOf("xa"));
		assertEquals(4, st.lastIndexOf("a"));
		assertEquals(3, st.lastIndexOf("x"));
		assertEquals(3, st.lastIndexOf("xab"));
		assertEquals(0, st.lastIndexOf(""));
	}
	*/
	
	
	@Test
	public void testIsSuffixMid() {
		String word = "abcabxabcd";
		SuffixTree st = new SuffixTree(word);
		for (int i = 0; i < word.length(); i++) 
			assertTrue("Fail on: " + word.substring(i), st.isSuffix(word.substring(i)));
	}
	
	@Test
	public void testSearchSuffixMid() {
		String word = "abcabxabcd";
		SuffixTree st = new SuffixTree(word);
		for (int i = 0; i < word.length(); i++) 
			assertEquals("Fail on: " + word.substring(i), i, st.searchSuffix(word.substring(i)));
	}
	
	/*@Test
	public void testIndexOfMid() {
		//             0123456789
		String word = "abcabxabcd";
		SuffixTree2 st = new SuffixTree2(word);
		assertEquals(0, st.indexOf("abcabxabcd"));
		assertEquals(1, st.indexOf("bcabxabcd"));
		assertEquals(2, st.indexOf("cabxabcd"));
		assertEquals(4, st.indexOf("bxabcd"));
		assertEquals(7, st.indexOf("bcd"));
		assertEquals(7, st.indexOf("bc"));
	}*/
	
	@Test
	public void testCount() {
		String word = "xab";
		//xab$
		//012
		SuffixTree st = new SuffixTree(word);
		assertEquals(5, st.size());		
	}

	@Test
	public void testCountMid() {
		String word = "abcabxabcd";
		//xab$
		//012
		SuffixTree st = new SuffixTree(word);
		assertEquals(17, st.size());		
	}
}
