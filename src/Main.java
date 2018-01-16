package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	/**
	 * Fonction qui parse le fichier donné en entrée et qui renvoie la liste composée des datas
	 * @param filename
	 * @return datas
	 */
	private static ArrayList<Data> parseFile(String filename) {
		ArrayList<Data> datas = new ArrayList<Data>();
		FileReader input = null;
		try {
			input = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		try {
			while ( (myLine = bufRead.readLine()) != null){
				if(!myLine.equals("EOF")) {
					int city = Integer.parseInt(myLine.substring(0,myLine.indexOf(" ")));
					myLine = myLine.substring(myLine.indexOf(" ")+1);
					int coord_X = Integer.parseInt(myLine.substring(0,myLine.indexOf(" ")));
					myLine = myLine.substring(myLine.indexOf(" ")+1);
					int coord_Y = Integer.parseInt(myLine);
					Data d = new Data(city, coord_X, coord_Y);
					datas.add(d);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 
	 * @param datas
	 * @return
	 */
	private static HashMap<Integer, ArrayList<Integer>> generateMatrixInHashMap(ArrayList<Data> datas) {
		HashMap<Integer, ArrayList<Integer>> resultat = new HashMap<Integer, ArrayList<Integer>> ();
		int xa,xb,ya,yb;
		for (int i = 0; i < datas.size(); i++) {
			xa = datas.get(i).getCoord_X();
			ya = datas.get(i).getCoord_Y();
			ArrayList<Integer> distances = new ArrayList<Integer>();
			for (int j = 0; j < datas.size(); j++) {
				xb = datas.get(j).getCoord_X();
				yb = datas.get(j).getCoord_Y();
				//if(i<j)
				distances.add((int) Math.round(Math.sqrt(Math.pow((xa-xb), 2)+Math.pow((ya-yb), 2))));
			}
			resultat.put(i, distances);
		}
		return resultat;
	}

	/**
	 * Fonction permettant d'avoir un apercu terminal de la matrice
	 * @param MatrixInHashMap
	 */
	private static void visualizeMatrixInHashMap(HashMap<Integer, ArrayList<Integer>> MatrixInHashMap) {
		for (int i = 0; i < MatrixInHashMap.size(); i++) {
			System.out.println("ville "+ i + " : " + MatrixInHashMap.get(i));
		}
	}
	
	private static ArrayList<Integer> evaluateHeuristicSolutionWithHashMap(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, int starter) {
		ArrayList<Integer> availableCities = new ArrayList<Integer>();
		ArrayList<Integer> finalListCities = new ArrayList<Integer>();
		
		int min = 1000000000;
		int somme = 0;
		
		//visualizeMatrixInHashMap(matrixInHashMap);
		
		for (int i = 0; i < matrixInHashMap.size(); i++) {
			availableCities.add(i);
		}
		
		while(availableCities.size()>0) {
			min = 1000000000;
			finalListCities.add(starter);
			availableCities.remove(availableCities.indexOf(starter));
			ArrayList<Integer> actualCities = matrixInHashMap.get(starter);

			for (int i = 0; i < actualCities.size(); i++) {
				//System.out.println(availableCities.contains(availableCities.indexOf(i)));
				if(!finalListCities.contains(i)) {
					if (actualCities.get(i)<min && actualCities.get(i)!=0) {
						min = actualCities.get(i);
						starter = i;
						//System.out.println("min : "+min);
					}
				} else if(starter == 0) {
					if (actualCities.get(i)<min && actualCities.get(i)!=0) {
						min = actualCities.get(i);
						//System.out.println("min : "+min);
					}
				}
			}
			if(min!=1000000000) {
				somme = somme + min;
			}
			/*
			System.out.println(finalListCities);
			System.out.println(availableCities);
			System.out.println(somme);
			System.out.println(starter);*/	
		}
		return finalListCities;
	}
	
	private static int bestHeuristic(HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		ArrayList<Integer> solutions = new ArrayList<Integer>();
		for (int i = 0; i < matrixInHashMap.size(); i++) {
			solutions.add(evaluateDistances(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,i),matrixInHashMap));
		}
		int min = 1000000000;
		for (int i = 0; i < solutions.size(); i++) {
			if(solutions.get(i)<min) {
				min = solutions.get(i);
			}
		}		
		return min;
	}
	
	/**
	 * Echange de place 2 items de la liste
	 * @param listCities
	 * @param a
	 * @param b
	 * @param matrixInHashMap
	 * @return
	 */
	private static ArrayList<Integer> swap(ArrayList<Integer> listCities, int a, int b) {
		int indexTmpA = listCities.indexOf(a);
		int indexTmpB = listCities.indexOf(b);
		int tempA = listCities.get(indexTmpA);
		
		listCities.set(indexTmpA, b);
		listCities.set(indexTmpB, tempA);
		
		return listCities;
	}
	
	private static int evaluateDistances(ArrayList<Integer> order, HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		int somme = 0;
		int starter = order.get(0);
		int last = order.get(order.size()-1);
		while(order.size()>0) {
			int min = 100000000;
			for (int i = 0; i < order.size(); i++) {
				if(min>matrixInHashMap.get(order.get(0)).get(i) && matrixInHashMap.get(order.get(0)).get(i)!=0) {
					min = matrixInHashMap.get(order.get(0)).get(i);
				}
			}
			if(min!=100000000) {
				somme = somme + min;
				order.remove(0);
			}
		}
		// bouclement du circuit
		somme = somme + matrixInHashMap.get(starter).get(last);
		
		return somme;
	}

	public static void main(String [] args) {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		HashMap<Integer, ArrayList<Integer>> matrixInHashMap = generateMatrixInHashMap(datas_kroA100);
		
		System.out.println("Solution à partir de la 1ère ville: " + evaluateDistances(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,0), matrixInHashMap));
		System.out.println(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,0));
		
		System.out.println("Swap entre la 1ère ville et la 2ème : " + evaluateDistances(swap(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,0), 0, 1),matrixInHashMap));
		System.out.println(swap(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,0), 0, 1));
		
		System.out.println("Best heuristic : " + bestHeuristic(matrixInHashMap));
		
	}
}
