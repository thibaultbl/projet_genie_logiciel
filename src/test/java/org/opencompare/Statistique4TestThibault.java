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

public class Statistique4TestThibault {
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

}
