package com.example.pruebaoffloading;

import java.util.concurrent.ExecutionException;

public class FibonnacciProxy {
	
	public int calcular(int n){
		if (n%2 == 0){
			GetServerData server = new GetServerData();
			server.execute(String.valueOf(n));
			try {
				return Integer.parseInt(server.get());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Fibonnacci fb = new Fibonnacci();
				return fb.calcular(n);
			}			
		}else {
			Fibonnacci fb = new Fibonnacci();
			return fb.calcular(n);
		}
		
	}
}
