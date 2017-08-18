package jbadillo.daq;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/***
 * Calculates an integer array's median, using divide and conquer
 * in O(n) time.
 * Partially quick-sorts
 * @author jbadillo
 *
 */
public class Median {
	
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
        //read array and k
        String p [] = in.nextLine().split(" ");
        int k = in.nextInt();
        //parse
        int [] arr = new int[p.length];
        for(int i=0; i<p.length; i++) arr[i] = Integer.parseInt(p[i]);
        //int [] arr = {3,10,7,6,12};
        //int k = 2;
        System.out.println(kselect(arr, k-1));
        in.close();
	}
	
	/**
	 * @param s an unsorted integer array
	 * @return the median value of the array, that is s[n/2]
	 * if the array were sorted.
	 */
	public static int median(int arr[]){
		// make a copy, it will be partially sorted
		arr = Arrays.copyOf(arr, arr.length);
		return kselect(arr, arr.length/2, 0, arr.length);
	}
	
	
	public static int kselect(int arr[], int k)
	{
		// make a copy, it will be partially sorted
		arr = Arrays.copyOf(arr, arr.length);
		return kselect(arr, k, 0, arr.length);
	}
	
	static Random r = new Random();
	
	/**
	 * @param arr and unsorted array
	 * @param k the position of the desired value
	 * @param start
	 * @param end
	 * @return s[k] if s were sorted. 
	 */
	private static int kselect(int [] arr, int k, int start, int end){
        //start=0,end=4 arr[3] and k==3
        if(k < start || end <= k)
            throw new Error("!!!"+start+","+end+":"+k);        
        //base case, k == 1, start=0, end=1, first element
        if(start == end-1) return arr[start];        

        //choose pivot
        int p = start + r.nextInt(end-start); 
        
        //swap with fist
        int t = arr[p];
        arr[p] = arr[start];
        arr[start] = t;
        p = start;
        
        //pivoting around    
        for(int i=start+1; i<end; i++){
            //if we have to pivot
            if(arr[p] >= arr[i]){
                //three swap
                t = arr[i];
                arr[i] = arr[p+1]; //send a bigger one to the tail
                arr[p+1] = arr[p]; //move the pivot
                arr[p] = t; //bring the small one to pivot
                p++;
            }
        }
        //now do recursion
        if( k < p ) //left
            return kselect(arr, k, start, p);
        if (k > p) //right
            return kselect(arr, k, p, end);
        //if ( k == p ) //we found it by chance hehehe
        return arr[p];

        //return -1;
    }
	
	
}
