package jbadillo.geom;

/**
 * A line segment between two points
 * @author jbadillo
 *
 */
public class Line {
	
	Point p1, p2;
	double dx, dy;
	private double angle;
	private double b;
	private double a;

	public Line(Point p1, Point p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.dx = p2.x - p1.x;
		this.dy = p2.y - p1.y;
		this.angle = Math.atan2(dy, dx);

		// x-intersect
		a = dy != 0? (p1.x*p2.y - p2.x*p1.y) / dy: Double.NaN;
		// y-intersect
		b = dx != 0? (p2.x*p1.y - p1.x*p2.y) / dx: Double.NaN;
	}
	
	public Line(double x1, double y1, double x2, double y2){
		this.p1 = new Point(x1, y1);
		this.p2 = new Point(x2, y2);
		this.dx = p2.x - p1.x;
		this.dy = p2.y - p1.y;
		this.angle = Math.atan2(dy, dx);
		// x-intersect
		a = dy != 0? (p1.x*p2.y - p2.x*p1.y) / dy: Double.NaN;
		// y-intersect
		b = dx != 0? (p2.x*p1.y - p1.x*p2.y) / dx: Double.NaN;
	}
	
	public double getDx() {
		return dx;
	}
	
	public double getDy() {
		return dy;
	}
	
	/**
	 * @return x-intersect (root) if extended infinitelly, NaN if none
	 */
	public double getXIntersect(){
		return a;
	}
	
	/**
	 * @return y-intersect if extended infinitelly, NaN if none
	 */
	public double getYIntersect(){
		return b;
	}
	
	/**
	 * @return get angle from p1 to p2
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * @param p
	 * @return true if p is contained in the line segment
	 */
	public boolean contains(Point p){
		if(p1.equals(p) || p2.equals(p))
			return true;
		// colinear
		if(!colinear(p))
			return false;
		// outside bounds
		if(p.x < Math.min(p1.x, p2.x) || Math.max(p1.x, p2.x) < p.x)
			return false;
		if(p.y < Math.min(p1.y, p2.y) || Math.max(p1.y, p2.y) < p.y)
			return false;
		return true;
	}
	
	/**
	 * @param p
	 * @return true if p is colinear (even if outside of the line segment).
	 */
	public boolean colinear(Point p){
		// check slope matches
		double dx2 = p1.x - p.x, dy2 = p1.y - p.y;
		
		// m1 = (dy/dx) = m2 = (dy2 / dx2)
		return dy * dx2 == dy2 * dx;
	}
	
	public boolean isParallel(Line l){
		return this.dy * l.dx == l.dy * this.dx;
	}
	
	/**
	 * Point of intersection with other line.
	 * @param l
	 * @return null if none or more than one
	 */
	public Point intersection(Line l){
		// parallel case
		if(isParallel(l))
		{
			if(p1.equals(l.p1) || p1.equals(l.p2))
				return p1;
			if(p2.equals(l.p1) || p2.equals(l.p2))
				return p2;
			return null;
		}
		Point p = new Point(0, 0);
		
		// if one is vertical
		if(this.dx == 0){
			p.x = p1.x;
			// if the other horizontal
			if(l.dy == 0)
				p.y = l.p1.y;
			// line equation y = m2*x + b2
			else
				p.y = l.dy / l.dx * p.x + l.b; 
		}
		// symmetric case
		else if(l.dx == 0){
			p.x = l.p1.x;
			if(dy == 0)
				p.y = p1.y;
			else 
				p.y = dy / dx * p.x + b; 
		}
		else{
			// default case, line equation
			p.x = (b - l.b) / (l.dy/l.dx - dy/dx);
			p.y = dy / dx * p.x + b;
		}
		// if inside both line segments
		if(contains(p) && l.contains(p))
			return p;
		return null;
	}

	public String toString() {
		return String.format("[%s - %s]", p1.toString(), p2.toString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Line))
			return false;
		Line l = (Line)obj;
		return (this.p1.equals(l.p1) && this.p2.equals(l.p2))
				|| (this.p1.equals(l.p2) && this.p2.equals(l.p1));
	}
	
}

