import java.util.Scanner;

public class FunctionAnalysis {
    private static Scanner scan = new Scanner(System.in);
	private Function func;

	public FunctionAnalysis() {
        double a0 = this.getCoefficient(0);
        double a1 = this.getCoefficient(1);
        double a2 = this.getCoefficient(2);
        double a3 = this.getCoefficient(3);
        double a4 = this.getCoefficient(4);
		this.func = new Function(a0, a1, a2, a3, a4);
	}

    public String toString() {
		return func.toString();
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
