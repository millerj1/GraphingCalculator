import java.util.*;
import java.awt.*;
public class Function
{
    public static final String[] VALID_COLORS = {"BLACK", "RED", "ORANGE", "GRAY", "GREEN", "BLUE", "PINK", "CYAN", "MAGENTA"};
    
    private String function;
    private Color color;
    private String colorName;
    private ArrayList<Double> memory;
    
    public Function(String function)
    {
        this.function = function;
        colorName = "BLACK";
        color = Color.BLACK;
        memory = new ArrayList<Double>();
    }
    
    public Function(String function, String colorName)
    {
        this.function = function;
        this.colorName = colorName;
        if(colorName.equals("BLACK"))
            this.color = Color.BLACK;
        else if(colorName.equals("RED"))
            this.color = Color.RED;
        else if(colorName.equals("ORANGE"))
            this.color = Color.ORANGE;
        else if(colorName.equals("GRAY"))
            this.color = Color.GRAY;
        else if(colorName.equals("GREEN"))
            this.color = Color.GREEN;
        else if(colorName.equals("BLUE"))
            this.color = Color.BLUE;
        else if(colorName.equals("PINK"))
            this.color = Color.PINK;
        else if(colorName.equals("CYAN"))
            this.color = Color.CYAN;
        else if(colorName.equals("MAGENTA"))
            this.color = Color.MAGENTA;
        
        memory = new ArrayList<Double>();
    }
    
