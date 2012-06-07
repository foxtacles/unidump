package uebung3.exercise.adt;

public class BinarySearchTree<T extends Comparable<T>> {

	private T value = null;
	private BinarySearchTree<T> left = null;
	private BinarySearchTree<T> right = null;
	
	public BinarySearchTree() {}
	
	public BinarySearchTree(T value) {
		this.value = value;
	}
	
	public void insert(T value) throws Exception {
		if (value == null)
			throw new NullPointerException();
		
		if (this.value == null) {
			this.value = value;
			return;
		}
		
		int q = value.compareTo(this.value);
		
		if (q == 0)
			throw new Exception("Value already in tree");
		
		// value is smaller than this
		if (q < 0) {
			if (left == null)
				left = new BinarySearchTree<T>(value);
			else 
				left.insert(value);
		}
		// value is bigger than this
		else {
			if (right == null)
				right = new BinarySearchTree<T>(value);
			else 
				right.insert(value);
		}
	}
	
	public boolean contains(T value) {
		if (this.value == null)
			return false;
		
		int q = value.compareTo(this.value);
		
		if (q == 0)
			return true;
		else if (q < 0 && left != null)
			return left.contains(value);
		else if (q > 0 && right != null)
			return right.contains(value);
		
		return false;
	}
	
	public T getMaximum() {
		if (right != null)
			return right.getMaximum();
		return this.value;
	}
	
	public T getMinimum() {
		if (left != null)
			return left.getMinimum();
		return this.value;
	}
}
