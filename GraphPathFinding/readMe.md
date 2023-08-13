Created a graph pathfinding tool to help Pacman solve mazes. Was given the field as input, represented as a simple text file. The start-point and end point are indicated in this text file, as well as the layout of the field (the location of walls). Program produces a similar text file, but with the shortest path from the start-point to the end-point added to the field. If there is no path at all, the path indicators will simply not exist in the output.

To solve this problem, the maze is represented as a graph, and performs a breadth-first search from Pacman's starting location.
