package jbadillo.dynamic;

import java.util.Arrays;

public class KnapSackSingle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int c []= { 1, 3, 4, 5 };
		int r []= {15, 9, 5, 10 };
		int D = 8;

		System.out.println(maxRet(c, r, D));
		System.out.println(Arrays.toString(maxRet2(c, r, D)));
		
		c = new int[]{ 6, 3, 4, 2 };
		r= new int[]{30, 14, 16, 9 };
		 D = 10;
		System.out.println(maxRet(c, r, D));
		System.out.println(Arrays.toString(maxRet2(c, r, D)));
		
	}

	static int maxRet(int c[], int r[], int D){

		int[][] M = new int[c.length][D+1];

		//easy cases, first row, only first project
		for (int d = 0; d <= D; d++) {
			//if cost less than budget, take the project
			if(c[0]<=d){
				M[0][d] = r[0];
			}
			//else, dont
			else{
				M[0][d] = 0;
			}
		}

		//first column, if budget zero
		for (int i = 0; i < M.length; i++) {
			M[i][0] = 0;
		}

		//from left to right, top to bottom
		for (int i = 1; i < M.length; i++) {
			for (int d = 1; d <= D; d++) {
				int ret1 = 0, ret2 = 0;
				//if affordable project
				if(c[i] <= d){
					ret1 = r[i] + M[i-1][d-c[i]];
				}
				ret2 = M[i-1][d];
				//pick maximum
				M[i][d] = ret1>ret2?ret1:ret2;
			}
		}
		
		//return last position
		return M[M.length-1][D];
	}
	
	
	static boolean[] maxRet2(int c[], int r[], int D){

		int[][] M = new int[c.length][D+1];
		char[][] prev = new char[c.length][D+1];
		boolean p[] = new boolean[c.length];
		
		
		//easy cases, first row, only first project
		for (int d = 0; d <= D; d++) {
			//if cost less than budget, take the project
			if(c[0]<=d){
				M[0][d] = r[0];
				prev[0][d] = 'l';
			}
			//else, dont
			else{
				M[0][d] = 0;
				prev[0][d] = 'u';
			}
		}

		//first column, if budget zero
		for (int i = 0; i < M.length; i++) {
			M[i][0] = 0;
			prev[i][0] = 'u';
		}

		//from left to right, top to bottom
		for (int i = 1; i < M.length; i++) {
			for (int d = 1; d <= D; d++) {
				int ret1 = 0, ret2 = 0;
				//if affordable project
				if(c[i] <= d){
					ret1 = r[i] + M[i-1][d-c[i]];
				}
				ret2 = M[i-1][d];
				//pick maximum
				if(ret1>ret2){
					//go up left
					M[i][d] = ret1;
					prev[i][d] = 'l';
				}else{
					//go only up
					M[i][d] = ret2;
					prev[i][d] = 'u';
				}
			}
		}
		
		for (int i = 0; i < M.length; i++) {
			System.out.println(Arrays.toString(M[i]));
		}
		
		
		//build from last
		for (int k = 0, i = M.length-1, d=D; k < p.length; k++) {
			//if up and left, project was used
			if(prev[i][d] == 'l'){
				p[i] = true;
				//trace back
				d = d-c[i];
				i = i-1;
			}
			//else, wasnt used
			else{
				i = i-1;
			}
		}
		
		//return if proyect is used
		return p;
	}
}
