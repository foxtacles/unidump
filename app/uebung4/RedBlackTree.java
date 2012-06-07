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
			
			this.repair_insert();
		}
		
		private void repair_insert() throws Exception {
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
				root.root.repair_insert();
				return;
			}
			
			// "Fall 4"

			if (root == root.root.left) {
				// father is left
				
				if (this == root.right) {
					// this is right
					root.lrot();
					root.repair_insert();
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
					root.repair_insert();
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
		
		public void remove() throws Exception {
			
			// "Fall 1"
			if (left == null && right == null) {
				if (root == null)
					origin.root = null;
				else {
					if (this == root.left)
						root.left = null;
					else
						root.right = null;
				}
				
				//if (!this.red)
			}
			// "Fall 2"
			else if (left == null || right == null) {
				RedBlackNode tmp = (left != null ? left : right);
				tmp.root = this.root;
				
				if (root == null)
					origin.root = tmp;
				else {
					if (this == root.left)
						root.left = tmp;
					else
						root.right = tmp;
				}
				
				if (!this.red)
					tmp.repair_remove();
			}
			// "Fall 3"
			else {
				RedBlackNode tmp = right;
				
				while (tmp.left != null)
					tmp = tmp.left;
				
				this.value = tmp.value;
				tmp.remove();
			}
		}
		
		private void repair_remove() throws Exception {
			
			// reached origin
			if (this == origin.root)
				return;

			if (this.red) {
				this.red = false;
				return;
			}
			
			// "Fall 2"
			if (this == root.right) {
				if (root.left != null) {
					
					// 2.1
					if (root.left.red) {
						root.red = true;
						root.left.red = false;
						root.rrot();
					}
					
					// 2.2
					if (!root.left.left.red && !root.left.right.red) {
						root.left.red = true;
						root.repair_remove();
					}
					else {
						// 2.3
						if (root.left.right.red) {
							root.right.red = true;
							
							root.left.right.red = false;
							
							root.left.lrot();
						}
						
						// 2.4
						if (root.left.left.red) {
							boolean tmp = root.left.red;
							root.left.red = root.red;
							root.red = tmp;
							
							root.left.left.red = false;
							
							root.rrot();
						}
					}
				}
			}
			// "Fall 1"
			else {
				if (root.right != null) {
					
					// 1.1
					if (root.right.red) {
						root.red = true;
						root.right.red = false;
						root.lrot();
					}
					
					// 1.2
					if (!root.right.left.red && !root.right.right.red) {
						root.right.red = true;
						root.repair_remove();
					}
					else {
						// 1.3
						if (root.right.left.red) {
							root.right.red = true;
							
							root.right.left.red = false;
							
							root.right.rrot();
						}
						
						// 1.4
						if (root.right.right.red) {
							boolean tmp = root.right.red;
							root.right.red = root.red;
							root.red = tmp;
							
							root.right.right.red = false;
							
							root.lrot();
						}
					}
				}
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
			int left = (this.left != null ? this.left.blackness() : 0);
			int right = (this.right != null ? this.right.blackness() : 0);
			
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
			if (tree.root != null)
				this.node = tree.root.getMinimum();
			else
				node = null;
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
			if (last == null)
				throw new IllegalStateException();
			
			try {
				last.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class RedBlackReverseIter implements Iterator<T> {
		RedBlackNode node = null;
		RedBlackNode last = null;
		
		public RedBlackReverseIter(RedBlackTree<T> tree) {
			if (tree.root != null)
				this.node = tree.root.getMaximum();
			else
				node = null;
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
			if (last == null)
				throw new IllegalStateException();
			
			try {
				last.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private RedBlackNode root = null;

	public RedBlackTree(T value) {
		root = new RedBlackNode(value, this);
	}
	
	public boolean add(T value) {
		
		if (root == null)
			root = new RedBlackNode(value, this);
		else {
			try {
				root.insert(value);
			}
			catch (Exception e) { return false; }
		}

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
		return root != null ? root.blackness() + 1 : 0;
	}
	
	public String toString() {
		return root != null ? root.toString() : "<empty>";
	}
}
