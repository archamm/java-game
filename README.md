# Creeps.

## Brief

This document is a quick overview of the creeps given files.

## What's in the archive

In the provided archive you will find the following files:

  * `README.md`: this file.
  * `pom.xml`: the sample maven project file that you must use for your project.
  * `creeps-voxel-2.0-SNAPSHOT.jar`: the server, for you to train on.
  * `given-2.0-SNAPSHOT.jar`: a compiled java library to help you in your
    endeavor.
  * `apidocs/`: the given library's documentation, open 'index.html' in your
    browser to use it.
  * `given-2.0-SNAPSHOT-javadoc.jar`: the same documentation, can be extracted
    with `jar xf`.
  * `tests.http`: simple tests provided to validate your network implementation.
  * `maps/`: some examples of custom maps that you can load in your server.
  * `src/`: a dummy project architecture.

## Installing the given jar to your local repository

In order to use the provided library, you need to install it to your local
maven repository first, proceed as follows:

  * Make sure the folder `~/.m2/repository/` exists,  if not create it
    (assuming you have built maven projects before, the folder should already
    exist).
  * Run the command `mvn install:install-file -Dfile=given-2.0-SNAPSHOT.jar -DgroupId=com.epita -DartifactId=given -Dversion=2.0-SNAPSHOT -Dpackaging=jar`.
  * You should be good to use the provided pom.xml file.

In case the assistants publish a new version of the file, simply repeat the
process to update your local version.

## Launching the server

To get all the available options, simply launch the server with the --help
option:

`java -jar creeps-voxel-2.0-SNAPSHOT.jar --help`

Here are some useful option combinations.

### First run:

`java -jar creeps-voxel-2.0-SNAPSHOT.jar --printAchievements=true` will print
the list of all achievements you can get.

### Tutorial:

`java -jar creeps-voxel-2.0-SNAPSHOT.jar --autoStart=true --warmup=3600 --trackAchievements=true`
starts the server without enemies, with a long warmup, but will start the game
automagically once a players connects. Ideal for early development. You can
even add `--enableEasyMode=true` earlier on to help grasping the mechanics
of the game.

### The game:

`java -jar creeps-voxel-2.0-SNAPSHOT.jar --autoStart=true --warmup=3600 --trackAchievements=true --enableHorde=true`
starts the server with the horde mode activated and its default setup.
Add `--enableGC=true` to add more difficulty.

### Multi-player / standard Yaka configuration
`java -jar creeps-voxel-2.0-SNAPSHOT.jar --warmup=30 --timeLimit=1000 --tickLimit=900 --trackAchievements=true
--enableHorde=true --playerCap=5 --seed=<seed_value>`.

## Web client

To help you visualize what is happening during a game, you can connect to your
server using any modern browser as long as `--enableWebClient` is set to true
(which is the default), on the port indicated by the value of `--httpPort`
(default is 1337).

## Documentation

The documentation is generated using Javadoc.
To use it, you can open `apidocs/index.html` with the browser of your choice.
