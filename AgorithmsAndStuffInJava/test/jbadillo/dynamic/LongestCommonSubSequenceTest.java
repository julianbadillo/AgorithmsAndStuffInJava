package jbadillo.dynamic;

import static org.junit.Assert.*;
import static jbadillo.dynamic.LongestCommonSubSequence.*;
import org.junit.Test;

public class LongestCommonSubSequenceTest {

	
	@Test
	public void longestCommonSubSeqTestEmpty() {
		assertEquals("", longestCommonSubSeq("", ""));
		assertEquals("", longestCommonSubSeq("abcdefgh", ""));
		assertEquals("", longestCommonSubSeq("", "abcdefg"));
		assertEquals("", longestCommonSubSeq("abcdefg", "hijklmnopq"));
		assertEquals("", longestCommonSubSeq("hijklmnopq", "abcdefg"));
	}
	
	@Test
	public void longestCommonSubSeqTestShort() {
		assertEquals("secreto", longestCommonSubSeq("asbedcfrgehitjkol", "msepcqreutvwox"));
		assertEquals("secreto", longestCommonSubSeq("msepcqreutvwox", "asbedcfrgehitjkol"));
		assertEquals("abc", longestCommonSubSeq("abc", "mammmbmmmmc"));
		assertEquals("abc", longestCommonSubSeq("mammmbmmmmc", "abc"));
		
	}
	@Test
	public void longestCommonSubSeqTestMid() {
		assertEquals("1234567890", longestCommonSubSeq("a1b2c3d4e5f6g7h8i9j0k", "l1u2m3fy4n5o6p7qr8s9t0"));
		assertEquals("1234567890", longestCommonSubSeq("1a2cb3d4e5f6hg7i8ljk9mno0pq", "h1g2klm3n4o5prs6t7uv8wx9yz0a"));
		
	}
	
	@Test
	public void longestCommonSubSeqTestRepeat() {
		assertEquals("ababc", longestCommonSubSeq("aaaaaabbbbbbaaaabbbccc", "cababca"));
		assertEquals("ababc", longestCommonSubSeq("cababca", "aaaaaabbbbbbaaaabbbccc"));
		
	}
	
	
}
