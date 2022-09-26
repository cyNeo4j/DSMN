Note: This documentation was written for Neo4j version: community-3.5.x .

Download Neo4j (for free) [here](https://neo4j.com/download-center/#community).
The download on Windows machines is quite straightforward, for Linux see [here](https://neo4j.com/docs/operations-manual/current/installation/linux/).

# Run Neo4j on Windows:
- Most likely, your computer will create a shortcut on your desktop.
- Double click the icon, and press the start button.

# Run Neo4j on Linux 
(read the README.txt for updated instructions, when using a different version):
- On your computer, locate the location where you've downloaded Neo4j.
- Open up your terminal, and navigate to this location.
- Type "./bin/neo4j console" and hit enter to start.
- Shutdown the server by typing Ctrl+C in the console.

# Use Neo4j as Docker
- See [here](https://neo4j.com/developer/docker-run-neo4j/)

# Load pre-existing database

## Data Download
Find a copy of the graph.db file [here](https://doi.org/10.5281/zenodo.7113243) and **unzip** the file.

Unzip the file called 'DSMN_*releaseDate*.graph.db' after downloading.

### Windows
(Note: you can also use the command line for the steps listed below; see section '3.2 Linux' for more detailed instructions or the  [Neo4j documentation for Windows](https://neo4j.com/docs/operations-manual/current/installation/windows/) ).
- Start up the Neo4j instance
- For first time users: you can see a user interface of Neo4j in your internetbrowser by adding this location in the search bar: http://localhost:7474/ (see section '4. Visualise and interact with data' for more details). Login the first time with the username 'neo4j' and password 'neo4j'; after this you will be prompted to submit a personal password. For next usages (also in Cytoscape with the CyNeo4j app), you will use the username 'neo4j', and the personal password you picked.
- Find the location on your computer where you downloaded the graph.db file.
- Select the correct database location folder, and click on “start” 
- After a few seconds the status bar will turn from red to green and displays the message: “Neo4j is ready…”
- If you want to work from the command line in Windows, please see the original.

### Linux
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

### Mac/Apple:
Our approach has not been tested on a Mac, however the steps provided above for Linux should suffice. Consult the [Neo4j macOS](https://neo4j.com/docs/operations-manual/current/installation/osx/) installation guidelines for additional information.

# Windows + Linux: visualise and interact with data
- To see your Neo4j data, open http://localhost:7474/ in an (internet)browser.
- To see which data is loaded, click on the database icon (Top left corner, green in image below).
![image](https://user-images.githubusercontent.com/26277832/89410021-47ffe000-d723-11ea-97d2-9f522fd706f9.png)
- To create a simple query, click on one of the Node/Interaction Labels or property types.
The Neo4j server automatically creates a query and displays the results, see image below for "Metabolites".
![image](https://user-images.githubusercontent.com/26277832/89410210-957c4d00-d723-11ea-884d-3d8b474182e8.png)
