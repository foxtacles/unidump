package uebung4;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RedBlackTree<T extends Comparable<T>> extends AbstractCollection<T> {

	class RedBlackNode {
		
		private T value = null;
		private RedBlackTree<T> origin = null;
		private RedBlackNode root = null;
		private RedBlackNode left = null;
		private RedBlackNode right = null;
		
		private boolean red;

		public RedBlackNode(T value, RedBlackTree<T> origin) {
			if (value == null)
				throw new NullPointerException();
			
			this.origin = origin;
			this.value = value;
			this.red = true;
		}
		
		private RedBlackNode(T value, RedBlackNode root, boolean left) throws Exception {
			this.value = value;
			this.red = true;
			this.root = root;
			this.origin = root.origin;
			
			if (left)
				root.left = this;
			else
				root.right = this;
			
			this.repair();
		}
		
		private void repair() throws Exception {
			// root is black or this is pure root, no fix required ("Fall 1")
			if (root == null || !root.red)
				return;
			
			// root has no root and is red, fix redness ("Fall 2")
			if (root.root == null && root.red) {
				root.red = false;
				return;
			}
			
			// "Fall 3"
			if (root.root.left != null && root.root.right != null && root.root.left.red && root.root.right.red) {
				root.root.red = true;
				root.root.left.red = false;
				root.root.right.red = false;
				root.root.repair();
				return;
			}
			
			// "Fall 4"

			if (root == root.root.left) {
				// father is left
				
				if (this == root.right) {
					// this is right
					root.lrot();
					root.repair();
				}
				else {
					// this is left
					root.red = false;
					root.root.red = true;
					root.root.rrot();
				}
			}
			else {
				// father is right
				
				if (this == root.left) {
					// this is left
					root.rrot();
					root.repair();
				}
				else {
					// this is right
					
					root.red = false;
					root.root.red = true;
					root.root.lrot();
				}
			}
		}
		
		private void rrot() throws Exception {
			if (left == null)
				throw new Exception("rrot: left child is null");
			
			if (root == null)
				origin.root = left;
			else {
				if (this == root.left)
					root.left = left;
				else
					root.right = left;
			}
			
			left.root = root;
			
			RedBlackNode tmp = left.right;
			left.right = this;
			this.root = left;
			this.left = tmp;
			
			if (tmp != null)
				tmp.root = this;
		}
		
		private void lrot() throws Exception {
			if (right == null)
				throw new Exception("lrot: right child is null");
			
			if (root == null)
				origin.root = right;
			else {
				if (this == root.left)
					root.left = right;
				else
					root.right = right;
			}
			
			right.root = root;
			
			RedBlackNode tmp = right.left;
			right.left = this;
			this.root = right;
			this.right = tmp;
			
			if (tmp != null)
				tmp.root = this;
		}
		
		public void insert(T value) throws Exception {
			if (value == null)
				throw new NullPointerException();
			
			int q = value.compareTo(this.value);
			
			if (q == 0)
				throw new Exception("Value already in tree");
			
			// value is smaller than this
			if (q < 0) {
				if (left == null)
					new RedBlackNode(value, this, true);
				else 
					left.insert(value);
			}
			// value is bigger than this
			else {
				if (right == null)
					new RedBlackNode(value, this, false);
				else 
					right.insert(value);
			}
		}

		public boolean contains(T value) {
			int q = value.compareTo(this.value);
			
			if (q == 0)
				return true;
			else if (q < 0 && left != null)
				return left.contains(value);
			else if (q > 0 && right != null)
				return right.contains(value);
			
			return false;
		}
		
		public RedBlackNode getMaximum() {
			if (right != null)
				return right.getMaximum();
			return this;
		}
		
		public RedBlackNode getMinimum() {
			if (left != null)
				return left.getMinimum();
			return this;
		}
		
		public int blackness() {
			int left = (this.left != null ? this.left.blackness() : 1);
			int right = (this.right != null ? this.right.blackness() : 1);
			
			return (this.red ? 0 : 1) + (left > right ? left : right);
		}
		
		public String toString() {
			return this.value + " " + this.red + " left: (" + this.left + "), right: (" + this.right + ")";
		}
	}
	
	class RedBlackForwardIter implements Iterator<T> {
		RedBlackNode node = null;
		RedBlackNode last = null;
		
		public RedBlackForwardIter(RedBlackTree<T> tree) {
			this.node = tree.root.getMinimum();
		}
		
		public boolean hasNext() {
			return (node != null);
		}

		public T next() {
			if (node == null)
				throw new NoSuchElementException();
			
			last = node;
			T value = node.value;
			
			if (node.right == null) {
				RedBlackNode tmp;
				do {
					tmp = node;
					node = node.root;
				} while (node != null && node.right == tmp);
			}
			else {
				node = node.right;
					
				while (node.left != null)
					node = node.left;
			}
			
			return value;
		}

		public void remove() {

		}
	}
	
	class RedBlackReverseIter implements Iterator<T> {
		RedBlackNode node = null;
		RedBlackNode last = null;
		
		public RedBlackReverseIter(RedBlackTree<T> tree) {
			this.node = tree.root.getMaximum();
		}
		
		public boolean hasNext() {
			return (node != null);
		}

		public T next() {
			if (node == null)
				throw new NoSuchElementException();
			
			last = node;
			T value = node.value;
			
			if (node.left == null) {
				RedBlackNode tmp;
				do {
					tmp = node;
					node = node.root;
				} while (node != null && node.left == tmp);
			}
			else {
				node = node.left;
					
				while (node.right != null)
					node = node.right;
			}
			
			return value;
		}

		public void remove() {

		}
	}
	
	private RedBlackNode root = null;

	public RedBlackTree(T value) {
		root = new RedBlackNode(value, this);
	}
	
	public boolean add(T value) {
		try {
			root.insert(value);
		}
		catch (Exception e) { return false; }
		
		return true;
	}
	
	public Iterator<T> iterator() {
		return new RedBlackForwardIter(this);
	}
	
	public Iterator<T> reverse_iterator() {
		return new RedBlackReverseIter(this);
	}

	public int size() {
		RedBlackForwardIter RBFI = new RedBlackForwardIter(this);
		
		int size = 0;
		
		while (RBFI.hasNext()) {
			++size;
			RBFI.next();
		}
		
		return size;
	}
	
	public int blackness() {
		return root.blackness();
	}
	
	public String toString() {
		return root.toString();
	}
}
