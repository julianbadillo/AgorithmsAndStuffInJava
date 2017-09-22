package jbadillo.dynamic;

import java.util.Arrays;

public class KnapSackMulti {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int P []= { 3, 2, 4, 2 };
		int G []= {15, 9, 5, 10 };
		int W = 8;

		System.out.println(maxGanancia(P,G,W));
		System.out.println(Arrays.toString(maxGanancia2(P,G,W)));
		
		P = new int[]{ 6, 3, 4, 2 };
		G= new int[]{30, 14, 16, 9 };
		W = 10;
		
		System.out.println(maxGanancia(P,G,W));
		System.out.println(Arrays.toString(maxGanancia2(P,G,W)));
	}

	static int maxGanancia(int P[], int G[], int W){
		//memorizar las soluciones desde 0 hasta W
		int MG[] = new int[W+1];
		
		//resolver el caso facil
		//MG(0) = 0
		MG[0] = 0;
		
		//de facil a mas dificil
		for (int w2 = 0; w2 < MG.length; w2++) {
			
			//MG(w2) = (max MG(w2-P[i]) + G[i]) | i in[0,n-1] )
			int max = 0;
			//recorrer los arreglos de los items, calculando la maxima ganancia
			for (int i = 0; i < P.length; i++) {
				//verifiquemos que podamos meter el item dentro de la maleta
				if(w2 >= P[i]){
				//evaluo la ganacia de poner el item i dentro de la malerta
				int g = MG[w2-P[i]] + G[i];
				if(g > max)
					max = g;
				}
			}
			MG[w2] = max;
			
		}
		return MG[W];
	}
	
	static int[] maxGanancia2(int P[], int G[], int W){
		//memorizar las soluciones desde 0 hasta W
		int MG[] = new int[W+1];
		//memorizar las decisiones tomadas
		int dec[] = new int[W+1];
		
		//respuesta
		int X[] = new int [P.length];
		
		//resolver el caso facil
		//MG(0) = 0
		MG[0] = 0;
		
		//de facil a mas dificil
		for (int w2 = 0; w2 < MG.length; w2++) {
			
			//MG(w2) = (max MG(w2-P[i]) + G[i]) | i in[0,n-1] )
			int max = 0;
			//recordemos cual item tiene el maximo
			int imax = -1;
			//recorrer los arreglos de los items, calculando la maxima ganancia
			for (int i = 0; i < P.length; i++) {
				//verifiquemos que podamos meter el item dentro de la maleta
				if(w2 >= P[i]){
					//evaluo la ganacia de poner el item i dentro de la malerta
					int g = MG[w2-P[i]] + G[i];
					if(g > max){
						max = g;
						//guardar la posicion
						imax = i;
					}
				}
			}
			//guardar el maximo
			MG[w2] = max;
			//guardar la posicion del maximo
			dec[w2] = imax;
		}
		
		//reconstruir cuales decisiones tomamos a partir de la respuesta
		int i=0;
		for(int w2=W; w2>0 && i !=-1;){
			//miramos la decision tomada
			i = dec[w2];
			//incrementar la cantidad de items i en 1
			X[i]++;
			//devuelvo al caso anterior
			w2 = w2 - P[i];
		}
		
		return X;
	}

	
}
