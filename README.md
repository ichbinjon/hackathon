# Code Challenge - Contestant
## Set-up
These instructions will test that your environment is correctly configured and
that all the required tools are available.

### Building
From a command prompt in the root directory of the project:
```
gradlew :shadowJar
```
This should trigger the build process and create an executable jar in `build\libs`.

### Running
From a command prompt in the root directory of the project:
```
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT-all.jar com.contestantbots.team.ExampleBot
```

This should run a game between the initial bot that you can find in `src\main\java\com\contestantbots\team\ExampleBot.java`
and the default bot.  Once this completes you have the option to step through the game turn-by-turn, or exit the simulator.

The following parameters can be passed when running the client to control the game format:
- -m|map MapName
  - map to use [VeryEasy, Easy, Medium, LargeMedium, Hard]
  - default: Easy
- -b|bot Bot [Bot, [Bot]]
  - bot(s) to play against [Default, Milestone1, Milestone2, Milestone3]
  - default: Milestone1
- -c|className ClassName
  - fully specified class name of your bot, i.e. include the package

Alternatively you can run the following command to get the latest usage information:
```
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT-all.jar
```

### Game Output
The turn-by-turn game output is rendered as an ascii-art representation of the board with the following key:
```
O: spawn point
X: out of bounds
+: collectable
*: player
```
With the initial bot you should see a very short game consisting of 7 phases ending due to the `LONE_SURVIVOR` end
condition.  The turn-by-turn replay should show 8 players appearing from one spawn point (don't get your hopes up, these
belong to the default bot), and none appearing from the other spawn point.  This is because the initial bot does not
issue any orders, resulting in each player being eliminated by the next player to emerge from the spawn point.

If you have successfully reached this point then you're all set to start improving on the initial bot to ensure your
players have a longer, and perhaps more prosperous, life.

## Next Steps
First things first, rename the bot class so that there are no namespace clashes when uploading to the server,
additionally you might want to change the display name which is shown when testing locally.  There are only a few
restrictions on the compiled code:
- the jar file must be < 20MB
- the com.contestantbots.team package should only contain classes that extend `com.scottlogic.hackathon.game.Bot`
- any helper or utility classes should either be
  - inner classes, or
  - in a separate package
- your bot should take no more than 5 seconds to calculate the moves otherwise it will be timed out
- you can include more than one bot class in the uploaded jar file to allow you to test different strategies, however
only one bot can be active at any given time

When you're ready to move on this [tutorial](docs/tutorial/index.md) provides a step-by-step guide to adding
some basic intelligence to your bot.
