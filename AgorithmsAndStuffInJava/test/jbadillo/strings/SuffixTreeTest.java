package jbadillo.strings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.junit.Test;

import junit.framework.Assert;

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
	
	
	@Test
	public void testIndexOf() {
		String word = "xabxa";
		SuffixTree st = new SuffixTree(word);
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
	public void testIndexOfMid() {
		String word = "xababcxabdefedaxvbac";
		SuffixTree st = new SuffixTree(word);
		assertEquals(0, st.indexOf("x"));
		assertEquals(0, st.indexOf("xa"));
		assertEquals(0, st.indexOf("xab"));
		assertEquals(-1, st.indexOf("xabxa"));
		assertEquals(1, st.indexOf("a"));
		assertEquals(1, st.indexOf("ab"));
		assertEquals(-1, st.indexOf("abx"));
		assertEquals(-1, st.indexOf("abxa"));
		assertEquals(2, st.indexOf("b"));
		assertEquals(-1, st.indexOf("bx"));
		assertEquals(-1, st.indexOf("bxa"));
		assertEquals(0, st.indexOf(""));
		assertEquals(11, st.indexOf("fedax"));
		assertEquals(11, st.indexOf("fedaxv"));
		assertEquals(11, st.indexOf("fedaxvb"));
	}
	
	
	@Test
	public void testLastIndexOf() {
		String word = "xabxa";
		SuffixTree st = new SuffixTree(word);
		assertEquals(0, st.lastIndexOf("xabxa"));
		assertEquals(1, st.lastIndexOf("abxa"));
		assertEquals(2, st.lastIndexOf("b"));
		assertEquals(2, st.lastIndexOf("bx"));
		assertEquals(2, st.lastIndexOf("bxa"));
		assertEquals(3, st.lastIndexOf("xa"));
		assertEquals(4, st.lastIndexOf("a"));
		assertEquals(3, st.lastIndexOf("x"));
		assertEquals(0, st.lastIndexOf("xab"));
		assertEquals(5, st.lastIndexOf(""));
	}
	
	@Test
	public void testLastIndexOfMid() {
		String word = "xababcxabdefedaxvbac";
		SuffixTree st = new SuffixTree(word);
		assertEquals(15, st.lastIndexOf("x"));
		assertEquals(6, st.lastIndexOf("xa"));
		assertEquals(6, st.lastIndexOf("xab"));
		assertEquals(-1, st.lastIndexOf("xabxa"));
		assertEquals(18, st.lastIndexOf("a"));
		assertEquals(7, st.lastIndexOf("ab"));
		assertEquals(-1, st.lastIndexOf("abx"));
		assertEquals(-1, st.lastIndexOf("abxa"));
		assertEquals(17, st.lastIndexOf("b"));
		assertEquals(-1, st.lastIndexOf("bx"));
		assertEquals(-1, st.lastIndexOf("bxa"));
		assertEquals(20, st.lastIndexOf(""));
		assertEquals(11, st.lastIndexOf("fedax"));
		assertEquals(11, st.lastIndexOf("fedaxv"));
		assertEquals(11, st.lastIndexOf("fedaxvb"));
	}
	
	@Test
	public void testIndicesOfBrutal(){
		char [] letters = " \t\nabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		char [] pattern = " pattern ".toCharArray();
		Random rand = new Random(System.currentTimeMillis());
		
		// randomly compose string
		StringBuilder bf = new StringBuilder();
		Collection<Integer> indices = new LinkedList<>();
		for (int i = 0; i < 10000; i++) {
			// randomly put the pattern in the string
			if(rand.nextInt() % 100 == 0){
				bf.append(pattern);	
				indices.add(i);
				i += pattern.length;
			}
			else{
				// random string
				bf.append(letters[rand.nextInt(letters.length)]);
			}
		}
		
		// suffix tree
		SuffixTree tree = new SuffixTree(bf.toString());
		// search pattern
		Collection<Integer> result = tree.indicesOf(new String(pattern));
		assertEquals(indices.size(), result.size());
		
		// matching
		for(int i: indices)
			assertTrue("index not found: "+i,result.contains(i));
		
		
	}
	
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
	
	@Test
	public void testSearchSuffixLarge() {
		String word = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		SuffixTree st = new SuffixTree(word);
		for (int i = 0; i < word.length(); i++) 
			assertEquals("Fail on: " + word.substring(i), i, st.searchSuffix(word.substring(i)));
	}
	
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
	
	@Test
	public void testLongestCommonSubstring() {
		String s1 = "abcd";
		String s2 = "abcd";
		assertEquals("abcd", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abc";
		s2 = "abcd";
		assertEquals("abc", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcabd";
		s2 = "dcabcd";
		assertEquals("abc", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcxa";
		s2 = "axcba";
		assertEquals("a", SuffixTree.longestCommonSubstring(s1, s2));
	}
	
	@Test
	public void testLongestCommonSubstringMid() {
		String s1 = "abcabxabcd";
		String s2 = "abcabxabced";
		assertEquals("abcabxabc", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcabxabcd";
		s2 = "ffabcabxabcdff";
		assertEquals("abcabxabcd", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcabxabcd";
		s2 = "dcbaxbacba";
		assertEquals("d", SuffixTree.longestCommonSubstring(s1, s2));
	}
	
	@Test
	public void testLongestCommonSubstringLarge() {
		String s1 = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String s2 = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		assertEquals("abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		s2 = "ghijklmnopqrstuvwxyz1234567890ABCDEFGHI";
		assertEquals("ghijklmnopqrstuvwxyz1234567890ABCDEFGHI", SuffixTree.longestCommonSubstring(s1, s2));
		
		s1 = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		s2 = "zyxwvutsrqponmlkjihgfedcba0987654321ZYXWVUTSRQPONMLKJIHGFEDCBA";
		assertEquals("0", SuffixTree.longestCommonSubstring(s1, s2));
	}
}
