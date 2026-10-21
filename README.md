# Multiplayer Whac-A-Mole Game

A multiplayer **Whac-A-Mole-style game** developed in Java, using **TCP sockets** for player actions and **UDP multicast** to distribute game events to subscribed clients. The project also includes multithreaded client simulation and stress-testing documentation.

## Overview

Players compete to click a monster that appears at random positions on a **3 × 4 grid**. The server broadcasts the monster's position to subscribed clients, and players send their hits back to the server. The first player to reach **five points (round wins)** wins the match.

The monster update interval is configurable on the server; the original project description uses **500 milliseconds** as an example.

## Technologies

- **Java** — game logic, networking, and graphical client
- **TCP sockets** (`Socket`, `DataInputStream`, etc.) — client-to-server messages, including player actions
- **UDP multicast** (`MulticastSocket`) — distribution of monster positions and winner announcements
- **Java threads** — periodic server events and simulation of multiple clients
- **HashMap** — in-memory tracking of player scores and state

## Communication Architecture

```text
                  Game Server
                 /           \
       UDP multicast       TCP sockets
       (game updates)      (player hits)
             |                  ^
      +------+------+           |
      |             |           |
   Client A      Client B ------+
      |             |
   Game GUI      Game GUI
```

1. The server periodically generates a random position in the 3 × 4 grid.
2. It broadcasts the position through multicast to subscribed clients.
3. Each client displays the monster at the received position.
4. When a player clicks the monster, the client sends an action to the server over TCP.
5. The server updates the player's score and broadcasts subsequent game events.
6. When a player reaches five points, the server multicasts the winner's identifier and resets the scores for a new match.

## Player Reconnection

Players are identified by a **name or ID**. A player who disconnects can reconnect using the same identifier and recover their score while the current game state remains available on the server.

Player information is maintained in an **in-memory `HashMap`**; this does not imply that scores survive a server restart.

## Main Classes

| Class | Purpose |
|---|---|
| `ServidorLogin` | Accepts login connections and sends initial information to clients. |
| `ClienteLogin` | Simulates multiple client login connections. |
| `Counter` | Tracks the number of clients that have logged in. |
| `Person` | Launches multiple client threads for testing. |
| `Cliente` | Receives messages and launches the game GUI. |
| `Juego` | Displays the game and sends monster-click actions over TCP. |
| `Servidor` | Coordinates game events, networking, and player state. |
| `Jugadores` | Represents individual players. |
| `aleatorio` | Generates random values used by the game. |

## Running the Project

1. Open the project in **IntelliJ IDEA** or another Java development environment.
2. Run the `ServidorLogin` class.
3. Run the `Person` class. It launches threads of the `Cliente` class to simulate multiple clients and open their game interfaces.

These steps are based on the original project instructions. Depending on your environment, you may need to adjust network addresses, ports, multicast settings, and other configuration values in the source code.

## Stress Testing and Results

The repository includes supporting materials from stress tests using multiple simulated clients:

- An **Excel workbook** containing stress-test data.
- A **Word document** explaining the results and graphs.

These materials document the project's performance evaluation. Specific measurements and conclusions can be added here after reviewing the original results.


## Project Scope

This project explores Java socket programming, multicast publish-subscribe communication, concurrent client simulation, and server-side game-state management in a multiplayer application.
