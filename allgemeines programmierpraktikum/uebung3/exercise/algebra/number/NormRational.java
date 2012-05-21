package uebung3.exercise.algebra.number;

import uebung2.algebra.*;

public class NormRational extends Rational {

	public NormRational() {
		super();
		normalize();
	}
	
	public NormRational(long nom, long denom) throws Exception {
		super(nom, denom);
		normalize();
	}
	
	public NormRational(Rational r) throws Exception {
		super(r);
		normalize();
	}
	
	private void normalize() {
		reduce();
		
		// fix if denominator is negative
		if (getDenom() < 0) {
			try {
				super.setDenom(getDenom() * -1);
			} catch (Exception e) {} // never happens
			super.setNom(getNom() * -1);
		}
	}
	
	public static NormRational add(Rational r1, Rational r2) throws Exception {
		Rational r3 = new Rational(r1);
		r3.add(r2);
		return new NormRational(r3);
	}
	
	public static NormRational sub(Rational r1, Rational r2) throws Exception {
		return new NormRational(Rational.sub(r1, r2));
	}
	
	public static NormRational mult(Rational r1, Rational r2) throws Exception {
		return new NormRational(Rational.mult(r1, r2));
	}
	
	public static NormRational div(Rational r1, Rational r2) throws Exception {
		return new NormRational(Rational.div(r1, r2));
	}
	
	public void add(Rational r) {
		super.add(r);
		normalize();
	}
	
	public void setNom(long nom) {
		super.setNom(nom);
		normalize();
	}
	
	public void setDenom(long denom) throws Exception {
		super.setDenom(denom);
		normalize();
	}
}
