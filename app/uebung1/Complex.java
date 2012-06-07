package uebung1;

public class Complex {

	/*
	 * standard constant complex numbers
	 */
	
	static final public Complex ZERO = new Complex(0, 0);
	static final public Complex ONE = new Complex(1, 0);
	static final public Complex I = new Complex(0, 1);
	
	/*
	 * real / imag parts
	 */
	
	private double real = 0;
	private double imag = 0;
	
	/*
	 * default constructor
	 */
	
	public Complex() {}
	
	/*
	 * constructor with real / imag parts
	 */
	
	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	/*
	 * copy-constructor
	 */
	
	public Complex(Complex complex) {
		this(complex.getReal(), complex.getImag());
	}
	
	/*
	 * returns a boolean indicating whether two passed complex numbers are equal
	 */
	
	public static boolean compare(Complex c1, Complex c2) {
		return (c1 != null && c2 != null && c1.getReal() == c2.getReal() && c1.getImag() == c2.getImag());
	}
	
	/*
	 * @see java.lang.Object#clone()
	 */
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) { }
		return this;
	}
	
	/*
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return "Real: " + real + ", Imag: " + imag;
	}
	
	/*
	 * uses static compare method
	 */
	
	public boolean equals(Complex complex) {
		return Complex.compare(this, complex);
	}

	/*
	 * adds two complex numbers, returns a new Complex
	 */
	
	public static Complex add(Complex c1, Complex c2) {
		return new Complex(c1.getReal() + c2.getReal(), c1.getImag() + c2.getImag());
	}
	
	/*
	 * add the value of a complex number to this
	 */
	
	public void add(Complex complex) {
		this.real += complex.getReal();
		this.imag += complex.getImag();
	}
	
	/*
	 * multiply two complex numbers, returns a new Complex
	 */
	
	public static Complex mult(Complex c1, Complex c2) {
		return new Complex((c1.getReal() * c2.getReal()) - (c1.getImag() * c2.getImag()), 
							(c1.getReal() * c2.getImag()) + (c2.getReal() * c1.getImag()));
	}
	
	/*
	 * multiply the value of a complex number to this
	 */
	
	public void mult(Complex complex) {
		double real = (this.real * complex.getReal()) - (this.imag * complex.getImag());
		double imag = (this.real * complex.getImag()) + (this.imag * complex.getReal());
		this.real = real;
		this.imag = imag;
	}
	
	/*
	 * return the absolute value of a complex number
	 */
	
	public static double abs(Complex complex) {
		return Math.sqrt((complex.getReal() * complex.getReal()) + (complex.getImag() * complex.getImag()));
	}
	
	/*
	 * return the absolute value of this complex number
	 */
	
	public double abs() {
		return Math.sqrt((this.real * this.real) + (this.imag * this.imag));
	}
	
	/*
	 * get and setters
	 */
	
	public void setReal(double real) {
		this.real = real;
	}
	
	public void setImag(double imag) {
		this.imag = imag;
	}
	
	public double getReal() {
		return real;
	}
	
	public double getImag() {
		return imag;
	}
}
