package org.opencompare;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.opencompare.api.java.Value;

public class Statistiques5Test {
	
	@Test
	public ArrayList<ArrayList<Double>> matriceDistances() throws IOException{
		
		// importer les données
		//TODO
		
		
		// variable du nombre de lignes du fichier
		//TODO
		int nbLignes=1; 
		
		// on crée un tableau listant le type de toutes les variables "quali" ou "quanti"
		//TODO
		ArrayList<String> types = new ArrayList<String>();
		
		// on crée un tableau contenant les minimums des variables quanti, 0 si quali
		//TODO
		ArrayList<Double> minimums = new ArrayList<Double>();
		
		// on crée un tableau contenant les maximums des variables quanti, 0 si quali
		//TODO
		ArrayList<Double> maximums = new ArrayList<Double>();
				
		// stocker dans un arraylist(arraylist) les distances entre chaque couple d'individus
		ArrayList<ArrayList<Double>> distances= new ArrayList<ArrayList<Double>>();
		
		// on remplit la matrice des distances pour chaque couple de lignes
		for(int i=0;i<nbLignes;i++){
			ArrayList<Double> ligne = new ArrayList<Double>();
			// on compare la ligne i aux autres lignes j
			for(int j=0;j<nbLignes;j++){
				ArrayList<Value> valeurs1 = new ArrayList<Value>();
				ArrayList<Value> valeurs2 = new ArrayList<Value>();
				// Attribuer les vecteurs de valeurs
				//TODO
				ligne.add(distanceIndividus(valeurs1,valeurs2,types,minimums,maximums));
			}
			distances.add(ligne);
		}
		return distances;
	}
	
	
	
	// fonction de calcul de la distance entre 2 individus
	//	pour chaque variable, on regarde le type et on calcule la distance correspondante
	public double distanceIndividus(ArrayList<Value> valeurs1, ArrayList<Value> valeurs2, ArrayList<String> types, ArrayList<Double> minimums, ArrayList<Double> maximums) throws IOException {
		double distance=0;
		for(int i=0;i<valeurs1.size();i++){
			if(types.get(i)=="quali"){
				distance+=distanceQuali(String.valueOf(valeurs1.get(i)),String.valueOf(valeurs2.get(i)));
			}
			else{
				distance+=distanceQuanti(Double.valueOf(String.valueOf(valeurs1.get(i))),Double.valueOf(String.valueOf(valeurs2.get(i))),minimums.get(i),maximums.get(i));
			}
		}
		return distance;
	}
	
	// calcul des distances entre 2 valeurs quanti (comprises entre 0 et 1.5)
	public double distanceQuanti(double x1, double x2, double max, double min) throws IOException {
		double x=(max-min)/1.5;
		return Math.sqrt(Math.pow(x1-x2,2))/x;
	}
	
	// calcul des distances entre 2 valeurs quali (0 ou 1)
	public double distanceQuali(String x1, String x2) throws IOException {
		if(x1==x2){
			return 0;
		}
		else{
			return 1;
		}
	}
}
