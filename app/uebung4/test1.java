package uebung4;

import java.util.Iterator;

import uebung3.exercise.algebra.number.CompRational;

public class test1 {

	public static void main(String[] args) throws Exception {

		if (args.length % 2 != 0 || args.length < 2)
			throw new Exception("Non-even count of numbers");
		
		RedBlackTree<CompRational> tree = new RedBlackTree<CompRational>(new CompRational(Long.parseLong(args[0]), Long.parseLong(args[1])));
		
		for (int i = 2; i < args.length; i += 2) {
			Long N = Long.parseLong(args[i]);
			Long D = Long.parseLong(args[i + 1]);
			tree.add(new CompRational(N, D));
		}

		System.out.println("Schwarztiefe: " + tree.blackness());
		
		System.out.println("Forward iterator");
		
		Iterator<CompRational> iter = tree.iterator();
		
		while (iter.hasNext())
			System.out.println(iter.next());
		
		iter = tree.reverse_iterator();
		
		System.out.println("Reverse iterator");
		
		while (iter.hasNext())
			System.out.println(iter.next());
		
		System.out.println("Eliminate elements < 1");
		
		iter = tree.iterator();
		
		while (iter.hasNext())
			if (iter.next().val() < 1.00)
				iter.remove();
		
		iter = tree.iterator();
		
		while (iter.hasNext())
			System.out.println(iter.next());
	}

}
