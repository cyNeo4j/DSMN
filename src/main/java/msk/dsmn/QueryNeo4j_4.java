package msk.dsmn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;

/**
 * Fourth step in the DSMN workflow
 * Retrieving shortest path from Neo4j database in Cytoscape compatible format.
 * @author mkutmon
 * @author DeSl
 *
 */

public class QueryNeo4j_4 {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
			
		File queryList = new File("queryRist-metabolites-wd.txt"); //change "wd" with "chebi" to test the mapping approach.
		
		QueryNeo4j_4 qn = new QueryNeo4j_4(uri);
		qn.generateSubNetwork(queryList);
	}
	
	private Driver driver;
	
	public QueryNeo4j_4(String uri) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "dsmn") );
	}

	public void generateSubNetwork(File queryList) throws Exception {
		System.out.println("[INFO]\tRead query list.");
		Set<String> query = readQueryList(queryList);
		System.out.println("[INFO]\tIdentified " + query.size() + " unique query entries.");
		File output = new File("subnetwork-wd-Rist-MAR2021_Undirected.txt"); //("subnetwork-wd-Rist-20201116-wpallDEV.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		writer.write("Source\tTarget\tCount\tOccurrences\tPathways\tInteractionType\tPathwayName\tProteinName\tPathwayOntology\tDiseaseOntology\tInteractionRef\tPWref\tSourceRef\tTargetRef\n"); //Additional properties should be stored here!
		
		Session session = driver.session();
		String queryArray = "[";
		boolean first = true;
		for(String s : query) {
			if(first) {
				queryArray = queryArray + "\"" + s + "\""; first = false;
			} else queryArray = queryArray + "," + "\"" + s + "\"";
		}
		queryArray = queryArray + "]";
		
		System.out.println("[INFO]\tSend query to Neo4j database.");
		

		long startTime = System.nanoTime();
				
		//Check if query array is from Wikidata
		if(new String(queryArray).contains("Q")) {
				String neo4jQuery = "MATCH (n:Metabolite) where n.wdID IN " + queryArray + " WITH collect(n) as nodes " +      //[*] for all interactions
						" UNWIND nodes as n " +
						" UNWIND nodes as m " +
						" WITH * WHERE n <> m " +
						" MATCH p = allShortestPaths( (n)-[:AllInteractions|:AllCatalysis*]-(m) ) " + // [:AllInteractions|:AllCatalysis*..16] or [:AllInteractions*..16]
						" RETURN p "; 
				System.out.println("[INFO]\tRunning Neo4j query:\n" + neo4jQuery);
				StatementResult result2 = session.run(neo4jQuery);
				List<Record> resultList = result2.list();
				Map<Node, Set<Node>> interactions = new HashMap<Node, Set<Node>>();
				System.out.println("[INFO]\tReceived result with " + resultList.size() + " paths between metabolites.");
	
		for(Record record : resultList) {
			PathValue p = (PathValue)record.get("p");
			Path path = p.asPath();
			Map<Long, Node> nodes = new HashMap<Long, Node>();
			Map<Long, Relationship> relationships = new HashMap<Long, Relationship>();
			for(Node n : path.nodes()) {
				nodes.put(n.id(), n);
			}
			for(Relationship re : path.relationships()) {
				relationships.put(re.endNodeId(),re);			
			}
//			System.out.println(path.start().get("id").asString().replace("\"", "") + "\t" + path.end().get("id").asString().replace("\"", "") + "\t" + path.length());
			for(Relationship r : path.relationships()) {
				Node start = nodes.get(r.startNodeId());
				Node end =  nodes.get(r.endNodeId());
				
				if(!interactions.containsKey(start)) {
					interactions.put(start, new HashSet<Node>());
					interactions.get(start).add(end);
					//When a startnode is a metabolite, not a reaction node (meaning no reactionID(wrID) is present) or enzyme
					if(start.get("rwID").isNull() && start.get("enID").isNull()) { //&& start.get("enID").isNull()) {
					writer.write(start.get("wdID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
							"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
							"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
							"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
							"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
							"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + 
					r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
					}
					//When a startnode is a reaction node, not a metabolite or enzyme
					else if (start.get("wdID").isNull() && start.get("enID").isNull()) {
						writer.write(start.get("rwID").asString().replace("\"", "") + "\t" + end.get("wdID").asString().replace("\"", "") +
								"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
								"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
								"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
								"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
								"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
										
					} 
					//When a startnode is an enzyme
					else {
							writer.write(start.get("enID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
									"\t" + "1" + "\t" + "1" + "\t" + r.get("pathways").asString().replace("\"", "") + 
									"\t" + "[Enzyme Conversion]" + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
									"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
									"\t" + "[NA]" + "\t" + "[NA]" + "\t" + "[NA]" +
									"\t" + "[NA]" + "\t" + "[NA]" +  "\t" + "[NA]" + "\n" );
						}
						
				} else {
					if(!interactions.get(start).contains(end)) {
						interactions.get(start).add(end);
						//When a startnode is a metabolite, not a reaction node or enzyme:
						if(start.get("rwID").isNull() && start.get("enID").isNull()) {// && start.get("enID").isNull()) {
						writer.write(start.get("wdID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
								"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") +
								"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
								"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
								"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
								"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + 
								r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
							}
						//When a startnode is a reaction node, not a metabolite or enzyme
						else if (start.get("wdID").isNull() && start.get("enID").isNull()) {
							writer.write(start.get("rwID").asString().replace("\"", "") + "\t" + end.get("wdID").asString().replace("\"", "") +
									"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
									"\t" + r.get("intType").asString().replace("\"", "") + "\t" + start.get("pathwayName").asString().replace("\"", "") + 
									"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
									"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
									"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
										
							}
						//When a startnode is an enzyme
						else {
							writer.write(start.get("enID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
									"\t" + "1" + "\t" + "1" + "\t" + r.get("pathways").asString().replace("\"", "") + 
									"\t" + "[Enzyme Conversion]" + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
									"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
									"\t" + "[NA]" + "\t" + "[NA]" + "\t" + "[NA]" +
									"\t" + "[NA]" + "\t" + "[NA]" +  "\t" + "[NA]" + "\n" );
							
							
						}
						}
					}
				}
			}
		}
		//If not Wikidata, use mapping approach to get directed network
		else{
			String neo4jQuery = 
					" WITH " + queryArray + " AS coll " +
					" UNWIND coll AS y " + 
					" MATCH (a:Mapping) " +
					" WHERE single(x IN a.mappingIDs WHERE x = y) " +
					" WITH DISTINCT a, y " +
					" MATCH (a) " +
					" WITH [(a)-[:MappingInteractions*..1]->(b) WHERE b:Metabolite | b.wdID] AS MappedTo " +
					" UNWIND MappedTo as c " +
					" WITH collect(c) as List " +
					" MATCH (n:Metabolite) where n.wdID IN List WITH collect(n) as nodes " +
					" UNWIND nodes as n " +
					" UNWIND nodes as m " +
					" WITH * WHERE n <> m " +
					" MATCH p = allShortestPaths( (n)-[:AllInteractions|:AllCatalysis*]-(m) ) " +
					" return p ";
			System.out.println("[INFO]\tRunning Neo4j query:\n" + neo4jQuery);
			StatementResult result2 = session.run(neo4jQuery);
			List<Record> resultList = result2.list();
			Map<Node, Set<Node>> interactions = new HashMap<Node, Set<Node>>();
			System.out.println("[INFO]\tReceived result with " + resultList.size() + " paths between metabolites.");

	for(Record record : resultList) {
		PathValue p = (PathValue)record.get("p");
		Path path = p.asPath();
		Map<Long, Node> nodes = new HashMap<Long, Node>();
		for(Node n : path.nodes()) {
			nodes.put(n.id(), n);
		}
//		System.out.println(path.start().get("id").asString().replace("\"", "") + "\t" + path.end().get("id").asString().replace("\"", "") + "\t" + path.length());
		for(Relationship r : path.relationships()) {
			Node start = nodes.get(r.startNodeId());
			Node end =  nodes.get(r.endNodeId());
			
			
			if(!interactions.containsKey(start)) {
				interactions.put(start, new HashSet<Node>());
				interactions.get(start).add(end);
				//When a startnode is a metabolite, not a reaction node (meaning no reactionID(wrID) is present) or enzyme
				if(start.get("rwID").isNull() && start.get("enID").isNull()) { //&& start.get("enID").isNull()) {
				writer.write(start.get("wdID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
						"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
						"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
						"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
						"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
						"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + 
				r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
				}
				//When a startnode is a reaction node, not a metabolite or enzyme
				else if (start.get("wdID").isNull() && start.get("enID").isNull()) {
					writer.write(start.get("rwID").asString().replace("\"", "") + "\t" + end.get("wdID").asString().replace("\"", "") +
							"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
							"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
							"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
							"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
							"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
									
				} 
				//When a startnode is an enzyme
				else {
						writer.write(start.get("enID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
								"\t" + "1" + "\t" + "1" + "\t" + r.get("pathways").asString().replace("\"", "") + 
								"\t" + "[Enzyme Conversion]" + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
								"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
								"\t" + "[NA]" + "\t" + "[NA]" + "\t" + "[NA]" +
								"\t" + "[NA]" + "\t" + "[NA]" +  "\t" + "[NA]" + "\n" );
					}
					
			} else {
				if(!interactions.get(start).contains(end)) {
					interactions.get(start).add(end);
					//When a startnode is a metabolite, not a reaction node or enzyme:
					if(start.get("rwID").isNull() && start.get("enID").isNull()) {// && start.get("enID").isNull()) {
					writer.write(start.get("wdID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
							"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") +
							"\t" + r.get("intType").asString().replace("\"", "") + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
							"\t" + r.get("proteinName").asString().replace("\"", "") + //"\t" +  r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
							"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
							"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + 
							r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
						}
					//When a startnode is a reaction node, not a metabolite or enzyme
					else if (start.get("wdID").isNull() && start.get("enID").isNull()) {
						writer.write(start.get("rwID").asString().replace("\"", "") + "\t" + end.get("wdID").asString().replace("\"", "") +
								"\t" + r.get("count").asInt() + "\t" + r.get("occurences").asInt() + "\t" + r.get("pathways").asString().replace("\"", "") + 
								"\t" + r.get("intType").asString().replace("\"", "") + "\t" + start.get("pathwayName").asString().replace("\"", "") + 
								"\t" + r.get("proteinName").asString().replace("\"", "") +  //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
								"\t" + r.get("pwont").asString().replace("\"", "") + "\t" + r.get("disont").asString().replace("\"", "") + "\t" + r.get("intRef").asString().replace("\"", "") +
								"\t" + r.get("pwRef").asString().replace("\"", "") + "\t" + r.get("sourceRef").asString().replace("\"", "") +  "\t" + r.get("targetRef").asString().replace("\"", "") + "\n" );
									
						}
					//When a startnode is an enzyme
					else {
						writer.write(start.get("enID").asString().replace("\"", "") + "\t" + end.get("rwID").asString().replace("\"", "") +
								"\t" + "1" + "\t" + "1" + "\t" + r.get("pathways").asString().replace("\"", "") + 
								"\t" + "[Enzyme Conversion]" + "\t" + r.get("pathwayName").asString().replace("\"", "") + 
								"\t" + r.get("proteinName").asString().replace("\"", "") +  //"\t" + r.get("proteinId").asString().replace("\"", "") + "\t" + //r.get("interactionId").asString().replace("\"", "") +
								"\t" + "[NA]" + "\t" + "[NA]" + "\t" + "[NA]" +
								"\t" + "[NA]" + "\t" + "[NA]" +  "\t" + "[NA]" + "\n" );
						
						
							}
						}
					}
				}
			}
		}
		System.out.println("[INFO]\tSubnetwork saved in file " + output.getName());
		writer.close();
		driver.close();
		
		long endTime   = System.nanoTime();
		long totalTime = (endTime - startTime)/1000000;
		System.out.println(totalTime + " milliseconds");
	} 
	
	private Set<String> readQueryList(File queryList) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(queryList));
		Set<String> set = new HashSet<String>();
		String line;
		while((line = reader.readLine()) != null) {
			set.add(line);
		}
		reader.close();
		return set;
	}


}
