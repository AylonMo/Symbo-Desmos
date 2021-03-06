package src;

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;


public class Function {
    private static final double ROUNDING_DIST = 0.0001;
    private static final double ROUNDING_CONST = 1000.0;
    private static final int APPROX_DEPTH = 10000;

    private static Scanner scan = new Scanner(System.in);
    private double[] coefficients;

    // the first constructor of the class, gets the degree and coefficients from thge user
    public Function() {
        int degree = getInputDegree();
        double[] coeffs = new double[degree + 1];
        for (int i = degree; i >= 0; i--)
            coeffs[i] = getInputCoefficient(degree, i);
        this.coefficients = coeffs;
    }

    // the second constructor of the class, sets the coefficients array
    public Function(double[] coeffs) {
        this.coefficients = coeffs;
    }

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    // a method to get a valid integer for the degree of the polynomial
    public int getInputDegree() {
		boolean invalidInput = true;
        int degree = 0;
        System.out.print("Please enter the degree of the polynomial function to analyze: ");

        while (invalidInput) {
            String input = scan.nextLine();
            try {
                degree = Integer.parseInt(input);
                if (degree < 0) // the degree must be non-negative
                    System.out.print("This is not a valid value. Please enter a non-negative integer for the degree: ");
                else
                    invalidInput = false;
            } catch (NumberFormatException e) { // if the input is not an integer
                System.out.print("This is not a valid value. Please enter a non-negative integer for the degree: ");
            }
        }
        
        return degree;
    }

