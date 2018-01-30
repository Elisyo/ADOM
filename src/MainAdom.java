package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainAdom {

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

	private static ArrayList<ArrayList<Integer>> initialisationPopulation(HashMap<Integer, ArrayList<Integer>> matrixInHashMap,int nbPop, String optionsInit,String optionsMouv, int starter) {
		ArrayList<ArrayList<Integer>> worldPopulation = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> random = new ArrayList<Integer>();
		ArrayList<Integer> starters = new ArrayList<Integer>();

		while (starters.size() < nbPop) {
			random = initialisation(matrixInHashMap, optionsInit,optionsMouv, starter);

			if(!worldPopulation.contains(random)) {
				worldPopulation.add(random);
				starters.add(starter);
			}
			starter = (int) (Math.random()*100-1);
		}
		return worldPopulation;
	}

	/**
	 * Démarre une solution aléatoire ou heuristique
	 * @param matrix
	 * @param options
	 * @return
	 */
	private static ArrayList<Integer> initialisation(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, String optionsInit,String optionsMouv, int starter) {
		ArrayList<Integer> listCities = new ArrayList<Integer>();
		switch (optionsInit) {
		case "random":
			listCities = randomList();
			break;
		case "mouvement":
			listCities = mouvement(matrixInHashMap,optionsMouv, starter);
			break;
		default:
			System.out.println("Parametre d'initialisation non valide : 'random' OU 'mouvement'");
			break;
		}
		return listCities;
	}

	private static ArrayList<Integer> randomList(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> availableCities = new ArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			availableCities.add(i);
		}
		int r =0;
		while(availableCities.size()!=0) {
			r = (int) (Math.random()* (availableCities.size()-0));
			result.add(availableCities.get(r));
			availableCities.remove(r);
		}

		return result;
	}

	private static ArrayList<Integer> mouvement(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, String options, int starter){
		ArrayList<Integer> listCities = new ArrayList<Integer>();
		switch (options) {
		case "semi-heuristic":
			listCities = semiHeuristicWithHashMap(matrixInHashMap, starter);
			break;
		case "heuristic":
			listCities = heuristicWithHashMap(matrixInHashMap, starter);
			break;
		default:
			System.out.println("Parametre de mouvement non valide : 'semi-heuristic' OU 'heuristic'");
			break;
		}
		return listCities;
	}

	private static ArrayList<Integer> heuristicWithHashMap(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, int starter) {
		ArrayList<Integer> availableCities = new ArrayList<Integer>();
		ArrayList<Integer> finalListCities = new ArrayList<Integer>();

		int min = 1000000000;

		for (int i = 0; i < matrixInHashMap.size(); i++) {
			availableCities.add(i);
		}

		while(availableCities.size()>0) {
			min = 1000000000;
			finalListCities.add(starter);
			availableCities.remove(availableCities.indexOf(starter));
			ArrayList<Integer> actualCities = matrixInHashMap.get(starter);

			for (int i = 0; i < actualCities.size(); i++) {
				if(!finalListCities.contains(i)) {
					if (actualCities.get(i)<min && actualCities.get(i)!=0) {
						min = actualCities.get(i);
						starter = i;
					}
				} else if(starter == 0) {
					if (actualCities.get(i)<min && actualCities.get(i)!=0) {
						min = actualCities.get(i);
					}
				}
			}
		}
		return finalListCities;
	}

	private static ArrayList<Integer> semiHeuristicWithHashMap(HashMap<Integer, ArrayList<Integer>> matrixInHashMap, int starter) {
		ArrayList<Integer> availableCities = new ArrayList<Integer>();
		ArrayList<Integer> finalListCities = new ArrayList<Integer>();

		int min = 1000000000;
		int secondMin = 1000000000;
		boolean minFound = false;

		for (int i = 0; i < matrixInHashMap.size(); i++) {
			availableCities.add(i);
		}

		while(availableCities.size()>0) {
			finalListCities.add(starter);
			availableCities.remove(availableCities.indexOf(starter));
			if(availableCities.size()!=0) {
				ArrayList<Integer> actualCities = matrixInHashMap.get(starter);
				minFound = false;

				if(availableCities.size()>3) {
					if(actualCities.get(availableCities.get(0))==0) {
						min = actualCities.get(availableCities.get(1));
						secondMin = actualCities.get(availableCities.get(2));
					} else {
						min = actualCities.get(availableCities.get(0));
						secondMin = actualCities.get(availableCities.get(1));
					}
				} else if(availableCities.size()>2) {
					if(actualCities.get(availableCities.get(0))==0) {
						min = actualCities.get(availableCities.get(1));
					} else {
						min = actualCities.get(availableCities.get(0));
					}
				} else {
					min = actualCities.get(availableCities.get(0));
				}

				for (int i = 0; i < actualCities.size(); i++) {
					if(!finalListCities.contains(i)) {
						if (actualCities.get(i)<min && actualCities.get(i)!=0 && !minFound) {
							min = actualCities.get(i);
							starter = i;
							minFound = true;
						} else if(actualCities.get(i)<secondMin && actualCities.get(i)!=0){
							secondMin = actualCities.get(i);
							starter = i;
						}
					} else if(starter == 0) {
						if (actualCities.get(i)<min && actualCities.get(i)!=0 && !minFound) {
							min = actualCities.get(i);
							minFound = true;
						} else if(actualCities.get(i)<secondMin && actualCities.get(i)!=0){
							secondMin = actualCities.get(i);
							starter = i;
						}
					}
				}
				//gerer si le min est tout seul
				if(min == actualCities.get(availableCities.get(0)) && availableCities.size()==1) {
					starter = availableCities.get(0);
				}
			}
		}
		return finalListCities;
	}

	private static int bestHeuristic(HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		ArrayList<Integer> solutions = new ArrayList<Integer>();
		for (int i = 0; i < matrixInHashMap.size(); i++) {
			solutions.add(evaluateDistances(heuristicWithHashMap(matrixInHashMap,i),matrixInHashMap));
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
	 * Switch sur le voisinage
	 * @param listCities
	 * @param a
	 * @param b
	 * @param matrixInHashMap
	 * @return
	 */
	private static ArrayList<Integer> voisinage(ArrayList<Integer> listCities, int a, int b, String options) {
		switch (options) {
		case "swap":
			swap(listCities, a, b);
			break;
		case "two-opt":
			listCities = two_opt(listCities, a, b);
			break;
		default:
			System.out.println("Parametre de mouvement non valide : 'swap' OU 'two-opt'");
			break;
		}
		return listCities;
	}

	/** swap (2,5)
	 * [0,1,2,3,4,5,6]
	 * two-opt (2,5)
	 * [0,1,5,3,4,2,6]
	 * 
	 * @param listCities
	 * @param a
	 * @param b
	 * @return
	 */
	private static ArrayList<Integer> swap(ArrayList<Integer> listCities, int a, int b){
		int indexTmpA = listCities.indexOf(a);
		int indexTmpB = listCities.indexOf(b);
		int tempA = listCities.get(indexTmpA);

		listCities.set(indexTmpA, b);
		listCities.set(indexTmpB, tempA);

		return listCities;
	}	

	/** two-opt (2,5)
	 * [0,1,2,3,4,5,6]
	 * two-opt (2,5)
	 * [0,1,5,4,3,2,6]
	 * 
	 * @param listCities
	 * @param a
	 * @param b
	 * @return
	 */
	private static ArrayList<Integer> two_opt(ArrayList<Integer> listCities, int a, int b){
		int indexTmpA = listCities.indexOf(a);
		int indexTmpB = listCities.indexOf(b);

		ArrayList<Integer> listTmp = new ArrayList<Integer>();

		if(indexTmpA<indexTmpB) {
			for (int i = indexTmpA; i <= indexTmpB; i++) {
				listTmp.add(listCities.get(i));
			}
		} else {
			for (int i = indexTmpB; i <= indexTmpA; i++) {
				listTmp.add(listCities.get(i));
			}
		}

		if(indexTmpA<indexTmpB) {
			for (int i = indexTmpA; i <= indexTmpB; i++) {
				listCities.set(i, listTmp.get(listTmp.size()-1));
				listTmp.remove(listTmp.get(listTmp.size()-1));
			}
		} else {
			for (int i = indexTmpB; i <= indexTmpA; i++) {
				listCities.set(i, listTmp.get(listTmp.size()-1));
				listTmp.remove(listTmp.get(listTmp.size()-1));
			}
		}

		return listCities;
	}

	private static int evaluateDistances(ArrayList<Integer> order, HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		int somme = 0;
		int starter = order.get(0);
		int last = order.get(order.size()-1);

		ArrayList<Integer> currentCities = new ArrayList<Integer>();

		for (int i = 0; i < order.size(); i++) {
			currentCities.add(order.get(i));
		}

		while(currentCities.size()>0) {
			int min = 100000000;
			for (int i = 0; i < currentCities.size(); i++) {
				if(min>matrixInHashMap.get(currentCities.get(0)).get(i) && matrixInHashMap.get(currentCities.get(0)).get(i)!=0) {
					min = matrixInHashMap.get(currentCities.get(0)).get(i);
				}
			}
			if(min!=100000000) {
				somme = somme + min;
				currentCities.remove(0);
			}
		}
		// bouclement du circuit
		somme = somme + matrixInHashMap.get(starter).get(last);

		return somme;
	}

	public static void main(String [] args) {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		HashMap<Integer, ArrayList<Integer>> matrixInHashMap = generateMatrixInHashMap(datas_kroA100);

		//visualizeMatrixInHashMap(matrixInHashMap);

		ArrayList<Integer> heuristicFromFirstCity = initialisation(matrixInHashMap, "mouvement", "heuristic", 0);
		System.out.println("Solution à partir de la 1ère ville: " + evaluateDistances(heuristicFromFirstCity,matrixInHashMap));
		System.out.println(heuristicFromFirstCity);

		System.out.println("========================================================================");

		System.out.println("Swap entre la 1ère ville et la 2ème : " + evaluateDistances(voisinage(heuristicFromFirstCity, 0, 1,"swap"),matrixInHashMap));
		System.out.println(voisinage(initialisation(matrixInHashMap, "mouvement", "heuristic", 0), 0, 1,"swap"));

		System.out.println("========================================================================");

		System.out.println("Two-opt entre la 1ère ville et la 2ème : " + evaluateDistances(voisinage(heuristicFromFirstCity, 0, 1,"two-opt"),matrixInHashMap));
		System.out.println(voisinage(initialisation(matrixInHashMap, "mouvement", "heuristic", 0), 0, 1,"two-opt"));

		System.out.println("========================================================================");

		System.out.println("Best heuristic : " + bestHeuristic(matrixInHashMap));

		System.out.println("========================================================================");

		ArrayList<Integer> random = initialisation(matrixInHashMap, "random","", 0);
		System.out.println("Random list : " + evaluateDistances(random, matrixInHashMap));
		System.out.println(random);

		System.out.println("========================================================================");

		ArrayList<Integer> semiHeuristicFromFirstCity = initialisation(matrixInHashMap, "mouvement", "semi-heuristic", 0);
		System.out.println("Solution semi-heuristique à partir de la 1ère ville: " + evaluateDistances(semiHeuristicFromFirstCity,matrixInHashMap));
		System.out.println(semiHeuristicFromFirstCity);

		System.out.println("========================================================================");
		System.out.println("=======================TP 3 : algorithme genetique======================");
		System.out.println("========================================================================");

		ArrayList<ArrayList<Integer>> worldPopulation = initialisationPopulation(matrixInHashMap, 10, "mouvement", "heuristic", 0);
		for (int i = 0; i < worldPopulation.size(); i++) {
			System.out.println(evaluateDistances(worldPopulation.get(i),matrixInHashMap) + " : " + worldPopulation.get(i));
		}


	}
}