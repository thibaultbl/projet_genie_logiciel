package org.opencompare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class CreationFichierTexteTest {

	@Test
	public static void testEcritureFichier(ArrayList<String> faitsInteressants, String nomFichier){
		File f = new File ("fichiersTexte/"+nomFichier+".txt");

		try{
			FileWriter fw = new FileWriter(f);
			for(int i=0;i<faitsInteressants.size();i++){
				fw.write(faitsInteressants.get(i));
			}
			fw.close();
		}
		catch (IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}
}
