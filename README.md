# Writing a Bot

See com.team.ExampleBot, you should delete before submitting a bot.

You must pick a unique package name, the suggestion is replacing team with your team name in the above.

# Building 

From a command prompt in the root directory

```
#!batch
gradlew :shadowJar
```

this will create a fat (shadow) jar in build\libs

# Running

## Java

From a command prompt in the root directory


```
#!batch
java -classpath libs\client-1.0-SNAPSHOT-all.jar;build\libs\hackathon-contestant-1.0-SNAPSHOT.jar com.scottlogic.hackathon.client.Client com.team.ExampleBot
```

***com.team.ExampleBot should be changed to the correct full class name***

## Gradle

From a command prompt in the root directory

```
#!batch
gradlew :run
```

You can customise this in build.gradle

***com.team.ExampleBot should be changed to the correct full class name***