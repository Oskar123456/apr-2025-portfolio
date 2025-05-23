# APR 2025 EXAM PORTFOLIO

## NOTES

Problem: `StreetMap -> Graph -> PathFinder -> Graph -> MapPath -> Display StreetMap + MapPath`

  1. Internal OSM data format `MapData.java`
   - Ways (nodes, type, maxspeed, name)
   - Nodes (lon, lat)
   - Buildings (nodes, type?)
   - Addresses (node, addr-info...)
  2. Map edge format `MapEdge.java`
   - Source and destination map-node reference
   - weight
   - way name
  2. Map format `StreetMap.java`
   - Set of all nodes, buildings, addresses, pathable ways
   - Add footpaths from address-nodes to a new node created on the closest point
   on address' associated street
   - Set of all map-edges with their calculated weights
  3. Map path format `MapPath.java`
   - Collection of map-edges

OSM data format:

  + nodes
  + ways
  + addresses
  + buildings

Map format:

  - `MapNode` [IGUI]
   + id
   + lon, lat
   ____
   + draw()
  - `MapEdge`
   + id
   + src, dest
   + dist, weight
   ____
   + draw()
  - `MapWay`
   + id
   + edges
   + addr details
   ____
   + draw()
  - `MapAddress`
   + id
   + node
   + addr details
   ____
   + draw()
  - `MapBuilding`
   + id
   + nodes
   ____
   + draw()
