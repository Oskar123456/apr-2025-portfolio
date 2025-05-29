# APR 2025 EXAM PORTFOLIO

## TODO

  - MapEdge traveltime weirdness
  - Think about visiting nodes in alg that already have been visited, how do we avoid it? if PQ was a unique set/map
  - Animate path finding

## NOTES

Problem: `StreetMap -> Graph -> PathFinder -> Graph -> MapPath -> Display StreetMap + MapPath`
   
## Data

OSM data format (`MapData`):

  + nodes
  + ways
  + addresses
  + buildings

Map format (`StreetMap`):

  - `MapNode` [IGUI]
      + id
      + lon, lat
      + draw()
  - `MapEdge`
      + id
      + src, dest
      + dist, weight
      + mapway ref
      + draw()
  - `MapWay`
      + id
      + edges
      + addr details
      + draw()
  - `MapAddress`
      + id
      + node
      + addr details
      + draw()
  - `MapBuilding`
      + id
      + nodes
      + draw()

## Pathing

### DS

Graph (`Graph`):

  + nodes (`Node`)
  + edges (`Edge`)

### Alg

Path finder interface (`IPathFinder`):

  + `search(graph, src, dest)`

Need to map `StreetMap -> edges w/ weights + nodes -> graph -> alg -> graph -> edges w/ weights -> MapRoute`

  - take edges & nodes from streetmap
  - for each mapnode make node with original mapnode's ID as its data
  - for each mapedge, make edge with proper weight, and mapedge's info as its data

## App Driver

Responsibilities:

  - Controls application
  - Makes GUI out of a StreetMap
  - Calls pathfinding
  - Draws paths, replays of pathfinding

Has: 

  + StreetMap

## Design Patterns

  - `GUIFactory.java`
      + **factory**
  - `ApplicationConfig.java`
      + **singleton**

## Programming techniques (*curriculum*)

  - `GUIUtils.java`
      + `addEventHandlerRecursive` : **reflection**
  - `Stringify.java`
      + `toString` : **reflection**
