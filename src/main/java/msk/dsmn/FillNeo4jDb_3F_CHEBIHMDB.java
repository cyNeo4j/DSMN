package msk.dsmn;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

/**
 * Third step in the DSMN workflow
 * Saving Mapping identifiers in running Neo4j database
 * @author mkutmon
 * @author DeSl
 *
 */
public class FillNeo4jDb_3F_CHEBIHMDB {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
		
		// Neo4j database directory - adapt on your own computers
//		File neo4jDir = new File("C:/Users/denise.slenter/neo4j-community-3.3.2");
//		File neo4jDir = new File("C:/Users/denise.slenter/AppData/Roaming/Neo4j Desktop/Application/neo4jDatabases/database-d4848fea-f97b-4eed-9ea1-e5ff83eca022/installation-3.3.1");
		File neo4jDir = new File("/home/deniseslenter/Software/Neo4j/neo4j-community-3.5.7");
	
		
		// file is created in step 2A (FilterInteractionsAll_2.java)
		File network = new File("/home/deniseslenter/git/DirectedSmallMoleculesNetwork/Code/DSMNProject/network-filtered-wdAll-20201116-wpallDev.txt");
		
		FillNeo4jDb_3F_CHEBIHMDB neo4jdb = new FillNeo4jDb_3F_CHEBIHMDB(uri, network, neo4jDir);
		neo4jdb.loadNetwork();
	}
	
	private Driver driver;
	private File network;
	private File neo4jDir;
	
	public FillNeo4jDb_3F_CHEBIHMDB(String uri, File network, File neo4jDir) throws URISyntaxException {
		this.network = network;
		driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "dsmn") );
//		driver = GraphDatabase.driver(uri);
		this.neo4jDir = neo4jDir;
		System.out.println(this.neo4jDir.getAbsolutePath());
	}
	
	public void loadNetwork() throws Exception {
				
		System.out.println("[INFO]\tFill database with data from file: " + network.getName());
		// refill database
		try(Session session = driver.session()) {
			try (Transaction tx = session.beginTransaction()) {
				System.out.println("Testing directory for ChEBI + HMDB mappings: " + neo4jDir.getAbsolutePath());

				//TO-DO: Replace file from RDF with new query, connecting Wikidata to Chebi and HMDB separately. (unique WD IDs, to avoid additional node loading).
				
				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				//Add Chebi IDs for Source nodes
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t'" +
						"UNWIND split(line.SourceChebi, ',') AS chebi " + 
						"WITH line, chebi WHERE chebi IS NOT NULL " + 
						"MERGE (s:Mapping:CHEBI {mappingIDs: chebi})" + 
						"MERGE (t:Metabolite {wdID: line.Source}) " +
						"MERGE (s)-[:MappingInteractions]->(t)" );
						tx.success();
						
			}
			System.out.println("Added ChEBI source mappings");
			try (Transaction tx = session.beginTransaction()) {
				
				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				//Add Chebi IDs for Target nodes
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t'" +
						"UNWIND split(line.TargetChebi, ',') AS chebi " + 
						//"MERGE (s:Mapping:CHEBI {mappingIDs: chebi})" + 
						"WITH line, chebi WHERE chebi IS NOT NULL " + 
						"MERGE (s:Mapping:CHEBI {mappingIDs: chebi})" + 
						"MERGE (t:Metabolite {wdID: line.Target}) " +
						"MERGE (s)-[:MappingInteractions]->(t)" );
						tx.success();
						
			}
			System.out.println("Added ChEBI target mappings");
			
			try (Transaction tx = session.beginTransaction()) {
				//System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				//Add HDMB IDs for Source nodes
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t'" +
						"UNWIND split(line.SourceHMDB, ',') AS hmdb " + 
						"WITH line, hmdb WHERE hmdb IS NOT NULL " + 
						"MERGE (s:Mapping:HMDB {mappingIDs: hmdb})" + 
						"MERGE (t:Metabolite {wdID: line.Source}) " +
						"MERGE (s)-[:MappingInteractions]->(t)" );
						tx.success();
			}
			System.out.println("Added HMDB source mappings");
			
			try (Transaction tx = session.beginTransaction()) {
				//System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				//Add HDMB IDs for Target nodes
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t'" +
						"UNWIND split(line.TargetHMDB, ',') AS hmdb " + 
						"WITH line, hmdb WHERE hmdb IS NOT NULL " + 
						"MERGE (s:Mapping:HMDB {mappingIDs: hmdb})" + 
						"MERGE (t:Metabolite {wdID: line.Target}) " +
						"MERGE (s)-[:MappingInteractions]->(t)" );
						tx.success();
			} 
			System.out.println("Added HMDB target mappings");
			
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				
				//Remove spaces in Gene IDs if present.
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (e:Mapping)-[r:MappingInteractions]->(m) " +
				"where  e.mappingIDs in [' ', ''] " +
				"DELETE e,r " );
				tx.success();
			}
			
		}
		System.out.println("[INFO]\tDatabase updated. Testing...");
		
		// Check database status
		try(Session session = driver.session()) {
			// count nodes
			StatementResult result = session.run("MATCH (n) RETURN count(n) AS count");
			int countN = 0;
            while (result.hasNext())
            {
                Record record = result.next();
                countN = record.get("count").asInt();
            }
            
            // count edges
            StatementResult result2 = session.run("MATCH ()-[r]->() RETURN count(r) AS count");
			int countE = 0;
            while (result2.hasNext())
            {
                Record record = result2.next();
                countE = record.get("count").asInt();
            }
            
         // count Mapping nodes
            StatementResult result7 = session.run("MATCH (n:Mapping) RETURN count(n) AS count");
			int countM = 0;
            while (result7.hasNext())
            {
                Record record = result7.next();
                countM = record.get("count").asInt();
            }
            
            // count ChEBI Mapping nodes
            StatementResult result8 = session.run("MATCH (n:CHEBI) RETURN count(n) AS count");
			int countCh = 0;
            while (result8.hasNext())
            {
                Record record = result8.next();
                countCh = record.get("count").asInt();
            }
            
            // count HMDB Mapping nodes
            StatementResult result9 = session.run("MATCH (n:HMDB) RETURN count(n) AS count");
			int countH = 0;
            while (result9.hasNext())
            {
                Record record = result9.next();
                countH = record.get("count").asInt();
            }
            
            System.out.println("[INFO]\tDatabase contains " + countN + " nodes and " + countE + " edges.");
            System.out.println("[INFO]\tDatabase contains " + countM + " Mapping Nodes.");
            System.out.println("[INFO]\tOut of which " + countCh + " CHEBI nodes and " + countH + " HMDB nodes.");
            System.out.println("[INFO]\tDSMN Step 3 Mapping interactions done.");
		}
	}
}
