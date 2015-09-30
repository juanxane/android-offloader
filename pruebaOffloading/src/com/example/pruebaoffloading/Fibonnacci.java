package com.example.pruebaoffloading;

public class Fibonnacci {

	//SE agrega un comentario para probar git desde consola
	public int calcular(int n){
		if (n>=0){
			if (n==0 || n==1)
				return 1;
			else {
				int va = calcular(n-1) + calcular(n-2);
				return va;
			}
		}
		return 0;
	}
	
	
	
}
