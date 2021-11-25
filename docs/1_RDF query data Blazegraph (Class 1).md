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
