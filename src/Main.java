package src;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
	 * Genere la matrice de cout selon la distance euclidienne entre 2 points
	 * @param datas
	 * @param options
	 * @return
	 */
	private static MatrixData [][] generateMatrix(ArrayList<Data> datas, String options) {
		MatrixData [][] matrix = new MatrixData [datas.size()][datas.size()];

		int xa,xb,ya,yb;
		for (int i = 0; i < matrix.length; i++) {
			xa = datas.get(i).getCoord_X();
			ya = datas.get(i).getCoord_Y();
			for (int j = 0; j < matrix.length; j++) {
				xb = datas.get(j).getCoord_X();
				yb = datas.get(j).getCoord_Y();

				// distance euclidienne entre 2 points :
				// -------------------------------------
				// racine((xA-xB)²+(yA-yB)²
				//______________________________________
				if(i<j) {
					matrix[i][j]= new MatrixData(Math.round(Math.sqrt(Math.pow((xa-xb), 2)+Math.pow((ya-yb), 2))));
					// pour le random
					if(options.equals("random")) {
						matrix[j][i]= new MatrixData(Math.round(Math.sqrt(Math.pow((xa-xb), 2)+Math.pow((ya-yb), 2))));
					}
				}
			}
		}
		return matrix;
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

	private static int evaluateRandomSolution(MatrixData [][] matrix,ArrayList<Integer> cities) {
		int resultat = 0;
		for (int i = 0; i < cities.size(); i++) {
			if(i == cities.size()-1) {
				resultat = (int) (resultat + matrix[cities.get(0)-1][cities.get(i)-1].getDistance());
			} else {
				resultat = (int) (resultat + matrix[cities.get(i)-1][cities.get(i+1)-1].getDistance());
			}
		}
		return resultat;
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

	private static int evaluateHeuristicSolution(MatrixData [][] matrix,int x, int y){
		int somme = 0;
		int newX=0;
		int newY=0;
		boolean allCitiesChecked = false;
		double min = 10000000;
		// partie verticale de la croix
		for (int i = 0; i < matrix.length; i++) {
			if(matrix[i][y]!=null) {
				if(matrix[i][y].getDistance()<min && !matrix[i][y].isAncestor()) {
					min = matrix[i][y].getDistance();
					System.out.println("vertical : " + min);
					allCitiesChecked = false;
					newX=i;
					newY=y;
				} else if(matrix[i][y].isAncestor()&& min!=10000000){
					allCitiesChecked = true;
				}
			}
		}
		// partie horizontale de la croix
		for (int j = 0; j < matrix.length; j++) {
			if(matrix[x][j]!=null) {
				if(matrix[x][j].getDistance()<min && !matrix[x][j].isAncestor()) {
					min = matrix[x][j].getDistance();
					System.out.println("horizontal : " + min);
					allCitiesChecked = false;
					newX=x;
					newY=j;
				} else if(matrix[x][j].isAncestor()&& min!=10000000){
					allCitiesChecked = true;
				}
			}
		}

		somme = (int) min;
		matrix[newX][newY].setAncestor(true);
		System.out.println("min final : " + min);
		if(!allCitiesChecked) {
			return somme + evaluateHeuristicSolution(matrix, newX, newY);
		} else {
			return somme;
		}
	}

	private static int evaluateHeuristicSolutionTaille(MatrixData [][] matrix,int x, int y, int TAILLE){
		int somme = 0;
		int newX=0;
		int newY=0;
		boolean allCitiesChecked = false;
		double min = 10000000;
		
		visualizeMatrix(matrix,TAILLE);
		
		// partie verticale de la croix
		for (int i = 0; i < TAILLE; i++) {
			if(matrix[i][y]!=null) {
				if(matrix[i][y].getDistance()<min && !matrix[i][y].isAncestor()) {
					min = matrix[i][y].getDistance();
					System.out.println("vertical : " + min);
					allCitiesChecked = false;
					newX=i;
					newY=y;
				} else if(matrix[i][y].isAncestor()&& min!=10000000){
					allCitiesChecked = true;
				}
			}
		}
		// partie horizontale de la croix
		for (int j = 0; j < TAILLE; j++) {
			if(matrix[x][j]!=null) {
				if(matrix[x][j].getDistance()<min && !matrix[x][j].isAncestor()) {
					min = matrix[x][j].getDistance();
					System.out.println("horizontal : " + min);
					allCitiesChecked = false;
					newX=x;
					newY=j;
				} else if(matrix[x][j].isAncestor()&& min!=10000000){
					allCitiesChecked = true;
				}
			}
		}

		somme = (int) min;
		matrix[newX][newY].setAncestor(true);
		System.out.println("min final : " + min);
		if(!allCitiesChecked) {
			return somme + evaluateHeuristicSolution(matrix, newX, newY);
		} else {
			return somme;
		}
	}
	
	public static void main(String [] args) {
		ArrayList<Data> datas_kroA100 = parseFile("kroA100.tsp");
		MatrixData [][] matrixARandom = generateMatrix(datas_kroA100,"random");
		MatrixData [][] matrixAHeuristic = generateMatrix(datas_kroA100,"heuristic");
		//visualizeMatrix(matrixARandom);
		ArrayList<Integer> cities = randomCities(datas_kroA100.size());
		// Meilleure solution connue pour kroA100 : 21282
		System.out.println("Cout de la solution par random : "+ evaluateRandomSolution(matrixARandom, cities));
		//visualizeMatrix(matrixAHeuristic);
		System.out.println("Cout de la solution heuristique : "+ evaluateHeuristicSolutionTaille(matrixAHeuristic,0,0,10));
	}

}
