package org.opencompare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opencompare.api.java.Value;

public class Statistiques5Test {

	@Test
	public void matriceDistances() throws IOException{

		// variable du nombre de lignes du fichier
		//TODO
		int nbLignes=3; 

		// on crée un tableau listant le type de toutes les variables "quali" ou "quanti"
		//TODO
		ArrayList<String> types = new ArrayList<String>();
		types.add("quali");
		types.add("quanti");

		// on crée un tableau contenant les minimums des variables quanti, 0 si quali
		//TODO
		ArrayList<Double> minimums = new ArrayList<Double>();
		minimums.add((double)0);
		minimums.add((double)1);

		// on crée un tableau contenant les maximums des variables quanti, 0 si quali
		//TODO
		ArrayList<Double> maximums = new ArrayList<Double>();
		minimums.add((double)0);
		minimums.add((double)3);

		// importer les données
		//TODO
		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		ArrayList row = new ArrayList();
		row.add("a");
		row.add(1);
		data.add(row);
		row = new ArrayList();
		row.add("a");
		row.add(2);
		data.add(row);
		row = new ArrayList();
		row.add("b");
		row.add(3);
		data.add(row);
		
		// stocker dans un arraylist(arraylist) les distances entre chaque couple d'individus
		ArrayList<ArrayList<Double>> distances= new ArrayList<ArrayList<Double>>();

		// on remplit la matrice des distances pour chaque couple de lignes
		for(int i=0;i<nbLignes;i++){
			ArrayList<Double> ligne = new ArrayList<Double>();
			// on compare la ligne i aux autres lignes j
			for(int j=0;j<nbLignes;j++){
				// Attribuer les vecteurs de valeurs
				//TODO
				ArrayList<Value> valeurs1 = new ArrayList<Value>();
				ArrayList<Value> valeurs2 = new ArrayList<Value>();
				valeurs1=data.get(i);
				valeurs2=data.get(j);
				System.out.println(distanceIndividus(valeurs1,valeurs2,types,minimums,maximums));
				ligne.add(distanceIndividus(valeurs1,valeurs2,types,minimums,maximums));
			}
			distances.add(ligne);
		}
		
	}



	// fonction de calcul de la distance entre 2 individus
	//	pour chaque variable, on regarde le type et on calcule la distance correspondante
	public double distanceIndividus(ArrayList<Value> valeurs1, ArrayList<Value> valeurs2, ArrayList<String> types, ArrayList<Double> minimums, ArrayList<Double> maximums) throws IOException {
		double distance=0;
		for(int i=0;i<valeurs1.size();i++){
			if(types.get(i)=="quali"){
				distance+=distanceQuali(String.valueOf(valeurs1.get(i)),String.valueOf(valeurs2.get(i)));
				System.out.println(distance);
			}
			else{
				System.out.println(Double.valueOf(String.valueOf(valeurs1.get(i))));
				System.out.println(Double.valueOf(String.valueOf(valeurs2.get(i))));
				System.out.println(minimums.get(i));
				//erreur ici
				distance+=distanceQuanti(Double.valueOf(String.valueOf(valeurs1.get(i))),Double.valueOf(String.valueOf(valeurs2.get(i))),minimums.get(i),maximums.get(i));
				System.out.println(distance);
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
