package org.opencompare;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

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

	/*@Test
	public void testNbCellsTotal() throws IOException {
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

						// Print the content of the cell
						System.out.println("(" + product.getName() + ", " + feature.getName() + ") = " + content);
					}
				}
			}
		}
	}*/
}