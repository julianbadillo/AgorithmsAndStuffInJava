package jbadillo.datastruct;

import java.util.LinkedList;

/**
 * A Red-Black balanced search tree as described on
 * Sedgewick, Robert, 1946 - Algorithms.s
 * Reprinted with corrections, August 1984
 * With minor differences to adapt for java.
 * @author jbadillo
 *
 */
public class RBTree<K extends Comparable<K>, V> {

	private int size = 0;
	
	private TreeNode root;
	
	public RBTree() {
		
	}
	
	/**
	 * @return size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @param key add to the tree
	 */
	public void put(K key, V val){
		size++;
		// first node
		if (root == null)
			root = new TreeNode(key, val);
		 else
			_put(key, val);
	}
	
	private void _put(K key, V val){
		// search if existing - not allow duplicates
		TreeNode node = root, parent = null;
		LinkedList<TreeNode> path = new LinkedList<>();
		
		int dir = 0;
		while(node != null){
			path.add(node);
			parent = node;
			// found - do nothing
			if(node.key.equals(key)){
				node.val = val;
				// assign
				return;
			}
			dir = key.compareTo(node.key);
			if(dir < 0)
				node = node.left;
			else
				node = node.right;
		}
		// not found - add a new node - red link
		node = new TreeNode(key, val);
		node.red = true;
		if(dir < 0)
			parent.left = node;
		else
			parent.right = node;
		path.add(node);
		// rebalance
		rebalance(path);
	}
		
	private void rebalance(LinkedList<TreeNode> path){
		TreeNode node = path.isEmpty() ? null : path.removeLast();
		TreeNode parent = path.isEmpty() ? null : path.removeLast();
		TreeNode grandpa = path.isEmpty() ? null : path.removeLast();
		TreeNode greatg = path.isEmpty() ? null : path.removeLast();
		
		while (node != null && node != root && node.red) {
			// double red link
			if (grandpa != null && parent != null && parent.red) {
				// Case 1: right - right
				if (grandpa.right == parent && parent.right == node) {
					// Case 1.1 easy splitting one 4-node to two 2-nodes and move the middle node up 
					if(grandpa.left != null && grandpa.left.red){
						grandpa.left.red = false;
						grandpa.right.red = false;
						// move the middle node up
						if(grandpa != root)
							grandpa.red = true;
					}
					// Case 1.2 left rotation (to balance a 4-node)
					else {
						// set old granpda to red
						grandpa.red = true;
						grandpa = rotateLeft(greatg, grandpa);
						// new granpa is black
						grandpa.red = false;
					}
				}
				// case 2: left - left, symmetric
				else if (grandpa.left == parent && parent.left == node) {
					if (grandpa.right != null && grandpa.right.red) {
						grandpa.left.red = false;
						grandpa.right.red = false;
						if (grandpa != root)
							grandpa.red = true;
					} 
					else {
						grandpa.red = true;
						grandpa = rotateRight(greatg, grandpa);
						grandpa.red = false;
					}
				}
				// case 3: right - left
				else if(grandpa.right == parent && parent.left == node){
					// Case 3.1: split one 4-node to two 2-nodes and move middle up
					if(grandpa.left != null && grandpa.left.red){
						grandpa.left.red = false;
						grandpa.right.red = false;
						// move middle up
						if(grandpa != root)
							grandpa.red = true;
					}
					// rotation
					else {
						grandpa.red = true;
						rotateRight(grandpa, parent);
						grandpa = rotateLeft(greatg, grandpa);
						grandpa.red = false;
					}
				}
				// case 4: left - right, symmetric
				else if(grandpa.left == parent && parent.right == node){
					if(grandpa.right != null && grandpa.right.red){
						grandpa.left.red = false;
						grandpa.right.red = false;
						if(grandpa != root)
							grandpa.red = true;
					}
					else {
						grandpa.red = true;
						rotateLeft(grandpa, parent);
						grandpa = rotateRight(greatg, grandpa);
						grandpa.red = false;
					} 
				}
			}
			node = grandpa;
			parent = greatg;
			grandpa = path.isEmpty() ? null : path.removeLast();
			greatg = path.isEmpty() ? null : path.removeLast();
		}
	}
	
	private TreeNode rotateLeft(TreeNode parent, TreeNode node){
		// Right Node becomes Up Node
		TreeNode up = node.right;
		// keep old left son to right
		node.right = up.left;
		// UP Node becomes Left
		up.left = node;
		
		// update parent
		// no parent - update root
		if(parent == null)
			root = up;
		// is left or right of the parent
		else if(parent.left == node)
			parent.left = up;
		else
			parent.right = up;
		return up;
	}	
	
	private TreeNode rotateRight(TreeNode parent, TreeNode node){
		// Left node becomes up
		TreeNode up = node.left;
		// keep old right son to left
		node.left = up.right;
		// up node becomse right
		up.right = node;
		
		// update parent
		// no parent - update root
		if(parent == null)
			root = up;
		// is left or right of the parent
		else if(parent.left == node)
			parent.left = up;
		else
			parent.right = up;
		return up;
	}
	
	/**
	 * @param key
	 * @return true if key in the stuff
	 */
	public boolean containsKey(K key){
		TreeNode node = get(root, key);
		return node != null;
	}
	
	/**
	 * @param key
	 * @return Value under key
	 */
	public V get(K key){
		TreeNode node = get(root, key);
		return node == null ? null : node.val;
	}
	
	private TreeNode get(TreeNode node, K key){
		// iterative
		while(node != null){
			if(node.key.equals(key))
				return node;
			else if(key.compareTo(node.key) < 0)
				node = node.left;
			else
				node = node.right;
		}
		// not found
		return null;
	}
	
	private class TreeNode {
		K key;
		V val;
		boolean red;
		TreeNode left;
		TreeNode right;
		public TreeNode(K key, V val) {
			this.key = key;
			this.val = val;
		}
		public String printNode() {
			return String.format("%s%s", key, (red ? "R" : "B"));
		}
		
		public String toString() {
			return String.format("%s:%s", key, val);
		}
	}
	
	/**
	 * Prints a flat view of the tree
	 */
	public String printTree() {
		StringBuilder bf = new StringBuilder();
		printTree(root, bf);
		return bf.toString();
	}

	private void printTree(TreeNode node, StringBuilder bf){
		// empty
		if(node == null)
			return;
		// leaf
		if(node.left == null && node.right == null){
			bf.append(node.printNode());
		}
		// non leaf
		else{
			bf.append('(');
			printTree(node.left, bf);
			bf.append(',').append(node.printNode()).append(',');
			printTree(node.right, bf);
			bf.append(')');
		}
	}
	
	/**
	 * Prints {K:V, ...}
	 */
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append('{');
		toString(root, bf);
		bf.append('}');
		return bf.toString();
	}
	
	private void toString(TreeNode node, StringBuilder bf){
		LinkedList<TravNode> stack = new LinkedList<>();
		stack.push(new TravNode(node));
		// iterative - using stack
		while(!stack.isEmpty()){
			TravNode tn = stack.pop();
			if (tn.visited) {
				bf.append(tn.node.toString());
				if(!stack.isEmpty())
					bf.append(", ");
			} else {
				// left on top
				if (tn.node.right != null)
					stack.push(new TravNode(tn.node.right));
				tn.visited = true;
				stack.push(tn);
				if (tn.node.left != null)
					stack.push(new TravNode(tn.node.left));
			}
		}
	}
	
	private class TravNode{
		TreeNode node;
		boolean visited = false;
		public TravNode(TreeNode node) {
			this.node = node;
		}
	}
}
