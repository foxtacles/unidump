package uebung2.algebra;

public class Matrix implements Cloneable {

	/*
	 * matrix array
	 */
	
	private Rational[][] matrix = null;
	private int n = 0, k = 0;
	
	/*
	 * construct new matrix, dimensions n * k
	 */
	
	public Matrix(int n, int k) throws Exception {
		if (n < 1 || k < 1)
			throw new Exception("Matrix: dimensions must be greater than zero");
		
		this.matrix = new Rational[n][k];
		this.n = n;
		this.k = k;
		
		for (int _n = 0; _n < n; ++_n)
			for (int _k = 0; _k < k; ++_k)
				this.matrix[_n][_k] = new Rational();
	}
	
	public int getRows() {
		return this.n;
	}
	
	public int getColumns() {
		return this.k;
	}
	
	/*
	 * sets a field of the matrix to a given Rational
	 */
	
	public void set(int n, int k, Rational r) throws Exception {
		if (n > this.n || k > this.k || n < 1 || k < 1)
			throw new Exception("Matrix: out of bounds");
		
		this.matrix[n - 1][k - 1].setNom(r.getNom());
		this.matrix[n - 1][k - 1].setDenom(r.getDenom());
	}
	
	/*
	 * gets a const (final) reference to the Rational at index n, k
	 */
	
	public final Rational get(int n, int k) throws Exception {
		if (n > this.n || k > this.k || n < 1 || k < 1)
			throw new Exception("Matrix: out of bounds");
		
		return this.matrix[n - 1][k - 1];
	}
	
	/*
	 * swaps two rows
	 */
	
	public void swap(int n, int j) throws Exception {
		if (n > this.n || j > this.n || n < 1 || j < 1)
			throw new Exception("Matrix: out of bounds");
		
		Rational tmp[] = this.matrix[n - 1];
		this.matrix[n - 1] = this.matrix[j - 1];
		this.matrix[j - 1] = tmp;
	}
	
	/*
	 * multiplicate two matrices
	 */
	
	public static Matrix mult(Matrix m1, Matrix m2) throws Exception {
		if (m1.getColumns() != m2.getRows())
			throw new Exception("Matrix: mult bad dimensions");
		
		Matrix m = new Matrix(m1.getRows(), m2.getColumns());
		
		for (int _n = 0; _n < m.getRows(); ++_n) {
			for (int _k = 0; _k < m.getColumns(); ++_k) {
				Rational r = new Rational();
				
				for (int p = 0; p < m1.getColumns(); ++p)
					r.add(Rational.mult(m1.matrix[_n][p], m2.matrix[p][_k]));

				m.set(_n + 1, _k + 1, r);
			}
		}
			
		return m;
	}
	
	public void mult(Matrix m) throws Exception {
		Matrix q = Matrix.mult(this, m);
		this.matrix = q.matrix;
		this.n = q.getRows();
		this.k = q.getColumns();
	}
	
	/*
	 * tests whether two Matrix objects are equal
	 */
	
	public boolean equals(Matrix m) throws Exception {
		if (m.getRows() != this.n || m.getColumns() != this.k)
			return false;
		
		for (int _n = 0; _n < this.n; ++_n)
			for (int _k = 0; _k < this.k; ++_k)
				if (!this.matrix[_n][_k].equals(m.matrix[_n][_k]))
					return false;
		
		return true;
	}
	
	/*
	 * @see java.lang.Object#clone()
	 */
	
	public Object clone() {
		try {
			Matrix m = (Matrix) super.clone();

			m.matrix = new Rational[this.n][this.k];
			
			for (int _n = 0; _n < this.n; ++_n)
				for (int _k = 0; _k < this.k; ++_k)
					m.matrix[_n][_k] = new Rational(this.matrix[_n][_k]);

			return m;
		} catch (Exception e) { System.out.println(e); }
		return this;
	}
	
	public String toString() {
		String m = "Matrix of dimension " + this.n + "/" + this.k + ":";
		
		for (int _n = 0; _n < this.n; ++_n) {
			m += "\n";
			for (int _k = 0; _k < this.k; ++_k)
				m += this.matrix[_n][_k].toString() + " ";
		}
		
		return m;
	}
}
