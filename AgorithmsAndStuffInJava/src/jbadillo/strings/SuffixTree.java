package jbadillo.strings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Implementation of a suffix Tree as described by
 * Ukkonen's algorithm. O(n) space and time.
 * Based on this tutorial:
 * http://www.geeksforgeeks.org/ukkonens-suffix-tree-construction-part-1/
 * @author jbadillo
 *
 */
public class SuffixTree {

	public static final int ALPHABET_SIZE = 256;
	Node root;
	final char[] str;
	static final char END = '$';
	private int end;
	private Node an; // active node
	private int ae; // active edge (position of the character in the word)
	private int al; // active length
	private int rsc; // remaining suffix count (implicit)
	

	/**
	 * 
	 * @param word
	 */
	public SuffixTree(String word) {
		this.str = (word+END).toCharArray();
		root = new Node(-1,-1);
		
		buildTree();
		setIndex(root, 0);
	}
	
	/**
	 * @param suffix
	 * @return the starting index of the suffix, or -1 if not found
	 */
	public int searchSuffix(String suffix){
		return searchSuffix(root, (suffix+END).toCharArray(), 0);
	}
	
	/**
	 * @param suffix
	 * @return true if is suffix
	 */
	public boolean isSuffix(String suffix) {
		return -1 != searchSuffix(root, (suffix+END).toCharArray(), 0);
	}

	/**
	 * Recursive call
	 * @param node
	 * @param suffix
	 * @param index
	 * @return
	 */
	private int searchSuffix(Node node, char[] suffix, int index){
		// base case, reached end
		if(index == suffix.length){
			if(node.leaf)
				return node.index;
			else
				return -1;
		}
		// end reached but still more to go
		if(node.leaf)
			return -1;
		
		// no child with given char
		if(node.children[suffix[index]] == null)
			return -1;
		Node next = node.children[suffix[index]];
		// match edge characters
		for(int i=0; i < next.length() && index + i < suffix.length; i++)
			if(next.charAt(i) != suffix[index + i])
				return -1;
		// recursive
		return searchSuffix(next, suffix, index + next.length());
	}
	
	private Node lastNewNode;
	
	private void buildTree(){
		// set active points
		lastNewNode = null;
		an = root;
		ae = -1;
		al = 0;
		end = 0;
		rsc = 0;
		for (int i = 0; i < str.length; i++) 
			extendTree(i);
	}
	
	private void extendTree(int pos){
		// Rule #1
		this.end = pos + 1;
		rsc ++;
		lastNewNode = null;
		// add al suffixes
		while(rsc > 0){
			// if activeLength == 0, look for current char
			if(al == 0)
				ae = pos;
			
			// Rule #2 simple
			// If word[i] doesn't exist in active point, create leaf edge [i,end]
			if(an.children[str[ae]] == null)
			{
				an.children[str[ae]] = new Node(pos);
				// add suffix link to from previously created node
				if(lastNewNode != null){
					lastNewNode.suffix = an;
					lastNewNode = null;
				}
			}
			// an outgoing edge from active node
			else{
				Node next = an.children[str[ae]];
				if(walkDown(next))
					continue;
				// Rule #3 next character matches
				if(str[next.start+al] == str[pos]){
					// if an internal node was waiting for suffix link
					if(lastNewNode != null && an != root){
						lastNewNode.suffix = an;
						lastNewNode = null;
					}
					al++;
					// stop this phase
					break;
				}
				// Rule #2 complicated - in the middle of an edge
				int splitEnd = next.start + al - 1;
				
				// new internal node, ActiveNode -> newInternalNode
				Node split = new Node(next.start, splitEnd + 1);
				an.children[str[ae]] = split;
				
				// new leaf, NewInternalNode -> leaf
				Node leaf = new Node(pos);
				split.children[str[pos]] = leaf;
				
				// move the next node's beginning and tie it
				next.start += al;
				split.children[str[next.start]] = next;
				// create suffix link
				if(lastNewNode != null)
					lastNewNode.suffix = split;
				// keep the newly created inner node to tie the suffix link later
				lastNewNode = split;
			}
			rsc--;
			// decrease active length and recalculate active edge
			if(an == root && al > 0){
				al--;
				ae = pos - rsc + 1;
			}
			else if(an != root){
				// move to suffix
				an = an.suffix;
			}
		}
	}
	
	private boolean walkDown(Node node) {
		// skip / count trick
		if(al >= node.length()){
			// skip to next edge/node
			ae += node.length();
			al -= node.length();
			an = node;
			return true;
		}
		return false;
	}
	
