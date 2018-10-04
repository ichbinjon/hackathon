# Exploring the Map

## The Plan
Due to the random directions being assigned on each turn your players are currently just milling around the spawn point
and need an incentive to move away and explore, this will serve two purposes:
- reduce the congestion around the spawn point reducing the chances that a player cannot move
- increase the visible proportion of the map and therefore increase your chances of spotting collectables

## Code Updates
To achieve this you need to track unseen areas of the map and assign players to explore these locations, obviously
this is a secondary priority to gathering collectables because you don't want to run out of intrepid explorers!

### Preparation
First of all let's refactor the route generation and assignment code into helper methods as this will avoid duplicated
code once you start adding routes to unseen positions:
```
private List<Route> generateRoutes(Set<Player> players, Set<Position> destinations) {
    List<Route> routes = new ArrayList<>();
    for (Position destination : destinations) {
        for (Player player : players) {
            int distance = map.distance(player.getPosition(), destination);
            Route route = new Route(player, destination, distance);
            routes.add(route);
        }
    }
    return routes;
}
```
```
private List<Move> assignRoutes(List<Route> routes) {
    return routes.stream()
            .filter(route -> !assignedPlayerDestinations.containsKey(route.getPlayer())&& !assignedPlayerDestinations.containsValue(route.getDestination()))
            .map(route -> {
                Optional<Direction> direction = map.directionsTowards(route.getPlayer().getPosition(), route.getDestination()).findFirst();
                if (direction.isPresent() && canMove(route.getPlayer(), direction.get())) {
                    assignedPlayerDestinations.put(route.getPlayer(), route.getDestination());
                    return new MoveImpl(route.getPlayer().getId(), direction.get());
                }
                return null;
            })
            .filter(move -> move != null)
            .collect(Collectors.toList());
}
```

Now you can make use of the new methods in `doCollect`:
```
List<Route> collectableRoutes = generateRoutes(players, collectablePositions);
```
```
collectMoves.addAll(assignRoutes(collectableRoutes));
```

### Track Unseen Positions
Add a set to manage the unseen positions in the map:
```
private Set<Position> unseenPositions = new HashSet<>();
```

And add the following to the `initialise` method as this only needs to be done once:
```
// add all positions to the unseen set
for (int x = 0; x < map.getWidth(); x++) {
    for (int y = 0; y < map.getHeight(); y++) {
        unseenPositions.add(new Position(x, y));
    }
}
```

*make the existing utility methods for this public?*
Before processing your moves, you need to update the set of unseen positions to remove those that are visible on the
current move. The distance that a player can see is not provided as part of the game or map state, let's 'guess' a
distance and then add methods to calculate the positions that your players can see and remove them from the tracked set:
```
private void updateUnseenLocations() {
    // assume players can 'see' a distance of 5 squares
    int visibleDistance = 5;
    final Set<Position> visiblePositions = gameState.getPlayers()
            .stream()
            .filter(player -> isMyPlayer(player))
            .map(player -> player.getPosition())
            .flatMap(playerPosition -> getSurroundingPositions(playerPosition, visibleDistance))
            .distinct()
            .collect(Collectors.toSet());

    // remove any positions that can be seen
    unseenPositions.removeIf(position -> visiblePositions.contains(position));
}
```

As you can see above, the initial guess is 5, does this make much difference to the performance of your bot?
```
private Stream<Position> getSurroundingPositions(Position position, int distance, boolean includeOrigin) {
    Stream<Position> positions = Arrays.stream(Direction.values())
            .flatMap(direction -> IntStream.rangeClosed(1, distance)
                    .mapToObj(currentDistance -> map.getRelativePosition(position, direction, currentDistance)));

    if (includeOrigin) {
        positions = Stream.concat(Stream.of(position), positions);
    }

    return positions;
}
```

And finally call the update method from `makeMoves`:
```
updateUnseenLocations();
```

### Assign Players to Explore
All that remains to be done is to add a `doExploreUnseen` method which will generate routes for each available player
to an unseen position and then assign them in much the same way as you did for collectables.
```
private List<Move> doExploreUnseen() {
    List<Move> exploreMoves = new ArrayList<>();

    Set<Player> players = gameState.getPlayers().stream()
            .filter(player -> isMyPlayer(player))
            .filter(player -> !assignedPlayerDestinations.containsKey(player))
            .collect(Collectors.toSet());

    List<Route> unseenRoutes = generateRoutes(players, unseenPositions);

    Collections.sort(unseenRoutes);
    exploreMoves.addAll(assignRoutes(unseenRoutes));

    System.out.println(exploreMoves.size() + " players exploring unseen");
    return exploreMoves;
}
```

Finally, you need to add a call to that method after the `doCollect` call:
```
moves.addAll(doExploreUnseen());
```

### Testing
Again you're ready to send your upgraded bot into battle, so run another game:
```
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT-all.jar <fully_qualified_bot_class_name>
```

Your players should now be exploring the map and collecting all visible food, with the result that your bot will
almost certainly survive to the `TURN_LIMIT_REACHED` end condition every time. If you're lucky you might even manage
to wander onto an enemy spawn point and end the game early, how about adding that as another goal before gathering
collectables and exploring? See the [next step](5-destroying-spawn-points.md) for more details.

## Bonus Questions
### Available Players
Reviewing your code you can probably spot a number of blocks of code which are virtually identical, e.g. obtaining a
stream of unassigned players, perhaps that could be refactored into a utility method?
