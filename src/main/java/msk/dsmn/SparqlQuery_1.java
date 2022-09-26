package msk.dsmn;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * First step in the DSMN workflow
 * Retrieving metabolic reactions from the WP SPARQL endpoint
 * @author mkutmon
 *
 */
public class SparqlQuery_1 {

	public static void main(String[] args) throws IOException {
		String sparqlUrl = "http://sparql.wikipathways.org";
		File out = new File("sparql-result.txt");
		
		SparqlQuery_1 sq = new SparqlQuery_1(sparqlUrl, out);
		sq.executeQuery();
	}
	
	private String sparqlUrl;
	private File out;

	public SparqlQuery_1(String sparqlUrl, File out) {
		this.sparqlUrl = sparqlUrl;
		this.out = out;
	}
	
	public void executeQuery() throws IOException {
		System.out.println("[INFO]\tQuerying WikiPathways SPARQL endpoint.");
		String queryString = getQuery(); 
		queryString = queryString.replace(" ", "+");
		queryString = queryString.replace("?", "%3F");
		
		String queryUrl = sparqlUrl + "?default-graph-uri=&query=" + queryString + "&format=text%2Fcsv";
		URL url = new URL(queryUrl);		
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		
		while((line = reader.readLine()) != null) {
			writer.write(line + "\n");
		}
		
		reader.close();
		writer.close();
		
		System.out.println("[INFO]\tSPARQL result saved in file: " + out.getName());
	}
	
	private String getQuery() {
		// TODO: retrieve query from github repo
		String queryString = "SELECT DISTINCT ?interaction ?pathway ?sourceDb ?targetDb ?mimtype " +
				"WHERE { " + 
				" ?pathway a wp:Pathway . " +
				"?pathway wp:organismName \"Homo sapiens\"^^xsd:string . " +
				"?interaction dcterms:isPartOf ?pathway . " +				
				"?interaction a wp:DirectedInteraction . " + 
				"?interaction wp:source ?source . " +
				"?interaction wp:target ?target . " +  
				"OPTIONAL{?interaction a ?mimtype}. " +
				"VALUES ?mimtype {wp:ComplexBinding wp:Conversion wp:Inhibition wp:Catalysis wp:Stimulation wp:TranscriptionTranslation wp:DirectedInteraction} . " +
				"?source a wp:Metabolite . " +
				"?source wp:bdbWikidata ?sourceDb . " +
				"?target a wp:Metabolite . " +
				"?target wp:bdbWikidata ?targetDb . " +
				"}";
		return queryString;
	}
	
}
