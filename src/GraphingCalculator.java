import java.util.*;
import javax.swing.*;
public class GraphingCalculator
{
    private Grapher grapher;
    private ArrayList<Function> funcs;
	private Scanner input;
    
    public GraphingCalculator()
    {
    	input = new Scanner(System.in);
        funcs = new ArrayList<Function>();
        grapher = new Grapher(funcs);
    }
    
    public static void main(String[] args)
    {
        GraphingCalculator graphing = new GraphingCalculator();
        graphing.mainMenu();
    }
    
    public void mainMenu()
    {
        System.out.println("Graphing Calculator");
        System.out.println("1: Calculate");
        System.out.println("2: Y=");
        System.out.println("3: Window");
        System.out.println("4: Graph");
        System.out.println("5: Exit");
        int response = input.nextInt();
        if(response == 1)
            calcMenu();
        else if(response == 2)
            funcMenu();
        else if(response == 3)
            windowMenu();
        else if(response == 4)
        {
            JFrame frame = new JFrame("Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		frame.setLocation(0,0);
            frame.getContentPane().add(grapher);
    		frame.pack();
    		frame.setVisible(true);
            grapher.graph();
            mainMenu();
        }
        else if(response != 5)
        {
            System.out.println("Invalid Input");
            mainMenu();
        }
    }
    
    public void calcMenu()
    {
        System.out.println("Enter calculation or E to exit");
        String func = input.nextLine();
        if(func.equals("E"))
            mainMenu();
        else
        {
            double answer = Function.evaluate(func);
            System.out.println(answer);
            calcMenu();
        }
    }
    
    public void funcMenu()
    {
        System.out.println("Enter number for function to edit");
        int last = grapher.printFuncs();
        System.out.println("" + (last + 1) + ": Add New Function");
        System.out.println("" + (last + 2) + ": Set Function Colors");
        System.out.println("" + (last + 3) + ": Exit");
        int response = input.nextInt();
        if(0 < response && response <= last + 1)
        {
            System.out.println("Enter new declaration for Y" + response);
            String newFunc = input.nextLine();
            newFunc = input.nextLine();
            grapher.setFunc(response, new Function(newFunc));
            funcMenu();
        }
        else if(response == last + 2)
        {
            colorMenu();
        }
        else if(response == last + 3)
        {
            mainMenu();
        }
        else
        {
            System.out.println("Invalid Input");
            funcMenu();
        }
    }
    
    public void colorMenu()
    {
        System.out.println("Enter number for function to change the color of");
        int last = grapher.printFuncColors();
        System.out.println("" + (last + 1) + ": Exit");
        int response = input.nextInt();
        if(0 < response && response <= last)
        {
            System.out.println("Enter new color for Y" + response);
            System.out.println("Valid colors are:");
            System.out.println("BLACK, RED, ORANGE, GRAY, GREEN, BLUE, PINK, CYAN, and MAGENTA");
            String newColor = input.nextLine();
            newColor = input.nextLine();
            if(!Function.isValidColor(newColor))
            {
                System.out.println("Invalid Color Name");
                colorMenu();
            }
            grapher.setFuncColor(response, newColor);
            colorMenu();
        }
        else if(response == last + 1)
        {
            funcMenu();
        }
        else
        {
            System.out.println("Invalid Input");
            colorMenu();
        }
    }
    
    public void windowMenu()
    {
        System.out.println("1: xMin: " + grapher.getXMin());
        System.out.println("2: xMax: " + grapher.getXMax());
        System.out.println("3: yMin: " + grapher.getYMin());
        System.out.println("4: yMax: " + grapher.getYMax());
        System.out.println("5: Exit");
        int response = input.nextInt();
        if(response == 1)
        {
            System.out.println("Enter new xMin");
            input.nextLine();
            String xMin = input.nextLine();
            grapher.setXMin(Function.evaluate(xMin));
            windowMenu();
        }
        else if(response == 2)
        {
            System.out.println("Enter new xMax");
            input.nextLine();
            String xMax = input.nextLine();
            grapher.setXMax(Function.evaluate(xMax));
            windowMenu();
        }
        else if(response == 3)
        {
            System.out.println("Enter new yMin");
            input.nextLine();
            String yMin = input.nextLine();
            grapher.setYMin(Function.evaluate(yMin));
            windowMenu();
        }
        else if(response == 4)
        {
            System.out.println("Enter new yMax");
            input.nextLine();
            String yMax = input.nextLine();
            grapher.setYMax(Function.evaluate(yMax));
            windowMenu();
        }
        else if(response == 5)
        {
            mainMenu();
        }
        else
        {
            System.out.println("Invalid Input");
            windowMenu();
        }
    }
}