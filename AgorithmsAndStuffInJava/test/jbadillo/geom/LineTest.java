package jbadillo.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LineTest {

	
	@Test
	public void testEquals(){
		Line lines[] = new Line[]{
		new Line(1,2,3,4),
		new Line(3,4,1,2),
		new Line(new Point(1,2), new Point(3,4)),
		new Line(new Point(3,4), new Point(1,2)),
		};
		
		for (Line l1: lines) {
			for(Line l2: lines){
				assertEquals(l1, l2);
				// either same or right opposite angle
				double a1 = l1.getAngle();
				double a2 = l2.getAngle();
				assertTrue(a1 == a2 || a1 == a2 + Math.PI || a1 + Math.PI == a2);
				
				//assertEquals(l1.getAngle(), l2.getAngle(), Constants.EPSILON);
				assertEquals(l1.getYIntersect(), l2.getYIntersect(), Constants.EPSILON);
				assertEquals(l1.getXIntersect(), l2.getXIntersect(), Constants.EPSILON);
			}	
		}
	}
	
	@Test
	public void testContainsPositive() {
		
		Line l = new Line(1, 1, 3, 3);
		assertTrue(l.contains(new Point(1, 1)));
		assertTrue(l.contains(new Point(1.2, 1.2)));
		assertTrue(l.contains(new Point(2, 2)));
		assertTrue(l.contains(new Point(2.5, 2.5)));
		assertTrue(l.contains(new Point(3, 3)));
		
		
		l = new Line(0, 0, 30, 40);
		assertTrue(l.contains(new Point(0, 0)));
		assertTrue(l.contains(new Point(1.5, 2)));
		assertTrue(l.contains(new Point(3, 4)));
		assertTrue(l.contains(new Point(6, 8)));
		assertTrue(l.contains(new Point(12, 16)));
		assertTrue(l.contains(new Point(15, 20)));
		assertTrue(l.contains(new Point(21, 28)));
		assertTrue(l.contains(new Point(27, 36)));
		
		l = new Line(3, 3, 1, 1);
		assertTrue(l.contains(new Point(1, 1)));
		assertTrue(l.contains(new Point(1.2, 1.2)));
		assertTrue(l.contains(new Point(2, 2)));
		assertTrue(l.contains(new Point(2.5, 2.5)));
		assertTrue(l.contains(new Point(3, 3)));
		
		l = new Line(0, 40, 30, 0);
		assertTrue(l.contains(new Point(0, 40)));
		assertTrue(l.contains(new Point(30, 0)));
		assertTrue(l.contains(new Point(15, 20)));
		assertTrue(l.contains(new Point(3, 36)));
		
		l = new Line(0, 10, 10, 10);
		assertTrue(l.contains(new Point(0, 10)));
		assertTrue(l.contains(new Point(10, 10)));
		assertTrue(l.contains(new Point(5, 10)));
		
		l = new Line(10, 0, 10, 10);
		assertTrue(l.contains(new Point(10, 0)));
		assertTrue(l.contains(new Point(10, 10)));
		assertTrue(l.contains(new Point(10, 1)));
		assertTrue(l.contains(new Point(10, 5)));
		
	}
	
	@Test
	public void testContainsNegative() {
		
		Line l = new Line(1, 1, 3, 3);
		assertFalse(l.contains(new Point(0, 0)));
		assertFalse(l.contains(new Point(0.5, 0.5)));
		assertFalse(l.contains(new Point(3.2, 3.2)));
		assertFalse(l.contains(new Point(-1, -1)));
		assertFalse(l.contains(new Point(4, 4)));
	}

	@Test
	public void testColinear() {

		Line l = new Line(1, 1, 3, 3);
		assertTrue(l.colinear(new Point(1, 1)));
		assertTrue(l.colinear(new Point(1.2, 1.2)));
		assertTrue(l.colinear(new Point(2, 2)));
		assertTrue(l.colinear(new Point(2.5, 2.5)));
		assertTrue(l.colinear(new Point(3, 3)));
		assertTrue(l.colinear(new Point(0, 0)));
		assertTrue(l.colinear(new Point(3.5, 3.5)));
		
		l = new Line(0, 10, 30, 10);	
		assertTrue(l.colinear(new Point(0, 10)));
		assertTrue(l.colinear(new Point(30, 10)));
		assertTrue(l.colinear(new Point(20, 10)));
		assertTrue(l.colinear(new Point(-20, 10)));
		assertTrue(l.colinear(new Point(40, 10)));
		
		l = new Line(0, 10, 10, 10);
		assertTrue(l.colinear(new Point(0, 10)));
		assertTrue(l.colinear(new Point(10, 10)));
		assertTrue(l.colinear(new Point(5, 10)));
		assertTrue(l.colinear(new Point(30, 10)));
		assertTrue(l.colinear(new Point(-20, 10)));
		
		l = new Line(10, 0, 10, 10);
		assertTrue(l.colinear(new Point(10, 0)));
		assertTrue(l.colinear(new Point(10, 10)));
		assertTrue(l.colinear(new Point(10, 1)));
		assertTrue(l.colinear(new Point(10, 5)));
		assertTrue(l.colinear(new Point(10, 30)));
		assertTrue(l.colinear(new Point(10, -150)));
	}

	
	@Test
	public void testGetXIntersect() {
		Line l1 = new Line(0, 0, 10, 10);
		assertEquals(0.0d, l1.getXIntersect(), Constants.EPSILON);
		
		l1 = new Line(0, 10, 10, 0);
		assertEquals(10.0d, l1.getXIntersect(), Constants.EPSILON);
		
		l1 = new Line(10, 10, 20, 30);
		assertEquals(5.0, l1.getXIntersect(), Constants.EPSILON);
		
		l1 = new Line(10, 10, 20, 15);
		assertEquals(-10, l1.getXIntersect(), Constants.EPSILON);
		
		// horizontal
		l1 = new Line(5, 5, 10, 5);
		assertEquals(Double.NaN, l1.getXIntersect(), Constants.EPSILON);
		
		// vertical
		l1 = new Line(5, 5, 5, 10);
		assertEquals(5.0, l1.getXIntersect(), Constants.EPSILON);
		
	}
	
	@Test
	public void testGetYIntersect() {
		Line l1 = new Line(0, 0, 10, 10);
		assertEquals(0.0d, l1.getYIntersect(), Constants.EPSILON);
		l1 = new Line(0, 10, 10, 0);
		assertEquals(10.0d, l1.getYIntersect(), Constants.EPSILON);
		
		l1 = new Line(10, 10, 20, 30);
		assertEquals(-10, l1.getYIntersect(), Constants.EPSILON);
		
		l1 = new Line(10, 10, 20, 15);
		assertEquals(5, l1.getYIntersect(), Constants.EPSILON);
		
		l1 = new Line(10.0, 5.0, 5.0, 0.0);
		assertEquals(-5.0, l1.getYIntersect(), Constants.EPSILON);
		
		// horizontal
		l1 = new Line(5, 5, 10, 5);
		assertEquals(5.0, l1.getYIntersect(), Constants.EPSILON);
		
		// vertical
		l1 = new Line(5, 5, 5, 10);
		assertEquals(Double.NaN, l1.getYIntersect(), Constants.EPSILON);
	}
	
	@Test
	public void testIntersection() {
		Line l1 = new Line(0, 0, 10, 10);
		Line l2 = new Line(0, 10, 10, 0);
		assertEquals(new Point(5, 5), l1.intersection(l2));
		assertEquals(new Point(5, 5), l2.intersection(l1));
		
		l1 = new Line(0, 10, 10, 10);
		l2 = new Line(0, 0, 10, 20);
		assertEquals(new Point(5, 10), l1.intersection(l2));
		assertEquals(new Point(5, 10), l2.intersection(l1));
		
		l1 = new Line(10, 0, 10, 10);
		l2 = new Line(0, 0, 20, 10);
		assertEquals(new Point(10, 5), l1.intersection(l2));
		assertEquals(new Point(10, 5), l2.intersection(l1));
		
		l1 = new Line(10, 0, 10, 50);
		l2 = new Line(0, 10, 20, 10);
		assertEquals(new Point(10, 10), l1.intersection(l2));
		assertEquals(new Point(10, 10), l2.intersection(l1));
		
		
		l1 = new Line(5.0, 0.0, 10.0, 5.0);
		l2 = new Line(5.0, 5.0, 10.0, 5.0);
		assertEquals(new Point(10.0, 5.0), l1.intersection(l2));
		assertEquals(new Point(10.0, 5.0), l2.intersection(l1));
	}

}
