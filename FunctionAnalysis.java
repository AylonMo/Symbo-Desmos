import java.util.Scanner;


public class FunctionAnalysis {
    private static Scanner scan = new Scanner(System.in);
	private Function func;

    // the constructor of the class, asks the degree and coefficients and sets func accordingly
	public FunctionAnalysis() {
        int degree = getDegree();
        double[] coefficients = new double[degree + 1];
        
        for (int i = 0; i <= degree; i++)
            coefficients[i] = getCoefficient(degree, i);
		this.func = new Function(coefficients);
	}

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    // represents the class by the function that is analyzed
    public String toString() {
		return "func is: " + func.toString();
	}

    // a method to print a point in the form (x, f(x))
    public void printPoint(double x) {
        double y = func.smartRounding(func.calcValue(x));
		System.out.println("(" + x + ", " + y + ")");
	}

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    // a method to get a valid integer for the degree of the polynomial
    public int getDegree() {
		boolean invalidInput = true;
        int degree = 0;
        System.out.print("\nPlease enter the degree of the polynomial function to analyze: ");

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
                if (coeffIndex == degree && coefficient == 0)
                    System.out.print("This is not a valid value. Please enter a non-zero number to represent the coefficient: ");
                else
                    invalidInput = false;
            } catch (NumberFormatException e) {
                System.out.print("This is not a valid value. Please enter a number to represent the coefficient: ");
            }
		}
        
        return coefficient;
	}

    // ——————————————————————————————————————————————————————————————————————————————————————————————————————————————

    public void run() {

    }

}
