package jbadillo.datastruct;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RBTreeTest {

	@Test
	public void testPrintTreeNoBalance() {
		RBTree<Integer, Integer> tree = new RBTree<>();
		int [] a = {5,3,7,2,4,6,8};
		for (int x: a) 
			tree.put(x, x);
		String expected = "((2R,3B,4R),5B,(6R,7B,8R))";
		assertEquals(expected, tree.printTree());
		
	}
	
	@Test
	public void testToStringTreeNoBalance() {
		RBTree<Integer, Integer> tree = new RBTree<>();
		int [] a = {5,3,7,2,4,6,8};
		for (int x: a) 
			tree.put(x, x);
		String expected = "{2:2, 3:3, 4:4, 5:5, 6:6, 7:7, 8:8}";
		assertEquals(expected, tree.toString());
		
	}
	
	@Test
	public void testAddOneByOneRight() {
		RBTree<Integer, Integer> tree = new RBTree<>();
		int [] a = {1,2,3,4,5,6,7,8};
		int i = 0;
		
		tree.put(a[i], a[i++]);
		assertEquals("1B", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(,1B,2R)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(1R,2B,3R)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(1B,2B,(,3B,4R))", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(1B,2B,(3R,4B,5R))", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(1B,2B,(3B,4R,(,5B,6R)))", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(1B,2B,(3B,4R,(5R,6B,7R)))", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("((1B,2R,3B),4B,(5B,6R,(,7B,8R)))", tree.printTree());
	}
	
	@Test
	public void testAddOneByOneMixed() {
		RBTree<Integer, Integer> tree = new RBTree<>();
		int [] a = {50, 30, 40, 45, 35, 32, 60, 47};
		String[] expected = {
				"50B",
				"(30R,50B,)",
				"(30R,40B,50R)",
				"(30B,40B,(45R,50B,))",
				"((,30B,35R),40B,(45R,50B,))",
				"((30R,32B,35R),40B,(45R,50B,))",
				"((30R,32B,35R),40B,(45R,50B,60R))",
				"((30R,32B,35R),40B,((,45B,47R),50R,60B))",
		};
		
		for (int i = 0; i < a.length; i++) {
			tree.put(a[i], a[i]);
			assertEquals(expected[i], tree.printTree());	
		}
		
	}
	
	
	@Test
	public void testAddOneByOneLeft() {
		RBTree<Integer, Integer> tree = new RBTree<>();
		int [] a = {8,7,6,5,4,3,2,1};
		int i = 0;
		
		tree.put(a[i], a[i++]);
		assertEquals("8B", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(7R,8B,)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(6R,7B,8R)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("((5R,6B,),7B,8B)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("((4R,5B,6R),7B,8B)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(((3R,4B,),5R,6B),7B,8B)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(((2R,3B,4R),5R,6B),7B,8B)", tree.printTree());
		
		tree.put(a[i], a[i++]);
		assertEquals("(((1R,2B,),3R,4B),5B,(6B,7R,8B))", tree.printTree());
	}
	
	@Test
	public void testStringTree() {
		RBTree<String, String> tree = new RBTree<>();
		String [] a = {"Epicurious","Calendar","Gondor","Break","Dude","Fillibuster","Hotel"};
		for (String x: a) 
			tree.put(x, x);
		String expected = "((BreakR,CalendarB,DudeR),EpicuriousB,(FillibusterR,GondorB,HotelR))";
		assertEquals(expected, tree.printTree());	
	}
	
	@Test
	public void testPutGet(){
		RBTree<Integer, Integer> tree = new RBTree<>();
		Random rand = new Random(System.currentTimeMillis());
		// 10^5 -> enough for n*log n, but too much for n^2
		for (int i = 0; i < 100000; i++) {
			int key = rand.nextInt();
			int val = rand.nextInt();
			tree.put(key, val);
			// verify get
			assertEquals((Integer)val, tree.get(key));
		}	
	}
	
}
