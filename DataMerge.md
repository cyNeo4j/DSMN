The original data formats are all converted to the native PathVisio pathway model language (GPML), which is subsequently converted to a semantic model (RDF). Three different [queries](tba) are executed to retrieve the metabolic reaction data from the RDF data.

![image](https://user-images.githubusercontent.com/26277832/192289862-e7de44ef-d232-493f-9c91-280391c2ddf5.png)

The data cleanup consisted out of merging metabolic reactions based on the combination of unique Wikidata identifiers for the source and target metabolites, for each reaction. Alos, a unique reaction ID was added for each unique source-target combination. 
