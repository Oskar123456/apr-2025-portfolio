# APR 2025 EXAM PORTFOLIO



# Præsentation

  - Kort forklaring af korteste vej:
      + At finde vej i en vægtet graph $G = {V, E}$.
      + Hvor $e.weight >= 0$ for alle e i E.
      
  - Kort forklaring af dijkstra
      + Korrekthed:
          - Kort forklaring. 
      + Showcase selve dijkstra implementation:
          - Køretid: $O(|V| + |E| lg(|V|))$ -- vi kører igennem alle knuder èn
            gang, alle edges, og for hver edge relaxerer vi, som koster $O(lg|V|)$.
  
  - Kort forklaring af A*
      + Korrekthed:
          - Heuristisk funktions estimat *må* aldrig overstige mulig/aktuel distance.
      + Showcase selve A* implementation.
  
  - Vis projekt
      + Snak om design: hvordan vi konvertere fra OSM XML data til vores eget format,
        som vi kan lave om til GUI i vores GUIFactory, og derefter til en
        generisk graph, indeholdene den originale data, dvs. mapknuder med
        længde- og breddegrader.
      + **Demo**.
      + Snak om design pattern:
          - Observer

  - Cycle detection
      + In singly linked list: rabbit & hare algorithm.
      + In graph: see algorithm section in notes.
  

## Korteste vej



### Dijkstra

```
for alle v:
  G.dist(v) := inf
G.dist(src) := 0

Visited := {}
PQ := {V}

while PQ not empty:
  u = PQ.extractMin
  Visited += u
  for e in G.outgoingEdges(u):
    if G.dist(e.dest) > G.dist(u) + e.weight:
      G.dist(e.dest) := G.dist(u) + e.weight
      G.srcs(e.dest) := u
      PQ.update(e.dest, G.dist(u) + e.weight)
```

**Invariant**: PQ = V - Visited

**Greedy algorithm**: it picks the closest unvisited node on each iteration.

**Correctness**:

  - *At the start of each iteration, dist(v) is optimal for all v in Visited*.

### A*

# NOTES 

MVVC / Observer Pattern er interessant

## DESIGN PATTERNS

### STRUCTURAL

#### Composite

  - Filsystem, GUI- og scene layout (game engine).

#### Decorator

  - Add or change behavior of classes, by wrapping them in decorator.
  - Wrapping classes; example: unbuffered I/O -> buffered I/O.

#### Proxy
#### Adapter
  
### CREATIONAL

#### Singleton
#### Factory
#### Builder

### BEHAVIORAL

#### Command
#### Strategy
#### Observer

## Algorithm

### Cycle detection in graph

```java
  // Function to perform DFS and detect cycle in a
  // directed graph
  private static boolean isCyclicUtil(List<Integer>[] adj,
                                      int u,
                                      boolean[] visited,
                                      boolean[] recStack)
  {
      // If the current node is already in the recursion
      // stack, a cycle is detected
      if (recStack[u])
          return true;

      // If already visited and not in recStack, it's not
      // part of a cycle
      if (visited[u])
          return false;

      // Mark the current node as visited and add it to
      // the recursion stack
      visited[u] = true;
      recStack[u] = true;

      // Recur for all adjacent vertices
      for (int v : adj[u]) {
          if (isCyclicUtil(adj, v, visited, recStack))
              return true;
      }

      // Backtrack: remove the vertex from recursion stack
      recStack[u] = false;
      return false;
  }
```
