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

	private static void visualizeMatrixInHashMap(HashMap<Integer, ArrayList<Integer>> MatrixInHashMap) {
		for (int i = 0; i < MatrixInHashMap.size(); i++) {
			System.out.println("ville "+ i + " : " + MatrixInHashMap.get(i));
		}
	}
	
	/**
	 * Fonction permettant d'avoir un apercu terminal de la matrice
	 * @param matrix
	 */
	private static void visualizeMatrix(MatrixData [][] matrix, int TAILLLE) {
		if(TAILLLE==0) {
			TAILLLE=matrix.length;
		}

		for (int i = 0; i < TAILLLE; i++) {
			for (int j = 0; j < TAILLLE; j++) {
				System.out.print(matrix[i][j] + " | ");
			}
			System.out.println();
		}

	}

	/**
	 * inspiration : http://www.journaldunet.com/web-tech/developpement/1202399-comment-generer-un-nombre-aleatoire-random-en-java-compris-entre-deux-chiffres/
	 * @param x
	 * @return
	 */
	private static ArrayList<Integer> randomCities(int x){
		ArrayList<Integer> cities = new ArrayList<Integer>();
		int r=0;
		ArrayList<Integer> availableCities = new ArrayList<Integer>();
		for (int i = 1; i < x+1; i++) {
			availableCities.add(i);
		}
		while(cities.size()!=x) {
			r = (int) (Math.random() * ( availableCities.size() - 1));
			if(!cities.contains(availableCities.get(r))) {
				cities.add(availableCities.get(r));
				availableCities.remove(r);
			}
		}
		return cities;
	}

	private static int evaluateHeuristicSolutionWithHashMap(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, int starter) {
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
		//ajout du dernier couple pour boucler la boucle
		ArrayList<Integer> lastCities = matrixInHashMap.get(finalListCities.get(finalListCities.size()-1));
		somme = somme + lastCities.get(finalListCities.get(0));
		
		return somme;
	}
	
	private static int bestHeuristic(HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		ArrayList<Integer> solutions = new ArrayList<Integer>();
		for (int i = 0; i < matrixInHashMap.size(); i++) {
			solutions.add(evaluateHeuristicSolutionWithHashMap(matrixInHashMap,i));
		}
		int min = 1000000000;
		for (int i = 0; i < solutions.size(); i++) {
			if(solutions.get(i)<min) {
				min = solutions.get(i);
			}
		}
		System.out.println(solutions);
		
		return min;
	}

	public static void main(String [] args) {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		HashMap<Integer, ArrayList<Integer>> matrixInHashMap = generateMatrixInHashMap(datas_kroA100);
		//visualizeMatrix(matrixARandom);
		ArrayList<Integer> cities = randomCities(datas_kroA100.size());
		
		//System.out.println("ville 79 : " + matrixInHashMap.get(79) + ";" +matrixInHashMap.get(79).size());
		System.out.println("Solution : " + evaluateHeuristicSolutionWithHashMap(matrixInHashMap,19));
		
		System.out.println("Best heuristic : " + bestHeuristic(matrixInHashMap));
	}
}
