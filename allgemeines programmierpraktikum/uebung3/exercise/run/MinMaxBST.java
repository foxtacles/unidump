package uebung3.exercise.run;

import uebung3.exercise.adt.BinarySearchTree;
import uebung3.exercise.algebra.number.CompRational;

public class MinMaxBST {

	public static void main(String[] args) throws Exception {
		
		if (args.length < 1)
			return;
		
		if (args.length % 2 != 0)
			throw new Exception("Non-even count of numbers");
		
		BinarySearchTree<CompRational> tree = new BinarySearchTree<CompRational>();
		
		for (int i = 0; i < args.length; i += 2) {
			Long N = Long.parseLong(args[i]);
			Long D = Long.parseLong(args[i + 1]);
			tree.insert(new CompRational(N, D));
		}
		
		System.out.println("Maximum: " + tree.getMaximum());
		System.out.println("Minimum: " + tree.getMinimum());
	}
}
