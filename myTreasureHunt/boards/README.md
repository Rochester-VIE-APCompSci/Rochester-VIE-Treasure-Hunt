# Board Level Descriptions
We designed the boards to allow for a wide range of solutions. While most boards make it easy to find non-optimal solutions, finding the best solution progresses from easy to (we think) impossible as you advance levels.

We recommend that you try to traverse the maze using different approaches than your classmates. That helps the class as a whole by increasing the chance that one of you will find the treasure quickly.

For (very) advanced students, we suggest learning about path finding algorithms, as these can make a big difference in improving your score.

## Regular boards


### Level 0
|Feature|Value|
----| ---
Number of treasures | 1
Type of (non-edge) obstacles | None
Open path exists | Yes
Open path is optimal | Yes
Optimal solution has moves away from treasure | No
Dead ends | No

These are open boards with a single treasure. In order to succeed, you need only make moves in the direction of the treasure.

These boards are extremely easy for humans to solve, but they are more complex than they appear at first. There are a lot of possible moves that might be made at each step. Analyzing all combinations and finding the optimal path can become too expensive for a computer, even for smaller boards. Some "advanced" solutions might need some creativity.

### Level 1
|Feature|Value|
----| ---
Number of Treasures | 1
Type of (non-edge) Obstacles | Anything other than walls
Open path exists | Yes
Open path is optimal | Yes
Optimal solution has moves away from treasure | No
Dead ends | No


Level 1 boards add non-wall obstacles. Stepping on these obstacles will reduce your score much more than stepping on an open space. An optimal path that does not require stepping on an obstacle will always exist. Further more all steps on this solution will be in the direction of the treasure. There are no dead ends on these boards.

A simple algorithm for improving your score on these boards might be to choose a non-obstacle move in the direction of the treasure.

### Level 2
|Feature|Value|
----| ---
Number of treasures | 1
Type of (non-edge) obstacles | Anything other than walls
Open path exists | Yes
Open path is optimal | Yes
Optimal solution has moves away from treasure | No
Dead ends | Yes

Level 2 boards add some dead ends. This means that (at any point) there may be two paths in the direction of the treasure, one of which has a dead end.

There are a couple of options here:
1) Handle the dead end by going through the obstacle to get to the treasure. (This is not optimal, but it will get you there).
2) Backtrack your steps until you reach a square that has an open & unexplored path in the direction of the treasure, and explore that path.
3) Backtracking is still not quite optimal, since you've wasted steps going down a road with a dead end. The best solutions will analyze the board ahead of time, and choose a path that avoids dead ends all together. This approach is addressed in college level algorithm courses, and we don't expect you to understand it now. However, if you are interested, these are some algorithms to check out:
* https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
* https://en.wikipedia.org/wiki/Depth-first_search

Because all solutions are in the direction of the treasure, you have a most two moves to consider each time a step is made.

### Level 3
|Feature|Value|
----| ---
Number of treasures | 1
Type of (non-edge) obstacles | Anything other than walls
Open path exists | Yes
Open path is optimal | Yes
Optimal solution has moves away from treasure | Yes
Dead ends | Yes

This builds on level 2 by allowing the solution path to temporarily move away from the treasure. This means that in general you cannot assume that the best move is toward the treasure at this level and above.

This is a true maze-solver solution, which is a very hard problem. When you have great solutions here you have risen to the challenge and should be proud of your accomplishments.

## Bonus Boards

Level 3 is a hard problem, and starting with level 4 we've made it (much) more difficult. Optimal solutions are extremely difficult to calculate. (Most of the volunteers can't do it with the resources that you have).

So with these boards, you want to get a solution that is as close to optimal as possible, and maybe be happy with any reasonable solution.

---

### Level 4
|Feature|Value|
----| ---
Number of treasures | Many
Type of (non-edge) obstacles | None
Open path exists | Yes
Open path is optimal | Yes
Dead ends | No

Level 4 has many treasures on an open board. This seems trivial, but it quickly becomes complicated when you think about how to go from treasure to treasure in the most efficient way. This is a very hard problem that you don't have to solve...you just have to figure out how to be better than the other schools.

### Level 5
|Feature|Value|
----| ---
Number of treasures | Many
Type of (non-edge) obstacles | Anything other than walls
Open path exists | Yes
Open path is optimal | Yes
Dead ends | Yes

Level 5 extends level 4 by adding some obstacles. There is a path through the maze that secures all the treasure, finding it is not so easy.

### Level 6
|Feature|Value|
----| ---
Number of treasures | Many
Type of (non-edge) obstacles | Walls
Open path exists | Yes
Open path is optimal | Yes
Dead ends | Yes

Level 6 uses walls for all the obstacles. In order to get the treasure, you must find an open path through the maze. Stepping on a wall will consume all your remaining steps.

### Level 7
|Feature|Value|
----| ---
Number of treasures | Many
Type of (non-edge) obstacles | Anything other than walls
Open path exists | Yes
Open path is optimal | No
Dead ends | Yes

Up until level 7, the best solutions avoided obstacles. At this level, the best solution may be in fact to step on an obstacle. An open path will always exist to the treasures, however the cost of following it may be greater than going through the obstacle.

This is a VERY hard AI problem in general. Exploring all possible combinations may be the only way, but this will take more CPU time than we give your algorithm to run. The best approach might be to consider a couple of candidate solutions, and pick the best one..and this approach is still not trivial.

### Level 8
|Feature|Value|
----| ---
Number of treasures | Many
Type of (non-edge) obstacles | Anything other than walls
Open path exists | No
Dead ends | Yes

Level 8 is the final level. At this level, the only way to get to the treasure is to go through an obstacle. This is a good test of what your path finding algorithm does. If it considers only non-obstacle paths, what does it do if there is no such path.

A simple fall back of "go straight for the treasure(s) if I can't find an open path" might pay big dividends at this level.
