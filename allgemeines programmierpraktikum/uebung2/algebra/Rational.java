package uebung2.algebra;

public class Rational {

	private long nom = 0;
	private long denom = 1;

	public Rational() {}
	
	/*
	 * constructor
	 * throws exception if denominator = 0
	 */
	
	public Rational(long nom, long denom) throws Exception
	{
		if (denom == 0)
			throw new Exception("Rational: denominator must not be null");
		
		this.nom = nom;
		this.denom = denom;
		
		this.reduce();
	}
	
	/*
	 * copy constructor
	 */
	
	public Rational(Rational r) throws Exception
	{
		this(r.getNom(), r.getDenom());
	}
	
	/*
	 * tests whether two Rational objects are equal
	 */

	public boolean equals(Rational r) {
		if (r.getNom() == 0)
		{
			if (this.nom == 0)
				return true;
			return false;
		}
				
		return (this.nom / r.getNom()) == (this.denom / r.getDenom());
	}
	
	public String toString() {
		return this.nom + "/" + this.denom;
	}
	
	/*
	 * subtract two Rational
	 */
	
	public static Rational sub(Rational r1, Rational r2) throws Exception {
		long denom = r1.getDenom() * r2.getDenom();
		long nom = (r1.getNom() * r2.getDenom()) - (r1.getDenom() * r2.getNom());
		return new Rational(nom, denom);
	}
	
	/*
	 * multiplicate two Rational
	 */
	
	public static Rational mult(Rational r1, Rational r2) throws Exception {
		return new Rational(r1.getNom() * r2.getNom(), r1.getDenom() * r2.getDenom());
	}
	
	/*
	 * divide two Rational
	 */
	
	public static Rational div(Rational r1, Rational r2) throws Exception {
		return Rational.mult(r1, new Rational(r2.getDenom(), r2.getNom()));
	}
	
	/*
	 * add up two Rational
	 */
	
	public void add(Rational r) {
		if (r.getNom() == 0)
			return;
		
		this.nom = (this.denom * r.getNom()) + (this.nom * r.getDenom());
		this.denom = (this.denom * r.getDenom());
	}
	
	/*
	 * return value
	 */
	
	public double val() {
		return ((double) this.nom / this.denom);
	}
	
	/*
	 * return absolute value
	 */
	
	public double abs() {
		return Math.abs((double) this.nom / this.denom);
	}
	
	/*
	 * get/set
	 */
	
	public void setNom(long nom) { 
		this.nom = nom; 
	}
	
	public long getNom() {
		return this.nom;
	}
	
	public void setDenom(long denom) throws Exception { 
		if (denom == 0) 
			throw new Exception("Rational: denominator must not be null"); 
		
		this.denom = denom;
	}
	
	public long getDenom() {
		return this.denom;
	}
	
	public void reduce() {
		long gcf = gcf(this.nom, this.denom);
		this.nom /= gcf;
		this.denom /= gcf;
	}
	
    private static long gcf(long n, long d)
    {
        if (n % d == 0)
            return d;
        else if (d % n == 0)
            return n;
        else
            return gcf(n % d, d % n);
    }
}
