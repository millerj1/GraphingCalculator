public class Coordinate
{
    private double x;
    private double y;
    
    public Coordinate(double x, double y)
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
    
    //returns the x value within the JFrame coordinate system
    public double getJX(double scale, double min)
    {
        return (x - min) / scale;
    }
    
    //returns the y value within the JFrame coordinate system
    public double getJY(double scale, double max)
    {
        return (max - y) / scale;
    }
    
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
    
    public int compareTo(Coordinate other)
    {
        return (int)((x - other.getX()) + (y - other.getY()));
    }
}