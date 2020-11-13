# Card game with chat platform
This is a university project in distributed programming course. This project is a card game between four players which can play remotely, with a chat platform, implemented using Java RMI. The game is played between four players, which has eight cards each, dealt in a random way. The game end when one of the players reaches the maximum number of points.

## Getting started
These instructions will get you a copy of the project to run it on your computer for testing purposes.

## Installing
In your terminal write these commands:
```
git clone https://github.com/zanitarahimi/card-game-with-chat
```
Now you have a copy of my repo in your local machine. Go to that directory (card-game-with-chat) and open your terminal. Write this: 
```
start rmiregistry
```
Then open another terminal and write
```
javac *.java
```
The code above will compile all the classes in that directory. Then in that terminal write:
```
java Server
```
Then you will get a message from the server like this: <br>
*__Server is running. Have fun playing my game.__* <br>
Then open another terminal and write:
```
java Client
```
Now you will see a window which requires two things:
*  IP address (which is prewritten, it's my IP address, but you can change it)
*  Username <br>
then you press enter.<br>
After that you see your cards, and wait for other players to join the game.
