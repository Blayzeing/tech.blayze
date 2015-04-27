package classes.math;
import java.util.Arrays;
import classes.math.NVector;
/**
 * A Matrix class for N-by-M matrices
 */

public class NMatrix {
	
	private double[][] elements;
	private int width, height;
	
	public NMatrix (double[][] elements)
	{
		if(checkSensible(elements))
		{
			this.elements = elements;
			height = elements.length;
			width = elements[0].length;
		}
	}
	public NMatrix (int width, int height)
	{
		this(new double[height][width]);
	}
	
	// Getters
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public double getElement (int x, int y)
	{
		try{
			return(elements[y][x]);
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR:\n"+e+"\n Returning Double.NaN");
			return(Double.NaN);
		}
	}
	public NVector getColumn (int c)
	{
		NVector v = new NVector(height);
		for(int i = 0; i<height; i++)
			v.setElement(i, elements[i][c]);
		return v;
	}
	public NVector getRow (int r)
	{
		return (new NVector(elements[r]));
	}
	
	// Setters
	public void setElement (int x, int y, double value)
	{
		try{
			elements[y][x] = value;
		}catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("WARNING:\n"+e);
		}
	}
	/**
	 * Sets the elements in this matrix to those of values, eg:
	 * a = [1,2]  b = [5]  c = [6,7,2]
	 *     [3,4]               [8,9,0]
	 *                         [1,7,3]
	 * a.setElements(b) = [5,2]
	 *                    [3,4]
	 *
	 * a.setElements(c) = [6,7]
	 *                    [8,9]
	 */
	public void setElements(double[][] values)
	{
		if(checkSensible(values))
		{
			int w = Math.min(width, values[0].length);
			int h = Math.min(height, values.length);
			for(int i = 0; i<w; i++)
				for(int o = 0; o<h; o++)
					setElement(i,o, values[o][i]);
		}
	}

	// Maths functions
	//	Standard (instance)
	public void zero()
	{
		int len = width * height;
		for(int i = 0; i<len; i++)
			elements[(int)(i/height)][i%width] = 0;
	}
	public NMatrix returnZeroed()
	{
		return (new NMatrix(width,height));
	}
	public NMatrix scale(double s)
	{
		NMatrix out = new NMatrix(width, height);
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				out.setElement(o,i,elements[i][o] * s);
		return out;
	}
	public boolean isSameSize(NMatrix m)
	{
		return (width == m.getWidth() && height == m.getHeight());
	}
	public boolean equals(NMatrix m)
	{
		if(!isSameSize(m))
			return false;
		for(int i = 0; i<height; i++)
			for(int o = 0; o<width; o++)
				if(m.getElement(o,i) != elements[i][o])
					return false;
		return true;
	}
	public NMatrix add(NMatrix m)
	{
		NMatrix out = new NMatrix(width, height);
		if(isSameSize(m))
		{
			for(int i = 0; i<height; i++)
				for(int o = 0; o<width; o++)
					out.setElement(o,i,elements[i][o] + m.getElement(o,i));
		}// Maybe throw an exception here?
		return out;
	}
	public NMatrix subtract(NMatrix m)
	{
		NMatrix out = new NMatrix(width, height);
		if(isSameSize(m))
		{
			for(int i = 0; i<height; i++)
				for(int o = 0; o<width; o++)
					out.setElement(o,i,elements[i][o] - m.getElement(o,i));
		}// Again, maybe throw an exception
		return out;
	}
	public NMatrix multiply(NMatrix m)
	{
		NMatrix out = new NMatrix(width, height);
		if(this.height == m.getWidth())
		{
			for(int i = 0; i<width; i++)
				for(int o = 0; o<height; o++)
				{
					NVector a = getRow(o);
					NVector b = m.getColumn(i);
					out.setElement(i,o,a.dot(b));
				}
		}
		return out;
	}
					
	//	Static
	public static void zero (NMatrix m)
	{
		m.zero();
	}
	public static boolean isSameSize (NMatrix a, NMatrix b)
	{
		return(a.isSameSize(b));
	}
	public static boolean equals (NMatrix a, NMatrix b)
	{
		return(a.equals(b));
	}
	public static NMatrix scale (NMatrix m, double s)
	{
		return(m.copy().scale(s));
	}
	public static NMatrix add (NMatrix a, NMatrix b)
	{
		return(a.add(b));
	}
	public static NMatrix subtract (NMatrix a, NMatrix b)
	{
		return(a.subtract(b));
	}
	public static NMatrix multiply (NMatrix a, NMatrix b)
	{
		return(a.multiply(b));
	}
	
	// Utilities
	/**
	 * Returns a deep copy of this matrix
	 */
	public NMatrix copy()
	{
		return(new NMatrix(this.to2DArray()));
	}
	public String toString()
	{
		String out = "\n";
		for (double[] a : elements)
			out += Arrays.toString(a) + "\n";
		return out;
	}
	/**
	 * Returns a 2D array of the elements of this matrix (deep)
	 */
	public double[][] to2DArray ()
	{
		double[][] out = new double[height][width];
		for (int i = 0; i<height; i++)
			out[i] = elements[i].clone();
		return out;
	}
	
	/**
	 * This method checks if the given array is rectangular
	 */
	private static boolean checkRect (double[][] array)
	{
		int size = array[0].length;
		for(int i = 0; i<array.length; i++)
			if (array[i].length != size)
				return false;
		return true;
	}
	private static boolean checkSensible(double[][] array)
	{
		return (array != null && array.length != 0 && array[0].length != 0 && checkRect(array));
	}
	
	public static void main (String[] args)
	{
		System.out.println("\nNMatrix Test (see source):\n");
		double[][] rectTest1 = {{1,2,3,4,5},
								{1,2,3,4},
								{1,2,3,4,5}};
		double[][] rectTest2 = {{1,2,3,4,5},
								{1,2,3,4,5},
								{1,2,3,4,5}};
		System.out.println("rectTest1 test: " + checkRect(rectTest1) + "  [should be false]");
		System.out.println("rectTest2 test: " + checkRect(rectTest2) + "   [should be true]");
		NMatrix m = new NMatrix(rectTest2);
		System.out.println("toString():" + m);
		System.out.println("Deep copy of Array:");
		System.out.println("Original's element's identity hash:   " + System.identityHashCode(m.elements));
		System.out.println("Original.to2DArray()'s identity hash: " + System.identityHashCode(m.to2DArray()));
		System.out.println("Deep copy of Matrix:");
		System.out.println("Original's identity hash: " + System.identityHashCode(m));
		System.out.println("Copy's identity hash:     " + System.identityHashCode(m.copy()));
		System.out.println("setElementsTo():");
		NMatrix a = new NMatrix(new double[][]{new double[]{8,3},new double[]{2,6}});
		NMatrix b = new NMatrix(new double[][]{new double[]{7,9}});
		System.out.println("Setting" + a + " with " + b + " gives");
		a.setElements(b.to2DArray());
		System.out.println(a);
		System.out.println("Setting" + a + " with " + m + " gives");
		a.setElements(m.to2DArray());
		System.out.println(a);
		System.out.println("Scaling" + a + "by 2:");
		a = a.scale(2);
		System.out.println(a);
		//TODO: randomize
		double[][] ca = {{5,7,2},{1,6,3}};
		double[][] da = {{1,2,9},{8,4,3}};
		NMatrix c = new NMatrix(ca);
		NMatrix d = new NMatrix(da);
		System.out.println("Getting leftmost columnn from" + c);
		System.out.println(c.getColumn(0));
		System.out.println("Getting topmost row...");
		System.out.println(c.getRow(0));
		System.out.println("Adding"+c+"and"+d+"equals"+c.add(d));
	}
}
