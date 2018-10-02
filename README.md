# Code Challenge - Contestant
# Writing a Bot

See com.contestantbots.team.ExampleBot, you should delete before submitting a bot.

You must pick a unique package name, the suggestion is replacing team with your team name in the above.

# Installing

Configure internet settings used to build the project.  This allows us to set a proxy that will be used to download gradle and maven dependencies used in the project.
```
install -r [PATH_TO_MAVEN_REPO]
```
For example if the repository manager was running on host: WS01190 port 8081 then:
```
install -r http://WS01190:8081/repository
```

# Building 

From a command prompt in the root directory

```
#!batch
gradlew :shadowJar
```

this will create a fat (shadow) jar in build\libs

# Running

From a command prompt in the root directory


```
#!batch
java -jar build\libs\hackathon-contestant-1.0-SNAPSHOT.jar com.contestantbots.team.ExampleBot
```

***com.contestantbots.team.ExampleBot should be changed to the correct full class name***
