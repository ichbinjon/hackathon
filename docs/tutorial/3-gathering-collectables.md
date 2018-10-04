# Gathering Collectables

## The Plan
You need to gather collectable items so that you can spawn new players to increase the size of your army, with more
players you can occupy more of the map and react to new collectable items before your opponents thus increasing the
size of your army, etc. etc.

## Code Updates
Given the importance of growing your army let's assign players to gather collectables before assigning the remaining
players to exploration duties.  Add the following method:
```
private List<Move> doCollect() {
    List<Move> collectMoves = new ArrayList<>();
    System.out.println(collectMoves.size() + " players collecting");
    return collectMoves;
}
```

And then add a call to `doCollect` above the call to `doExplore`:
```
moves.addAll(doCollect());
```

### Track Collectables and Players
First things first, you need to know where the collectables are and also a list of your players, so let's add some code
to the `doCollect` method, after the list initialisation, for those two tasks:
```
Set<Position> collectablePositions = gameState.getCollectables().stream()
        .map(collectable -> collectable.getPosition())
        .collect(Collectors.toSet());
Set<Player> players = gameState.getPlayers().stream()
        .filter(player -> isMyPlayer(player))
        .collect(Collectors.toSet());
```

### Calculate Distances and Routes
Next you want to assign a player to gather each collectable, it makes sense to assign the player closest to each
collectable to that task so you need to calculate the distances from all players to all collectables using another
utility method `map.distance(from, to)`.  Add the following code next:
```
List<Route> collectableRoutes = new ArrayList<>();
for (Position collectablePosition : collectablePositions) {
    for (Player player : players) {
        int distance = map.distance(player.getPosition(), collectablePosition);
        Route route = new Route(player, collectablePosition, distance);
        collectableRoutes.add(route);
    }
}
```

The `Route` data structure is given below:
```
public class Route implements Comparable<Route> {
    private final Player player;
    private final Position destination;
    private final int distance;
    public Route(Player player, Position destination, int distance) {
        this.player = player;
        this.destination = destination;
        this.distance = distance;
    }
    public Player getPlayer() {
        return player;
    }
    public Position getDestination() {
        return destination;
    }
    public int getDistance() {
        return distance;
    }
    @Override
    public int compareTo(Route o) {
        return distance - o.getDistance();
    }
}
```

This class implements the `Comparable` interface which means you can make use of the `Collections.sort` utility method
to order the routes based on distance:
```
Collections.sort(collectableRoutes);
```

### One Collectable Per Player
It makes sense to ensure that each player/collectable is only assigned once so add a `Map` to manage these assignments:
```
private Map<Player, Position> assignedPlayerDestinations = new HashMap<>();
```

Now everything is in place so that you can work through the routes and assign your players accordingly using yet another
utility method: `map.directionsTowards(from, to)`, this returns a `Stream` of `Direction`s that a player could make
that will reduce the distance between the starting position and the destination.
```
for (Route route : collectableRoutes) {
    if (!assignedPlayerDestinations.containsKey(route.getPlayer())
            && !assignedPlayerDestinations.containsValue(route.getDestination())) {
        Optional<Direction> direction = map.directionsTowards(route.getPlayer().getPosition(), route.getDestination()).findFirst();
        if (direction.isPresent() && canMove(route.getPlayer(), direction.get())) {
            collectMoves.add(new MoveImpl(route.getPlayer().getId(), direction.get()));
            assignedPlayerDestinations.put(route.getPlayer(), route.getDestination());
        }
    }
}
```

Notice the double check on the `assignedPlayerDestinations` map, once to check the keys for the current player and then
again to check the values for the current collectable. If both checks are negative, i.e. neither the player nor the
collectable have already been assigned, then a new assignment is added to the map and the relevant first move is
added to the list of collection moves.

### Testing
Again you're ready to send your upgraded bot into battle, so run another game:
```
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT-all.jar <fully_qualified_bot_class_name>
```

Your bot should now survive to the `TURN_LIMIT_REACHED` end condition every time, and if you're lucky you might even
see the `LONE_SURVIVOR` end condition with just your bot remaining, congratulations you've just **won** a game! But
your players are now doing a very good impression of a swarm of bees, you can fix that by attempting to
[explore the map](4-exploring-the-map.md).

## Bonus Questions
### Calculation Efficiency
Does it make sense to recalculate the routes every time? Or is there some scope for improving the efficiency of the
algorithm?

### Routing Efficiency
Conversely it might make sense to separately manage the assignment of collectables that have just appeared and reassign
players to gather these items if the new collectable is nearer than the originally assigned destination.
