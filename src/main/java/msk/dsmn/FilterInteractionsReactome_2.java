package msk.dsmn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msk.dsmn.utils.Interaction;

/**
 * Second step in the DSMN workflow
 * Creates network file from SPARQL query
 *  - one complete network file with all interactions
 *  - one filtered network file removing interactions with common side metabolites
 * @author mkutmon
 * @author DeSl
 * 
 */
public class FilterInteractionsReactome_2 {
	
	public static void main(String[] args) throws Exception {
		
		// run SparqlQuery_1a_Jenkins to create this file (change name accordingly)
		File sparqlResult = new File("sparql-resultA-wdReactome-20201116-wpallDev.txt");
	
		// run SparqlQuery_1b_Jenkins to create this file (change name accordingly)
		File sparqlResultB = new File("sparql-resultB-wdReactome-20201116-wpallDev.txt");
		
		// run SparqlQuery_1c_Jenkins to create this file (change name accordingly)
		File sparqlResultC = new File("sparql-resultC-wdReactome-20201116-wpallDev.txt");
			
		// list of metabolites that will be excluded from the network (ATP, NADH, etc) 
		// TODO: retrieve file from github repo
		File sideMetabolites = new File("side-metabolites-wd.txt");
		
		FilterInteractionsReactome_2 fi = new FilterInteractionsReactome_2(sparqlResult, sparqlResultC, sparqlResultB, sideMetabolites);

		// output files (add date and change name accordingly)
		File network = new File("network-wdReactome-20201116-wpallDev.txt");
		File networkFiltered = new File("network-filtered-wdReactome-20201116-wpallDev.txt");
		
		fi.createNetworkFiles(network, networkFiltered);
	}
		
	private File sparqlResult;
	private File sparqlResultC;
	private File sparqlResultB;
	private File sideMetabolites;
	
	public FilterInteractionsReactome_2(File sparqlResult, File sparqlResultC, File sparqlResultB, File sideMetabolites) {
		this.sparqlResult = sparqlResult;
		this.sparqlResultC = sparqlResultC;
		this.sparqlResultB = sparqlResultB;
		this.sideMetabolites = sideMetabolites;
	}
	
