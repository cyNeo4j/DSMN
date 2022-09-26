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
public class FillNeo4jDb_3G_DatabaseInfo {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
		
		// Neo4j database directory - adapt on your own computer
		File neo4jDir = new File("/home/deniseslenter/Software/Neo4j/neo4j-community-3.5.7");
	
		FillNeo4jDb_3G_DatabaseInfo neo4jdb = new FillNeo4jDb_3G_DatabaseInfo(uri, neo4jDir);
		neo4jdb.loadNetwork();
	}
	
	private Driver driver;
	private File network;
	private File neo4jDir;
	
	public FillNeo4jDb_3G_DatabaseInfo(String uri, File neo4jDir) throws URISyntaxException {
		this.network = network;
		driver = GraphDatabase.driver(uri, AuthTokens.basic("neo4j", "dsmn") );
//		driver = GraphDatabase.driver(uri); //use this line when no authentication is needed for Neo4j
		this.neo4jDir = neo4jDir;
		System.out.println("Directory of Neo4j for step 3A: " + this.neo4jDir.getAbsolutePath());
	}
	
	public void loadNetwork() throws Exception {
				
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
            
            // count Unconnected Nodes
            StatementResult result17 = session.run("MATCH (n) WHERE NOT (n)-[]-() RETURN count(n) AS count");
 			int countU = 0;
             while (result17.hasNext())
             {
                 Record record = result17.next();
                 countU = record.get("count").asInt();
             } 
            
         // count Unconnected Metabolite Nodes
           StatementResult result3 = session.run("MATCH (n:Metabolite) WHERE NOT (n)-[]-() RETURN count(n) AS count");
			int countUM = 0;
            while (result3.hasNext())
            {
                Record record = result3.next();
                countUM = record.get("count").asInt();
            } 
            
            // count Unconnected Protein Nodes
            StatementResult result16 = session.run("MATCH (n:Protein) WHERE NOT (n)-[]-() RETURN count(n) AS count");
 			int countUP = 0;
             while (result16.hasNext())
             {
                 Record record = result16.next();
                 countUP = record.get("count").asInt();
             } 
            
            // count All edges
            StatementResult result4 = session.run("MATCH ()-[r:AllInteractions]->() RETURN count(r) AS count");
			int countA = 0;
            while (result4.hasNext())
            {
                Record record = result4.next();
                countA = record.get("count").asInt();
            }
            
            // count WikiPathways edges
            StatementResult result5 = session.run("MATCH ()-[r:WikiPathwaysInteractions]->() RETURN count(r) AS count");
			int countC = 0;
            while (result5.hasNext())
            {
                Record record = result5.next();
                countC = record.get("count").asInt();
            }
            
            // count Reactome edges
            StatementResult result6 = session.run("MATCH ()-[r:ReactomeInteractions]->() RETURN count(r) AS count");
			int countR = 0;
            while (result6.hasNext())
            {
                Record record = result6.next();
                countR = record.get("count").asInt();
            }
            
            // count LIPIDMAPS edges
            StatementResult result7 = session.run("MATCH ()-[r:LIPIDMAPSInteractions]->() RETURN count(r) AS count");
			int countL = 0;
            while (result7.hasNext())
            {
                Record record = result7.next();
                countL = record.get("count").asInt();
            }
            
            // count Analysis edges
            StatementResult result15 = session.run("MATCH ()-[r:AnalysisInteractions]->() RETURN count(r) AS count");
			int countAna = 0;
            while (result15.hasNext())
            {
                Record record = result15.next();
                countAna = record.get("count").asInt();
            }
            
            
            // count Mapping nodes
            StatementResult result8 = session.run("MATCH (n:Mapping) RETURN count(n) AS count");
			int countM = 0;
            while (result8.hasNext())
            {
                Record record = result8.next();
                countM = record.get("count").asInt();
            }
            
            // count ChEBI Mapping nodes
            StatementResult result9 = session.run("MATCH (n:CHEBI) RETURN count(n) AS count");
			int countCh = 0;
            while (result9.hasNext())
            {
                Record record = result9.next();
                countCh = record.get("count").asInt();
            }
            
            // count HMDB Mapping nodes
            StatementResult result10 = session.run("MATCH (n:HMDB) RETURN count(n) AS count");
			int countH = 0;
            while (result10.hasNext())
            {
                Record record = result10.next();
                countH = record.get("count").asInt();
            }        
    
            
            // count Protein interactions
            StatementResult result11 = session.run("MATCH ()-[r:AllCatalysis]->() RETURN count(r) AS count");
			int countPi = 0;
            while (result11.hasNext())
            {
                Record record = result11.next();
                countPi = record.get("count").asInt();
            }
            
            // count Protein Nodes
            StatementResult result12 = session.run("MATCH (n:Protein) RETURN count(n) AS count");
			int countP = 0;
            while (result12.hasNext())
            {
                Record record = result12.next();
                countP = record.get("count").asInt();
            }
            
            // count Metabolite Nodes
            StatementResult result13 = session.run("MATCH (n:Metabolite) RETURN count(n) AS count");
			int countMe = 0;
            while (result13.hasNext())
            {
                Record record = result13.next();
                countMe = record.get("count").asInt();
            }
            
            System.out.println("[INFO]\tDatabase contains " + countN + " nodes and " + countE + " edges.");
            System.out.println("[INFO]\tDatabase contains " + countMe + " Metabolites and " + countP + " Proteins.");
            System.out.println("[INFO]\tDatabase contains " + countU + " Unconnected Nodes in total.");
       //    System.out.println("[INFO]\tDatabase contains " + countUM + " Unconnected Metabolite Nodes.");
       //     System.out.println("[INFO]\tDatabase contains " + countUP + " Unconnected Protein Nodes.");
            System.out.println("[INFO]\tDatabase contains " + countA + " AllInteraction edges.");
            System.out.println("[INFO]\tDatabase contains " + countC + " Wikipathways edges.");
            System.out.println("[INFO]\tDatabase contains " + countR + " Reactome edges.");
            System.out.println("[INFO]\tDatabase contains " + countL + " LIPIDMAPS edges.");
            System.out.println("[INFO]\tDatabase contains " + countAna + " Analysis edges.");
            System.out.println("[INFO]\tDatabase contains " + countPi + " Catalysis edges.");
            System.out.println("[INFO]\tDatabase contains " + countM + " Mapping Nodes.");
            System.out.println("[INFO]\tOut of which " + countCh + " CHEBI nodes and " + countH + " HMDB nodes.");
            
            // Find path length --> Comment section below due to execution time...
    	//	StatementResult result14 = session.run("MATCH (from:Metabolite), (to:Metabolite), p=shortestPath((from)-[:AllInteractions*]->(to)) WHERE from<>to RETURN min(length(p)) as min, max(length(p)) as max");
    	//	int countMin = 0;
    	//	int countMax = 0;
        //   while (result14.hasNext())
        //    {
        //        Record record = result14.next();
        //        countMin = record.get("min").asInt();
        //        countMax = record.get("max").asInt();
        //    }
                      
                      
        //    System.out.println("[INFO]\tPath length: " + countMin + " minimum length and " + countMax + " maximum length.");
            System.out.println("[INFO]\tDSMN Step 3 DataBase info done.");
		}
	}
}
