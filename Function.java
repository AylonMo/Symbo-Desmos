public class Function {
    private double[] coefficients;

    public Function(double[] coefficients) {
        this.coefficients = coefficients;
    }


    public String toString() {
        String repr = "f(x) = ";
        for (int i = coefficients.length - 1; i >= 0; i--)
            repr += coefficients[i] + " * x^" + i + " + ";
        return repr.substring(0, repr.length() - 3);
	}

    public double calcValue(double x) {
        double result = 0;
		for (int i = 0; i < coefficients.length; i++)
			result += coefficients[i] * Math.pow(x, i);
		return result;
	}

    public void printPoint(double x) {
		System.out.println("(" + x + ", " + calcValue(x) + ")");
	}

    public Function calcDerivative() {
        double[] derCoeffs = new double[coefficients.length - 1];
		for (int i = 0; i < derCoeffs.length; i++)
			derCoeffs[i] = (i + 1) * coefficients[i + 1];
		return new Function(derCoeffs);
	}

}