	public void createNetworkFiles(File network, File networkFiltered) throws Exception {
		System.out.println("[INFO]\tReading file containing side metabolites.");
		List<String> sideMetaboliteList = readSideMetabolites(sideMetabolites);
		System.out.println("[INFO]\t" + sideMetaboliteList.size() + " side metabolites IDs found.");
		
		System.out.println("[INFO]\tReading SPARQL result file and parsing interactions.");
		BufferedReader reader = new BufferedReader(new FileReader(sparqlResult));
		String line;
		reader.readLine(); // skip header
		int rheacount = 0;
		int count = 0;
		// Define a map (containing key + value pairs, with key being unique source_target combination, and value stored in interactions class)
		// Interaction class should contain all attributes in the results file, in order to end up in the final network.
		Map<String, Interaction> map = new HashMap<String, Interaction>();
		
		while((line = reader.readLine()) != null) { //if a  line is not empty
			line = line.replaceAll("\"", ""); // check if line contains \, and replace with nothing.
			line = line.replaceAll("<", ""); // check if line contains <, and replace with nothing (due to tsv structure).
			line = line.replaceAll(">", ""); // check if line contains >, and replace with nothing (due to tsv structure).
			
			
			String [] buffer = line.split("\t", -1); //split all lines on tab (since file is retrieved as tsv), keep empty columns.
			String source = parseUrl(buffer[1]); //define in which column the source is located (2nd, but indexed as 1st).	
			String target = parseUrl(buffer[2]); //define in which column the target is located (3nd, but indexed as 2nd).
			String key = source + "_" + target; //create unique key with source and target, to store value from interaction class in.
			
			if(!map.containsKey(key)) { //Check if map doesn't have a key yet
				Interaction i = new Interaction(key); //if key is not present yet, store it in i.
				i.setSource(source); //retrieve source
				i.setTarget(target); //retrieve target
				map.put(key, i); //connect key to value (->i)
				
			}
			String pathway = parseUrl(buffer[4]).split("_")[0]; // define in which column the PW ID is located.
			String intType = parseUrl(buffer[3]).replace("wp#", ""); //define in which column MIM-type is located.

			//for a key, get the PW, and add the attribute PW to the interaction class
			if(pathway.isEmpty()) {
				 map.get(key).getPathways().add("NA"); }
				else{
				  map.get(key).getPathways().add(pathway);			  
				  }
			
			//for a key, get the MIM, and add the attribute MIM to the interaction class
			if(intType.isEmpty()) {
				 map.get(key).getIntTypes().add("NA"); }
				else{
				  map.get(key).getIntTypes().add(intType);			  
				  }
			
			
			String intId = parseUrl(buffer[0]); //define in which column interaction ID is located.
			
			if(!map.get(key).getIntIds().contains(intId)) { //Check if map doesn't have a interaction ID as attribute in Interactions class
				map.get(key).setOccurence(map.get(key).getOccurence()+1); //count how often interaction in total occurs.
				//for a key, get the interaction ID, and add the attribute intID to the interaction class	
			if(intId.isEmpty()) {
				 map.get(key).getIntIds().add("NotOccuring"); }
				else{
					map.get(key).getIntIds().add(intId);			  
					}
				
			
			String pathwayName = parseUrl(buffer[5]);
			//for a key, get the PWname, and add the attribute PWname to the interaction class
			if(pathwayName.isEmpty()) {
				 map.get(key).getPathwayName().add("NA"); }
				else{
				  map.get(key).getPathwayName().add(pathwayName);			  
				  }
				
			String sourceChebi = parseUrl(buffer[6]);
			String targetChebi = parseUrl(buffer[7]);
			String sourceHMDB = parseUrl(buffer[8]);
			String targetHMDB = parseUrl(buffer[9]);
			
			String interactionID = parseUrl(buffer[10]);
			
			if(sourceChebi.isEmpty()) {
			 map.get(key).getSourceChebi().add("NA"); }
			else{
			  map.get(key).getSourceChebi().add(sourceChebi);			  
			  }
			
			if(targetChebi.isEmpty()) {
				 map.get(key).getTargetChebi().add("NA"); }
				else{
				  map.get(key).getTargetChebi().add(targetChebi);			  
				  }
			
			if(sourceHMDB.isEmpty()) {
				 map.get(key).getSourceHMDB().add("NA"); }
			else{
				  map.get(key).getSourceHMDB().add(sourceHMDB);			  
				  }
			
			if(targetHMDB.isEmpty()) {
				 map.get(key).getTargetHMDB().add("NA"); }
			else{
				  map.get(key).getTargetHMDB().add(targetHMDB);			  
				  }		
			
			//Provide unique interactionIDs, store rhea IDs for later
			if((!interactionID.isEmpty()) && (map.get(key).getInteractionID().isEmpty())){
				map.get(key).getRheaID().add("RHEA:" + interactionID); //Store Rhea IDs as reaction node attribute later
				rheacount++; //Update rhea ID number, for metadata
				map.get(key).getInteractionID().add("ReactionID_Reactome:" + count); //Only add a reaction ID number, if none is present.
				count++; //Update reaction ID number, to get unique IDs for Neo4j later.
				}
			else if(map.get(key).getInteractionID().isEmpty()){
				map.get(key).getInteractionID().add("ReactionID_Reactome:" + count); //Only add a reaction ID number, if none is present.
				count++; //Update reaction ID number, to get unique IDs for Neo4j later.
				}
			//If Rhea has already been found for source_target pair, store Rhea ID later (no new interactionID).
			if((!interactionID.isEmpty()) && !(map.get(key).getInteractionID().isEmpty())){
				map.get(key).getRheaID().add("RHEA:" + interactionID); //Store Rhea IDs as reaction node attribute later
				rheacount++; //Update rhea ID number, for metadata
				}		
			
			}
		}
		
		reader.close();
		
		BufferedReader readerC = new BufferedReader(new FileReader(sparqlResultC));
		String lineC;
		readerC.readLine(); // skip header
		
		while((lineC = readerC.readLine()) != null) { //if a  line is not empty
			lineC = lineC.replaceAll("\"", ""); // check if line contains \, and replace with nothing.
			lineC = lineC.replaceAll("<", ""); // check if line contains <, and replace with nothing (due to tsv structure).
			lineC = lineC.replaceAll(">", ""); // check if line contains >, and replace with nothing (due to tsv structure).
			
			
			String [] buffer = lineC.split("\t", -1); //split all lines on tab (since file is retrieved as tsv), keep empty columns.
			String source = parseUrl(buffer[1]); //define in which column the source is located (2nd, but indexed as 1st).	
			String target = parseUrl(buffer[2]); //define in which column the target is located (3nd, but indexed as 2nd).
			String key = source + "_" + target; //create unique key with source and target, to store value from interaction class in.
			
			if(!map.containsKey(key)) { //Check if map doesn't have a key yet
				Interaction i = new Interaction(key); //if key is not present yet, store it in i.
				i.setSource(source); //retrieve source
				i.setTarget(target); //retrieve target
				map.put(key, i); //connect key to value (->i)
			}	
			String proteinName = parseStartUrl(buffer[4]); 
			String proteinID = parseUrl(buffer[3]);
			
			if(proteinName.isEmpty()) {
				  map.get(key).getProteinName().add("NA"); 
				  }
				else{
				  map.get(key).getProteinName().add(proteinName);			  
				  }
				
			 map.get(key).getProteinID().add(proteinID);	
			
		}
		
		readerC.close();
		
		
		BufferedReader readerB = new BufferedReader(new FileReader(sparqlResultB));
		String lineB;
		readerB.readLine(); // skip header
		
		while((lineB = readerB.readLine()) != null) { //if a  line is not empty
			lineB = lineB.replaceAll("\"", ""); // check if line contains \, and replace with nothing.
			lineB = lineB.replaceAll("<", ""); // check if line contains <, and replace with nothing (due to tsv structure).
			lineB = lineB.replaceAll(">", ""); // check if line contains >, and replace with nothing (due to tsv structure).
			
			
			String [] buffer = lineB.split("\t", -1); //split all lines on tab (since file is retrieved as tsv), keep empty columns.
			String source = parseUrl(buffer[1]); //define in which column the source is located (2nd, but indexed as 1st).	
			String target = parseUrl(buffer[2]); //define in which column the target is located (3nd, but indexed as 2nd).
			String key = source + "_" + target; //create unique key with source and target, to store value from interaction class in.
			
			if(!map.containsKey(key)) { //Check if map doesn't have a key yet
				Interaction i = new Interaction(key); //if key is not present yet, store it in i.
				i.setSource(source); //retrieve source
				i.setTarget(target); //retrieve target
				map.put(key, i); //connect key to value (->i)
			}	
			String pathwayOnt = parseUrl(buffer[3]); 
			String diseaseOnt = parseUrl(buffer[4]);
			String curationStatus = parseUrl(buffer[5]);
			String interactionRef = parseUrl(buffer[6]);
			String pathwayRef = parseUrl(buffer[7]);
			String sourceRef = parseUrl(buffer[8]);
			String targetRef = parseUrl(buffer[9]);
			
			if(pathwayOnt.isEmpty()) {
				 map.get(key).getPathwayOnt().add("NA"); }
			else{
				  map.get(key).getPathwayOnt().add(pathwayOnt);			  
				  }
			
			if(diseaseOnt.isEmpty()) {
				 map.get(key).getDiseaseOnt().add("NA"); }
			else{
				  map.get(key).getDiseaseOnt().add(diseaseOnt);			  
				  }
			
			if(curationStatus.isEmpty()) {
				 map.get(key).getCurationStatus().add("NA"); }
			else{
				  map.get(key).getCurationStatus().add(curationStatus);			  
				  }
			
			if(interactionRef.isEmpty()) {
				 map.get(key).getInteractionRef().add("NA"); }
			else{
				  map.get(key).getInteractionRef().add(interactionRef);			  
				  }
			
			if(pathwayRef.isEmpty()) {
				 map.get(key).getPathwayRef().add("NA"); }
			else{
				  map.get(key).getPathwayRef().add(pathwayRef);			  
				  }
			
			if(sourceRef.isEmpty()) {
				 map.get(key).getSourceRef().add("NA"); }
			else{
				  map.get(key).getSourceRef().add(sourceRef);			  
				  }
			
			if(targetRef.isEmpty()) {
				 map.get(key).getTargetRef().add("NA"); }
			else{
				  map.get(key).getTargetRef().add(targetRef);			  
				  }	
			
						
			
		}
		
		readerC.close();
		
		
		System.out.println("[INFO]\t" + map.size() + " unique interactions found.");
		
		System.out.println("[INFO]\tWriting complete and filtered network files.");
		
		int countF = writeInteractionFile(network, false, map, sideMetaboliteList); // countF should be 0
		int countFiltered = writeInteractionFile(networkFiltered, true, map, sideMetaboliteList);
		
		System.out.println("[INFO]\tNetwork files created.\n\t\t" 
				+ network.getName() + ": " + countF + " interactions filtered.\n\t\t"
				+ networkFiltered.getName() + ": " + countFiltered + " interactions filtered.");
		System.out.println("[INFO]\tDSMN Step 2 done.");
		System.out.println("[INFO]\tThere are Rhea IDs for " + rheacount + " interactions.");
		
	}
	
