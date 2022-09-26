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

public class QueryNeo4j_4_speedtest {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
		
		 //Wikidata IDs to run query for
		File queryList = new File("query265-metabolites-wd.txt"); //change "wd" with "chebi" to test the mapping approach.
		
		QueryNeo4j_4_speedtest qn = new QueryNeo4j_4_speedtest(uri);
		qn.generateSubNetwork(queryList);
	}
	
	private Driver driver;
	
	public QueryNeo4j_4_speedtest(String uri) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "dsmn") );
	}

		public void generateSubNetwork(File queryList) throws Exception {
			System.out.println("[INFO]\tRead query list.");
			Set<String> query = readQueryList(queryList);
			System.out.println("[INFO]\tIdentified " + query.size() + " unique query entries.");
			File output = new File("subnetwork-wd-test265-20201116-wpallDEV.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("Source\tTarget\tCount\tOccurrences\tPathways\tInteractionType\tPathwayName\tProteinName\tPathwayOntology\tDiseaseOntology\tInteractionRef\tPWref\tSourceRef\tTargetRef\n"); //Additional properties should be stored here!
			
			Session session = driver.session();
			
			//Define variables for speed testing:
					//Path length
					int path_lenght;
					path_lenght = 24;
					//Relationship type
					
			
			String queryArray = "[";
			boolean first = true;
			for(String s : query) {
				if(first) {
					queryArray = queryArray + "\"" + s + "\""; first = false;
				} else queryArray = queryArray + "," + "\"" + s + "\"";
			}
			queryArray = queryArray + "]";
			
			System.out.println("[INFO]\tSend query to Neo4j database.");
			
	/*		//TEST Query for Neo4j:
	match (a),(b), p=allShortestPaths((a)-[:AllInteractions*..8]-(b:Metabolite))
	   where  a <> b and a.wdID in ['Q4545703', 'Q27109160', 'Q4673297', 'Q413822', 'Q7098084', 'Q312208', 'Q27093079', 'Q4673311', 'Q179894', 'Q408641', 'Q483745', 'Q484940', 'Q26987253', 'Q26987754']
	   and b.wdID in ['Q4545703', 'Q27109160', 'Q4673297', 'Q413822', 'Q7098084', 'Q312208', 'Q27093079', 'Q4673311', 'Q179894', 'Q408641', 'Q483745', 'Q484940', 'Q26987253', 'Q26987754']
	   return p
	   
	   OR
	   
	   WITH ['Q4545703', 'Q27109160', 'Q4673297', 'Q413822', 'Q7098084', 'Q312208', 'Q27093079', 'Q4673311', 'Q179894', 'Q408641', 'Q483745', 'Q484940', 'Q26987253', 'Q26987754'] as List
	UNWIND List AS y
	MATCH (a:Metabolite) 
	WHERE single(x IN a.wdID WHERE x = y)
	WITH DISTINCT a, y
	match (a),(b), p=allShortestPaths((a)-[:AllInteractions*..8]-(b:Metabolite))
	   where  a <> b and b.wdID in ['Q4545703', 'Q27109160', 'Q4673297', 'Q413822', 'Q7098084', 'Q312208', 'Q27093079', 'Q4673311', 'Q179894', 'Q408641', 'Q483745', 'Q484940', 'Q26987253', 'Q26987754']
	   return p
	   
	   OR
	   
	MATCH (n:Metabolite) where n.wdID IN ['Q4545703', 'Q27109160', 'Q4673297', 'Q413822', 'Q7098084', 'Q312208', 'Q27093079', 'Q4673311', 'Q179894', 'Q408641', 'Q483745', 'Q484940', 'Q26987253', 'Q26987754']
	WITH collect(n) as nodes
	UNWIND nodes as n
	UNWIND nodes as m
	WITH * WHERE n <> m
	MATCH path = allShortestPaths( (n)-[r:AllInteractions|:AllCatalysis*..4]-(m) )
	RETURN n,r,m
			
		*/	
			long startTime = System.nanoTime();
					
			//Check if query array is from Wikidata
			if(new String(queryArray).contains("Q")) {
					String neo4jQuery = "MATCH (n:Metabolite) where n.wdID IN " + queryArray + " WITH collect(n) as nodes " +      //[*] for all interactions
							" UNWIND nodes as n " +
							" UNWIND nodes as m " +
							//" WITH * WHERE id(n) < id(m) " + //SLOW!!
							" WITH * WHERE n <> m " + //Fastest solution now, tested with path length max 24, dataset MTBLS265
							" MATCH path = allShortestPaths( (n)-[:AllInteractions|:AllCatalysis*.." + path_lenght +  "]-(m) ) " +
							//"WHERE n <> m" + //Faster than line 116
							" RETURN path "; 
					System.out.println("[INFO]\tRunning Neo4j query:\n" + neo4jQuery);
					StatementResult result2 = session.run(neo4jQuery);
					List<Record> resultList = result2.list();
					Map<Node, Set<Node>> interactions = new HashMap<Node, Set<Node>>();
					System.out.println("[INFO]\tReceived result with " + resultList.size() + " paths.");
					
			long endTime   = System.nanoTime();
			long totalTime = (endTime - startTime)/1000000;
			
			System.out.println(path_lenght + "\t" + resultList.size() + "\t" + totalTime);
	
			}
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
