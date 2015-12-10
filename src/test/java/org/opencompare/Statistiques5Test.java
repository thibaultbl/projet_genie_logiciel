package org.opencompare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.RealValue;

public class Statistiques5Test {

	@Test
	public void matriceDistances() throws IOException{

		// variable du nombre de lignes du fichier
		int nbLignes=0; 

		// on crée un tableau listant le type de toutes les variables "quali" ou "quanti"
		ArrayList<String> types = new ArrayList<String>();
		boolean isQuanti;

		// on crée un tableau listant le nom de toutes les variables
		ArrayList<String> noms = new ArrayList<String>();
		boolean premiereLecture=true;

		// importer les données
		ArrayList<ArrayList> data = new ArrayList<ArrayList>();
		ArrayList row;

		// on ouvre le fichier
		File file = new File("pcms/Comparison_between_Argentine_provinces_and_countries_by_GDP_(PPP)_per_capita_0.pcm");
		PCMLoader loader = new KMFJSONLoader();
		List<PCMContainer> pcmContainers = loader.load(file);

		// lecture du fichier
		for (PCMContainer pcmContainer : pcmContainers) {
			PCM pcm = pcmContainer.getPcm();
			Cell cell=null;

			// on lit le fichier ligne par ligne
			for (Product product : pcm.getProducts()) {
				row = new ArrayList();
				for (Feature feature : pcm.getConcreteFeatures()) {

					// Find the cell corresponding to the current feature and product
					cell = product.findCell(feature);
					// Get information contained in the cell
					String content = cell.getContent();
					row.add(content);

					if(premiereLecture){
						noms.add(feature.getName());
					}
				}
				premiereLecture=false;
				data.add(row);
				nbLignes++;
			}
		}

		// 2eme lecture du fichier pour les types des variables
		for (PCMContainer pcmContainer : pcmContainers) {
			PCM pcm = pcmContainer.getPcm();
			Cell cell=null;

			// on lit le fichier colonne par colonne
			for (Feature feature : pcm.getConcreteFeatures()) {
				isQuanti=true;
				for (Product product : pcm.getProducts()) {

					// Find the cell corresponding to the current feature and product
					cell = product.findCell(feature);
					// Get information contained in the cell
					Value interpretation = cell.getInterpretation();

					if(!((interpretation instanceof IntegerValue) | (interpretation instanceof RealValue))){
						isQuanti=false;
					}

				}
				if(isQuanti){
					types.add("quanti");
				}
				else{
					types.add("quali");
				}
			}
		}




		// stocker dans un arraylist(arraylist) les distances entre chaque couple d'individus
		ArrayList<ArrayList<Double>> distances= new ArrayList<ArrayList<Double>>();

		// on remplit la matrice des distances pour chaque couple de lignes
		for(int i=0;i<nbLignes;i++){
			ArrayList<Double> ligne = new ArrayList<Double>();
			// on compare la ligne i aux autres lignes j
			for(int j=0;j<nbLignes;j++){
				// Attribuer les vecteurs de valeurs
				ArrayList<Value> valeurs1 = new ArrayList<Value>();
				ArrayList<Value> valeurs2 = new ArrayList<Value>();
				//System.out.println(distanceIndividus(valeurs1,valeurs2,types));
				ligne.add(distanceIndividus(valeurs1,valeurs2,types));
			}
			distances.add(ligne);
		}

	}



	// fonction de calcul de la distance entre 2 individus
	//	pour chaque variable, on regarde le type et on calcule la distance correspondante
	public double distanceIndividus(ArrayList<Value> valeurs1, ArrayList<Value> valeurs2, ArrayList<String> types) throws IOException {
		double distance=0;
		for(int i=0;i<valeurs1.size();i++){
			if(types.get(i)=="quali"){
				distance+=distanceQuali(String.valueOf(valeurs1.get(i)),String.valueOf(valeurs2.get(i)));
				//System.out.println(distance);
			}
			else{
				//System.out.println(Double.valueOf(String.valueOf(valeurs1.get(i))));
				//System.out.println(Double.valueOf(String.valueOf(valeurs2.get(i))));
				//System.out.println(minimums.get(i));
				//erreur ici
				distance+=distanceQuanti(Double.valueOf(String.valueOf(valeurs1.get(i))),Double.valueOf(String.valueOf(valeurs2.get(i))));
				//System.out.println(distance);
			}
		}
		//System.out.println(distance);
		return distance;
	}

	// calcul des distances entre 2 valeurs quanti (comprises entre 0 et 1.5)
	public double distanceQuanti(double x1, double x2) throws IOException {
		//System.out.println("quanti");
		//System.out.println(Math.sqrt(Math.pow(x1-x2,2)));
		return Math.sqrt(Math.pow(x1-x2,2));
	}

	// calcul des distances entre 2 valeurs quali (0 ou 1)
	public double distanceQuali(String x1, String x2) throws IOException {
		//System.out.println("quali");
		if(x1==x2){
			//System.out.println(0);
			return 0;
		}
		else{
			//System.out.println(1);
			return 1;
		}
	}
}
