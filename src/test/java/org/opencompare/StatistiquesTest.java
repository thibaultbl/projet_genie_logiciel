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
import org.opencompare.api.java.value.BooleanValue;
import org.opencompare.api.java.value.DateValue;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.RealValue;
import org.opencompare.api.java.value.StringValue;

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

		int cptString=0;
		int cptInteger=0;
		int cptReal=0;
		int cptBoolean=0;

		int nbString;
		int nbInteger;
		int nbReal;
		int nbBoolean;

		int nbFile=0;

		int minString=9999999;
		int minInteger=9999999;
		int minReal=9999999;
		int minBoolean=9999999;

		int maxString=0;
		int maxInteger=0;
		int maxReal=0;
		int maxBoolean=0;

		//parcours de l'ensemble des fichiers du répertoire
		for(File file : files){
			nbString=0;
			nbInteger=0;
			nbReal=0;
			nbBoolean=0;

			nbFile++;

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

						// Find the cell corresponding to the current feature and product
						Cell cell = product.findCell(feature);

						// Get information contained in the cell
						String content = cell.getContent();
						String rawContent = cell.getRawContent();
						Value interpretation = cell.getInterpretation();

						if (interpretation instanceof StringValue) {
							//StringValue v = (StringValue) interpretation;
							cptString++;
							nbString++;
						}
						else if(interpretation instanceof IntegerValue){
							cptInteger++;
							nbInteger++;
						}
						else if(interpretation instanceof RealValue){
							cptReal++;
							nbReal++;
						}
						else if(interpretation instanceof BooleanValue){
							cptBoolean++;
							nbBoolean++;
						}
						// Print the content of the cell
						//System.out.println("(" + product.getName() + ", " + feature.getName() + ") = " + content);
					}
				}
			}

			if(nbString<minString){
				minString=nbString;
			}
			if(nbInteger<minInteger){
				minInteger=nbInteger;
			}
			if(nbReal<minReal){
				minReal=nbReal;
			}
			if(nbBoolean<minBoolean){
				minBoolean=nbBoolean;
			}
			
			if(nbString>maxString){
				maxString=nbString;
			}
			if(nbInteger>maxInteger){
				maxInteger=nbInteger;
			}
			if(nbReal>maxReal){
				maxReal=nbReal;
			}
			if(nbBoolean>maxBoolean){
				maxBoolean=nbBoolean;
			}
		}
		
		float moyString=cptString/nbFile;
		float moyInteger=cptInteger/nbFile;
		float moyReal=cptReal/nbFile;
		float moyBoolean=cptBoolean/nbFile;

		System.out.println("Il y a au total "+cptString+" cellules de type String.");
		System.out.println("Il y a au total "+cptInteger+" cellules de type Integer.");
		System.out.println("Il y a au total "+cptReal+" cellules de type Real.");
		System.out.println("Il y a au total "+cptBoolean+" cellules de type Boolean.");
		
		System.out.println("Il y a en moyenne "+moyString+" cellules de type String.");
		System.out.println("Il y a en moyenne "+moyInteger+" cellules de type Integer.");
		System.out.println("Il y a en moyenne "+moyReal+" cellules de type Real.");
		System.out.println("Il y a en moyenne "+moyBoolean+" cellules de type Boolean.");
		
		System.out.println("Il y a au minimum "+minString+" cellules de type String.");
		System.out.println("Il y a au minimum "+minInteger+" cellules de type Integer.");
		System.out.println("Il y a au minimum "+minReal+" cellules de type Real.");
		System.out.println("Il y a au minimum "+minBoolean+" cellules de type Boolean.");
		
		System.out.println("Il y a au maximum "+maxString+" cellules de type String.");
		System.out.println("Il y a au maximum "+maxInteger+" cellules de type Integer.");
		System.out.println("Il y a au maximum "+maxReal+" cellules de type Real.");
		System.out.println("Il y a au maximum "+maxBoolean+" cellules de type Boolean.");
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
			//System.out.println(file.getName()+" : "+compteur);
			liste.add(compteur);
		}
		int min = Collections.min(liste);
		int max = Collections.max(liste);
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

	@Test
	public void nbFeatureMatrice() throws IOException {
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
				for (Feature feature : pcm.getConcreteFeatures()) {
					compteur++;
				}

			}
			//System.out.println(file.getName()+" : "+compteur);
			liste.add(compteur);
		}
		int min = Collections.min(liste);
		int max = Collections.max(liste);
		int mean = (int)this.calculateAverage(liste);
		int total = (int)this.sumListe(liste);
		System.out.println("min : "+ min+" max : "+max+" mean : "+mean+" total : "+total);
	}
}