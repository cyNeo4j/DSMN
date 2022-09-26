1. Download Blazegraph [here](https://blazegraph.com/).
1. Download RDF data [here](https://wikipathways-data.wmcloud.org/20210310/rdf/) ; file called wikipathways-2021xx10-rdf-wp.zip .
1. Start Blazgraph (make sure to remove old data, ending with .ttl and .jnl in folder where Blazegraph is downloaded):

### Windows (through git bash, note that a adjusted [properties file](add_link_to_file) is needed to allow for large file formats.)
``` 
cd C:\Users\NAME_USER\FOLDER_WITH_BLAZEGRAPH\Blazegraph
java -jar blazegraph.jar propertyFile (RWStore.properties)
```

### Linux (from the command line)
```
cd ~/FOLDER_WITH_BLAZEGRAPH/Blazegraph
java -jar blazegraph.jar
```

To write shorter SPARQL queries, prefixes can be used. In the WikiPathways SPARQL endpoint, several prefixes are loaded by default; in Blazegraph these need to be loaded before each query, and are listed [here](https://www.wikipathways.org/index.php/Help:WikiPathways_SPARQL_queries#Prefixes).

Queries:
1. Metabolic Conversions
```SPARQL
SELECT DISTINCT ?interaction ?sourceDb ?targetDb ?mimtype ?pathway (str(?titleLit) as ?title) ?sourceCHEBI ?targetDbCHEBI ?sourceHMDB ?targetDbHMDB ?InteractionID  WHERE {  
?pathway a wp:Pathway ;
                wp:organismName "Homo sapiens"^^xsd:string ; 
	     dc:title ?titleLit . 	
FILTER (EXISTS {?pathway wp:ontologyTag cur:AnalysisCollection}  .
FILTER EXISTS {?pathway wp:ontologyTag cur:Reactome_Approved}) .    
FILTER (EXISTS {?pathway wp:ontologyTag cur:LIPID_MAPS}) .
?interaction dcterms:isPartOf ?pathway ;			
            a wp:DirectedInteraction ;
	wp:source ?source ;
	wp:target ?target .  
OPTIONAL{?interaction a ?mimtype}. 
VALUES ?mimtype {wp:ComplexBinding wp:Conversion wp:Inhibition wp:Catalysis wp:Stimulation wp:TranscriptionTranslation wp:DirectedInteraction} .  
?source a wp:Metabolite . 
?source wp:bdbWikidata ?sourceDb . 
OPTIONAL{?source wp:bdbChEBI ?sourceCHEBI}.
OPTIONAL{?source wp:bdbHmdb ?sourceHMDB}.
?target a wp:Metabolite . 
?target wp:bdbWikidata ?targetDb . 
OPTIONAL{?target wp:bdbChEBI ?targetDbCHEBI}. 
OPTIONAL{?target wp:bdbHmdb ?targetDbHMDB}. 
OPTIONAL{?interaction wp:bdbRhea ?InteractionID} .
}
ORDER BY DESC(?InteractionID)
```
2. Pathway Metadata
```SPARQL
SELECT DISTINCT ?interaction ?sourceDb ?targetDb ?PWOnt ?DiseaseOnt ?curationstatus ?InteractionRef ?PWref ?sourceLit ?targetLit WHERE {
?pathway a wp:Pathway ; 
                 wp:organismName "Homo sapiens"^^xsd:string ; 
                 dc:title ?titleLit . 
?interaction dcterms:isPartOf ?pathway ; 
		a wp:DirectedInteraction ; 
		wp:source ?source ;
		wp:target ?target . 
?source a wp:Metabolite . 
?source wp:bdbWikidata ?sourceDb . 
?target a wp:Metabolite . 
?target wp:bdbWikidata ?targetDb . 
OPTIONAL{?pathway wp:pathwayOntologyTag ?PWOnt} . 
OPTIONAL{?pathway wp:diseaseOntologyTag ?DiseaseOnt} . 
OPTIONAL{?pathway wp:ontologyTag ?curationstatus} . 
OPTIONAL{?interaction dcterms:bibliographicCitation ?InteractionRef} . 
OPTIONAL{?pathway dcterms:references ?PWref} . 
OPTIONAL{?source dcterms:bibliographicCitation ?sourceLit} . 
OPTIONAL{?target dcterms:bibliographicCitation ?targetLit} . 
}
```
3. 
```SPARQL
SELECT DISTINCT ?interaction ?sourceDb ?targetDb ?proteinDBWPs ?proteinName WHERE {  
?pathway a wp:Pathway ; 
            wp:ontologyTag cur:AnalysisCollection ;
            wp:organismName "Homo sapiens"^^xsd:string ; 
            dc:title ?titleLit . 
?interaction dcterms:isPartOf ?pathway ;				
            a wp:DirectedInteraction .  
	wp:source ?source . 
	wp:target ?target .   
?source a wp:Metabolite . 
?source wp:bdbWikidata ?sourceDb . 
?target a wp:Metabolite . 
?target wp:bdbWikidata ?targetDb . 
?interactions2 dcterms:isPartOf ?pathway;
                       a wp:Catalysis; 
                      wp:source ?sources2;
                     wp:target ?interaction . 
OPTIONAL{?sources2 wp:bdbEnsembl ?proteinDBWPs}.
OPTIONAL{?sources2 rdfs:label ?proteinName} .
```
