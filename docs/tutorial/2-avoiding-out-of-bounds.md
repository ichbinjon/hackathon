# Avoiding Out of Bounds

## The Plan
Currently your players ignore where they are moving to and can either walk into water, or move into the same position
that another player is already occupying, with the result that you lose players.  This step of the tutorial will add
some logic to prevent these situations from happening.

## Code Updates
### Check For Out of Bounds
The `GameState` object passed to the `initialise` and `makeMoves` methods contains a range of useful information about
the state of the map on a given turn, for example which visible positions are water, or out of bounds, and also a
map-related utility class `GameMap`.  So you need to maintain references to these objects to avoid the need to pass
them to every utility method.
*refactor game.Map once we update code*
```
private GameState gameState;
private com.scottlogic.hackathon.game.Map map;
```

Because the methods provided by the `Map` class only reference features of the map that do not change on a turn-by-turn
basis you can cache that object in the `initialise` method.
```
@Override
public void initialise(GameState gameState) {
    map = gameState.getMap();
}
```

But the `GameState` contains information relevant for the current turn and needs to be kept up-to-date, so add the
following to the top of the `makeMoves` method:
```
this.gameState = gameState;
```

That's the set-up complete, now you can make use of this new information to avoid a watery death for your players.  The
current approach will be to make a player move randomly, so add a utility class to select a random direction from the
eight available:
```
public final class RandomDirection {
    private static final Random INDEX = new Random();

    private RandomDirection() {
        // prevent instantiation of utility class
    }

    public static Direction get() {
        return Direction.values()[INDEX.nextInt(Direction.values().length)];
    }
}
```

Next you need to check where a player would end up if it moved in that direction, so add a `canMove` method which
makes use of a utility method `map.getNeighbour(position, direction)` to get the next position (taking account of the
way that the map wraps at the top/bottom and left/right) and then checks whether that position is out of bounds.
```
private boolean canMove(Player player, Direction direction) {
    Set<Position> outOfBounds = this.gameState.getOutOfBoundsPositions();
    Position newPosition = map.getNeighbour(player.getPosition(), direction);
    if (!outOfBounds.contains(newPosition)) {
        return true;
    } else {
        return false;
    }
}
```

Now you're ready to make use of these methods, so replace the following line in the `doExplore` method:
```
.map(player -> new MoveImpl(player.getId(), Direction.NORTH))
```

with:
```
.map(player -> doMove(player))
```

This calls the `doMove` method which encapsulates the movement logic for a player:
```
private Move doMove(Player player) {
    Move move = null;
    while (move == null) {
        Direction direction = RandomDirection.get();
        if (canMove(player, direction)) {
            move = new MoveImpl(player.getId(), direction);
        }
    }
    return move;
}
```

So your players shouldn't drown but you haven't addressed the issue of your players attempting to occupy the same
position, let's fix that next.

### Track Next Positions
You need a list of the positions that your players will occupy on the next turn:
```
private List<Position> nextPositions = new ArrayList<>();
```

And because you're not interested in the last turn you should clear this list at the start of the current turn so add
the following to the start of the `makeMoves` method:
```
nextPositions.clear();
```

The final step is to check this list in the `canMove` method and also add to the list as your players are assigned
moves:
```
if (!nextPositions.contains(newPosition)
        && !outOfBounds.contains(newPosition)) {
    nextPositions.add(newPosition);
    return true;
} else {
    return false;
}
```

### Testing
Again you're ready to send your upgraded bot into battle, so run another game:
```
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT-all.jar com.contestantbots.team.ExampleBot
```

This game should now last much longer, and might even end with the `TURN_LIMIT_REACHED` instead of `LONE_SURVIVOR` end
condition, congratulations you've made it to the end of a game!  But look at all those collectable items that keep
appearing while your players just mill about around the spawn point.  The [next step](3-gathering-collectables.md) will
be to start picking up the collectables and spawning more players.

## Bonus Questions
### Random Directions
Looking at the `doMove` method can you spot the deliberate flaw?  For instance, it might be possible, now that you have
more players in the game, that there isn't an empty space available for a player to move to.  Can you think how else
the random selection code might 'fail'?

### Other Players
It's possible, now that your players are moving further from the spawn point and living longer, that you'll come across
players belonging to other bots.  What happens if you try to issue moves for a player that does not belong to your bot?

(*need to confirm that this will happen in future*)
Being disqualified for issuing moves to the wrong players is not going to win you any games so you should update the
stream of players to filter out any that do not belong to your bot:
```
moves.addAll(gameState.getPlayers().stream()
        .filter(player -> isMyPlayer(player))
        .map(player -> doMove(player))
        .collect(Collectors.toList());
```

```
private boolean isMyPlayer(Player player) {
    return player.getOwner().equals(getId());
}
```
