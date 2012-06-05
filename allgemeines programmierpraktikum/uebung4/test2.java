package uebung4;

import java.util.Iterator;

public class test2 {

	public static void main(String[] args) throws Exception {

		if (args.length < 1)
			return;
		
		RedBlackTree<Integer> tree = new RedBlackTree<Integer>(new Integer(Integer.parseInt(args[0])));
		
		for (int i = 1; i < args.length; ++i) {
			Integer I = Integer.parseInt(args[i]);
			tree.add(I);
		}

		System.out.println("Schwarztiefe: " + tree.blackness());
		
		System.out.println("Forward iterator");
		
		Iterator<Integer> iter = tree.iterator();
		
		while (iter.hasNext())
			System.out.println(iter.next());
		
		iter = tree.reverse_iterator();
		
		System.out.println("Reverse iterator");
		
		while (iter.hasNext())
			System.out.println(iter.next());
		
		System.out.println("Eliminate elements < 0");
		
		iter = tree.iterator();
		
		while (iter.hasNext())
			if (iter.next() < 0)
				iter.remove();
		
		iter = tree.iterator();
		
		while (iter.hasNext())
			System.out.println(iter.next());
	}

}
