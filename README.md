# Code Challenge - Contestant
## Writing a Bot

See the `com.contestantbots.team.ExampleBot`, you should delete before submitting a bot.

You must pick a unique package name, the suggestion is replacing team with your team name in the above.

## Installing

Configure internet settings used to build the project. 
This allows us to set a proxy that will be used to download Gradle and Maven dependencies used in the project.

Windows command prompt:
```batch
install -r <PATH_TO_MAVEN_REPO>
```

Unix shell:
```sh
./install.sh -r <PATH_TO_MAVEN_REPO>
```

For example if the repository manager was running on host: WS01190 port 8081 then (on Windows):
```batch
install -r http://WS01190:8081/repository
```

## Building & Running

The repository includes a pre-compiled game simulator application.
It can be run from either the command line, or your IDE.

After each phase of the game, the application prints an ASCII-art representation of the game's current state to the
console. You can review this before hitting `Enter` to play the next phase.
Since the printed state takes up quite a few lines in the console,
you may find that your IDE's output console can't be made big enough to properly review it,
in which case you may prefer to build and run the game from a command-line terminal.   

### From your IDE

There's a `void main(...)` method in the `ExampleBot` class. You can use that to run or debug a game simulation from
within your IDE.

### From your command line terminal

Open a command line terminal in the root folder of this project and run one of the following commands.

Windows command prompt:
```batch
gradlew run -P mainClass=<your_bot_class_fully_qualified_name>
```

Unix shell:
```sh
./gradlew run -P mainClass=<your_bot_class_fully_qualified_name>
```
