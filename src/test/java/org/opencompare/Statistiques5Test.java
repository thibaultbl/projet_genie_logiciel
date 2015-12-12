package org.opencompare;

import java.io.File;
import java.io.FileWriter;
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

		File directory = new File("pcms");
		File[] files = directory.listFiles();

		for(File file : files){
			// variable du nombre de lignes du fichier
			int nbLignes=0; 

			// on crée un tableau listant le type de toutes les variables "quali" ou "quanti"
			ArrayList<String> types = new ArrayList<String>();
			boolean isQuanti;

			// on crée un tableau listant le nom de toutes les lignes
			ArrayList<String> noms = new ArrayList<String>();

			// importer les données
			ArrayList<ArrayList> data = new ArrayList<ArrayList>();
			ArrayList row;

			// on ouvre le fichier
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
						//System.out.println(content);
						row.add(content);
					}
					noms.add(product.getName());
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
					valeurs1=data.get(i);
					valeurs2=data.get(j);
					//System.out.println(distanceIndividus(valeurs1,valeurs2,types));
					ligne.add(distanceIndividus(valeurs1,valeurs2,types));
				}
				distances.add(ligne);
			}
			
			//appeler fonctions pour creer fichiers json et html
			transformationMatrixToD3js(distances,file.getName(),noms);
			genererHTML(file.getName());
		}
	}



	// fonction de calcul de la distance entre 2 individus
	//	pour chaque variable, on regarde le type et on calcule la distance correspondante
	public double distanceIndividus(ArrayList<Value> valeurs1, ArrayList<Value> valeurs2, ArrayList<String> types) throws IOException {
		double distance=0;
		for(int i=0;i<valeurs1.size();i++){
			if(types.get(i)=="quali"){
				distance+=distanceQuali(String.valueOf(valeurs1.get(i)),String.valueOf(valeurs2.get(i)));
			}
			else{
				distance+=distanceQuanti(Double.valueOf(String.valueOf(valeurs1.get(i))),Double.valueOf(String.valueOf(valeurs2.get(i))));
			}
		}
		return distance;
	}

	// calcul des distances entre 2 valeurs quanti (comprises entre 0 et 1.5)
	public double distanceQuanti(double x1, double x2) throws IOException {
		return Math.sqrt(Math.pow(x1-x2,2));
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

	/**
	 * Fonction pour envoyer une ArrayList<ArrayList<double>> (ce qui correspondra à la matrice de distance
	 *  dans d3js (format Json spécifique)
	 */

	public void transformationMatrixToD3js(ArrayList<ArrayList<Double>> array, String nomFichier, ArrayList<String> nom){
		File f = new File ("fichiersHTML/fichiersJson/"+nomFichier+".json");

		try{
			FileWriter fw = new FileWriter(f);
			fw.write("{\r\n\"nodes\":[\r\n");
			for(int i=0;i<array.size();i++){
				if(i<array.size()-1){
					fw.write("{\"name\":\""+nom.get(i)+"\", \"group\":"+i+"},\r\n");
				}
				else{
					fw.write("{\"name\":\""+nom.get(i)+"\", \"group\":"+i+"}\r\n");
				}
			}
			fw.write(" ],\r\n\"links\":[");

			for(int i=0;i<array.size();i++){
				for(int j=0;j<array.get(i).size();j++){
					if((i<array.size()-1) | (j<array.get(i).size()-1)){
						fw.write("{\"source\":"+i+", \"target\":"+j+", \"value\":"+array.get(i).get(j)+"},\r\n");
					}
					else{
						fw.write("{\"source\":"+i+", \"target\":"+j+", \"value\":"+array.get(i).get(j)+"}\r\n");
					}
				}
			}
			fw.write(" ]\r\n }");
			fw.close();
		}
		catch (IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}

	}

	public void genererHTML(String nomFichier){
		File f = new File ("fichiersHTML/"+nomFichier+".html");

		try{
			FileWriter fw = new FileWriter(f);
			// ecriture du fichier HTML
			fw.write("﻿<!DOCTYPE html>\n<meta charset=\"utf-8\">\n<style>\n\n.node {\n  stroke: #fff;\n  stroke-width: 1.5px;\n}\n\n.link {\n  stroke: #999;\n  stroke-opacity: .6;\n}\n\n</style>\n<body>\n<script src=\"d3.min.js\"></script>\n<script>\n\nvar width = 960,\n    height = 500;\n\nvar color = d3.scale.category20();\n\nvar force = d3.layout.force()\n    .charge(-120)\n    .linkDistance(30)\n    .size([width, height]);\n\nvar svg = d3.select(\"body\").append(\"svg\")\n    .attr(\"width\", width)\n    .attr(\"height\", height);\n\nd3.json(\"fichiersJson/"+nomFichier+".json\", function(error, graph) {\n  if (error) throw error;\n\n  force\n      .nodes(graph.nodes)\n      .links(graph.links)\n      .start();\n\n  var link = svg.selectAll(\".link\")\n      .data(graph.links)\n    .enter().append(\"line\")\n      .attr(\"class\", \"link\")\n      .style(\"stroke-width\", function(d) { return Math.sqrt(d.value); });\n\n  var node = svg.selectAll(\".node\")\n      .data(graph.nodes)\n    .enter().append(\"circle\")\n      .attr(\"class\", \"node\")\n      .attr(\"r\", 5)\n      .style(\"fill\", function(d) { return color(d.group); })\n      .call(force.drag);\n\n  node.append(\"title\")\n      .text(function(d) { return d.name; });\n\n  force.on(\"tick\", function() {\n    link.attr(\"x1\", function(d) { return d.source.x; })\n        .attr(\"y1\", function(d) { return d.source.y; })\n        .attr(\"x2\", function(d) { return d.target.x; })\n        .attr(\"y2\", function(d) { return d.target.y; });\n\n    node.attr(\"cx\", function(d) { return d.x; })\n        .attr(\"cy\", function(d) { return d.y; });\n  });\n});\n\n</script>");

			fw.close();
		}
		catch (IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}

	@Test
	public void test(){
		genererHTML("test");
	}
}
