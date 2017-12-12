package jbadillo.geom;

public class Point {
	double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Point))
			return false;
		Point p = (Point)obj;
		return this.x == p.x && this.y == p.y;
	}
	
	public boolean aproxEquals(Object obj) {
		Point p = (Point)obj;
		return Math.abs(this.x - p.x) < Constants.EPSILON && Math.abs(this.y - p.y) < Constants.EPSILON;
	}
	
	public String toString() {
		return String.format("(%s, %s)", x, y);
	}
	
}
