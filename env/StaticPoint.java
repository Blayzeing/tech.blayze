package classes.env;

/**
 * A read-only point in 2D space.
 */
 
 public class StaticPoint {
	protected double x;
	protected double y;
	
	public StaticPoint(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}

	public String toString()
	{
		return ("[StaticPoint] x: "+x+"  y: "+y);
	}
}