	/**
	 * Calculates the starting index (height)
	 * of the suffix at each leaf.
	 * @param node
	 * @param h
	 */
	private void setIndex(Node node, int h){
		
		if(!node.leaf){
			for(Node child: node.children)
				if(child != null)
					setIndex(child, node.length() + h);
		}
		else{
			node.index = str.length - node.length() - h;
		}
	}
	
	/***
	 * @return number of nodes
	 */
	public int size(){
		return size(root);
	}
	
	private int size(Node node){
		if(node.leaf)
			return 1;
		int count = 1;
		for(Node child: node.children)
			if(child != null)
				count += size(child);
		return count;
	}	

	@Override
	public String toString() {
		StringBuilder bf = new StringBuilder();
		bf.append("- root -\n");
		toString(root, 1, bf);
		
		return bf.toString();
	}
	
	/**
	 * recursive traverse DFS
	 * @param n
	 * @param d
	 * @param bf
	 */
	private void toString(Node n, int d, StringBuilder bf){
		if(n.leaf)
			return;
		for (Node child : n.children) {
			if(child != null){
				for (int i = 0; i < d; i++)
					bf.append("   ");
				bf.append("[");
				bf.append(child.toString());
				bf.append("]\n");
				toString(child, d + 1, bf);
			}
		}
	}
	
	/**
	 * A node-edge simplification of the suffix tree node.
	 * If a node is a leaf, the end is the current end of
	 * the entire suffix tree.
	 * @author jbadillo
	 */
	private class Node {
		private Node[] children;
		private Node suffix;
		private boolean leaf;
		private int start, end, index;
		
		/***
		 * An inner node (possibly root)
		 * @param start
		 * @param end
		 */
		public Node(int start, int end) {
			// internal node - possibly root
			leaf = false;
			children = new Node[ALPHABET_SIZE];
			this.start = start;
			this.end = end;
			suffix = SuffixTree.this.root;
		}
		
		/**
		 * A leaf node, end is the currently known end of the
		 * string
		 * @param start
		 */
		public Node(int start) {
			// leaf node
			this.start = start;
			this.index = start;
			leaf = true;
		}

		/**
		 * @return number of characters on edge
		 */
		public int length(){
			return (leaf?SuffixTree.this.end:this.end) - start;
		}
		
		/**
		 * 
		 * @param i
		 * @return
		 */
		public char charAt(int i){
			return SuffixTree.this.str[this.start + i];
		}
		
		@Override
		public String toString() {
			
			return plain() + " ["+start+", "+end+")"+
						(leaf?(" = "+index): "");
		}
		
		public String plain(){
			if(leaf)
				end = SuffixTree.this.end;
			return new String(SuffixTree.this.str, start, end - start);
		}
	}

	public static final char SEPARATOR = '|';
	
	public static String longestCommonSubsequence(String s1, String s2){
		// build a separator
		String full = s1 + SEPARATOR + s2;
		SuffixTree tree = new SuffixTree(full);
		
		// TODO find the longest branch that ends with SEPARATOR and END
		LinkedList<Node> path = new LinkedList<>();
		int r = lcsR(tree.root, s1.length(), full.length()-1, path);
		
		// join the nodes content
		return path.stream()
				// non root
				.filter(n -> n!=tree.root)
				.map(Node::plain)
				.collect(Collectors.joining());
		
	}
	
	
	
	public static int lcsR(Node node, int sep1, int sep2, Collection<Node> path){
		// base case
		if(node.leaf)
			return 0; 
		
		// check the children
		int max = 0;
		
		Collection<Node> maxList = null;
		boolean end1 = false, end2 = false;
		for(Node child: node.children){
			if(child == null)
				continue;
			// has the middle separator
			if(child.start <= sep1 && sep1 < child.start + child.length())
				end1 = true;
			// has the end separator
			else if(child.leaf)
				end2 = true;
			if(!child.leaf){
				// recursive call
				Collection<Node> childPath = new LinkedList<>();
				int r = lcsR(child, sep1, sep2, childPath);
				if(max < r){
					max = r;
					maxList = childPath;
				}
			}
		}
		// if any recursive was successful
		if(max > 0){
			// append found
			path.add(node);
			path.addAll(maxList);
			return node.length() + max;
		}
		
		// if has both ends as children
		if(end1 && end2){
			path.add(node);
			return node.length();
		}
		
		// else not found
		return 0;
	}
	
}
