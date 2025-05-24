# APR 2025 EXAM PORTFOLIO

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
  - `GUIMapConfig.java`
      + **singleton**

## Programming techniques (*curriculum*)

  - `GUIUtils.java`
      + `addEventHandlerRecursive` : **reflection**
  - `Stringify.java`
      + `toString` : **reflection**
