package org.opencompare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class Statistique5TestThibault {
	/**
	 * Fonction pour envoyer une ArrayList<ArrayList<double>> (ce qui correspondra à la matrice de distance
	 *  dans d3js (format Json spécifique)
	 */
	ArrayList<ArrayList<Double>> array;
	
	public void transformationMatrixToD3js(ArrayList<ArrayList<Double>> array, String nomFichier, ArrayList<String> nom){
		File f = new File ("fichiersJson/"+nomFichier+".json");

		try{
			FileWriter fw = new FileWriter(f);
			fw.write("{\r\n\"nodes\":[\r\n");
			for(int i=0;i<array.size();i++){
				fw.write("{\"name\":\""+nom.get(i)+"\", \"group\":"+i+"},\r\n");
			}
			fw.write(" ],\r\n\"links\":[");
			
			
			for(int i=0;i<array.size();i++){
				for(int j=0;j<array.get(i).size();j++){
					fw.write("{\"source\":\""+i+"\", \"target\":"+j+", \"target\":"+array.get(i).get(j)+"},\r\n");
				}
			}
			fw.write(" ]\r\n }");
			fw.close();
		}
		catch (IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
		
	}
	
	@Test
	public void test(){
		ArrayList<Double> array1=new ArrayList<Double>();
		array1.add(1.5);
		array1.add(2.5);
		array1.add(3.5);
		ArrayList<Double> array2=new ArrayList<Double>();
		array2.add(4.5);
		array2.add(5.5);
		array2.add(6.5);
		ArrayList<Double> array3=new ArrayList<Double>();
		array2.add(7.5);
		array2.add(8.5);
		array2.add(9.5);
		ArrayList<ArrayList<Double>> array=new ArrayList<ArrayList<Double>>();
		array.add(array1);
		array.add(array2);
		array.add(array3);

		ArrayList<String> nom=new ArrayList<String>();
		nom.add("paul");
		nom.add("Jean");
		nom.add("Alfred");

		this.transformationMatrixToD3js(array, "test", nom);

		
	}
}
