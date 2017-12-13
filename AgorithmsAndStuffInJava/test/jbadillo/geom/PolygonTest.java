package jbadillo.geom;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class PolygonTest {

	@Test
	public void testIsConvex() {
		// counter clockwise
		Polygon square = new Polygon(new Point[]{new Point(0,0),
				new Point(0,10),
				new Point(10,10),
				new Point(10,0)});
		
		assertTrue(square.isConvex());
		// clockwise
		square = new Polygon(new Point[]{new Point(0,0),
				new Point(10,0),
				new Point(10,10),
				new Point(0,10),
				});
		
		assertTrue(square.isConvex());
		
		// clockwise
		Polygon trapezoid = new Polygon(new Point[]{new Point(10,0),
				new Point(20,20),
				new Point(40,20),
				new Point(50,0)});
		
		assertTrue(trapezoid.isConvex());
		
		// obtuse triangle
		Polygon obTriangle = new Polygon(new Point[]{new Point(10,0),
				new Point(20,0),
				new Point(50,30)});
		
		assertTrue(obTriangle.isConvex());
		
		
		
		Polygon diamond = new Polygon(new Point[]{new Point(5,0),
				new Point(10,5),
				new Point(5,10),
				new Point(0, 5)});
		
		assertTrue(diamond.isConvex());
		
		Polygon bridge = new Polygon(new Point[]{new Point(10,0),
				new Point(20,20),
				new Point(40,20),
				new Point(50, 0),
				new Point(60, 0),
				new Point(50,40),
				new Point(10,30),});
		
		assertFalse(bridge.isConvex());
		
		Polygon bowtie = new Polygon(new Point[]{new Point(0,0),
										new Point(10,10),
										new Point(10,0),
										new Point(0,10),
										});
		
		assertFalse(bowtie.isConvex());
		
	}

	@Test
	public void testContainsSquare() {
		Polygon square = new Polygon(new Point[]{new Point(0,0),
												new Point(0,10),
												new Point(10,10),
												new Point(10,0)});
		// a vertex
		assertTrue(square.contains(new Point(0,0)));
		// a side
		assertTrue(square.contains(new Point(0,5)));
		assertTrue(square.contains(new Point(10,5)));
		// inside
		assertTrue(square.contains(new Point(5,5)));
		assertTrue(square.contains(new Point(2,5)));
		assertTrue(square.contains(new Point(7,8)));
		
		// outside
		assertFalse(square.contains(new Point(-1,5)));
		assertFalse(square.contains(new Point(5,-1)));
		
	}
	
	@Test
	public void testContainsDiamond() {
		
		Polygon diamond = new Polygon(new Point[]{new Point(5,0),
												new Point(10,5),
												new Point(5,10),
												new Point(0, 5)});
		
		// a vertex
		assertTrue(diamond.contains(new Point(5,0)));
		assertTrue(diamond.contains(new Point(10,5)));
		
		// a side
		assertTrue(diamond.contains(new Point(6,1)));
		assertTrue(diamond.contains(new Point(7,2)));
		assertTrue(diamond.contains(new Point(9,6)));
		// inside
		assertTrue(diamond.contains(new Point(5, 5)));
		assertTrue(diamond.contains(new Point(4, 5)));
		assertTrue(diamond.contains(new Point(5, 4)));
		assertTrue(diamond.contains(new Point(5, 8)));
		assertTrue(diamond.contains(new Point(5,2)));
		assertTrue(diamond.contains(new Point(4,4)));
		assertTrue(diamond.contains(new Point(6,6)));
		
		// outside
		assertFalse(diamond.contains(new Point(0,0)));
		assertFalse(diamond.contains(new Point(10,10)));
		assertFalse(diamond.contains(new Point(10,0)));
		assertFalse(diamond.contains(new Point(0,10)));
		assertFalse(diamond.contains(new Point(1,1)));
		assertFalse(diamond.contains(new Point(3,1)));
		
		
		Polygon otherDiamond = new Polygon(new Point(2, 4), new Point(9, 0), new Point(9, 4));
		//Polygon otherDiamond = new Polygon(new Point(0, 3), new Point(0, 1), new Point(3, 0), new Point(3, 2));
		assertTrue(otherDiamond.contains(new Point(8, 2)));
		
	}
	
	@Test
	public void testContainsBridge() {
		Polygon bridge = new Polygon(new Point[]{new Point(10,0),
												new Point(20,20),
												new Point(40,20),
												new Point(50, 0),
												new Point(60, 0),
												new Point(50,40),
												new Point(10,30),});
		// a vertex
		assertTrue(bridge.contains(new Point(10,0)));
		assertTrue(bridge.contains(new Point(50,40)));
		assertTrue(bridge.contains(new Point(60,0)));
		
		// on a side
		assertTrue(bridge.contains(new Point(15,10)));
		assertTrue(bridge.contains(new Point(30,20)));
		assertTrue(bridge.contains(new Point(45,10)));
		assertTrue(bridge.contains(new Point(55,20)));
		assertTrue(bridge.contains(new Point(30,35)));
		assertTrue(bridge.contains(new Point(10,10)));
		
		// inside
		assertTrue(bridge.contains(new Point(15,25)));
		assertTrue(bridge.contains(new Point(25,25)));
		assertTrue(bridge.contains(new Point(40,25)));
		assertTrue(bridge.contains(new Point(50,20)));
		assertTrue(bridge.contains(new Point(50,10)));
		assertTrue(bridge.contains(new Point(45,35)));
		assertTrue(bridge.contains(new Point(15,15)));
		
		// outside
		assertFalse(bridge.contains(new Point(0,0)));
		assertFalse(bridge.contains(new Point(20,0)));
		assertFalse(bridge.contains(new Point(45,0)));
		assertFalse(bridge.contains(new Point(20,10)));
		assertFalse(bridge.contains(new Point(40,10)));
		assertFalse(bridge.contains(new Point(60,20)));
		assertFalse(bridge.contains(new Point(10,40)));
		assertFalse(bridge.contains(new Point(15,35)));
		
	}
	
	@Test
	public void testAreaTriangles() throws Exception {
		
		Polygon triangle = new Polygon(
					new Point(0,0),
					new Point(0,10),
					new Point(10,10)
				);
		assertEquals(50, triangle.getArea(), Constants.EPSILON);
		
		triangle = new Polygon(
				new Point(0,0),
				new Point(0,20),
				new Point(10,10)
			);
		assertEquals(100, triangle.getArea(), Constants.EPSILON);
		
		triangle = new Polygon(
				new Point(0,0),
				new Point(10,10),
				new Point(0,10)
			);
		assertEquals(50, triangle.getArea(), Constants.EPSILON);
		
		triangle = new Polygon(
				new Point(0,0),
				new Point(10,0),
				new Point(0,10)
			);
		assertEquals(50, triangle.getArea(), Constants.EPSILON);
		
		triangle = new Polygon(
				new Point(20,0),
				new Point(30,20),
				new Point(0,10)
			);
		assertEquals(250, triangle.getArea(), Constants.EPSILON);
	}
	
	@Test
	public void testAreaQuadrangles() throws Exception {
		// counter clockwise
		Polygon square = new Polygon(new Point[]{new Point(0,0),
				new Point(0,10),
				new Point(10,10),
				new Point(10,0)});
		
		assertEquals(100, square.getArea(), Constants.EPSILON);
		
		// clockwise
		square = new Polygon(new Point[]{new Point(0,0),
				new Point(10,0),
				new Point(10,10),
				new Point(0,10),
				});
		
		assertEquals(100, square.getArea(), Constants.EPSILON);
		
		// clockwise
		Polygon trapezoid = new Polygon(new Point[]{new Point(10,0),
				new Point(20,20),
				new Point(40,20),
				new Point(50,0)});
		assertEquals(600, trapezoid.getArea(), Constants.EPSILON);

		Polygon bowtie = new Polygon(new Point[]{new Point(0,0),
				new Point(10,10),
				new Point(10,0),
				new Point(0,10),
				});
		assertEquals(0, bowtie.getArea(), Constants.EPSILON);
	}
	@Test
	public void testAreaPolygons() throws Exception {
		Polygon bridge = new Polygon(new Point[]{new Point(10,0),
				new Point(20,20),
				new Point(40,20),
				new Point(50, 0),
				new Point(60, 0),
				new Point(50,40),
				new Point(10,30),});
		assertEquals(1000, bridge.getArea(), Constants.EPSILON);
		
		// same bridge, but points in different order
		bridge = new Polygon(new Point[]{new Point(60, 0),
				new Point(50,40),
				new Point(10,30),
				new Point(10,0),
				new Point(20,20),
				new Point(40,20),
				new Point(50, 0),});
		assertEquals(1000, bridge.getArea(), Constants.EPSILON);
		
		Polygon hexagon = new Polygon(new Point[]{new Point(10,0),
				new Point(30,0),
				new Point(40,20),
				new Point(30,40),
				new Point(10,40),
				new Point(0,20),});
		assertEquals(1200, hexagon.getArea(), Constants.EPSILON);
	}
	
	@Test
	public void testConvexHull() throws Exception {
		Random rand = new Random(System.currentTimeMillis());
		///*
		Point[] points = new Point[40];
		// random points in [0-100]^2
		for (int i = 0; i < points.length; i++)
			points[i] = new Point(rand.nextInt(100), rand.nextInt(100));//*/

		//Point[] points = new Point[]{new Point(41, 94), new Point(89, 53), new Point(53, 21), new Point(70, 37), new Point(40, 50)};
		System.out.println(Arrays.toString(points));
		Polygon convex = Polygon.convexHull(points);
		assertTrue(convex.isConvex());
		
		// contains them all
		for (int i = 0; i < points.length; i++) 
			assertTrue("Outside: "+points[i], convex.contains(points[i]));
		
	}

}
