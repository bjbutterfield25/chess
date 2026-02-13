# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[MySequenceDiagram](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkEkSYCkm+hi7jS+71NkABqJpIBwU76YZWw7HOnl3kuwowGKEpujKcplu8FaPDAak7JgKrBhqACSaBUP5676TA0BpUZ6k3hFDpJr6zpdjAPZ9tu7kWf6zLTJe0BIAAXigHBRjGcaFFpNXwMgqYwOmACMWbLDmqh5vM0FFiW9Q+B1epdb1uwwHRTbDtVFlWfFW5uYKB30jAh5yCgz7xOel7XhdApRfUj4Bo9p11e2Omlg54oZKoAGYL9IHVLphHlp8pGrGMFFUfWMOaeD8LJuN2EwLh+GjJDyXQ6hsPw4hiME3tDGeN4fj+F4KDoDEcSJDTdMOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMRNIeh8KVL99QS+gINmRhR0utZQms-Z6unk5aguc1523vyDJMndD3wcTaDhbykWVMuMXik+n3yLK8ry4UWXqjAeUFcg65u5V1uHbVnbdr2-ZnaNnOSht8RbX1A0oLGClS5h6NgGmTgzTj82LXsK3QGtMdx7sZMBwuL0q-Vd2vt9HmB5dHAoNwx6Xhi1faFb5f3tF0hN0yhjt-I+ujbLAla2AgPA6Dkcoy8u0p6Nok4Xhs2lxTzH+Ci67+Ng4oavxaIwAA4kqGjs61pYNEf-NC-YSri5eCPDSj2lK6W-vT8Hlmq8gOQnzm9k0T-zUDrEkw8qSG0uoyMAps3ad33N3eosVHYvm0C7Y0j8LaZVVLlfKhUMHm0ls9MGIcGph2HhfV0RcoA9XjtGROQ0F5oxKOnSamcCI53zIsfOpZ1oUWLg2eiZcEEz1IYPYA4CZCQNHNZIBp8MTwJtkKJBDtj5Khrp2H6b96gHz-qfSeCBAJvxITUOeYw745gLA0cYFiUA5WkAWKa4RgiBBBJseIuoUBuk5Hsb4yRQBqm8ZBRY3xbEADklQhIuDAToyMJJQBqkvTGK9RjLFsaoKxNilT2Mcc41xyx3GeKCdDMYfiEABOKUtUpIJwmRNKdE2JjZyZMSphwAA7G4JwKAnAxAjMEOAXEABs8AJyGGATAIoacOaz0aK0Dot974xyflmWpcw4mJhltoghlELYrKVBEtZitYTKy-lZa66JgEYjgKM4BoC9YRwgVVKBJtW5wOEUou2yCPqoOdolf2HtcE+wCtsp+7yg4dm-vVRq4da6UOjvwmh20E5J3jCNZhE1pocP5LnbhxYC4KmobQkuTSwWLkrm9J2EiHlSKeTI85KBLmrK5KSl6ttorXKPIYNAKBNgwFsTAcUOR2DenFGouYGjIVaOOaWDlN1bn-kMUcr8JjdJpOyQ4+oTiXHzzRZUJJWNZrmPVbk7Va8WkBEsE3GyvLYhIASGAS1fYIC8oAFIQFFeM-w-iQBqkmSw6Z8SuZNGZDJHotiH6EPQFmbA5TLVQDgBAGyUA1i2Psesk5DxpVy0wUhaNsbKAJqTSm9VplpUBshfUAAVu6tAly3XinlYSXWrla6PPrjI6BsCc3oEUdVNlKiJTiPQf8nBXs8G+xBVg4hoiK1kKahHOF7UEVEuRYw3VY0WEZyztmbFXCxg8MLsu7agj9rSJMVZcRkjnrGzAIy9VvbFz9vthKYBCV5SpukNg7KYrDAcAgGoBqEBmBWjRCy89qtoUUK-v6AAQiGO5q7k7rqSembGO7cx7oPbKEMMAURNpyCehi07yU-o9ACmATR82wBAIm0qJozQ1nDM-TRM7-SBgY2GJCiHUUv0XmnDOmZs67qqVh+jwZzQwFrPWUu1Lr0KmwFoC5SoMQfrWH4RTfVX0dzA69SUCmbqkbQXoAw64Ch8vVcaCU6mbocC00PBdmys0wHrbWpUBijFltY2BHVvH0UYwNaks1lMAheGAPKW19rQvykQMGWAwBsAxsIHkMzfrzDlskjzPmAshbGAXo5r8tQlXmVOarEA3A8AKAS8gEAyW0BXPK1ASriWatDTuS2ljbby71DK7FhROmn292bgBy906Bt93RAKE6Q9RvKJkONgDdmqWwvy3CeoMWKtVaS61hVHnlVebnum1Ger+PJLQ-PIRQA)