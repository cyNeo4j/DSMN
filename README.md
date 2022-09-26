# Directed Small Molecules Network (DSMN)

[![Java CI with Maven](https://github.com/cyNeo4j/DSMN/actions/workflows/maven.yml/badge.svg)](https://github.com/cyNeo4j/DSMN/blob/main/.github/workflows/maven.yml)

# Working with the DSMN data in Neo4j:
** Note: This documentation was written for Neo4j version: community-3.5.7 (which requires Java 8 to run!).

Find our tutorial website [here](https://cyneo4j.github.io/DSMN/).
Download the community edition of Neo4j (for free) [here](https://neo4j.com/download-center/#community).
The download on Windows machines is quite straightforward, for Linux see [here](https://neo4j.com/docs/operations-manual/current/installation/linux/).

## 1. Run Neo4j

### 1.1 Windows:
- Most likely, your computer will create a shortcut on your desktop.
- Double click the icon, ans press the start button (if you cannot start Neo4j, check your version of Java!).

### 1.2 Linux 
(read the README.txt for updated instructions, when using a different version):
- On your computer, locate the location where you have downloaded Neo4j.
- Open up your terminal, and navigate to this location.
- Type "./bin/neo4j console" and hit enter to start (if you cannot start Neo4j, check your version of Java!).
- Shutdown the server by typing Ctrl-C in the console.

## 2. Download DSMN data
Find a copy of the graph.db file [here](https://doi.org/10.5281/zenodo.7113243) and **unzip** the file.

## 3. Load pre-existing database:

### 3.1 Windows
(Note: you can also use the command line for the steps listed below; see section '3.2 Linux' for more detailed instructions).
- Start up the Neo4j instance
- For first time users: you can see a user interface of Neo4j in your internetbrowser by adding this location in the search bar: http://localhost:7474/ (see section '4. Visualise and interact with data' for more details). Login the first time with the username 'neo4j' and password 'neo4j'; after this you will be prompted to submit a personal password. For next usages (also in Cytoscape with the CyNeo4j app), you will use the username 'neo4j', and the personal password you picked.
- Find the location on your computer where you downloaded the graph.db file.
- Select the correct database location folder, and click on “start” 
- After a few seconds the status bar will turn from red to green and displays the message: “Neo4j is ready…”

### 3.2 Linux

For first time users, first start your neo4j instance:
```shell
cd PATH/TO/NEO4J
./bin/neo4j console
```

Then, view the user interface of Neo4j in your internetbrowser by adding this location in the search bar: http://localhost:7474/ . Login the first time with the username 'neo4j' and password 'neo4j'; after this you will be prompted to submit a personal password. For next usages (also in Cytoscape with the CyNeo4j app), you will use the username 'neo4j', and the personal password you picked.

After the initial setup (setting your personal password), you can follow the following instructions:
```shell
cd PATH/TO/NEO4J/data/databases
rm -r graph.db
cp -r /PATH/TO/DOWNLOADED_DATA/DSMN_MONTHYEAR.graph.db/graph.db/ /PATH/TO/NEO4J/data/databases/
cd ../..
./bin/neo4j console
```
Open the remote interface (hhtp://localhost:7474) and login with your personal password.

## 4. Visualise and interact with data
 Windows+Linux:
- To see your Neo4j data, open http://localhost:7474/ in an (internet)browser.
- To see which data is loaded, click on the database icon (Top left corner, green in image below).
![image](https://user-images.githubusercontent.com/26277832/89410021-47ffe000-d723-11ea-97d2-9f522fd706f9.png)
- To create a simple query, click on one of the Node/Interaction Labels or property types.
The Neo4j server automatically creates a query and displays the results, see image below for "Metabolites".
![image](https://user-images.githubusercontent.com/26277832/89410210-957c4d00-d723-11ea-884d-3d8b474182e8.png)

# Working with the CyNeo4j App:

## 5. Download the CyNeo4j app 
From the Cytoscape App store [here]() or from within Cytoscape.

## 6. Connect to the Neo4j database 
Note: you need to have a Neo4j instance running.

Under 'Apps/cyNeo4j', select the option called 'Connect to instance'. ![image](https://user-images.githubusercontent.com/26277832/192294006-5afce3d5-00e1-4f97-944b-58fa5e497d61.png)
Fill in your username and personal password, and click 'Connect'.

## 7. Run your queries 
Note: accepted IDs for metabolites are Wikidata, ChEBI, and HMDB.

1. Under 'Apps/cyNeo4j', select the option called 'Directed Small Molecules Network'. ![image](https://user-images.githubusercontent.com/26277832/192294652-c3509687-acda-47ba-a009-426185266f30.png)
Fill in the IDs that you want to retrieve a subnetwork for (examples can be found [here](https://github.com/cyNeo4j/DSMN/tree/main/examples)), one ID per line, and click 'Submit'. The shortest path calculation will now start, and the results visualized in individual Cytoscape networks (note that this might take a few seconds, depending on the size of your input query).
2. Use the button located right under the main menu bar ![image](https://user-images.githubusercontent.com/26277832/192295417-7ff679bd-e3a0-4abb-b14a-32ce4f30c912.png) Fill in the IDs that you want to retrieve a subnetwork for (examples can be found [here](https://github.com/cyNeo4j/DSMN/tree/main/examples)), one ID per line, and click 'Submit'. The shortest path calculation will now start, and the results visualized in individual Cytoscape networks (note that this might take a few seconds, depending on the size of your input query).

## 8. Results interpretation 

The lefthand side menu (1) shows you which networks you have build, the middle top panel (2) shows the network from the shortest path query itself, the right panel (3) and overview of the results for the shortest path query, and the middle bottom panel (4) a table overview of the retrieved information (notice that you can switch here between Node, Edge, and Network Table).

![image](https://user-images.githubusercontent.com/26277832/192297248-a267efd3-f51b-4b0d-8e31-de25372e1d94.png)


<!---

## Working with the DSMN data (Docker)

Work in progress, to be updated

1. Install Docker Desktop for your specific Operating System (Windows, Mac, Linux):
follow the instructions [here](https://docs.docker.com/get-docker/)

2. Download the docker image with DSMN data [here](add link):

Windows+Mac:
TBA

Linux:
```shell
docker pull neo4j-DSMN-latest
```

3. Install Cytoscape for your OS:
Find the instructions [here](https://cytoscape.org/download.html)

4. Download the CyNeo4j app from the Cytoscape App store [here]() or from within Cytoscape.

5. Start the DSMN-docker container:

Windows+Mac:
TBA

###Update to load data!
Linux:
```shell
docker run neo4j-DSMN-latest
```

6. Connect to the Neo4j docker from within Cytoscape

7. Run your queries (accepted IDs: Wikidata, ChEBI, HMDB).

-->

## If you wish to create the DSMN data in a Neo4j Graph database yourself, follow the following steps:

GitHub repository to work on the Directed Small Molecules Network project.

Minimum System Requirements:
- Java 8 (Eclipse, Neo4j)
- WikiPathways [RDF Nov. 2020](https://doi.org/10.5281/zenodo.5776229) [GPML2RDF](https://github.com/wikipathways/GPML2RDF/commit/a16290242450d3933716a3a0d8cff1b64848b83d)

## Compiling the script
The setup of this project in Eclipse has been tested with:
* Eclipse Java Neon, OS Windows 7 
* Eclipse IDE 2019-03, OS Linux (Debian)

### From the command line
The code can also be compiled from the command line:

```shell
mvn clean install
```
