# Adventure Jukebox #

Adventure Jukebox is a web interface for text based adventure games. It was inspired by an assignment on
Aalto University's programming course.

This application allows users to play multiple games that are packaged into separate jar archives. This prevents
collisions in package names etc...

Feel free to fork this project at GitHub!

## Preparing an adventure game ##

1. Include `o1.adventure.Game` trait in your project. Do not change its contents, name or location!
2. Implement Game trait either in the main class of your game or use it for an adapter that wraps your game
3. Package your compiled class files ...
4. ... and a metadata.properties file into a JAR file
5. Place the JAR file under `src/main/webapp/WEB-INF/games/`
6. Optionally place other games there too!

### An example of a metadata file ###

```gameClass=fi.karilavikka.Adventure
title=Tehtävän palautus
authors=Kari Lavikka
year=2014
```

### An example of a jar archive file layout ###

```/metadata.properties
/o1/adventure/Game.class
/fi/karilavikka/Adventure.class
/fi/karilavikka/Other.class
/fi/karilavikka/...
```

## Build & Run ##

Application is built on top of [Scalatra](http://www.scalatra.org/) micro-framework.
Read its documentation for more information.

```sh
$ cd adventure-jukebox
$ ./sbt
> container:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Author ##

Kari Lavikka <kari.lavikka@bdb.fi>