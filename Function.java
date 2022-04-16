public class Function {
    private double a0;
	private double a1;
	private double a2;
	private double a3;
	private double a4;
	private int degree;


    public Function(double a0, double a1, double a2, double a3, double a4) {
        this.a0 = a0;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.a4 = a4;

        this.degree = 0;
		if (a1 != 0) this.degree = 1;
		if (a2 != 0) this.degree = 2;
		if (a3 != 0) this.degree = 3;
		if (a4 != 0) this.degree = 4;
    }


    public String toString() {
		return "f(x) = " + a4 + ("*x^4 + ") + a3 + ("*x^3 + ") + a2 + ("*x^2 + ") + a1 + ("*x + ") + a0;
	}

    public double calcValue(double x){
		return a4 * Math.pow(x, 4) + a3 * Math.pow(x, 3) + a2 * Math.pow(x, 2) + a1 * x + a0;
	}

    public void printPoint(double x) {
		System.out.println("(" + x + ", " + calcValue(x) + ")");
	}

    public Function calcDerivative() {
		return new Function(a1, 2 * a2, 3 * a3, 4 * a4, 0);
	}
}
