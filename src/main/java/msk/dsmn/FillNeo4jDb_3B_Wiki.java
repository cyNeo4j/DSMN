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
 * Saving interactions in running Neo4j database
 * @author mkutmon
 * @author DeSl
 *
 */
public class FillNeo4jDb_3B_Wiki {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
		
		// Neo4j database directory - adapt on your own computers
//		File neo4jDir = new File("C:/Users/denise.slenter/neo4j-community-3.3.2");
//		File neo4jDir = new File("C:/Users/denise.slenter/AppData/Roaming/Neo4j Desktop/Application/neo4jDatabases/database-d4848fea-f97b-4eed-9ea1-e5ff83eca022/installation-3.3.1");
		File neo4jDir = new File("/home/deniseslenter/Software/Neo4j/neo4j-community-3.5.7");
	
		
		// file is created in step 2 (FilterInteractionsA_2.java)
		File network = new File("/home/deniseslenter/git/DirectedSmallMoleculesNetwork/Code/DSMNProject/network-wdWiki-20201116-wpallDev.txt");
		
		FillNeo4jDb_3B_Wiki neo4jdb = new FillNeo4jDb_3B_Wiki(uri, network, neo4jDir);
		neo4jdb.loadNetwork();
	}
	
	private Driver driver;
	private File network;
	private File neo4jDir;
	
	public FillNeo4jDb_3B_Wiki(String uri, File network, File neo4jDir) throws URISyntaxException {
		this.network = network;
		driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "dsmn") );
//		driver = GraphDatabase.driver(uri);
		this.neo4jDir = neo4jDir;
		System.out.println("Directory of Neo4j for step 3C: " + this.neo4jDir.getAbsolutePath());
	}
	
	public void loadNetwork() throws Exception {
				
		System.out.println("[INFO]\tFill database with data from file: " + network.getName());
		// refill database
		try(Session session = driver.session()) {
			
			//Load Metabolite->ReactionID->Metabolite
			try (Transaction tx = session.beginTransaction()) {
				System.out.println("Testing directory for source->Wikireaction->target: " + neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t' " +
						"MERGE (source:Metabolite {wdID: line.Source}) " + 
						"MERGE (target:Metabolite {wdID: line.Target}) " + 
						"MERGE (reaction:Reaction {rwID: line.interactionID}) " + 
						"CREATE (source)-[:WikiPathwaysInteractions {intType: line.IntTypes, intRef: line.interactionRef, occurences: toInteger(line.Occurences), count: toInteger(line.CountPathways), pathways: line.Pathways, pathwayName: line.PathwayName, proteinName: line.proteinName, pwont: line.pathwayOntology, disont: line.diseaseOntology, pwRef: line.pathwayRef, proteinName: line.proteinName, rheaID: line.rheaIDs, sourceRef: line.sourceRef, targetRef: line.targetRef}]->(reaction) " +
						"CREATE (reaction)-[:WikiPathwaysInteractions {intType: line.IntTypes, intRef: line.interactionRef, occurences: toInteger(line.Occurences), count: toInteger(line.CountPathways), pathways: line.Pathways, pathwayName: line.PathwayName, proteinName: line.proteinName, pwont: line.pathwayOntology, disont: line.diseaseOntology, pwRef: line.pathwayRef, proteinName: line.proteinName, rheaID: line.rheaIDs, sourceRef: line.sourceRef, targetRef: line.targetRef}]->(target) " );
						
				tx.success();
			}
			
			//Load Enzyme->ReactionID data
						try (Transaction tx = session.beginTransaction()) {
				System.out.println("Testing directory for enzyme->reaction: " + neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				
				tx.run("LOAD CSV WITH HEADERS FROM 'file:///edges.txt' AS line FIELDTERMINATOR '\t' " +
						"UNWIND split(line.proteinID, ',') AS ensembl " + 
						"WITH line, ensembl WHERE ensembl IS NOT NULL " + 
						"MERGE (geneID:Protein {enID: ensembl}) " +
						"MERGE (reactionID:Reaction {rwID: line.interactionID}) " +
						"MERGE (geneID)-[:AllCatalysis {pathways: line.Pathways, pathwayName: line.PathwayName, proteinName: line.proteinName, proteinId: line.proteinID}]->(reactionID) ");
				tx.success();
			}		
			
			
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				
				//Remove spaces in Gene IDs if present.
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (e:Protein)-[r:AllCatalysis]->(m) " +
				"where  e.enID in [' ', ''] " +
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
            
         // count WikiPathways edges
            StatementResult result3 = session.run("MATCH ()-[r:WikiPathwaysInteractions]->() RETURN count(r) AS count");
			int countC = 0;
            while (result3.hasNext())
            {
                Record record = result3.next();
                countC = record.get("count").asInt();
            }
            
            System.out.println("[INFO]\tDatabase contains " + countN + " nodes and " + countE + " edges.");
            System.out.println("[INFO]\tDatabase contains " + countC + " WikiPathways edges.");
            System.out.println("[INFO]\tDSMN Step 3 WikiPathways interactions done.");
		}
	}
}
