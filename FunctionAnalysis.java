import java.util.Scanner;
import java.util.Arrays;


public class FunctionAnalysis {
    private static Scanner scan = new Scanner(System.in);
	private Function func;

    // the constructor of the class, asks for the degree and coefficients and sets func accordingly
	public FunctionAnalysis() {
        System.out.println("\n----------------------------------------------------------------------------------------------\n");
        System.out.println("Welcome to our \"Symbo-Desmos\" Program!");
		System.out.println("The program will analyze a polynomial function that will be set by the coefficients you enter.");

		System.out.println("\nThe program will return the following data about the function:\n"
				    + "    1. The domain of the function.\n"
				    + "    2. Intersection points with the x axis & the y axis.\n"
				    + "    3. Positivity & negativity intervals of f(x).\n"
				    + "    4. The 1st derivative, f'(x), and extrema points of the function.\n"
				    + "    5. Increasing & decreasing intervals of f(x).\n"
				    + "    6. The 2nd derivative, f''(x), and inflection points of the function.\n"
				    + "    7. Concavity & convexity intervals of f(x).");
        
        System.out.println("\n----------------------------------------------------------------------------------------------\n");

        int degree = getDegree();
        double[] coefficients = new double[degree + 1];
        
        for (int i = 0; i <= degree; i++)
            coefficients[i] = getCoefficient(degree, i);
		this.func = new Function(coefficients);
	}

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    // a method to get the string representation of a value
    public String getStringRepr(double x) {
        // remove the ".0" if x is an integer
        return ((x == Math.round(x))? ("" + Math.round(x)):("" + x));
    }
    
    // a method to represent a point in the form (x, f(x))
    public String reprPoint(double x) {
        double y = func.smartRound(func.calcValue(x));
        return "(" + getStringRepr(x) + ", " + getStringRepr(y) + ")";
	}
    
    /* a method to represent the positive/negative intervals of a function, where points contains the zero-points
     * of f(x) and signs contains the positivity/negativity of each interval.
     * for example: if points = [0.5, 1.33, 5.0] and signs = [-1, 1, -1, 1] then the output will be the following
     * two-dimensional array: [["0.5 < x < 1.33", "x > 5.0"], ["x < 0.5", "1.33 < x < 5.0"]].
     */
    public String[][] reprIntervals(double[] points, int[] signs) {
        // if points is empty then func is a constant
        if (points.length == 0) {
            String[] positives = new String[] {(signs[0] > 0)? ("all x"):("no x")};
            String[] negatives = new String[] {(signs[0] > 0)? ("no x"):("all x")};
            return (new String[][] {positives, negatives});
        }
        
        String[] positives = new String[points.length + 1];
        String[] negatives = new String[points.length + 1];
        int numOfPos = 0; int numOfNeg = 0;
        
        String interval;
        for (int i = 0; i <= points.length; i++) {
            // check the interval type and set the string accordingly
            if (i == 0) {
                interval = ("x < " + getStringRepr(points[i]));
            } else if (i == points.length) {
                interval = "x > " + getStringRepr(points[i - 1]);
            } else {
                interval = (getStringRepr(points[i - 1]) + " < x < " + getStringRepr(points[i]));
            }

            // check the sign of the interval and add the interval to the correct array
            if (signs[i] > 0) {
                positives[numOfPos] = interval; numOfPos++;
            } else {
                negatives[numOfNeg] = interval; numOfNeg++;
            }
        }

        // slice the arrays to the correct length and return
        positives = Arrays.copyOf(positives, numOfPos);
        negatives = Arrays.copyOf(negatives, numOfNeg);
        return (new String[][] {positives, negatives});
    }

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    // a method to get a valid integer for the degree of the polynomial
    public int getDegree() {
		boolean invalidInput = true;
        int degree = 0;
        System.out.print("Please enter the degree of the polynomial function to analyze: ");

        while (invalidInput) {
            String input = scan.nextLine();
            try {
                degree = Integer.parseInt(input);
                if (degree < 0)
                    System.out.print("This is not a valid value. Please enter a positive integer for the degree: ");
                else
                    invalidInput = false;
            } catch (NumberFormatException e) {
                System.out.print("This is not a valid value. Please enter a positive integer for the degree: ");
            }
        }
        
        return degree;
    }

    // a method to get a valid number for a coefficient of the function
	public double getCoefficient(int degree, int coeffIndex) {
		double coefficient = 0;
		boolean invalidInput = true;

		if (coeffIndex == 0)
			System.out.print("Please enter the value of a0, the constant of the function: ");
		else if (coeffIndex == 1)
			System.out.print("Please enter the value of a1, the coefficient of x: ");
		else
			System.out.print("Please enter the value of a" + coeffIndex + ", the coefficient of x^" + coeffIndex + ": ");

		while (invalidInput) {
            String input = scan.nextLine();
            try {
                coefficient = Double.parseDouble(input);
                if (coeffIndex == degree && coefficient == 0 && degree != 0)
                    System.out.print("This is not a valid value. Please enter a non-zero value for this coefficient: ");
                else
                    invalidInput = false;
            } catch (NumberFormatException e) {
                System.out.print("This is not a valid value. Please enter a number to represent the coefficient: ");
            }
		}
        
        return coefficient;
	}

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    // a method to print the intersection points of func with the axis
    public void axisIntersections(double[] intersectX) {
        String intersectXMsg = "none";

        // add the intersection points with the x axis to the string
        if (intersectX.length != 0) {
            String[] message = new String[intersectX.length];
            for (int i = 0; i < intersectX.length; i++)
                message[i] = "(" + getStringRepr(intersectX[i]) + ", 0)";
            intersectXMsg = String.join(", ", message);
        }

        // this condition is equivalent to f(x) = 0
        if (func.getDegree() == 0 && func.getCoefficient(0) == 0)
            intersectXMsg = "all x";

        System.out.println("\nIntersection points with the x axis: " + intersectXMsg);
        System.out.println("Intersection point with the y axis: " + reprPoint(0));
    }

