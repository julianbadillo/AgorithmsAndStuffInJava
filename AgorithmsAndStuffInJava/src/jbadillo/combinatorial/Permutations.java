package jbadillo.combinatorial;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.joining;

import static java.util.stream.IntStream.range;

import java.util.ArrayList;

/***
 * Produces all permutations of a set of objects
 * @author jbadillo
 *
 */
public class Permutations<T> {
	
	private boolean hasNext;
	private Set<T> set;
	
	private LinkedList<T> perm;
	private LinkedList<Iterator<T>> iteStack;
	
	/**
	 * Empty constructor
	 */
	public Permutations(){
		hasNext = false;
	}
	
	/**
	 * Set constructor
	 * @param set
	 */
	public Permutations(Set<T> set){
		this.set = set;
		this.perm = new LinkedList<>();
		this.iteStack = new LinkedList<>();
		// initialize
		iteStack.add(new HashSet<>(set).iterator());
		nextPermutation();
	}
	
	/**
	 * From 0 to n-1
	 * @param n
	 */
	public static void printPermutations(int n){
		(new Permutations<>()).printPermutations(range(0, n)
							.mapToObj(Integer::new)
							.collect(toSet()));
	}
	
	/**
	 * @return true if next permutation
	 */
	public boolean hasNext(){
		return hasNext;
	}
	
	/**
	 * @return next permutation
	 */
	public List<T> getNextPermutation(){
		List<T> res = new ArrayList<>(perm);
		nextPermutation();
		return res;
	}
	
	private void nextPermutation(){
		if(!hasNext)
			return;
		do{
			Iterator<T> ite = iteStack.peekLast();
			// if can be iterated yet, push on the stack
			if(ite.hasNext()){
				T t = ite.next();
				set.remove(t);
				perm.addLast(t);
				iteStack.addLast(new HashSet<>(set).iterator());
			}
			// if cannot be iterated anymore
			else{
				// pop it
				iteStack.removeLast();
				// return last one to set
				if(!perm.isEmpty()){
					T t = perm.removeLast();
					set.add(t);
				}
				// no more permutations
				else{
					hasNext = false;
					perm = null;
					return;
				}
			}
		} while (!set.isEmpty());
		hasNext = true;
	}
	
	/*
	private void printPermutationsRecursive(LinkedList<T> perm, Set<T> set){
		if(set.isEmpty()){
			System.out.println(perm.stream().map(T::toString).collect(joining(", ")));
		}
		else{
			Set<T> copy = new HashSet<>(set);
			for(T t: copy){
				set.remove(t);
				perm.addLast(t);
				printPermutationsRecursive(perm, set);
				perm.removeLast();
				set.add(t);
			}
		}
	}*/
	
	/***
	 * All permutations of given set in stdin. Iterative.
	 * @param set
	 */
	public void printPermutations(Set<T> set){
		LinkedList<T> perm = new LinkedList<>();
		LinkedList<Iterator<T>> iteStack = new LinkedList<>();
		iteStack.add(new HashSet<>(set).iterator());
		
		do{
			if(set.isEmpty())
				System.out.println(perm.stream().map(T::toString).collect(joining(", ")));
		
			Iterator<T> ite = iteStack.peekLast();
			// if can be iterated yet, push on the stack
			if(ite.hasNext()){
				T t = ite.next();
				set.remove(t);
				perm.addLast(t);
				iteStack.addLast(new HashSet<>(set).iterator());
			}
			// if cannot be iterated anymore
			else{
				// pop it
				iteStack.removeLast();
				// return last one to set
				if(!perm.isEmpty()){
					T t = perm.removeLast();
					set.add(t);
				}
			}
		} while(!perm.isEmpty());
	}

}
