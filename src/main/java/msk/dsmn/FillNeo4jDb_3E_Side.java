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
 * Updating side interactions in running Neo4j database
 * @author mkutmon
 * @author DeSl
 *
 */
public class FillNeo4jDb_3E_Side {

	public static void main(String[] args) throws Exception {
		// make sure Neo4j is started and located at this URI
		String uri = "bolt://localhost:7687";
		
		// Neo4j database directory - adapt on your own computers
//		File neo4jDir = new File("C:/Users/denise.slenter/neo4j-community-3.3.2");
//		File neo4jDir = new File("C:/Users/denise.slenter/AppData/Roaming/Neo4j Desktop/Application/neo4jDatabases/database-d4848fea-f97b-4eed-9ea1-e5ff83eca022/installation-3.3.1");
		File neo4jDir = new File("/home/deniseslenter/Software/Neo4j/neo4j-community-3.5.7"); //C:/Users/denise.slenter/Documents/Neo4j/default.graphdb
	
		
		// file is created in step 2 (FilterInteractionsA_2.java)
		File network = new File("/home/deniseslenter/git/DirectedSmallMoleculesNetwork/Code/DSMNProject/side-metabolites-wd-step3.txt");
		
		FillNeo4jDb_3E_Side neo4jdb = new FillNeo4jDb_3E_Side(uri, network, neo4jDir);
		neo4jdb.loadNetwork();
	}
	
	private Driver driver;
	private File network;
	private File neo4jDir;
	
	public FillNeo4jDb_3E_Side(String uri, File network, File neo4jDir) throws URISyntaxException {
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
				//System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				
				//TODO: create array reader here, to read in side metabolites list properly (see also QueryNeo4j_4 script).
				
				// Relabel Side->Reaction->Side reactions with "SideInteractions" iso "AllInteractions"
				//Added:Q7430(DNA) Q11053(RNA) Q172290(sulfate ion) Q427071(hydroxyl radical) Q428946(iron(II)) Q3233795(iron(III)) Q24301658(L-amino acid) Q2225(electron) Q27225748 (ADP3-); Q27104508 (HPO4 -);Q27113900 (ATP4-); Q27125072 (NADH 2-), Q28529711 (NAD -), Q4087 (NH3), Q171877 (H2O2), Q108200 (Cl-).
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n)-[a:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(R)-[b:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(m) " +
				"where  n <> m and n.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901'] " +
				" and m.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901']  " +
				"MERGE (n)-[r2:SideInteractions]->(R)-[r3:SideInteractions]->(m) " +				
				"SET r2 = a, r3=b " +
				"WITH a, b " +
				"DELETE a, b " );
				tx.success();
			}
			System.out.println("Added Side Metabolite interactions");
			 
			try (Transaction tx = session.beginTransaction()) {
				//System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Remove Side->Reaction->Metabolite
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n)-[a:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(R)-[b:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(m) " +
				"where n.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901'] " +
				"DETACH DELETE R " );
				//"DELETE R, a, n ");
				tx.success();
			} 
			System.out.println("Removed interactions TO Reaction nodes"); 
			
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Remove Metabolite->Reaction->Side
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n)-[a:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(R)-[b:AllInteractions|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]->(m) " +
				"where m.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901'] " +
				"DETACH DELETE R"  );
				tx.success();
			} 
			System.out.println("Removed interactions FROM Reaction nodes"); 
	
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Remove Enzyme->Reaction->Side
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n)-[a:AllCatalysis]->(R)-[]->(m) " +
				"where m.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901'] " +
				"DETACH DELETE R"  );
				tx.success();
			} 
			System.out.println("Removed interactions FROM Enzyme nodes"); 
			
	
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Remove unconnected nodes
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n:Metabolite) WHERE NOT (n)-[:AllInteractions|:SideInteractions|:AllCatalysis|:WikiPathwaysInteractions|:ReactomeInteractions|:LIPIDMAPSInteractions|:AnalysisInteractions]-() " +
				"DETACH DELETE n"  );
				tx.success();
			} 
			System.out.println("Remove unconnected side metabolite nodes"); 
			
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Relabel Side Metabolite labels, to be skipped during shortest path calculation.
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n:Metabolite) " +
				"where n.wdID in ['Q7430', 'Q11053', 'Q172290', 'Q427071', 'Q428946', 'Q3233795', 'Q24301658', 'Q2225', 'Q108200', 'Q4087', 'Q171877', 'Q28529711', 'Q27125072', 'Q27113900', 'Q27104508', 'Q27225748', 'Q80863', 'Q5203615', 'Q506710', 'Q422582', 'Q411092','Q392227', 'Q318369', 'Q307434', 'Q283', 'Q26987754', 'Q26841327', 'Q20856948', 'Q3154110', 'Q201312', 'Q185253', 'Q177811', 'Q26987253', 'Q27102690', 'Q26987453', 'Q1997', 'Q407635', 'Q715317', 'Q27110008', 'Q56250422', 'Q29004', 'Q26987404', 'Q27104290', 'Q27115726', 'Q27255903', 'Q27104730', 'Q23905776', 'Q3154110', 'Q152763', 'Q190901'] " +
				"REMOVE n:Metabolite " +
				"SET n:SideMetabolite"  );
				tx.success();
			} 
			System.out.println("Relabel Side Metabolites"); 
			
           System.out.println("Remove unconnected protein nodes"); 
			
			try (Transaction tx = session.beginTransaction()) {
				System.out.println(neo4jDir.getAbsolutePath());

				// copy interactions file in neo4j import directory
				FileUtils.copyFile(network, new File(neo4jDir, "/import/edges.txt"));
				// Relabel Side Metabolite labels, to be skipped during shortest path calculation.
				tx.run(//"LOAD CSV FROM 'file:///edges.txt' AS line " +
				"MATCH (n:Protein) " +
				"WHERE NOT (n)-[]-() " +
				"DELETE n ");
				tx.success();
			} 
			System.out.println("Relabel Side Metabolites"); 

		}
		System.out.println("[INFO]\tDatabase updated. Testing...");
		
		// Check database status
		try(Session session = driver.session()) {
			// count nodes
			StatementResult result = session.run("MATCH (node) RETURN count(node) AS count");
			int countN = 0;
            while (result.hasNext())
            {
                Record record = result.next();
                countN = record.get("count").asInt();
            }
            
            // count edges
            StatementResult result2 = session.run("MATCH ()-[reaction]->() RETURN count(reaction) AS count");
			int countE = 0;
            while (result2.hasNext())
            {
                Record record = result2.next();
                countE = record.get("count").asInt();
            }
            
         // count Side edges 
            StatementResult result3 = session.run("MATCH (n)-[r:SideInteractions]->(R) RETURN count(r) AS count");
			int countC = 0;
            while (result3.hasNext())
            {
                Record record = result3.next();
                countC = record.get("count").asInt();
            } 
            
            // count Unconnected Nodes
            StatementResult result4 = session.run("MATCH (n:Metabolite) WHERE NOT (n)-[:AllInteractions|:SideInteractions|:AllCatalysis]-() RETURN count(n) AS count");
 			int countU = 0;
             while (result4.hasNext())
             {
                 Record record = result4.next();
                 countU = record.get("count").asInt();
             } 
            
            System.out.println("[INFO]\tDatabase contains " + countN + " nodes and " + countE + " edges.");
            System.out.println("[INFO]\tDatabase contains " + countC + " Side edges.");
            System.out.println("[INFO]\tDatabase contains " + countU + " Unconnected nodes.");
            System.out.println("[INFO]\tDSMN Step 3 Side interactions done.");
		}
	}
}