    // a method to get and print the positive and negative intervals of a function
    public void funcIntervals(Function f, double[] roots, String positiveMsg, String negativeMsg) {
        int[] signs = f.calcIntervals(roots);
        String[][] intervals = reprIntervals(roots, signs);

        // get the intervals representation
        String positive = String.join(", ", intervals[0]);
        String negative = String.join(", ", intervals[1]);

        // if f(x) = 0 then it is not positive nor negative for all x
        if (f.getDegree() == 0 && f.getCoefficient(0) == 0) {
            positive = "no x"; negative = "no x";
        }

        // if there are no positive/negative intervals
        if (positive.equals("")) positive = "none";
        if (negative.equals("")) negative = "none";

        // assemble the message print it
        positive = positiveMsg + " intervals: " + positive;
        negative = negativeMsg + " intervals: " + negative;
        System.out.println("\n" + positive + "\n" + negative);
    }
    
    // a method to get the extrema points of a function, minimum or maximum
    public double[][] extremaPoints(Function f) {
        double[] roots = f.modifyArray(f.findRoots());
        double[] extremaPoints = new double[roots.length];

        double[] minPoints = new double[roots.length];
        double[] maxPoints = new double[roots.length];
        int numOfMin = 0, numOfMax = 0;

        // there are potential extrema points (a constant function does not have extrema points)
        if (func.getDegree() != 0 && roots.length != 0) {
            
            // add only the extrema points to the array, as defined before the function run()
            int[] signs = f.calcIntervals(roots);
            for (int i = 0; i < signs.length - 1; i++) {
                if (signs[i] == signs[i + 1]) continue;
                extremaPoints[numOfMin + numOfMax] = roots[i];

                // check the extrema point's type (min/max)
                if (signs[i] < signs[i + 1]) {
                    minPoints[numOfMin] = roots[i]; numOfMin++;
                } else {
                    maxPoints[numOfMax] = roots[i]; numOfMax++;
                }
            }
        }

        // slice the arrays in the correct length and return
        extremaPoints = Arrays.copyOf(extremaPoints, numOfMin + numOfMax);
        minPoints = Arrays.copyOf(minPoints, numOfMin);
        maxPoints = Arrays.copyOf(maxPoints, numOfMax);
        return (new double[][] {extremaPoints, minPoints, maxPoints});
    }

    // a method to print the extrema points of a function
    public void printExtremaPoints(double[] extremaPoints, String pointMsg) {
        String printMsg = "";
        if (extremaPoints.length == 0) {
            printMsg = "none";
        } else {
            String[] message = new String[extremaPoints.length];
            for (int i = 0; i < message.length; i++)
                message[i] = reprPoint(extremaPoints[i]);
            printMsg = String.join(", ", message);
        }

        System.out.println(pointMsg + " points: " + printMsg);
    }

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    /* the main method of this class, prints to user the information & analysis of the function.
     * This method uses the following definitions and rules:
     * 1. a minimum point is a point where f(x) goes from increasing to decreasing, which means f'(x) went from
     *    positivity to negativity. for maximum points the case is the opposite.
     * 2. an inflection point is a point where f(x) goes from concavity to convexity (or the opposite),
     *    which means f''(x) went from positivity to negativity (or the opposite).
     * 3. increasing/decreasing intervals of f(x) are the positivity/negativity intervals of f'(x), and concavity/
     *    convexity intervals of f(x) are the positivity/negativity intervals of f''(x).
     * 4. inflection points of f(x) are the extrema points of f'(x).
     */
    public void run() {
        System.out.println("\n----------------------------------------------------------------------------------------------\n");
        System.out.println("The function: f(x) = " + func);
		System.out.println("The domain of the function: all x");

        // print axis intersections and positivity/negativity intervals
        double [] intersectX = func.modifyArray(func.findRoots());
        axisIntersections(intersectX);
        funcIntervals(func, intersectX, "Positivity", "Negativity");

        // first derivative and extrema points
        Function firstDer = func.calcDerivative();
        double[][] extrema = extremaPoints(firstDer);
        System.out.println("\nFirst derivative: f'(x) = " + firstDer);
        
        // print extrema points and increasing/decreasing intervals
        printExtremaPoints(extrema[1], "Minimum");
        printExtremaPoints(extrema[2], "Maximum");
        funcIntervals(firstDer, extrema[0], "Increasing", "Decreasing");

        // second derivative and inflection points
        Function secondDer = firstDer.calcDerivative();
        double[] infPoints = extremaPoints(secondDer)[0];
        System.out.println("\nSecond derivative: f''(x) = " + secondDer);

        // print inflection points and concavity/convexity intervals
        printExtremaPoints(infPoints, "Inflection");
        funcIntervals(secondDer, infPoints, "Concavity", "Convexity");

        System.out.println("\n----------------------------------------------------------------------------------------------\n");
    }

}
