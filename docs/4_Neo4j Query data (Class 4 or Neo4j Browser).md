When testing (new) queries, one can use the browser or Eclipse. 

Some thoughts on pros and cons (with examples at the bottom if applicable):

### Browser

++ 1. Browser visualizes results automatically

++ 2. Browser has autocomplete for node/edge labels + properties

++ 3. Browser shows "query execution plan"

-- 4. Browser can be quite slow in visualizing results of complex querie(s).

-- 5. Browser timer is not that accurate

### Eclipse
++ 6. Eclipse is fast in executing a query (without visualisation)

++ 7. Eclipses can time execution of queries very accurate

-- 8. Obtaining results of Nodes with different properties needs quite some coding 


### Examples:

2. To change the visualization in the GUI, see the extensive (Neo4j documentation)[https://neo4j.com/developer/neo4j-browser/] and example below.

- 1 Click on one of the Node labels to select it (top left corner of query results visualization window).
- 2 Select the visualization option you desire in the panel (bottom left corner of query results visualization window)

![image](https://user-images.githubusercontent.com/26277832/124771155-34fd1d80-df3b-11eb-84fe-d5a4f04a3f87.png)


3. A more complex query is the shortest path, especially when performed on a (large) [set of Nodes](https://neo4j.com/developer/kb/all-shortest-paths-between-set-of-nodes/). When [executing shortest path, Neo4j](https://neo4j.com/docs/cypher-manual/current/execution-plans/shortestpath-planning/) can use two algorithms, a fast algorithm (bidirectional breadth-first search), or a much slower exhaustive depth-first search algorithm. Therefore it is important to plan how the shortest path is executed; calculating all paths for all nodes is computationally much more time consuming than only using a handful of nodes. Add the "EXPLAIN" term in front of a query, click run and in the result section on "plan" (see also image below).
![image](https://user-images.githubusercontent.com/26277832/100460396-640b0100-30c7-11eb-8d64-fc0ae15b1ef7.png)
