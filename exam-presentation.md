# PATHFINDING

Stifinding i vægtede grafer

**Definition**: en graf er en mængde af knuder og en mængde af vægtede kanter,
der forbinder grafens knuder.

## DIJKSTRA

Før:
  - **Kildeknuden** (*src*).
  - **Destinationsknuden** (*dest*).
  - **Den ubesøgte mængde** (*unvisited*); indeholder alle knuder, sorteret således, at
  det første element *e* minimerer dists(e).
  - **Afstandsmængden** (*dists*); afbilder alle grafens knuder i kendte mindste afstand til kildeknuden.

### ALGORITHM

```
dists(src) := 0
while **unvisited**.length > 0:
    cur := unvisited.pop()
    unvisited.remove(cur)
    if (cur == dest):  // hvis vi kun er interesserede i src -> dest, og ikke alle src -> node
        done
    foreach neighbor in neighbors(cur):
        if (dists(neighbor) > dist(cur, neighbor) + dists(cur)):
            dists(neighbor) := dist(cur, neighbor) + dists(cur)
```

### CORRECTNESS

### SHOWCASE

Gui example

## A*

### DESCRIPTION

### CORRECTNESS

### SHOWCASE

Gui example
