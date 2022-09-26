# Directed Small Molecules Network (DSMN)

[![Java CI with Maven](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/actions/workflows/main.yml/badge.svg)](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/actions/workflows/main.yml)

# Working with the DSMN data in Neo4j:
** Note: This documentation was written for Neo4j version: community-3.5.7 (which requires Java 8 to run!).

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
Open the remote interface (hhtp://localhost:7474) and login with the password: dsmn.

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
(note: you need to have a Neo4j instance running):

## 7. Run your queries 
(accepted IDs: Wikidata, ChEBI, HMDB).

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
