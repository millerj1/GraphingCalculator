import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
public class Grapher extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 500;
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private double xScale;
    private double yScale;
    private ArrayList<Coordinate> coords;
    private ArrayList<Function> funcs;
    private ArrayList<Line2D.Double> lines;
    private ArrayList<Color> colors;
    
    public Grapher(ArrayList<Function> funcs)
    {
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(SIZE, SIZE));
        
        this.xMin = -10;
        this.xMax = 10;
        this.yMin = -10;
        this.yMax = 10;
        this.xScale = stepFinder(xMin, xMax, SIZE);
        this.yScale = stepFinder(yMin, yMax, SIZE);
        this.funcs = funcs;
        this.coords = new ArrayList<Coordinate>();
        this.lines = new ArrayList<Line2D.Double>();
        this.colors = new ArrayList<Color>();
    }
    
    public void setWindow(int xMin, int xMax, int yMin, int yMax)
    {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.xScale = stepFinder(xMin, xMax, 500);
        this.yScale = stepFinder(yMin, yMax, 500);
    }
    
    public double getXMin()
    {
        return xMin;
    }
    
    public double getXMax()
    {
        return xMax;
    }
    
    public double getYMin()
    {
        return yMin;
    }
    
    public double getYMax()
    {
        return yMax;
    }
    
    public void setXMin(double newValue)
    {
        xMin = newValue;
        xScale = stepFinder(xMin, xMax, 500);
    }
    
    public void setXMax(double newValue)
    {
        xMax = newValue;
        xScale = stepFinder(xMin, xMax, 500);
    }
    
    public void setYMin(double newValue)
    {
        yMin = newValue;
        yScale = stepFinder(yMin, yMax, 500);
    }
    
    public void setYMax(double newValue)
    {
        yMax = newValue;
        yScale = stepFinder(yMin, yMax, 500);
    }
    
    public int printFuncs()
    {
        int last = funcs.size();
        for(int i = 0; i < funcs.size(); i ++)
        {
            System.out.println("Y" + (i + 1) + "= " + funcs.get(i));
        }
        return last;
    }
    
    public int printFuncColors()
    {
        int last = funcs.size();
        for(int i = 0; i < funcs.size(); i ++)
        {
            System.out.println("Y" + (i + 1) + "= " + funcs.get(i).getColorName());
        }
        return last;
    }
    
    public void setFuncs(ArrayList<Function> funcs)
    {
        this.funcs = funcs;
    }
    
    public void setFunc(int i, Function func)
    {
        if(i < funcs.size())
            funcs.set(i - 1, func);
        else
            funcs.add(func);
    }
    
    public void setFuncColor(int i, String colorName)
    {
        funcs.get(i - 1).setColor(colorName);
    }
    
    public void clearCoords()
    {
        while(!coords.isEmpty())
        {
            coords.remove(0);
        }
    }
    
    public static double stepFinder(double start, double end, int length)
    {
        double step = 0;
        step = (end - start)/(double)(length);
        return step;
    }
    
    public void graph()
    {
        drawAxes();
        for(int i = 0; i < funcs.size(); i ++)
        {
            Function func = funcs.get(i);
            coords = func.getCoords(xMin, xMax, xScale, yMin, yMax, yScale);
            Color col = func.getColor();
            graph(null, col);
        }
        clearCoords();
    }
    
    public void graph(Coordinate prev, Color col)
    {
        if(coords.size() > 0)
        {
            Coordinate coord = coords.remove(0);
            if(prev != null && coord.getJX(xScale, xMin) - prev.getJX(xScale, xMin) < 1.01)
            {
                lines.add(new Line2D.Double(prev.getJX(xScale, xMin), prev.getJY(yScale, yMax), coord.getJX(xScale, xMin), coord.getJY(yScale, yMax)));
                colors.add(col);
            }
            repaint();
            try
            {
            	Thread.sleep(5);
            }catch(Exception e) {}
            graph(coord, col);
        }
    }
    
    public void drawAxes()
    {
        if(xMin <= 0 && 0 <= xMax)
        {
            Coordinate yStart = new Coordinate(0, yMin);
            Coordinate yEnd = new Coordinate(0, yMax);
            Line2D.Double yAxis = new Line2D.Double(yStart.getJX(xScale, xMin), yStart.getJY(yScale, yMax), yEnd.getJX(xScale, xMin), yEnd.getJY(yScale, yMax));
            lines.add(yAxis);
            colors.add(Color.BLACK);
        }
        
        if(yMin <= 0 && 0 <= yMax)
        {
            Coordinate xStart = new Coordinate(xMin, 0);
            Coordinate xEnd = new Coordinate(xMax, 0);
            Line2D.Double xAxis = new Line2D.Double(xStart.getJX(xScale, xMin), xStart.getJY(yScale, yMax), xEnd.getJX(xScale, xMin), xEnd.getJY(yScale, yMax));
            lines.add(xAxis);
            colors.add(Color.BLACK);
        }
        repaint();
    }
    
    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        for(int i = 0; i < lines.size(); i ++)
        {
            Line2D.Double line = lines.get(i);
            Color col = colors.get(i);
            page.setColor(col);
            page.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
        }
    }
}