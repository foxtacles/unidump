package uebung1;

public class main {

	public static void main(String[] args) {
	
		System.out.println("Testing class Complex...");
		
		Complex c1, c2, c3;
		
		c1 = new Complex(1.0, -2.0);
		c2 = new Complex(2.0, 3.0);
		c3 = new Complex(c2);
		
		System.out.println("Using add on Complex (" + c2.toString() + ") with Complex.ONE:");
		c2.add(Complex.ONE);
		System.out.println(c2.toString());
		
		System.out.println("Using add on Complex (" + c2.toString() + ") with Complex.ZERO:");
		c2.add(Complex.ZERO);
		System.out.println(c2.toString());
		
		System.out.println("Using add on Complex (" + c2.toString() + ") with Complex.I:");
		c2.add(Complex.I);
		System.out.println(c2.toString());
		
		System.out.println("Using abs on Complex (" + c2.toString() + "):");
		System.out.println(c2.abs());
		
		System.out.println("Using mult on Complex (" + c1.toString() + ") with Complex (" + c2.toString() + ")");
		c1.mult(c2);
		System.out.println(c1.toString());
		
		System.out.println("Using mult on Complex (" + c2.toString() + ") with Complex (" + c1.toString() + ")");
		c2.mult(c1);
		System.out.println(c2.toString());
		
		System.out.println("Using mult on Complex (" + c2.toString() + ") with Complex (" + c2.toString() + ")");
		c2.mult(c2);
		System.out.println(c2.toString());
		
		System.out.println("Using abs on Complex (" + c2.toString() + "):");
		System.out.println(c2.abs());
		
		System.out.println("Using equals on Complex (" + c2.toString() + ") with Complex (" + c1.toString() + ")");
		System.out.println(c2.equals(c1));
		
		System.out.println("Using equals on Complex (" + c1.toString() + ") with Complex (" + c2.toString() + ")");
		System.out.println(c1.equals(c2));
		
		System.out.println("Using clone on Complex (" + c1.toString() + ")");
		c3 = (Complex) c1.clone();
		System.out.println("Cloned Complex (" + c3.toString() + ")");
		
		System.out.println("Using equals on Complex (" + c1.toString() + ") with Complex (" + c3.toString() + ")");
		System.out.println(c1.equals(c3));
		
		System.out.println("Using clone on Complex.I");
		System.out.println("Cloned Complex (" + Complex.I.clone().toString() + ")");
		
		System.out.println("Using clone on Complex.ONE");
		System.out.println("Cloned Complex (" + Complex.ONE.clone().toString() + ")");
		
		System.out.println("Using abs on Complex (" + c1.toString() + ")");
		System.out.println(c1.abs());
		
	}

}
