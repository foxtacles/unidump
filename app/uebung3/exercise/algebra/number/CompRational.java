package uebung3.exercise.algebra.number;
import uebung2.algebra.Rational;

public class CompRational extends NormRational implements Comparable<CompRational> {
	public CompRational() {
		super();
	}
	
	public CompRational(long nom, long denom) throws Exception {
		super(nom, denom);
	}
	
	public CompRational(Rational r) throws Exception {
		super(r);
	}
	
	public int compareTo(CompRational c) {
		if (this.equals(c))
			return 0;

		if (this.val() < c.val())
			return -1;
		
		return 1;
	}
}
