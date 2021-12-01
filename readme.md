# Multi-player poker on sockets #

## Game overview ##
The app implement rules of five-card draw poker, which consists of following steps:
- collecting ante from all players
- dealing out 5 cards for each player
- players get to know their cards
- 1st betting round
- swapping cards - each player can swap from 0 to 5 cards
- 2nd betting round
- revealing cards

## How to run game ##

Go to the project directory: `cd ${path_with_the_project}/multi-module-two-main`

Compile project: `mvn clean package`

This command will generate .jar files

Run server in one terminal: `java -jar poker-server/target/poker-server-1.0-jar-with-dependencies.jar`

After running server will ask you about number of players

Run as many clients, each in new terminal as you have declared: `java -jar poker-client/target/poker-client-1.0-jar-with-dependencies.jar`


## How to play ##
To start the game player has to input their name.

Next, in order to make any decision player has to input a proper integer number.

If the number is out of given range, server will ask player to input number once again, for example:

**SERVER:**
*<br />What do you want to do?<br />(1) Wait<br />(2) Pass<br />(3) Raise the stakes*

Players can make such decisions only when it is their turn. Otherwise they have to wait for other players and 
they are informed what other players did.
