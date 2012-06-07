package uebung2.algebra;

public class Algebra {

	public static Matrix gaussJordan(Matrix matrix, Matrix vector) throws Exception {

		// tests for sanity
		
		if (matrix.getRows() != matrix.getColumns())
			throw new Exception("gaussJordan: not a square matrix");
		
		if (matrix.getRows() != vector.getRows())
			throw new Exception("gaussJordan: vector and matrix dimension mismatch");
		
		if (vector.getColumns() != 1)
			throw new Exception("gaussJordan: vector not a vector");
		
		// copys to work with
		
		Matrix result = new Matrix(vector.getRows(), 1);
		Matrix A = (Matrix) matrix.clone();
		Matrix b = (Matrix) vector.clone();

		// main loop
		
		for (int k = 1; k <= vector.getRows(); ++k) {
			
			int l = 0;
			double max = 0.00;
			
			// get max element in current column
			
			for (int j = k; j <= vector.getRows(); ++j) {
				double abs = A.get(j, k).abs();

				if (abs > max) {
					max = abs;
					l = j;
				}
			}
			
			if (l == 0)
				throw new Exception("gaussJordan: matrix is singular");
			
			// swap rows k and l
			
			A.swap(k, l);
			b.swap(k, l);
				
			Matrix quot = new Matrix(vector.getRows(), 1);

			for (int i = 1; i <= vector.getRows(); ++i) {
				
				// vector for the quotients
				
				quot.set(i, 1, Rational.div(A.get(i, k), A.get(k, k)));
				
				for (int j = 1; j <= vector.getRows(); ++j) {
					
					if (j <= (k - 1) || (i == k && j >= k))
						continue;
					
					if (i != k && j == k)
						A.set(i, j, new Rational());
					else 
						A.set(i, j, Rational.sub(A.get(i, j), Rational.mult(A.get(k, j), quot.get(i, 1))));
				}
				
				if (i == k)
					continue;
				
				b.set(i, 1, Rational.sub(b.get(i, 1), Rational.mult(b.get(k, 1), quot.get(i, 1))));
			}
		}
		
		for (int i = 1; i <= vector.getRows(); ++i)
			result.set(i, 1, Rational.div(b.get(i, 1), A.get(i, i)));

		return result;
	}
	
}
