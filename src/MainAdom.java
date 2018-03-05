package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author guilbertf
 * @author clemence
 */
public class MainAdom {
	static HashMap<Integer, ArrayList<Integer>> matrixInHashMap;
	static HashMap<Integer, ArrayList<Integer>> matrixInHashMapB;

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
	 * Fonction permettant de stocker nos résultats, elle sera uniquement utilisée pour la partie "SingleCritere"
	 * @param originFile
	 * @param name
	 * @param num
	 * @param city
	 */
	private static void putInFileSingleCritere (String originFile,String name, int num, ArrayList<Integer> city){
		try{
			String path=new File("").getAbsolutePath();
			File ff=new File(path+"/resources/"+name+"/"+originFile+"_distance_"+num+".txt"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			ffw.write(evaluateDistances(city, matrixInHashMap)+"");  // écrire une ligne dans le fichier name/num.txt
			ffw.close(); // fermer le fichier à la fin des traitements

			ff=new File(path+"/resources/"+name+"/"+originFile+"_listCities_"+num+".txt"); // définir l'arborescence
			ff.createNewFile();
			ffw=new FileWriter(ff);
			ffw.write(city+"");
			ffw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {

		}
	}

	/**
	 * Fonction permettant de stocker nos résultats, elle sera uniquement utilisée pour la partie "SingleCritere"
	 * @param originFile
	 * @param name
	 * @param num
	 * @param city
	 */
	private static void putInCSVFileDistance (String originFile,String name, ArrayList<Integer> listDistance){
		try{
			String path=new File("").getAbsolutePath();
			File ff=new File(path+"/resources/"+name+"/"+originFile+"_distance.csv"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			for (int i = 0; i < listDistance.size(); i++) {
				ffw.write(listDistance.get(i)+"\n");  // écrire une ligne dans le fichier name/num.csv
			}
			ffw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {

		}
	}
	
	/**
	 * Fonction permettant de stocker nos résultats, elle sera uniquement utilisée pour la partie "SingleCritere"
	 * @param originFile
	 * @param name
	 * @param num
	 * @param city
	 */
	private static void putInCSVFileListCities (String originFile,String name, ArrayList<ArrayList<Integer>> listCities){
		try{
			String path=new File("").getAbsolutePath();
			File ff=new File(path+"/resources/"+name+"/"+originFile+"_listCities.csv"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			for (int i = 0; i < listCities.size(); i++) {
				ffw.write(listCities.get(i)+"\n");  // écrire une ligne dans le fichier name/num.csv
			}
			ffw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {

		}
	}
	
	/**
	 * Fonction permettant de stocker nos résultats, elle sera uniquement utilisée pour la partie "Order Based Cross-Over"
	 * @param originFile
	 * @param name
	 * @param num
	 * @param city
	 */
	private static void putInFileOrderBasedCrossover (String originFile,String name,int num, ArrayList<ArrayList<Integer>> worldPopulation){
		try{
			String path=new File("").getAbsolutePath();
			File ff=new File(path+"/resources/"+name+"/"+originFile+"_distance_"+num+".txt"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			for (int i = 0; i < worldPopulation.size(); i++) {
				ffw.write(evaluateDistances(worldPopulation.get(i), matrixInHashMap)+"");  // écrire une ligne dans le fichier name/num.txt
				if(i!=worldPopulation.size()-1) {
					ffw.write("\n"); // retour à la ligne
				}
			}
			ffw.close(); // fermer le fichier à la fin des traitements

			ff=new File(path+"/resources/"+name+"/"+originFile+"_listCities_"+num+".txt"); // définir l'arborescence
			ff.createNewFile();
			ffw=new FileWriter(ff);
			for (int i = 0; i < worldPopulation.size(); i++) {
				ffw.write(worldPopulation.get(i)+"");  // écrire une ligne dans le fichier name/num.txt
				if(i!=worldPopulation.size()-1) {
					ffw.write("\n"); // retour à la ligne
				}
			}
			ffw.close(); // fermer le fichier à la fin des traitements
		} catch (Exception e) {

		}
	}

	/**
	 * Génération de la matrice par une HashMap : (ville, [distance entre cette ville et les autres])
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

	/**
	 * Permet de visualiser notre population mondiale
	 * @param worldPopulation
	 * @param matrixInHashMap
	 */
	private static void seeWorldPopulation(ArrayList<ArrayList<Integer>> worldPopulation,HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		System.out.println("========================================================================");
		System.out.println("=======================    Population mondiale    ======================");
		for (int i = 0; i < worldPopulation.size(); i++) {
			System.out.println(evaluateDistances(worldPopulation.get(i),matrixInHashMap) + " : " + worldPopulation.get(i));
		}
		System.out.println("========================================================================");
	}

	/**
	 * Initialisation de plusieurs ensemble de 100 villes avec des ordres différents
	 * @param matrixInHashMap
	 * @param nbPop
	 * @param optionsInit
	 * @param optionsMouv
	 * @param starter
	 * @return
	 */
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

	/**
	 * Renvoie une liste aléatoire entre 0 et 99
	 * @return
	 */
	private static ArrayList<Integer> randomList(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> availableCities = new ArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			availableCities.add(i);
		}
		int r =0;
		while(availableCities.size()!=0) {
			r = (int) (Math.random()* (availableCities.size()-1));
			result.add(availableCities.get(r));
			availableCities.remove(r);
		}
		return result;
	}

	/**
	 * Suite à l'initialisation, précision entre semi-heuristique et heuristique
	 * @param matrixInHashMap
	 * @param options
	 * @param starter
	 * @return
	 */
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

	/**
	 * Resultat heuristique à partir d'une ville de départ (starter)
	 * @param matrixInHashMap
	 * @param starter
	 * @return
	 */
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

	/**
	 * Resultat semi-heuristique à partir d'une ville de départ (starter)
	 * @param matrixInHashMap
	 * @param starter
	 * @return
	 */
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

	/**
	 * Fonction "test" qui calcule selon tous les starters possibles l'heuristique associé
	 * @param matrixInHashMap
	 * @return
	 */
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
			listCities = swap(listCities, a, b);
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

	/**
	 * Parmi la population, sélectionner 2 parents (ceux avec les meilleurs résultats)
	 * Se servir du 1er parent en sélectionnant un segment entre 2 villes choisies aléatoirement
	 * Compléter ce segment avec les villes du 2ème parent
	 * Cela crée notre enfant
	 * @param worldPopulation
	 * @param matrixInHashMap
	 * @return newWorldPopulation
	 */
	private static ArrayList<ArrayList<Integer>> orderBasedCrossover(ArrayList<ArrayList<Integer>> worldPopulation,HashMap<Integer, ArrayList<Integer>> matrixInHashMap){
		ArrayList<ArrayList<Integer>> newWorldPopulation = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> distances = new ArrayList<Integer>();
		for (int i = 0; i < worldPopulation.size(); i++) {
			newWorldPopulation.add(worldPopulation.get(i));
			distances.add(evaluateDistances(newWorldPopulation.get(i),matrixInHashMap));
		}

		int minDistance1=distances.get(0);
		for (Integer distance : distances) {
			if(distance<minDistance1) {
				minDistance1=distance;
			}
		}
		int minDistance2=distances.get(0);
		for (Integer distance : distances) {
			if(distance<minDistance2 && distance!=minDistance1) {
				minDistance2=distance;
			}
		}
		ArrayList<Integer> parent1 = newWorldPopulation.get(distances.indexOf(minDistance1));
		ArrayList<Integer> parent2 = newWorldPopulation.get(distances.indexOf(minDistance2));

		seeWorldPopulation(newWorldPopulation, matrixInHashMap);

		int frag1 = (int) (Math.random()*100-1);
		int frag2 = (int) (Math.random()*100-1);
		while(frag1==frag2) {
			frag2 = (int) (Math.random()*100-1);
		}
		int fragTmp = 0;
		if(parent1.indexOf(frag1)>parent1.indexOf(frag2)) {
			fragTmp = frag1;
			frag1 = frag2;
			frag2 = fragTmp;
		}

		ArrayList<Integer> segmentParent1 = new ArrayList<Integer>();
		for (int i = parent1.indexOf(frag1); i <= parent1.indexOf(frag2); i++) {
			segmentParent1.add(parent1.get(i));
		}

		//System.out.println(frag1 + " : " + frag2);
		//System.out.println(parent1);
		//System.out.println(segmentParent1);

		//System.out.println("parent 2 : " + parent2);
		fragTmp = 0;
		boolean reversed = false;
		if(parent2.indexOf(frag1)>parent2.indexOf(frag2)) {
			fragTmp = frag1;
			frag1 = frag2;
			frag2 = fragTmp;
			reversed = true;
		}

		ArrayList<Integer> alreadyTakenCities = new ArrayList<Integer>();
		for (int i = 0; i < segmentParent1.size(); i++) {
			if(!alreadyTakenCities.contains(segmentParent1.get(i))) {
				alreadyTakenCities.add(segmentParent1.get(i));
			}
		}

		ArrayList<Integer> segmentDebut = new ArrayList<Integer>();
		ArrayList<Integer> segmentFin = new ArrayList<Integer>();

		if(parent2.indexOf(frag1)!=0) {
			for (int i = 0; i < parent2.indexOf(frag1); i++) {
				if(!alreadyTakenCities.contains(parent2.get(i))) {
					segmentDebut.add(parent2.get(i));
					alreadyTakenCities.add(parent2.get(i));
				}
			}
		}

		if(parent2.indexOf(frag2)!=parent2.size()-1) {
			for (int i = parent2.indexOf(frag2)+1; i < parent2.size(); i++) {
				if(!alreadyTakenCities.contains(parent2.get(i))) {
					segmentFin.add(parent2.get(i));
					alreadyTakenCities.add(parent2.get(i));
				}
			}
		}
		//System.out.println(segmentDebut);
		//System.out.println(segmentFin);

		ArrayList<Integer> availableCities = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			if(!alreadyTakenCities.contains(i)) {
				availableCities.add(i);
			}
		}
		//System.out.println("available Cities : " + availableCities);
		ArrayList<Integer> finalListCities = new ArrayList<Integer>();
		int min = 0;
		int starter = 0;
		if(availableCities.size()!=0) {
			starter = availableCities.get(0);
			while(availableCities.size()>0) {
				min = 1000000000;
				finalListCities.add(starter);
				availableCities.remove(availableCities.indexOf(starter));
				ArrayList<Integer> actualCities = matrixInHashMap.get(starter);

				for (int i = 0; i < actualCities.size(); i++) {
					if(!finalListCities.contains(i) && availableCities.contains(i)) {
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
		}
		ArrayList<Integer> prodigalChild = new ArrayList<Integer>();
		if(!reversed) {
			for (int i = 0; i < segmentDebut.size(); i++) {
				prodigalChild.add(segmentDebut.get(i));
			}
			for (int i = 0; i < segmentParent1.size(); i++) {
				prodigalChild.add(segmentParent1.get(i));
			}
			for (int i = 0; i < segmentFin.size(); i++) {
				prodigalChild.add(segmentFin.get(i));
			}
		} else {
			if(segmentDebut.size()!=0) {
				segmentDebut = two_opt(segmentDebut, segmentDebut.get(0), segmentDebut.get(segmentDebut.size()-1));
			}
			if(segmentFin.size()!=0) {
				segmentFin = two_opt(segmentFin, segmentFin.get(0), segmentFin.get(segmentFin.size()-1));
			}
			for (int i = 0; i < segmentFin.size(); i++) {
				prodigalChild.add(segmentFin.get(i));
			}
			for (int i = 0; i < segmentParent1.size(); i++) {
				prodigalChild.add(segmentParent1.get(i));
			}
			for (int i = 0; i < segmentDebut.size(); i++) {
				prodigalChild.add(segmentDebut.get(i));
			}
		}
		for (int i = 0; i < finalListCities.size(); i++) {
			prodigalChild.add(finalListCities.get(i));
		}

		//System.out.println(distances);
		int maxDistance = 0;
		for (Integer distance : distances) {
			if(maxDistance<distance) {
				maxDistance=distance;
			}
		}
		//System.out.println(evaluateDistances(newWorldPopulation.get(distances.indexOf(maxDistance)), matrixInHashMap) + " (maxdistance)");
		if(evaluateDistances(prodigalChild, matrixInHashMap)<evaluateDistances(newWorldPopulation.get(distances.indexOf(maxDistance)), matrixInHashMap)) {
			if(!newWorldPopulation.contains(prodigalChild))
				newWorldPopulation.set(distances.indexOf(maxDistance), prodigalChild);
		}
		System.out.println(evaluateDistances(prodigalChild, matrixInHashMap) + " : " + prodigalChild);
		seeWorldPopulation(newWorldPopulation, matrixInHashMap);

		return newWorldPopulation;
	}

	/**
	 * Mutation selon un taux donné
	 * @param actualChild
	 * @param mutationRate
	 * @return
	 */
	private static ArrayList<Integer> mutation(ArrayList<Integer> actualChild, int mutationRate) {
		int city1 = 0, city2 = 0;
		int rate = actualChild.size()/mutationRate;
		ArrayList<Integer> resultChild = new ArrayList<Integer>();
		for (int i = 0; i < actualChild.size(); i++) {
			resultChild.add(actualChild.get(i));
		}
		for (int i = 0; i < rate; i++) {
			city1 =(int) (Math.random()* (100-1));
			do {
				city2 =(int) (Math.random()* (100-1));
			}while(city1==city2);
			resultChild = voisinage(resultChild, city1, city2,"swap");
		}
		return resultChild;
	}

	/**
	 * Calcul des distances entre les points des différentes villes
	 * @param order
	 * @param matrixInHashMap
	 * @return
	 */
	private static int evaluateDistances(ArrayList<Integer> order, HashMap<Integer, ArrayList<Integer>> matrixInHashMap) {
		int somme = 0;
		int starter = order.get(0);
		int last = order.get(order.size()-1);

		for (int i = 0; i < order.size()-1; i++) {
			somme = somme + matrixInHashMap.get(order.get(i)).get(order.get(i+1));
		}

		// bouclement du circuit
		somme = somme + matrixInHashMap.get(starter).get(last);

		return somme;
	}

	/**
	 * Cas de tests pour des critères simples
	 */
	private static void singleCritere() {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		ArrayList<Data> datas_kroB100 = parseFile("kroB100.tsp");
		matrixInHashMap = generateMatrixInHashMap(datas_kroA100);
		matrixInHashMapB = generateMatrixInHashMap(datas_kroB100);

		//visualizeMatrixInHashMap(matrixInHashMap);

		/*
		ArrayList<Integer> heuristicFromFirstCity = initialisation(matrixInHashMap, "mouvement", "heuristic", 0);
		System.out.println("Solution à partir de la 1ère ville: " + evaluateDistances(heuristicFromFirstCity,matrixInHashMap));
		System.out.println(heuristicFromFirstCity);
		putInFileSingleCritere("kroA","HeuristicFromFirstCity", 1, heuristicFromFirstCity);
		System.out.println("========================================================================");
*/
		/*
		ArrayList<Integer> resultatDistances = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> resultatListCities = new ArrayList<ArrayList<Integer>>();

		for (int i = 1; i < 31; i++) {
			ArrayList<Integer> voisinageSwapBetweenFirstAndSecondCity = voisinage(initialisation(matrixInHashMap, "mouvement", "heuristic", 0), 0, i,"swap");
			System.out.println("Swap entre la 1ère ville et la "+(i+1)+"ème : " + evaluateDistances(voisinageSwapBetweenFirstAndSecondCity,matrixInHashMap));
			System.out.println(voisinageSwapBetweenFirstAndSecondCity);
			resultatDistances.add(evaluateDistances(voisinageSwapBetweenFirstAndSecondCity,matrixInHashMap));
			resultatListCities.add(voisinageSwapBetweenFirstAndSecondCity);
		}
		putInCSVFileDistance("kroA","Voisinage/Swap", resultatDistances);
		putInCSVFileListCities("kroA","Voisinage/Swap", resultatListCities);
		
*//*
		System.out.println("========================================================================");

		ArrayList<Integer> resultatDistances = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> resultatListCities = new ArrayList<ArrayList<Integer>>();
		for (int i = 1; i < 31; i++) {
			ArrayList<Integer> voisinageTwoOptBetweenFirstAndSecondCity = voisinage(initialisation(matrixInHashMapB, "mouvement", "heuristic", 0), 0, i,"two-opt");
			System.out.println("Two-opt entre la 1ère ville et la "+(i+1)+"ème : " + evaluateDistances(voisinageTwoOptBetweenFirstAndSecondCity,matrixInHashMapB));
			System.out.println(voisinageTwoOptBetweenFirstAndSecondCity);

			resultatDistances.add(evaluateDistances(voisinageTwoOptBetweenFirstAndSecondCity,matrixInHashMapB));
			resultatListCities.add(voisinageTwoOptBetweenFirstAndSecondCity);
		}
		putInCSVFileDistance("kroB","Voisinage/Two-opt", resultatDistances);
		putInCSVFileListCities("kroB","Voisinage/Two-opt", resultatListCities);

		System.out.println("========================================================================");
		 
*/
		ArrayList<Integer> resultatDistances = new ArrayList<Integer>();
		for (int j = 0; j < 30; j++) {
			ArrayList<Integer> heuristic = initialisation(matrixInHashMapB, "mouvement", "heuristic", j);
			System.out.println("Solution à partir de la "+j+"ème ville: " + evaluateDistances(heuristic,matrixInHashMapB));
			System.out.println(heuristic);
			resultatDistances.add(evaluateDistances(heuristic,matrixInHashMapB));
			//putInFileSingleCritere("kroA","Heuristic", j, heuristic);
			System.out.println("========================================================================");
		}
		putInCSVFileDistance("kroB","Heuristic", resultatDistances);

		System.out.println("Best heuristic : " + bestHeuristic(matrixInHashMapB));

		System.out.println("========================================================================");
		 
		/*
		ArrayList<Integer> random = new ArrayList<Integer>();
		for (int i = 1; i < 31; i++) {
			ArrayList<Integer> list = initialisation(matrixInHashMapB, "random","", 0);
			random.add(evaluateDistances(list, matrixInHashMapB));

		}
		putInCSVFileDistance("kroB","Random", random);

		System.out.println("========================================================================");
		*/

		/*
		for (int j = 1; j < 31; j++) {
			ArrayList<Integer> random2 = initialisation(matrixInHashMap, "random","", 0);
			ArrayList<Integer> random2Tmp = new ArrayList<Integer>();
			for (int i = 0; i < random2.size(); i++) {
				random2Tmp.add(random2.get(i));
			}
			putInFileSingleCritere("kroA","Mutation", j, random2);
			System.out.println("Random list 2 : " + evaluateDistances(random2, matrixInHashMap));
			System.out.println(random2);
			random2 = mutation(random2, 10);
			putInFileSingleCritere("kroA","Mutation/10", j, random2);
			System.out.println("Random list 2  (après la mutation, 10%): " + evaluateDistances(random2, matrixInHashMap));
			System.out.println(random2);
			random2Tmp = mutation(random2Tmp, 30);
			putInFileSingleCritere("kroA","Mutation/30", j, random2Tmp);
			System.out.println("Random list 2  (après la mutation, 30%): " + evaluateDistances(random2Tmp, matrixInHashMap));
			System.out.println(random2Tmp);
		}

		System.out.println("========================================================================");
		 */

		
		ArrayList<Integer> semiHeuristicFromFirstCity = initialisation(matrixInHashMap, "mouvement", "semi-heuristic", 0);
		System.out.println("Solution semi-heuristique à partir de la 1ère ville: " + evaluateDistances(semiHeuristicFromFirstCity,matrixInHashMap));
		System.out.println(semiHeuristicFromFirstCity);
		/*
		System.out.println("========================================================================");
		System.out.println("=======================TP 3 : algorithme genetique======================");
		System.out.println("========================================================================");

		ArrayList<ArrayList<Integer>> worldPopulation = initialisationPopulation(matrixInHashMap, 10, "mouvement", "heuristic", 0);

		for (int i = 0; i < 10; i++) {
			worldPopulation = orderBasedCrossover(worldPopulation, matrixInHashMap);
			putInFileOrderBasedCrossover("kroA", "OrderBased", i, worldPopulation);
		}*/
	}

	/**
	 * Coût pondéré entre A et B 
	 * @param list
	 * @param matrixA
	 * @param matrixB
	 * @return
	 */
	private static int qualityOfPermutation(ArrayList<Integer> list,HashMap<Integer, ArrayList<Integer>> matrixA,HashMap<Integer, ArrayList<Integer>> matrixB) {
		int costA = evaluateDistances(list, matrixA);
		int costB = evaluateDistances(list, matrixB);
		return (costA+costB)/2;
	}

	/**
	 * @param population
	 * @param options : offline OR online
	 * @return
	 */
	private static ArrayList<MultiData> filterNonDominated(ArrayList<MultiData> population,String options){
		ArrayList<MultiData> non_dominated = new ArrayList<MultiData>();
		switch (options) {
		case "offline":
			non_dominated = offline(population);
			break;
		case "online":
			for (int i = 0; i < population.size(); i++) {
				non_dominated = online(population.get(i), non_dominated);
			}
			break;
		default:
			break;
		}
		System.out.println("Liste de non-dominés ("+non_dominated.size()+")");
		for (int j = 0; j < non_dominated.size(); j++) {
			System.out.println(non_dominated.get(j));
		}
		return non_dominated;
	}

	/**
	 * Offline car on prend toute la population d'un coup
	 * @param population
	 * @return
	 */
	private static ArrayList<MultiData> offline(ArrayList<MultiData> population){
		ArrayList<MultiData> non_dominated = new ArrayList<MultiData>();
		ArrayList<MultiData> toBeDeleted = new ArrayList<MultiData>();
		ArrayList<MultiData> toBeAdded = new ArrayList<MultiData>();
		boolean alreadyDominated = false;
		for (int i = 0; i < population.size(); i++) {
			if(non_dominated.isEmpty()) {
				non_dominated.add(population.get(i));
			} else {
				for (MultiData nd : non_dominated) {
					if(!alreadyDominated)
						if(!toBeDeleted.contains(population.get(i)) && !non_dominated.contains(population.get(i)) && population.get(i).getCostA()<nd.getCostA() && population.get(i).getCostB()<nd.getCostB()) {
							toBeAdded.add(population.get(i));
							toBeDeleted.add(nd);
							//System.out.println(i +" : archi dominant");
							//System.out.println(population.get(i) + " sur " + nd);
						} else if(!toBeDeleted.contains(population.get(i)) && !non_dominated.contains(population.get(i)) && population.get(i).getCostA()>nd.getCostA() && population.get(i).getCostB()>nd.getCostB()) {
							alreadyDominated = true;
							if(toBeAdded.contains(population.get(i))){
								toBeAdded.remove(population.get(i));
							}
							//System.out.println(i +" : archi dominé");
						}else if(!toBeAdded.contains(population.get(i)) && !non_dominated.contains(population.get(i)) && population.get(i).getCostA()<nd.getCostA()) {
							toBeAdded.add(population.get(i));
							//System.out.println(i +" : dominant A");
						} else if(!toBeAdded.contains(population.get(i)) && !non_dominated.contains(population.get(i)) && population.get(i).getCostB()<nd.getCostB()) {
							toBeAdded.add(population.get(i));
							//System.out.println(i +" : dominant B");
						}
				}
				int sizeToBeAdded = toBeAdded.size();
				for (int j = 0; j < sizeToBeAdded; j++) {
					//System.out.println("pret a etre ajouter");
					if(!non_dominated.contains(toBeAdded.get(j)))
						non_dominated.add(toBeAdded.get(j));
				}
				toBeAdded.clear();
				int sizeToBeDeleted = toBeDeleted.size();
				for (int j = 0; j < sizeToBeDeleted; j++) {
					non_dominated.remove(toBeDeleted.get(j));
				}
				toBeDeleted.clear();
				alreadyDominated = false;
			}
		}
		return non_dominated;
	}

	/**
	 * 
	 * @param md
	 * @param non_dominated
	 * @return
	 */
	private static ArrayList<MultiData> online(MultiData md, ArrayList<MultiData> non_dominated){
		ArrayList<MultiData> toBeDeleted = new ArrayList<MultiData>();
		ArrayList<MultiData> toBeAdded = new ArrayList<MultiData>();
		boolean alreadyDominated = false;

		if(non_dominated.isEmpty()) {
			non_dominated.add(md);
		} else {
			for (MultiData nd : non_dominated) {
				if(!alreadyDominated)
					if(!toBeDeleted.contains(md) && !non_dominated.contains(md) && md.getCostA()<nd.getCostA() && md.getCostB()<nd.getCostB()) {
						toBeAdded.add(md);
						toBeDeleted.add(nd);
						//System.out.println(i +" : archi dominant");
						//System.out.println(population.get(i) + " sur " + nd);
					} else if(!toBeDeleted.contains(md) && !non_dominated.contains(md) && md.getCostA()>nd.getCostA() && md.getCostB()>nd.getCostB()) {
						alreadyDominated = true;
						if(toBeAdded.contains(md)){
							toBeAdded.remove(md);
						}
						//System.out.println(i +" : archi dominé");
					}else if(!toBeAdded.contains(md) && !non_dominated.contains(md) && md.getCostA()<nd.getCostA()) {
						toBeAdded.add(md);
						//System.out.println(i +" : dominant A");
					} else if(!toBeAdded.contains(md) && !non_dominated.contains(md) && md.getCostB()<nd.getCostB()) {
						toBeAdded.add(md);
						//System.out.println(i +" : dominant B");
					}
			}
			int sizeToBeAdded = toBeAdded.size();
			for (int j = 0; j < sizeToBeAdded; j++) {
				//System.out.println("pret a etre ajouter");
				if(!non_dominated.contains(toBeAdded.get(j)))
					non_dominated.add(toBeAdded.get(j));
			}
			toBeAdded.clear();
			int sizeToBeDeleted = toBeDeleted.size();
			for (int j = 0; j < sizeToBeDeleted; j++) {
				non_dominated.remove(toBeDeleted.get(j));
			}
			toBeDeleted.clear();
			alreadyDominated = false;
		}
		return non_dominated;
	}

	/**
	 * Cas de tests pour des critères multiples
	 */
	private static void multiCritere() {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		ArrayList<Data> datas_kroB100 = parseFile("kroB100.tsp");
		HashMap<Integer, ArrayList<Integer>> matrixA = generateMatrixInHashMap(datas_kroA100);
		HashMap<Integer, ArrayList<Integer>> matrixB = generateMatrixInHashMap(datas_kroB100);

		ArrayList<MultiData> randomPopulation = new ArrayList<MultiData>();
		for (int i = 0; i < 500; i++) {
			MultiData tmp = new MultiData(randomList());
			tmp.setCostA(evaluateDistances(tmp.getCities(), matrixA));
			tmp.setCostB(evaluateDistances(tmp.getCities(), matrixB));
			randomPopulation.add(tmp);
		}
		for (int i = 0; i < randomPopulation.size(); i++) {
			randomPopulation.get(i).setCostPonderate(qualityOfPermutation(randomPopulation.get(i).getCities(), matrixA, matrixB));
			System.out.println("Coût pondéré entre A et B  (iteration "+i+") : "+ randomPopulation.get(i).costPonderate +" (avec A :" + randomPopulation.get(i).getCostA()+" et B :" + randomPopulation.get(i).getCostB()+")");
		}

		System.out.println("offline");
		filterNonDominated(randomPopulation, "offline");
		System.out.println("online");
		filterNonDominated(randomPopulation, "online");
	}

	public static void main(String [] args) {
		singleCritere();
		//multiCritere();
	}
}