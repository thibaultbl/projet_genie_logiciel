package org.opencompare;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

public class StatistiquesTest {
	
	@Test
	public void testParcours() throws IOException {
		File directory = new File("pcms");
		File[] files = directory.listFiles();
		int compteur=0;
		
		//parcours de l'ensemble des fichiers du répertoire
		for(File file : files){
			compteur++;
		}
		assertEquals(1193,compteur);
	}

	@Test
	public void testTotalMoyenneMinMax() throws IOException {
		File directory = new File("pcms");
		File[] files = directory.listFiles();
		int total=0;
		float moy=0;
		int min=99999999;
		int max=0;
		int nbCells;

		//parcours de l'ensemble des fichiers du répertoire
		for(File file : files){
			nbCells=0;
			
			// Define a file representing a PCM to load //test
	        File pcmFile = file;

	        // Create a loader that can handle the file format
	        PCMLoader loader = new KMFJSONLoader();

	        // Load the file
	        // A loader may return multiple PCM containers depending on the input format
	        // A PCM container encapsulates a PCM and its associated metadata
	        List<PCMContainer> pcmContainers = loader.load(pcmFile);

	        for (PCMContainer pcmContainer : pcmContainers) {

	            // Get the PCM
	            PCM pcm = pcmContainer.getPcm();

	            // Browse the cells of the PCM
	            for (Product product : pcm.getProducts()) {
	                for (Feature feature : pcm.getConcreteFeatures()) {
	                	total++;
	                	nbCells++;
	                }
	            }
	        }
	        if(nbCells<min){
	        	min=nbCells;
	        }
	        if(nbCells>max){
	        	max=nbCells;
	        }
		}
		System.out.println("Il y a au total "+total+" cellules dans le dataset.");
		moy=total/files.length;
		System.out.println("Il y a en moyenne "+moy+" cellules par matrice.");
		System.out.println("Il y a au minimum "+min+" cellules par matrice.");
		System.out.println("Il y a au maximum "+max+" cellules par matrice.");
	}
	
	@Test
	public void testTypes() throws IOException {
		File directory = new File("pcms");
		File[] files = directory.listFiles();

		//parcours de l'ensemble des fichiers du répertoire
		for(File file : files){
			// Define a file representing a PCM to load //test
	        File pcmFile = new File("pcms/example.pcm");

	        // Create a loader that can handle the file format
	        PCMLoader loader = new KMFJSONLoader();

	        // Load the file
	        // A loader may return multiple PCM containers depending on the input format
	        // A PCM container encapsulates a PCM and its associated metadata
	        List<PCMContainer> pcmContainers = loader.load(pcmFile);

	        for (PCMContainer pcmContainer : pcmContainers) {

	            // Get the PCM
	            PCM pcm = pcmContainer.getPcm();

	            // Browse the cells of the PCM
	            for (Product product : pcm.getProducts()) {
	                for (Feature feature : pcm.getConcreteFeatures()) {

	                    // Find the cell corresponding to the current feature and product
	                    Cell cell = product.findCell(feature);

	                    // Get information contained in the cell
	                    String content = cell.getContent();
	                    String rawContent = cell.getRawContent();
	                    Value interpretation = cell.getInterpretation();
	                    System.out.println(interpretation.toString());
	                }
	            }
	        }
		}
	}
	

@Test
	public void nbProduitMatrice() throws IOException {
		File directory = new File("pcms");
		File[] files = directory.listFiles();
		List<Integer> liste=new ArrayList<Integer>();
		int compteur=0;

		//parcours de l'ensemble des fichiers du répertoire
		for(File file : files){
			// Create a loader that can handle the file format
			PCMLoader loader = new KMFJSONLoader();

			// Load the file
			// A loader may return multiple PCM containers depending on the input format
			// A PCM container encapsulates a PCM and its associated metadata
			List<PCMContainer> pcmContainers = loader.load(file);
			compteur=0;
			for (PCMContainer pcmContainer : pcmContainers) {
				// Get the PCM
				PCM pcm = pcmContainer.getPcm();
				// Browse the cells of the PCM
				for (Product product : pcm.getProducts()) {
					compteur++;
				}

			}
			System.out.println(file.getName()+" : "+compteur);
			liste.add(compteur);
		}
		int min = liste.indexOf(Collections.min(liste));
		int max = liste.indexOf(Collections.max(liste));
		int mean = (int)this.calculateAverage(liste);
		int total = (int)this.sumListe(liste);
		System.out.println("min : "+ min+" max : "+max+" mean : "+mean+" total : "+total);

	}

	private double calculateAverage(List <Integer> marks) {
		Integer sum = 0;
		if(!marks.isEmpty()) {
			for (Integer mark : marks) {
				sum += mark;
			}
			return sum.doubleValue() / marks.size();
		}
		return sum;
	}
	private double sumListe(List <Integer> marks) {
		Integer sum = 0;
		if(!marks.isEmpty()) {
			for (Integer mark : marks) {
				sum += mark;
			}
		}
		return sum;
	}
}