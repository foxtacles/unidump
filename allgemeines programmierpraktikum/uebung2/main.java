package uebung2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import uebung2.algebra.Algebra;
import uebung2.algebra.Matrix;
import uebung2.algebra.Rational;

public class main {

	public static void main(String[] args) throws Exception {
		
		if (args.length == 0)
			throw new Exception("Missing file argument");
		  
		FileInputStream fstream = new FileInputStream(args[0]);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String line;
		
		Matrix matrix = null, vector = null;
		int q = 0;
		
		while ((line = br.readLine()) != null && (vector != null ? q != vector.getRows() : true)) {
			
			String[] tokens = line.split("\\s+");
			
			if (tokens.length == 0 || tokens[0].length() == 0)
				continue;

			if (vector == null) {
				vector = new Matrix(tokens.length, 1);
				matrix = new Matrix(tokens.length, tokens.length);
				
				for (int i = 0; i < tokens.length; ++i) {
					vector.set(i + 1, 1, new Rational(Long.parseLong(tokens[i]), 1));
				}
			}
			else {
				if (tokens.length != vector.getRows()) {
					in.close();
					throw new Exception("read file: wrong number of arguments");
				}

				++q;
				
				for (int i = 0; i < tokens.length; ++i) {
					matrix.set(q, i + 1, new Rational(Long.parseLong(tokens[i]), 1));
				}
			}
		}

		in.close();
		
		if (vector == null || q != vector.getRows())
			throw new Exception("read file: invalid file format");
		
		Matrix result = Algebra.gaussJordan(matrix, vector);
		
		System.out.println(result.toString());
	}
}
