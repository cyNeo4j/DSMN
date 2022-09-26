Data can be queried through:

1. cyNeo4j app:

Details on how to use this app is available [here](https://github.com/cyNeo4j/cyNeo4j).

**Connect to the Neo4j database** 
Note: you need to have a Neo4j instance running.

Under 'Apps/cyNeo4j', select the option called 'Connect to instance'. Fill in your username and personal password, and click 'Connect'. ![image](https://user-images.githubusercontent.com/26277832/192294006-5afce3d5-00e1-4f97-944b-58fa5e497d61.png)


**Run your queries** 
Note: accepted IDs for metabolites are Wikidata, ChEBI, and HMDB.

1. Under 'Apps/cyNeo4j', select the option called 'Directed Small Molecules Network'. Fill in the IDs that you want to retrieve a subnetwork for (examples can be found [here](https://github.com/cyNeo4j/DSMN/tree/main/examples)), one ID per line, and click 'Submit'. The shortest path calculation will now start, and the results visualized in individual Cytoscape networks (note that this might take a few seconds, depending on the size of your input query). ![image](https://user-images.githubusercontent.com/26277832/192294652-c3509687-acda-47ba-a009-426185266f30.png)

2. Use the button located right under the main menu bar. Fill in the IDs that you want to retrieve a subnetwork for (examples can be found [here](https://github.com/cyNeo4j/DSMN/tree/main/examples)), one ID per line, and click 'Submit'. The shortest path calculation will now start, and the results visualized in individual Cytoscape networks (note that this might take a few seconds, depending on the size of your input query).

![image](https://user-images.githubusercontent.com/26277832/192295417-7ff679bd-e3a0-4abb-b14a-32ce4f30c912.png)


**Results interpretation** 

The lefthand side menu (1) shows you which networks you have build, the middle top panel (2) shows the network from the shortest path query itself, the right panel (3) and overview of the results for the shortest path query, and the middle bottom panel (4) a table overview of the retrieved information (notice that you can switch here between Node, Edge, and Network Table).

![image](https://user-images.githubusercontent.com/26277832/192297248-a267efd3-f51b-4b0d-8e31-de25372e1d94.png)

2. Remote interface (available at http://localhost:7474/ ):

![image](https://user-images.githubusercontent.com/26277832/90616790-2cf69b00-e20e-11ea-82c4-11061f9421ff.png)
