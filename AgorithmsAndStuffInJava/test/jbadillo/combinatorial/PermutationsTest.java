package jbadillo.combinatorial;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import static org.junit.Assert.*;

public class PermutationsTest {

	@Test
	public void testPrintPermutationsInt() {
		Permutations.printPermutations(5);
	}

	
	@Test
	public void testPermutations() {
		Permutations<String> p = new Permutations<>( Stream.of("A", "B", "C", "D", "E")
													.collect(Collectors.toSet()));
		int i = 0;
		while(p.hasNext())
		{
			System.out.println(p.getNextPermutation());
			i++;
		}
		assertEquals(120, i);
	}

	
	@Test
	public void testPrintPermutationsString() {
		Permutations<String> p = new Permutations<>();
		Set<String> set = Stream.of("A", "B", "C", "D", "E")
						.collect(Collectors.toSet());
		p.printPermutations(set);
	}
}
