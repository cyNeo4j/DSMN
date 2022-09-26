The Neo4j data model is constructed in the following format:

![image](https://user-images.githubusercontent.com/26277832/192291111-ec44a953-52ef-484f-8a70-c085e67b36ba.png)

Class 3 consist out of 7 individual steps, marked as A-G.
Please execute these classes in this order.

- [FillNeo4jDb_3A_All.java](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3A_All.java) : Removes existing data, and adds data from Wikipathways (curated and uncurated), Reactome and LipidMaps pathways (integrated [here](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FilterInteractionsAll_2.java)).

- FillNeo4jDb_" [3B](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3B_Wiki.java) / [3C](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3C_Reactome.java) / [3D](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3D_LIPIDMAPS.java) " updates the interaction data with the WikiPathways (3B), Reactome (3C) and Lipid Maps (3D) data.

- The combined Data Analysis Collection is loaded through scripts [3B2](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3B2_Wiki.java) / [3C2](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3C2_Reactome.java) / [3D2](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3D2_LIPIDMAPS.java).

- [FillNeo4jDb_3E_Side](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3E_Side.java) Relabels the interactions between side metabolites, and removes interactions from main to side metabolites (and vice versa).

- [FillNeo4jDb_3F_CHEBIHMDB](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3F_CHEBIHMDB.java) adds the identifier links from ChEBi and HMDB to Wikidata (the main identifier of the network).

- [FillNeo4jDb_3G_DatabaseInfo](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/blob/master/Code/DSMNProject/src/msk/dsmn/FillNeo4jDb_3G_DatabaseInfo.java) provided some queries on the final data in Neo4j, to show the Node and interaction size.
