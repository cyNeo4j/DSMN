package msk.dsmn;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * First step in the DSMN workflow
 * Retrieving metabolic reactions from the WP SPARQL .ttl file through Jenkins releases
 * @author mkutmon 
 * @author egonw
 * @author DeSl 
 *
 */
public class SparqlQuery_1aWiki_Jenkins {

	public static void main(String[] args) throws Exception {
		//String sparqlUrl = "http://sparql.wikipathways.org";
		String sparqlUrl = "http://localhost:9999/blazegraph/sparql";
		File out = new File("sparql-resultA-wdWiki-20201116-wpallDev.txt");
		
		SparqlQuery_1aWiki_Jenkins sq = new SparqlQuery_1aWiki_Jenkins(sparqlUrl, out);
		sq.executeQuery();
	}
	
	private String sparqlUrl;
	private File out;

	public SparqlQuery_1aWiki_Jenkins(String sparqlUrl, File out) {
		this.sparqlUrl = sparqlUrl;
		this.out = out;
	}
	
	public void executeQuery() throws Exception {
		System.out.println("[INFO]\tQuerying WikiPathways SPARQL via Blazegraph.");
		String queryString = getQuery();
		
		DefaultHttpClient httpclient = new DefaultHttpClient();

		// Set credentials on the client
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("query", queryString));
        try {
       	 UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
         System.out.println("params: " + entity.toString());
       	 HttpPost httppost = new HttpPost(this.sparqlUrl);
       	 httppost.setEntity(entity);
       	 httppost.setHeader(HttpHeaders.ACCEPT, "text/tab-separated-values");
       	 HttpResponse response = httpclient.execute(httppost);
       	 StatusLine statusLine = response.getStatusLine();
       	 int statusCode = statusLine.getStatusCode();
       	 if (statusCode != 200) throw new Exception(
       		 "Expected HTTP 200, but got a " + statusCode + ": " + statusLine.getReasonPhrase()
       	 );

        	 HttpEntity responseEntity = response.getEntity();
        	 FileOutputStream outStream = new FileOutputStream(out);
        	 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        	 responseEntity.writeTo(outStream);
        	 outStream.flush();
        	 outStream.close();
        } catch (UnsupportedEncodingException exception) {
       	 throw new Exception(
                "Error while creating the SPARQL query: " + exception.getMessage(), exception
            );
        } catch (IOException exception) {
       	 throw new Exception(
                "Error while processing the SPARQL endpoint feedback: " + exception.getMessage(), exception
            );
        }
		
		System.out.println("[INFO]\tSPARQL result saved in file: " + out.getAbsoluteFile().getAbsolutePath());
	}
	
	private String getQuery() {
		// TODO: retrieve query from github repo
		String queryString = "PREFIX gpml: <http://vocabularies.wikipathways.org/gpml#> " +
			"PREFIX wp:      <http://vocabularies.wikipathways.org/wp#> " + 
			"PREFIX wprdf:   <http://rdf.wikipathways.org/> " +
			"PREFIX biopax:  <http://www.biopax.org/release/biopax-level3.owl#> " + 
			"PREFIX cas:     <http://identifiers.org/cas/> " +
				"PREFIX dc:      <http://purl.org/dc/elements/1.1/> " + 
				"PREFIX dcterms: <http://purl.org/dc/terms/> " +
				"PREFIX foaf:    <http://xmlns.com/foaf/0.1/> " + 
				"PREFIX ncbigene: <http://identifiers.org/ncbigene/> " +
				"PREFIX pubmed:  <http://www.ncbi.nlm.nih.gov/pubmed/> " + 
				"PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
				"PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#> " +
				"PREFIX skos:    <http://www.w3.org/2004/02/skos/core#> " +
				"PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#> " +
				"PREFIX cur: <http://vocabularies.wikipathways.org/wp#Curation:>" + 
				"SELECT DISTINCT ?interaction ?sourceDb ?targetDb ?mimtype ?pathway (str(?titleLit) as ?title) ?sourceCHEBI ?targetDbCHEBI ?sourceHMDB ?targetDbHMDB ?InteractionID " +
				"WHERE { " + 
				"?pathway a wp:Pathway ; " +
				"               wp:organismName \"Homo sapiens\"^^xsd:string ; " +
				"               dc:title ?titleLit . " +
				"FILTER (EXISTS {?pathway wp:ontologyTag cur:AnalysisCollection}) . " + //|| EXISTS {?pathway wp:ontologyTag cur:Reactome_Approved}) . " + //comment this line for all pathways
				"?interaction dcterms:isPartOf ?pathway . " +				
				"?interaction a wp:DirectedInteraction . " + 
				"?interaction wp:source ?source . " +
				"?interaction wp:target ?target . " +  
				"OPTIONAL{?interaction a ?mimtype}. " +
				"VALUES ?mimtype {wp:ComplexBinding wp:Conversion wp:Inhibition wp:Catalysis wp:Stimulation wp:TranscriptionTranslation wp:DirectedInteraction} . " +
				"?source a wp:Metabolite . " +
				"?source wp:bdbWikidata ?sourceDb . " +
				"OPTIONAL{?source wp:bdbChEBI ?sourceCHEBI}. " +
				"OPTIONAL{?source wp:bdbHmdb ?sourceHMDB}. " +
				"?target a wp:Metabolite . " +
				"?target wp:bdbWikidata ?targetDb . " +
				"OPTIONAL{?target wp:bdbChEBI ?targetDbCHEBI}. " +
				"OPTIONAL{?target wp:bdbHmdb ?targetDbHMDB}. " +
				"OPTIONAL{?interaction wp:bdbRhea ?InteractionID} . " +
                "}";
		return queryString;
	}
	
}