    // a method to get a valid number for a coefficient of the function
	public double getInputCoefficient(int degree, int coeffIndex) {
		double coefficient = 0;
		boolean invalidInput = true;

        // print the correct message for the current input
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
                if (coeffIndex == degree && coefficient == 0 && degree != 0) // the leading coefficient can't be 0
                    System.out.print("This is not a valid value. Please enter a non-zero value for this coefficient: ");
                else
                    invalidInput = false;
            } catch (NumberFormatException e) { // if the input is not a real number (double)
                System.out.print("This is not a valid value. Please enter a number to represent the coefficient: ");
            }
		}
        
        return coefficient;
	}

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    // a method to get the degree of the polynomial function
    public int getDegree() {
        return coefficients.length - 1;
    }
    
    // a method to get the coefficient of x??? in the function
    public double getCoefficient(int i) {
        return (i > getDegree())? (0):(coefficients[i]);
    }
    
    // representation of the function, printed user-friendly
    public String toString() {
        int n = getDegree();

        // if the function is constant, f(x) = coefficients[0]
        if (n == 0) {
            if (coefficients[0] == Math.round(coefficients[0])) 
                return "" + Math.round(coefficients[0]);
            return "" + smartRound(coefficients[0]);
        }
        
        double coeff;
        String repr = "", sign, xExpression, coeffRepr;
        
        for (int i = n; i >= 0; i--) {
            // if the coefficient is 0, ignore the current expression
            if (coefficients[i] == 0) continue; // example: we want to print [-3, 0, 2] as "-3x?? + 2", not as "-3x?? + 0x + 2"
            
            // get the correct form of sign of the coefficient
            if (i == n) {                                       // examples: (1) we want to print [-2, 2] as "-2x + 2", not as " - 2x + 2"
                sign = (coefficients[i] > 0)? (""):("-");       //           (2) we want to print [2, 2] as "2x + 2", not as " + 2x + 2"
            } else {
                sign = (coefficients[i] > 0)? (" + "):(" - ");  // the other signs will be printed with spaces
            }

            // get the correct form of the coefficient
            coeff = smartRound(Math.abs(coefficients[i]));  // get the positive and smart-rounded value (sign is already set)
            if (coeff == 1 && i != 0) {                     // if the coefficient is 1, ignore it
                coeffRepr = "";                                 // example: we want to print [1, 2] as "x + 2", not as "1x + 2"
            } else if (coeff == Math.round(coeff)) {        // if the coefficient is not 1 but is an integer, get the integer value
                coeffRepr = "" + Math.round(coeff);             // example: we want to print [2, 2] as "2x + 2", not as "2.0x + 2.0"
            } else {                                        // otherwise, add the coefficient as is
                coeffRepr = "" + coeff;                         // example: we want to print [2, 1.385] as "2x + 1.385"
            }
            
            // get the correct form of the x expression
            if (i == 0) {
                xExpression = "";       // if the degree of x is 0, ignore it (x??? = 1)
            } else if (i == 1) {
                xExpression = "x";      // if the degree of x is 1, add only "x" (x?? = x)
            } else {
                xExpression = "x^" + i; // otherwise, add "x^" + the degree
            }
            
            // finally, add all the representations to repr
            repr += (sign + coeffRepr + xExpression);
        }
        
        return repr;
	}

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    // a method to calculate f(x) for a given x
    public double calcValue(double x) {
        double result = 0;
		for (int i = 0; i < coefficients.length; i++)
			result += coefficients[i] * Math.pow(x, i);
		return result;
	}

    /* a method to get the derivative of the function
     * for example: if f(x) = 6x?? - 41x?? + 59x - 20 then the derivative is f'(x) = 18x?? - 82x + 59
     */
    public Function calcDerivative() {
        if (this.getDegree() == 0)
            return new Function(new double[] {0});
        
        // create the coefficients array of the derivative
        double[] derCoeffs = new double[coefficients.length - 1];
		for (int i = 0; i < derCoeffs.length; i++)
			derCoeffs[i] = smartRound((i + 1) * coefficients[i + 1]);
		return new Function(derCoeffs);
	}

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    // a method that returns a rounded value of the parameter without changing it
    public static double smartRound(double value) {
        if (Math.abs(Math.round(value) - value) < ROUNDING_DIST)
            return Math.round(value);
        return Math.round(value * ROUNDING_CONST) / ROUNDING_CONST;
    }

    // a method to get a sorted copy of an array without repetition of elements
    public static double[] modifyArray(double[] array) {
        if (array.length == 0 || array.length == 1)
            return array;
        
        // create a sorted copy of array
        double[] sorted = Arrays.copyOf(array, array.length);
        Arrays.sort(sorted);
        
        double[] result = new double[sorted.length];
        int newLength = 0;

        // go over the sorted array and put every element in result once
        for (int i = 0; i < sorted.length - 1; i++) {
            if (smartRound(sorted[i]) != smartRound(sorted[i + 1]))
                result[newLength++] = sorted[i];
        }
        
        // add the last element, slice the array and return
        result[newLength++] = sorted[sorted.length - 1];
        return Arrays.copyOf(result, newLength);
    }

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    /* a method to approximate a root of the function using the Newton-Raphson technique.
     * for a given function f(x) we can approximate a root of the function using the following recurrence relation:
     * an+1 = an - f(an) / f'(an) where f'(x) is the derivative of f(x) and the initial term a0 is randomly chosen.
     */
    public double newtonRaphson() {
        Random rand = new Random();
        double a0 = (rand.nextDouble() + 1) * 10;
        Function derivative = calcDerivative();

        // make sure the denominator is not 0 so we don't divide by 0
        while (derivative.calcValue(a0) == 0)
            a0 = (rand.nextDouble() + 1) * 10;
        
        double current = a0, next = a0;
        for (int i = 0; i < APPROX_DEPTH; i++) {
            next = current - this.calcValue(current) / derivative.calcValue(current);
            current = next;
        } return next;
    }


    /* a method to compute the division of f(x) by the factor (x - alpha), ignoring the remainder.
     * the division works for any parameter, but it is used only when alpha is a root of the function.
     */
    public double[] polynomialDivision(double alpha) {
        return polDivRec(this.coefficients, -alpha, new double[getDegree()]);
    }
    
    /* a recursive method to execute the polynomial division.
     * for example: f(x) = 6x?? - 41x?? + 59x - 20, divided by (x - 5).
     * notice that f(x) = (6x?? - 11x + 4)(x - 5) so we expect the result to be (6x?? - 11x + 4).
     * the technique is exactly like long division but for polynomials:
     * 
     *         6x?? - 11x + 4
     *  ??????????????????????????????????????????????????????????????????
     *  6x?? - 41x?? + 59x - 20 | x - 5
     *  6x?? - 30x??    |    | 
     *  ??????????????????????????????    V    | 
     *      - 11x?? + 59x   | 
     *      - 11x?? + 55x   | 
     *      ????????????????????????????????????   V 
     *                4x - 20
     *                4x - 20
     *                ?????????????????????
     *                      0
     * the remainder is zero because 5 is a root of f(x), and that will be the case when we call this function.
     */
    public static double[] polDivRec(double[] current, double alpha, double[] resCoeffs) {
        int pos = current.length - 1;   // == getDegree()
        if (pos == 0)                   // if the degree is 0 then we finished the division
            return resCoeffs;
        
        double[] divRes = Arrays.copyOf(current, pos);              // divRes is the result of the current division
        divRes[pos - 1] = current[pos - 1] - current[pos] * alpha;  // subtract (x - a) * the leading coefficient

        resCoeffs[pos - 1] = current[pos];          // update the coefficient of the final result
        return polDivRec(divRes, alpha, resCoeffs); // continue the division with the current remainder
    }

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________

    // helper method for finding the other roots of a function when one root is given
    public double[] getRootsWhenExists(double root) {
        // divide the polynomial by (x - root) and find the roots of the result
        Function result = new Function(polynomialDivision(root));
        double[] rootsArray = result.findRoots();

        // add the given root to the roots array and return it
        rootsArray = Arrays.copyOf(rootsArray, rootsArray.length + 1);
        rootsArray[rootsArray.length - 1] = root;
        return rootsArray;
    }

    /* a method to find the roots of a function with an odd degree.
     * by a theorem from calculus, every polynomial with real coefficients and an odd degree has a real root
     * therefore, we can compute this root using the Newton-Raphson technique.
     */
    public double[] findRootsOdd() {
        if (getDegree() == 1)
            return (new double[] {-coefficients[0] / coefficients[1]});
        return getRootsWhenExists(newtonRaphson());
    }

    /* a method to find the roots of a function with an even degree.
     * determined by the graphs of polynomial functions with an even degree, there are no roots of the function
     * (intersection points with the x axis) in the following cases:
     * 1. all the extrema points (minimum/maximum) are above the axis and the leading coefficient is positive.
     * 2. all the extrema points (minimum/maximum) are below the axis and the leading coefficient is negative.
     * otherwise, using the intermediate-value-theorem from calculus we can deduct that there is at least one real
     * root of the function, because there are positive and negative values of the function.
     */
    public double[] findRootsEven() {
        // the extrema points of the function are the roots of the derivative
        double[] extremaPoints = this.calcDerivative().findRoots();
        double root = 0; boolean existsRoot = false;
        
        boolean allPositive = true; boolean allNegative = true;
        for (double x : extremaPoints) {
            if (calcValue(x) > 0) {             // the extrema point is above the x axis
                allNegative = false;
            } else if (calcValue(x) < 0) {      // the extrema point is below the x axis
                allPositive = false;
            } else {                            // the extrema point is a root of the function
                root = x; existsRoot = true;
                break;
            }
        }

        // if a root was found amongst the extrema points
        if (existsRoot)
            return getRootsWhenExists(root);
        
        // check if there is a root to function according to the explanation above
        boolean option1 = (coefficients[getDegree()] < 0 && allPositive);
        boolean option2 = (coefficients[getDegree()] > 0 && allNegative);
        existsRoot = option1 || option2 || (!allPositive && !allNegative);

        // if there is a root, find it using Newton-Raphson and the other roots
        if (existsRoot)
            return getRootsWhenExists(newtonRaphson());
        return new double[0];
    }

    // a method to find the roots of the function, using the relevant methods from above
    public double[] findRoots() {
        if (getDegree() == 0)
            return new double[0];
        
        if (getDegree() % 2 == 0)
            return findRootsEven();
        else
            return findRootsOdd();
    }

    // ______________________________________________________________________________________________________________
    // ______________________________________________________________________________________________________________
    
    /* a method to get the positive/negative intervals of f(x), where points contains the zero-points of f(x)
     * for example: f(x) = 6x?? - 41x?? + 59x - 20, so points = [0.5, 1.33, 5.0].
     * we substitute a random value between every two points and check if f(x) is positive or negative:
     * 
     *      0.5   1.33   5.0
     *  _____|_____|_____|_____
     *    -  |  +  |  -  |  +  
     * 
     * we will represent the sign of each interval using -1 or 1, so for this example we get [-1, 1, -1, 1]
     */
    public int[] calcIntervals(double[] points) {
        if (points.length == 0)
            return (new int[] {(getCoefficient(getDegree()) > 0)? (1):(-1)});
        
        points = modifyArray(points);
        int[] signs = new int[points.length + 1];
        double substitue;

        for (int i = 0; i <= points.length; i++) {
            // check the interval type and set the substitution accordingly
            if (i == 0) substitue = points[i] - 1;
            else if (i == points.length) substitue = points[i - 1] + 1;
            else substitue = (points[i - 1] + points[i]) / 2.0;

            // check the sign of the substitution and set 1 or -1
            signs[i] = (calcValue(substitue) > 0)? (1):(-1);
        }

        return signs;
    }

}
