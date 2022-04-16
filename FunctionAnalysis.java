import java.util.Scanner;

public class FunctionAnalysis {
    private static Scanner scan = new Scanner(System.in);
	private Function func;

	public FunctionAnalysis() {
        int degree = getDegree();
        double[] coefficients = new double[getDegree()];
        
        for (int i = 0; i < degree; i++)
            coefficients[i] = getCoefficient(i);
		this.func = new Function(coefficients);
	}

    public String toString() {
		return func.toString();
	}


    public int getDegree() {
		boolean invalidInput = true;
        int degree = 0;
        System.out.println("Please enter the degree of the polynomial function to analize: ");

        while (invalidInput) {
            String input = scan.nextLine();

            try {
                degree = Integer.parseInt(input);
                invalidInput = false;
            } catch (NumberFormatException e) {
                System.out.print("This is not a valid value. Please enter an integer for the degree: ");
            }
        }
        
        return degree;
    }


	public double getCoefficient(int coeffIndex) {
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
                invalidInput = false;
            } catch (NumberFormatException e) {
                System.out.print("This is not a valid value. Please enter a number to represent the coefficient: ");
            }
		}
        
        return coefficient;
	}

    public void run() {
        
    }

}
