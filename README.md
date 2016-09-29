# Writing a Bot

See com.contestantbots.team.ExampleBot, you should delete before submitting a bot.

You must pick a unique package name, the suggestion is replacing team with your team name in the above.

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