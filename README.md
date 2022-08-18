# Directed Small Molecules Network (DSMN)

[![Java CI with Maven](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/actions/workflows/main.yml/badge.svg)](https://github.com/mkutmon/DirectedSmallMoleculesNetwork/actions/workflows/main.yml)

## Working with the DSMN data

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

Linux:
```shell
docker run neo4j-DSMN-latest
```

6. Connect to the Neo4j docker from within Cytoscape

7. Run your queries (accepted IDs: Wikidata, ChEBI, HMDB).

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
