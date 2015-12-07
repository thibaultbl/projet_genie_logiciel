package org.opencompare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class Statistiques4Test {
	@Test
	public void faitsInteressants() throws IOException {
		File directory = new File("pcms2");
		File[] files = directory.listFiles();
		//On crée une liste contenant l'ensemble des features
		List<ArrayList<String>> liste=new ArrayList<ArrayList<String>>();
		ArrayList<String> listeTemp=null;
		

		for(File file : files){
			PCMLoader loader = new KMFJSONLoader();

			List<PCMContainer> pcmContainers = loader.load(file);
			
			for (PCMContainer pcmContainer : pcmContainers) {
				PCM pcm = pcmContainer.getPcm();
				liste=new ArrayList<ArrayList<String>>();
				for (Feature feature : pcm.getConcreteFeatures()) {
					listeTemp=new ArrayList<String>();
					for (Product product : pcm.getProducts()) {
						

						// Find the cell corresponding to the current feature and product
						Cell cell = product.findCell(feature);
						// Get information contained in the cell
						String content = cell.getContent();
						
						listeTemp.add(content);


						// Print the content of the cell
						//System.out.println("(" + product.getName() + ", " + feature.getName() + ") = " + content);


					}
					liste.add(listeTemp);
				}
			}
			System.out.println("change product :"+file.getName());
			for(int i=0;i<liste.size();i++ ){
				System.out.println("change feature");
				for(int j=0;j<liste.get(i).size();j++){
					System.out.println(liste.get(i).get(j));
				}
			}
		}
		
	}
	
	@Test
	public void faitsInteressantsWithTreatment() throws IOException {
		File directory = new File("pcms2");
		File[] files = directory.listFiles();
		//On crée une liste contenant l'ensemble des features
		List<ArrayList<String>> liste=new ArrayList<ArrayList<String>>();
		ArrayList<String> listeTemp=null;
		
		List<Value> listeInterpretation=null;
		

		for(File file : files){
			PCMLoader loader = new KMFJSONLoader();

			List<PCMContainer> pcmContainers = loader.load(file);
			
			
			for (PCMContainer pcmContainer : pcmContainers) {
				PCM pcm = pcmContainer.getPcm();
				liste=new ArrayList<ArrayList<String>>();
				listeInterpretation=new ArrayList<Value>();
				Cell cell=null;
				for (Feature feature : pcm.getConcreteFeatures()) {
					listeTemp=new ArrayList<String>();
					for (Product product : pcm.getProducts()) {
						

						// Find the cell corresponding to the current feature and product
						cell = product.findCell(feature);
						// Get information contained in the cell
						String content = cell.getContent();
						
						listeTemp.add(content);


						// Print the content of the cell
						//System.out.println("(" + product.getName() + ", " + feature.getName() + ") = " + content);


					}
					listeInterpretation.add(cell.getInterpretation());
					liste.add(listeTemp);
				}
			}
			System.out.println("change product :"+file.getName());
			for(int i=0;i<liste.size();i++ ){
				System.out.println("change feature");
				for(int j=0;j<liste.get(i).size();j++){
					System.out.println(liste.get(i).get(j));
				}
			}
		}
	}
	
	@Test
	public double faitsInteressantsQuanti(ArrayList<String> liste) throws IOException {
		double[] tabQuanti = new double[liste.size()];
		for(int i=0;i<liste.size();i++){
			double chiffre=Double.parseDouble(liste.get(i));
			tabQuanti[i]=chiffre;
		}
		
		Arrays.sort(tabQuanti);
		
		//index du thresold à 90%
		int index=(int)Math.round((tabQuanti.length)*0.9);
		
		// on retourne le quantile à 90%
		return tabQuanti[index];
	}
	
	@Test
	public String faitsInteressantsQuali(ArrayList<String> liste) throws IOException {
		ArrayList<String> modalites = new ArrayList<String>();
		ArrayList<Double> frequences = new ArrayList<Double>();
		
		// on remplit le tableau des modalités
		for(int i=0;i<liste.size();i++){
			boolean ajout=true;
			for(int j=0;j<modalites.size();j++){
				if(liste.get(i)==modalites.get(j)){
					ajout=false;
				}
			}
			if(ajout){
				modalites.add(liste.get(i));
				frequences.add((double)0);
			}
		}
		
		// on compte le nombre d'occurence des modalités
		for(int i=0;i<liste.size();i++){
			for(int j=0;j<modalites.size();j++){
				if(liste.get(i)==modalites.get(j)){
					frequences.set(i, frequences.get(i)+1);
				}
			}
		}
		
		// on passe les comptages en fréquences
		for(int j=0;j<frequences.size();j++){
			frequences.set(j, frequences.get(j)/liste.size());
		}
		
		// on relève une éventuelle fréquence supérieure à 90%
		int trouve=-1;
		for(int j=0;j<frequences.size();j++){
			if(frequences.get(j)>=0.9){
				trouve=j;
			}
		}
		if(trouve!=-1){
			return modalites.get(trouve);
		}
		else{
			return null;
		}
	}
}
