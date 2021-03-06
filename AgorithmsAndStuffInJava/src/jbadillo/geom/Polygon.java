package jbadillo.geom;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.util.Arrays.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Polygon {
		
	private Point[] points;
	private Line[] sides;
	
	/**
	 * Polygon is build with line segments between consecutive points
	 * and a line segment from the first to the last.
	 * @param points
	 */
	public Polygon(Point... points) {
		this.points = points;
		sides = new Line[points.length];
		for (int i = 0; i < points.length; i++)
			sides[i] = new Line(points[i], points[(i + 1) % points.length]);
		
	}
	
	public Polygon(Collection<Point> points) {
		this(points.toArray(new Point[points.size()]));
		
	}

	public Point[] getPoints() {
		// a copy to avoid inconsistencies
		return stream(points)
				.map(p -> new Point(p.x, p.y))
				.toArray(Point[]::new);
	}
	
	public Line[] getSides() {
		// a copy to avoid inconsistencies
		return stream(sides)
				.map(l -> new Line(l.p1, l.p2))
				.toArray(Line[]::new);
	}
	
	/***
	 * A polygon is convex if all point pairs inside the polygon can be connected
	 * by a line that remains inside the polygon.
	 * @return true if all angles are convex, polygon is equal to its convex hull
	 */
	public boolean isConvex(){
		
		double [] innerAngles = new double[sides.length];
		for (int i = 0; i < sides.length; i++){
			innerAngles[i] = sides[i].getAngle() - sides[(i + 1) % sides.length].getAngle();
			// normalize from -PI to PI
			if(innerAngles[i] < -PI)
				innerAngles[i] += 2*PI;
			else if(innerAngles[i] > PI)
				innerAngles[i] -= 2*PI;
		}
		
		boolean allPositive = stream(innerAngles).allMatch(a -> a > 0);
		boolean allNegative = stream(innerAngles).allMatch(a -> a < 0);
		return allPositive || allNegative;
	}
	
	/**
	 * @param p
	 * @return true if p is inside the polygon
	 */
	public boolean contains(Point p){
		// min and max x
		double minx = stream(points).mapToDouble(p1 -> p1.x).min().getAsDouble();
		double maxx = stream(points).mapToDouble(p1 -> p1.x).max().getAsDouble();
		// easy case, out of bounds
		if(p.x < minx || p.x > maxx)
			return false;
		// other easy case, contained in any side
		if(stream(sides).anyMatch(s -> s.contains(p)))
			return true;
				
		// two horizontal line segments from min-p and p-max
		Line l1 = new Line(new Point(minx, p.y), p);
		Line l2 = new Line(p, new Point(maxx, p.y));
		// how many are crossed
		int cross1 = 0;
		int cross2 = 0;
		for(Line side: sides){
			// no parallel sides
			if(!side.isParallel(l1)){
				Point pint = side.intersection(l1);
				if(pint != null){
					// not a vertex
					if(!pint.equals(side.p1) && !pint.equals(side.p2))
						cross1++;
					// if a vertex, count only if above
					else if(side.p1.y > p.y || side.p2.y > p.y)
						cross1++;
				}
				pint = side.intersection(l2);
				if(pint != null){
					// not a vertex
					if(!pint.equals(side.p1) && !pint.equals(side.p2))
						cross2++;
					// if a vertex, count only if above
					else if(side.p1.y > p.y || side.p2.y > p.y)
						cross2++;
				}
			}
		}
		
		// easy case, if one of them is not crossed, its out
		if(cross1 == 0 || cross2 == 0)
			return false;
		// if both even, it's out
		if(cross1 % 2 == 0 && cross1 % 2 == 0)
			return false;
		// if both odd, it's in
		if(cross1 % 2 == 1 && cross1 % 1 == 0)
			return true;
		return false;
		
	}
	
	/**
	 * @return Area of the polygon.
	 */
	public double getArea(){
		if(points.length < 3)
			return 0.0;
		return abs(getArea(this.points));		
	}
	
	/**
	 * Keeps the area sign (negative if clockwise, positive if otherwise)
	 * @param points
	 * @return
	 */
	private double getArea(Point... points)
	{
		double area = 0.0;
		// easy case, triangles
		if(points.length == 3){
			double v1x = points[1].x - points[0].x,
					v1y = points[1].y - points[0].y,
					v2x = points[2].x - points[1].x,
					v2y = points[2].y - points[1].y;
			// cross product of first two sides
			area = (v1x * v2y - v1y * v2x) / 2.0;
		}
		else{
			// triangulate from first point to every other consecutive point pair
			for (int i = 1; i < points.length - 1; i++)
				area += getArea(points[0], points[i], points[i + 1]);
		}
		return area;
	}
	
	/**
	 * Calculates the convex hull of a given set of points
	 * @param points
	 * @return A polygon made with the convex hull
	 */
	public static Polygon convexHull(Point...points){
		LinkedList<Point> list = new LinkedList<>();
		// sort them desc y and asc x
		// first one will be the top-left most
		Arrays.sort(points, (p1, p2) -> {
			if (p1.y < p2.y)
				return 1;
			if (p1.y > p2.y)
				return -1;
			if (p1.x > p2.x)
				return 1;
			return -1;
		});
		
		
		Point start = points[0], point = start, next;
		double prevangle = -2*PI, minangle = -PI;
		
		do{
			list.add(point);
			prevangle = minangle;
			minangle = PI;	
			next = null;
			// minimum angle to point
			for (int i = 0; i < points.length; i++) {
				if(point.equals(points[i]))
					continue;
				Line l = new Line(point, points[i]);
				if(prevangle < l.getAngle() && l.getAngle() < minangle){
					next = points[i];
					minangle = l.getAngle();
				}
			}
			point = next;
		}while(point != null && !point.equals(start));
		
		return new Polygon(list);
	}
	
	
}
