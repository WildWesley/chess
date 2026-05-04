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

Diagram for Client-Server relationship:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5afJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTJTspXI3l5d5LsKYoSm6MpymW7xKpgKrBhqABCIYwM5ahYMODpJr6zpdjAPZ9tuHmWf6yUcGlOTofCybIKmMDpvhoxjDmqh5vM0FFiW9RlalKCEulDb0YFvLBZZ1kRVu8XqpK0yXtASAAF4oAx7mCu5tRNPofw7EYEBqGgADkzDHGAIDxEVIHVP6zKzXq81LStV3VZUolpk4ACMBEtW1BZjJ10Curd8T3ctg1NqtnkjfyY4Tigz7xOel7Xpli6VA+a4BkjW4Qx+5n+o54oZKoAGYDpz0djULyEfp8wkah3wUVR9Z07RVWYbV2EwLhjV6TFxGkQzl5M8hLNoUNnjeH4-heCg6AxHEiQy3Ljm+FgolrU99QNNIEb8RG7QRt0PRyaoCnDIziHoGzuOwrp5FC5bIsC2Ztvwjlnb1LZ9iqw5QmqxVrkXZDC6jjAjJgPDPKI-BjtzkFWVo8KMAAOJMsaDtITAABm3gzEY2h6AYcWqhqN0USDDEowK2UU9ZBX9jj7uUzN5dQIty3WzVJRgG9PPNfyP0dcWAMt3NbcPWDle3gntcupjL7Y7lgpV-UHAoNwx5Y-I0eUbHw0h-ewrSOvTKGJH2gwJANl+6eQc21+9Qq6eRMk2Tl0SWBMAwJpT3s93OF4SzF-OiTYJbMX8Cidc-hsDig1PxNEKclQaHViVUsDRk76yNvYJU5sM5Wy0vfOELx7Yx2oqLBsb8xpz2QDkZOSCHJojoTmAObkl7B28mHJk59t4WyQnHKGqMhT1FTswXhcsc4QDzloeQhdlQl1HndceHcq7vysnPeud8m7XSBhXTuL0OY93qoApq318xDy6go4GSjdggKnvHRcVC8rcOAEHKk096ScIjn2JhagMT8IPiFWoIjKwIDDkg7Oud84yP0GIFRNd1oKGLMASw8CcgAB4fE8nKJoh4rthFKh5C-BAgE8bk1ArUEYyxsE5gLA0cY1SUAAElpAFneuEYIgQQSbHiLqFAbpOR7G+MkUAap+mQUWN8BpAA5JUEyLgwE6D-D+f86rcyAWMBpqhan1KVM01p7TOnLG6b0sZBkxhDIQCM057VzkgmmbM858zFmNgYmAqWHAADsbgnAoCcDECMwQ4BcQAGzwFhoguYMAigGI1h-LWrQOhYJwUDYWWZ7lzCWYmSoZNiEbKVDM-m9M1gkN3mQ52pNSmwrUXlQ8cgUA+IxHAWGPiWGuJkO40O4dnE72Fv4-ch9hFpzEWgCJkiom6BicXBKljdFxMcd2XsDc2GoMBq3duj1lk11ekYvupj2qFmHqWMuY91WT33vy+JuV6jOLZSvGAtL0QMvRQFOJidahMqPIYBpMBxTitkea0aTckSZMXp2dsOKIVNOkBS12qjniVLxXMPZ9Q2kdO-no+ABiAE8yqUghYCaGnJpgKmwI6bxZMSlpYdetlNjyyQAkMAVa+wQFrQAKQgL6nxMRhkgDVNC7uVLJJNGZDJHoDTcGkPQFmbAlyq1QDgBAWyUA1iFukJijChC7Y-FnZQBdS69gAHUWCNINj0RK-EFBwAANKTN2S0lNBziVjGFTRNClCg1zwAFYdrQAy9t4oWV9RcqwsN7CRw+QjlvYAPK96uqESnIVeCRUSKkQXSVU1S46OsQGrK8r8qKpyeUmV2GCFdzqg1L6A8zEGoscaxRprbE4YcR+pxUHbUcogyG7eq6+XBTdcE710jgCitQ9EouGGYBlQDkx6ueGNGNwpqVEM0nSP6P-jqyjuZqN-UNd1FKKJ+o5DNXKljQSCmTXkYGM0lgwxITZeteAi7oCVms5fcMhQFNEas8GUM7mM3aooyYqj+qdMWO89WNz1FGMSb3c57I2oenonY-Y0OfhpH0vM-IA9iWUAYmcSuu9vGZ7LgDBjfyl8IAwAMy5ZaYTIXHPRPapzUAXlSumrF2A8We19ohm4lL9Q0t0q48ARKlze15agwVpN0giuCJK4+aKRor7VfSrV713WmtLtawpiN-7f1KiKSU2Nlq4WVPTapzN6m1mjGAa1t5AQvDJLrQ2x78pEDBlgMAbAM7CB5AKFClBWi0E6z1gbI2xhrbYtKcQ9N77Z40u4HgBQ33kAgD+2gDEIBEdQGRz9tHcY0Cst6+y-r9rEfctm9XN1x8N6GBNKE5xIm-UxKhcTu1WOPvDb8TJgVNPT4hMjTKFDzODCs7YX1gRocOfAAmtvSnvOT6Nfpxud0F9hdCdkWL0Dm7SzvaRyj37BPDsxq-HGz+53f5aqzVzYxZ3GNAA