    public void setColor(String colorName)
    {
        if(isValidColor(colorName))
        {
            this.colorName = colorName;
            if(colorName.equals("BLACK"))
                this.color = Color.BLACK;
            else if(colorName.equals("RED"))
                this.color = Color.RED;
            else if(colorName.equals("ORANGE"))
                this.color = Color.ORANGE;
            else if(colorName.equals("GRAY"))
                this.color = Color.GRAY;
            else if(colorName.equals("GREEN"))
                this.color = Color.GREEN;
            else if(colorName.equals("BLUE"))
                this.color = Color.BLUE;
            else if(colorName.equals("PINK"))
                this.color = Color.PINK;
            else if(colorName.equals("CYAN"))
                this.color = Color.CYAN;
            else if(colorName.equals("MAGENTA"))
                this.color = Color.MAGENTA;
        }
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public String getColorName()
    {
        return colorName;
    }
    
    public static boolean isValidColor(String colorName)
    {
        for(int i = 0; i < VALID_COLORS.length; i ++)
        {
            if(VALID_COLORS[i].equals(colorName))
                return true;
        }
        return false;
    }
    
    //precondition: func is properly notated
    public static double evaluate(String func)
    {
        Function function = new Function(func);
        return function.evaluate(func, 0);
    }
    
    public void clearMem()
    {
        while(!memory.isEmpty())
        {
            memory.remove(0);
        }
    }
    
    public String getFunc()
    {
        return function;
    }
    
    public String toString()
    {
        return function;
    }
    
    public ArrayList<Coordinate> getCoords(double xMin, double xMax, double xScale, double yMin, double yMax, double yScale)
    {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        
        for(double x = xMin - xScale; x <= xMax + xScale; x += xScale)
        {
            if(evaluate(function, x) <= yMax * 1.5 && evaluate(function, x) >= yMin * 1.5 && x <= xMax * 1.5 && x >= xMin * 1.5)
            {
                coords.add(new Coordinate(x, evaluate(function, x)));
            }
        }
        return coords;
    }
    
    //precondition: func is properly notated
    public double evaluate(String func, double x)
    {
        //System.out.println("Evaluating " + func + " at " + x); //For debugging purposes
        
        //If this is first call, clear internal memory
        if(func.equals(function))
            clearMem();
        
        func = removeSpaces(func); //removing spaces
        
        //Base cases
        if(func.equals("x"))
        {
        	//func is only an x
            return x;
        }
        else if(func.length() == 0)
        {
        	//func is nothing
            return 0;
        }
        else if(allNums(func))
        {
        	//func is an integer
            return Integer.parseInt(func);
        }
        else if((func.charAt(0) == '-' && allNums(func.substring(1))))
        {
        	//func is a negative integer
            return Integer.parseInt(func.substring(1)) * -1;
        }
        else if(allDouble(func))
        {
        	//func is a double
            return Double.parseDouble(func);
        }
        else if((func.charAt(0) == '-' && allDouble(func.substring(1))))
        {
        	//func is a negative double
            return Double.parseDouble(func.substring(1)) * -1;
        }
        else if(func.length() >= 4 &&(func.substring(0,3).equals("mem") && allNums(func.substring(3))))
        {
        	//func is a memory marker
            double val = memory.get(Integer.parseInt(func.substring(3)));
            if(Math.abs(val) < 1E-5)
                return 0.0;
            else
                return val;
        }
        else if(func.equals("pi"))
        {
        	//func is pi
            return Math.PI;
        }
        else if(func.equals("e"))
        {
        	//func is e
            return Math.E;
        }
        
        //if func contains a derivative
        if(func.indexOf("d/dx") >= 0)
        {
            return processDerivative(func, x);
        }
        
        //if func contains sine
        if(func.indexOf("sin") >= 0)
        {
            return processSine(func, x);
        }
        
        //if func contains cosine
        if(func.indexOf("cos") >= 0)
        {
            return processCosine(func, x);
        }
        
        //if func contains tangent
        if(func.indexOf("tan") >= 0)
        {
            return processTangent(func, x);
        }
        
        //if func contains logarithm (base 10)
        if(func.indexOf("log") >= 0)
        {
            return processLogarithm(func, x);
        }
        
        //if func contains natural log
        if(func.indexOf("ln") >= 0)
        {
            return processNaturalLog(func, x);
        }
        
        //Beginning of basic order of operations
        
        //if func contains parentheses
        if(func.indexOf('(') >= 0)
        {
            return processParentheses(func, func.indexOf('('), findParentheses(func, func.indexOf('(')), x);
        }
        
        //if func contains an exponent
        if(func.indexOf('^') >= 0)
        {
            return processExp(func, x);
        }
        
        //if func contains + or -
        if(func.indexOf('+') >= 0 || func.indexOf('-') >= 0)
        {
        	//if + occurs before - or there is no -
            if((func.indexOf('+') < func.indexOf('-') || func.indexOf('-') < 0) && func.indexOf('+') >= 0)
            {
                return evaluate(func.substring(0, func.indexOf('+')), x) + evaluate(func.substring(func.indexOf('+') + 1), x);
            }
            return evaluate(func.substring(0, func.indexOf('-')), x) - evaluate(func.substring(func.indexOf('-') + 1), x);
        }
        
        //if func contains / or *
        if(func.indexOf('/') >= 0 || func.indexOf('*') >= 0)
        {
        	//if / occurs before * or there is no *
            if((func.indexOf('/') < func.indexOf('*') || func.indexOf('*') < 0) && func.indexOf('/') >= 0)
            {
                return evaluate(func.substring(0, func.indexOf('/')), x) / evaluate(func.substring(func.indexOf('/') + 1), x);
            }
            return evaluate(func.substring(0, func.indexOf('*')), x) * evaluate(func.substring(func.indexOf('*') + 1), x);
        }
        //if no operators were detected, assume func is in coefficient form
        return processCoefficient(func, x);
    }
    
    private double processCoefficient(String string, double x)
    {
        int end = string.length();
        for(int i = 0; i < string.length(); i ++)
        {
            if(!allDouble(string.substring(0, i)))
            {
                end = i - 1;
            }
        }
        return evaluate(string.substring(0, end), x) * evaluate(string.substring(end), x);
    }
    
    private double processExp(String string, double x)
    {
        int index = string.indexOf('^');
        String leadTerm = prevTerm(string, index);
        String followingTerm = nextTerm(string, index);
        double powTerm = Math.pow(evaluate(leadTerm, x), evaluate(followingTerm, x));
        memory.add(powTerm);
        String newString = string.substring(0, string.lastIndexOf(leadTerm, index)) + "(mem" + (memory.size() - 1) + ")" + string.substring(string.indexOf(followingTerm, index) + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processSine(String string, double x)
    {
        int index = string.indexOf("sin");
        String followingTerm = nextTerm(string, index + 2);
        double sinTerm = Math.sin(evaluate(followingTerm, x));
        memory.add(sinTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 3 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processCosine(String string, double x)
    {
        int index = string.indexOf("cos");
        String followingTerm = nextTerm(string, index + 2);
        double cosTerm = Math.cos(evaluate(followingTerm, x));
        memory.add(cosTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 3 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processTangent(String string, double x)
    {
        int index = string.indexOf("tan");
        String followingTerm = nextTerm(string, index + 2);
        double tanTerm = Math.tan(evaluate(followingTerm, x));
        memory.add(tanTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 3 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processLogarithm(String string, double x)
    {
        int index = string.indexOf("log");
        String followingTerm = nextTerm(string, index + 2);
        double logTerm = Math.log10(evaluate(followingTerm, x));
        memory.add(logTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 3 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processNaturalLog(String string, double x)
    {
        int index = string.indexOf("ln");
        String followingTerm = nextTerm(string, index + 1);
        double lnTerm = Math.log(evaluate(followingTerm, x));
        memory.add(lnTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 2 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    private double processDerivative(String string, double x)
    {
        int index = string.indexOf("d/dx");
        String followingTerm = nextTerm(string, index + 3);
        double derivTerm = (evaluate(followingTerm, x + 0.000001) - evaluate(followingTerm, x)) / 0.000001;
        memory.add(derivTerm);
        String newString = string.substring(0, index) + "(mem" + (memory.size() - 1) + ")" + string.substring(index + 4 + followingTerm.length());
        return evaluate(newString, x);
    }
    
    //returns the next term (grouped by non-basic operation or parentheses) in string after index start
    private String nextTerm(String string, int start)
    {
        if(string.charAt(start + 1) == 'x')
            return "x";
        else if(allNums(String.valueOf(string.charAt(start + 1))))
        {
            String num = String.valueOf(string.charAt(start + 1));
            for(int i = start + 2; i < string.length(); i ++)
            {
                if(!allNums(String.valueOf(string.charAt(i))))
                {
                    return num;
                }
                num += string.charAt(i);
            }
            return num;
        }
        else
        {
            return string.substring(start + 1, findParentheses(string, start + 1) + 1);
        }
    }
    
    //returns the previous term (grouped by non-basic operation or parentheses) in string before index start
    private String prevTerm(String string, int end)
    {
        if(string.charAt(end - 1) == 'x')
            return "x";
        else if(allNums(String.valueOf(string.charAt(end - 1))))
        {
            String num = String.valueOf(string.charAt(end - 1));
            for(int i = end - 2; i >= 0; i --)
            {
                if(!allNums(String.valueOf(string.charAt(i))))
                {
                    num = reverse(num);
                    return num;
                }
                num += string.charAt(i);
            }
            num = reverse(num);
            return num;
        }
        else
        {
            return string.substring(findParenthesesBack(string, end - 1), end);
        }
    }
    
    //returns input in reverse order
    private String reverse(String input)
    {
        String output = new String("");
        for(int i = 0; i < input.length(); i ++)
        {
            output += input.charAt(input.length()- 1 - i);
        }
        return output;
    }
    
    //returns the index of the closing parenthesis corresponding to the opening one at index i
    private int findParentheses(String string, int i)
    {
        i++;
        int counter = 1;
        while(counter != 0 && i < string.length())
        {
            if(string.charAt(i) == '(')
            {
                counter ++;
            }
            else if(string.charAt(i) == ')')
            {
                counter --;
            }
            i++;
        }
        i --;
        return i;
    }
    
    //returns the index of the opening parenthesis corresponding to the closing one at index i
    private int findParenthesesBack(String string, int i)
    {
        i--;
        int counter = 1;
        while(counter != 0 && i >= 0)
        {
            if(string.charAt(i) == ')')
            {
                counter --;
            }
            else if(string.charAt(i) == '(')
            {
                counter ++;
            }
            i--;
        }
        i ++;
        return i;
    }
    
    //returns the evaluation of the argument within the parentheses from start to end
    private double processParentheses(String string, int start, int end, double x)
    {
        if(end != string.length() - 1)
            return processParenBack(string, start, end, x);
        
        if(start != 0)
            return processParenFront(string, start, end, x);
            
        return evaluate(string.substring(1, string.length() - 1), x);
    }
    
    private double processParenFront(String string, int start, int end, double x)
    {
        if(string.charAt(start - 1) == '+')
            return evaluate(string.substring(0, start - 1), x) + evaluate(string.substring(start), x);
            
        if(string.charAt(start - 1) == '-')
            return evaluate(string.substring(0, start - 1), x) - evaluate(string.substring(start), x);
            
        if(string.charAt(start - 1) == '*')
            return evaluate(string.substring(0, start - 1), x) * evaluate(string.substring(start), x);
            
        if(string.charAt(start - 1) == '/')
            return evaluate(string.substring(0, start - 1), x) / evaluate(string.substring(start), x);
            
        if(string.charAt(start - 1) == '^')
            return processExp(string, x);
            
        return evaluate(string.substring(0, start), x) * evaluate(string.substring(start), x);
    }
    
    private double processParenBack(String string, int start, int end, double x)
    {
        if(string.charAt(end + 1) == '+')
            return evaluate(string.substring(0, end + 1), x) + evaluate(string.substring(end + 2), x);
            
        if(string.charAt(end + 1) == '-')
            return evaluate(string.substring(0, end + 1), x) - evaluate(string.substring(end + 2), x);
        
        if(string.charAt(end + 1) == '*')
            return evaluate(string.substring(0, end + 1), x) * evaluate(string.substring(end + 2), x);
            
        if(string.charAt(end + 1) == '/')
            return evaluate(string.substring(0, end + 1), x) / evaluate(string.substring(end + 2), x);
            
        if(string.charAt(end + 1) == '^')
            return processExp(string, x);
            
        return evaluate(string.substring(0, end + 1), x) * evaluate(string.substring(end + 1), x);
    }
    
    //returns true if string only contains the digits 1-9 and the negative sign
    private static boolean allNums(String string)
    {
        for(int i = 0; i < string.length(); i ++)
        {
            if(string.charAt(i) < '0' || '9' < string.charAt(i))
                return false;
        }
        return true;
    }
    
    //returns true if string only contains the digits 1-9, a deciman point,  and the negative sign
    private static boolean allDouble(String string)
    {
        for(int i = 0; i < string.length(); i ++)
        {
            if((string.charAt(i) < '0' || '9' < string.charAt(i)) || (string.charAt(i) == '-' && i != 0))
            {
                if(string.charAt(i) != '.')
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    //returns string with the spaces removed
    private static String removeSpaces(String string)
    {
        String newString = "";
        for(int i = 0; i < string.length(); i ++)
        {
            if(string.charAt(i) != ' ')
                newString += string.charAt(i);
        }
        return newString;
    }
}