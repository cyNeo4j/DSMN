Note: This documentation was written for Neo4j version: community-3.5.7 .

Download Neo4j (for free) [here](https://neo4j.com/download-center/#community).
The download on Windows machines is quite straightforward, for Linux see [here](https://neo4j.com/docs/operations-manual/current/installation/linux/).

# Run Neo4j on Windows:
- Most likely, your computer will create a shortcut on your desktop.
- Double click the icon, ans press the start button.

# Run Neo4j on Linux 
(read the README.txt for updated instructions, when using a different version):
- On your computer, locate the location where you've downloaded Neo4j.
- Open up your terminal, and navigate to this location.
- Type "./bin/neo4j console" and hit enter to start.
- Shutdown the server by typing Ctrl-C in the console.

# Use Neo4j as Docker
- See [here](https://neo4j.com/developer/docker-run-neo4j/)

# Load pre-existing database
## Windows
- Start up the Neo4j instance
- Select the correct database location folder, and click on “start” 
- After a few seconds the status bar will turn from red to green and displays the message: “Neo4j is ready…”
## Linux
tba

# Windows+Linux: visualise and interact with data
- To see your Neo4j data, open http://localhost:7474/ in an (internet)browser.
- To see which data is loaded, click on the database icon (Top left corner, green in image below).
![image](https://user-images.githubusercontent.com/26277832/89410021-47ffe000-d723-11ea-97d2-9f522fd706f9.png)
- To create a simple query, click on one of the Node/Interaction Labels or property types.
The Neo4j server automatically creates a query and displays the results, see image below for "Metabolites".
![image](https://user-images.githubusercontent.com/26277832/89410210-957c4d00-d723-11ea-884d-3d8b474182e8.png)
