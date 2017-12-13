package jbadillo.geom;
import static java.lang.Math.abs;
import static jbadillo.geom.Constants.EPSILON;

public class Point {
	double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Point))
			return false;
		// check reference
		if(this == obj)
			return true;
		Point p = (Point)obj;
		return abs(this.x - p.x) < EPSILON && abs(this.y - p.y) < EPSILON;
	}
	
	public String toString() {
		return String.format("(%s, %s)", x, y);
	}
	
}
