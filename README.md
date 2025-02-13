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
Phase 2 Sequence Diagram Link: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcuj3ZfF5vD6L9sgwr5pcw63O+nxPF+SwfgC5zAgUCYoOUCAHjysL7oeqLorE2LwQUrosmSFIGrSb6jCaRLhpGHIwNyvIGoKwowKK4p4eoSb5PBCpKiqOH5Cx7owJ63pBgGQYhnxBSWjA0ZSaJ2iOqIbGAWmaE8tmuaYEpbHFmmwEVhMXzTrOzb6Qc2lsdkPYwP2g69K+I6jCsBlBkZ84mWYnCrt4fiBF4KDoHuB6+Mwx7pJkmAWReRTUNe0gAKK7rF9Sxc0LQPqoT7dIZjboH+BRKeUWVzhpMGUHBcoITASH2EFqGBb6GEYth5W4WGboEWAsb+oV6BkUyboSRyNExrJ8hCmE3VoKG5H9WVaoIYqyrySoLXTfhRgoNwmSdSJcbaL1ZqqAN5TSBtFKGJ18bNXlJXKXVYBqQgeY3VpV5lD0pmveZ55gKU1lDkuHmeF5G6Qrau7QjAADio6siFp7hd9bLaeUFSQ4lKX2KOmXOdlk1mfk+UwBNxVAqV7HlYh0KodCDVYUtKArX1a3ILE0OjKoO0zrj+0UfkklDUTyowJj7NjUTONFeJs1OvKUMw+DsT04pN3lAr91qOpmnSyWUwi2o4yVP0esAJLSAbACMvYAMwACxPCemQGnpfRfDoCCgA2TugS7Tx6wAcqOJkwI0H3RVAX05D9VkDt0uswwbFRG6OpsW9bdtTA7+okfcPtTG7Htew5+m+6OAejEHIfuSuQProE2A+FA2DcPAuqZHLowpKFZ6R0jr0o7UDQY1jwQS+gQ7+6OoclNdpNphNsw9BPozQbP0vggJrcoLCcCb7TWJK7xrVrYJmRs1vS8oDz-V81R0lnw6Uvk3NnGLThM+puUO9epkD1Pav2s6TjqMFO5RLa2xgFPUqBQIpRz+r0IBKAQEwDATbCBVdPK1wCJYDaSFkgwAAFIQB5O3QwAR84gAbAjHusow790pHeFoetsZ1lxkOJuwBsFQDgBAJCUBZgm2kJA8OBMVbixYXONh7tOHcN4fw5OgiSapjXvKAAVkQtAsJCGqRQGiRqB8+LlAAGY8g4GfWEAir4siOtRExJD6JhAEVNJmrFlEIXvnJN+IjZ7lC0WgX+ijYIAKuHMKRlAZHQBWAAdRYMbJKLQABCu4FBwAANJfAEancBQiI6WTgT0EJHCwk8IiRMaJsTYoJKSak9J8jMmoIBtXNc3kAheA4V2L0sBgDYCboQeIiRO7wxgb3WhlQ4oJSSilYwuUvEfxgAEsmHF1qbS3idZZe8mpzUZgdD03A8CwksaxG+pRVlnUFggOx2hZh0Uud0C6clH6LIWtxK6MzgTHVOj-DWj15nhyCe9XK0Dvq-Rjr0BpmAgA