	private int writeInteractionFile(File out, boolean filtered, Map<String, Interaction> interactions, List<String> sideMetaboliteList) throws Exception {
		int countFiltered = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		writer.write("Source\tTarget\tOccurences\tCountPathways\tIntTypes\tPathways\tPathwayName\tSourceChebi\tTargetChebi\tSourceHMDB\tTargetHMDB\tproteinName\tproteinID\tinteractionID\tpathwayOntology\tdiseaseOntology\tcurationStatus\tinteractionRef\tpathwayRef\tsourceRef\ttargetRef\trheaIDs\n");
		for(String s : interactions.keySet()) {
			
			Interaction i = interactions.get(s);
			boolean sideSoure = sideMetaboliteList.contains(i.getSource());
			boolean sideTarget = sideMetaboliteList.contains(i.getTarget());
			
			if(!filtered || (filtered && (!sideSoure && !sideTarget))) {
				
				
				writer.write(i.getSource() + "\t" + i.getTarget() + "\t");
				writer.write(i.getOccurence() + "\t" + i.getPathways().size() + "\t");
				writer.write(i.getIntTypes() + "\t" + i.getPathways() + "\t" + i.getPathwayName() + "\t");
				writer.write(i.getSourceChebi().toString().replace("[","").replace("]", "") + "\t" + i.getTargetChebi().toString().replace("[","").replace("]", "") + "\t");
				writer.write(i.getSourceHMDB().toString().replace("[","").replace("]", "") + "\t" + i.getTargetHMDB().toString().replace("[","").replace("]", "") + "\t");
				writer.write(i.getProteinName().toString().replace("[]","[NA]") + "\t" + i.getProteinID().toString().replace("[","").replace("]", "") +  "\t" +  i.getInteractionID().toString().replace("[]","[NA]") + "\t");
				writer.write(i.getPathwayOnt().toString().replace("[]","[NA]") + "\t" + i.getDiseaseOnt().toString().replace("[]","[NA]") + "\t" + i.getCurationStatus().toString().replace("[]","[NA]") + "\t");
				writer.write(i.getInteractionRef().toString().replace("[]","[NA]") + "\t" + i.getPathwayRef().toString().replace("[]","[NA]") + "\t");
				writer.write(i.getSourceRef().toString().replace("[]","[NA]") + "\t" + i.getTargetRef().toString().replace("[]","[NA]") + "\t");
				writer.write(i.getRheaID().toString().replace("[]","[NA]") + "\n");
			} else {
				countFiltered++;
			}
		}
		writer.close();
		return countFiltered;
	}
	
	private String parseUrl(String s) {
		String [] buffer = s.split("/");
		return buffer[buffer.length-1];
	}
	
	private String parseStartUrl(String s) {
		String [] buffer = s.split("\\^");
		return buffer[0].replaceAll("\"", "");
		//return buffer[buffer.length-1];
	}
	
	private static List<String> readSideMetabolites(File sideMetabolites) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(sideMetabolites));
		List<String> list = new ArrayList<String>();
		
		String line;
		while((line = reader.readLine()) != null) {
			list.add(line);
		}
		reader.close();
		return list;
	}

}

