/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 *  Chapter 8 exercises, completed 2026.11.04:
 *    8.20 - 8.22  items in the rooms (class Item; a room holds any number)
 *    8.23 - 8.26  "back" command that retraces the player's steps, repeatedly
 *    8.28 - 8.30  class Player; "take" and "drop" commands, any number of items
 *  Extra credit attempted: 8.31 - 8.33 (a weight limit on what the player
 *  can carry, the "items" command, and a magic cookie with an "eat" command).
 *
 *  The game no longer keeps track of the current room itself. That job
 *  belongs to the Player object (responsibility-driven design): the game
 *  asks the player where it is and tells the player where to go. The game
 *  still does all of the printing, so the Room and Player classes only
 *  have to return values and strings.
 *
 * @author  Michael Kölling and David J. Barnes
 * @author  Ashton Harbison
 * @version 2026.11.04
 */
public class Game
{
    // The name of the one item that can be eaten, and what eating it does.
    // Written here once, so the name is not repeated all over the class.
    private static final String MAGIC_COOKIE = "cookie";
    private static final int COOKIE_STRENGTH = 10;   // extra weight the player can carry after eating it

    private Parser parser;
    private Player player;      // the player: where they are and what they carry

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together, put the items
     * into the rooms, and create the player.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        // put some items into the rooms (exercise 8.22: any number per room).
        // The player starts out able to carry a total weight of 10, so some
        // of these can only be carried together after eating the cookie,
        // and the safe can never be lifted at all.
        outside.addItem(new Item("umbrella", "a big black umbrella", 3));
        theater.addItem(new Item("notes", "a pile of lecture notes", 1));
        theater.addItem(new Item("projector", "a heavy projector", 8));
        pub.addItem(new Item("glass", "a pint glass", 2));
        pub.addItem(new Item(MAGIC_COOKIE, "a magic cookie", 1));
        lab.addItem(new Item("laptop", "an old laptop", 6));
        office.addItem(new Item("key", "a small brass key", 1));
        office.addItem(new Item("safe", "an enormous steel safe", 100));

        // create the player, who starts the game outside (exercise 8.28)
        player = new Player("Adventurer", outside);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul, " + player.getName() + "!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case BACK:
                goBack(command);
                break;

            case LOOK:
                look();
                break;

            case TAKE:
                take(command);
                break;

            case DROP:
                drop(command);
                break;

            case ITEMS:
                printItems();
                break;

            case EAT:
                eat(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println("Pick things up with 'take', put them down with 'drop',");
        System.out.println("and type 'items' to see what you are carrying.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Print a description of the player's current room: where they are,
     * the exits, and the items lying there. Used after every move and
     * for the "look" command, so the printing is written only once.
     */
    private void printLocationInfo()
    {
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.enterRoom(nextRoom);     // the player remembers where it came from
            printLocationInfo();
        }
    }

    /**
     * "back" command: go back to the room the player came from. Typing
     * "back" again keeps retracing the player's steps, one room at a
     * time, until the starting room is reached. (Exercises 8.23 - 8.26)
     */
    private void goBack(Command command)
    {
        if(command.hasSecondWord()) {
            // "back" is a single word command, like "quit"
            System.out.println("Back what?");
            return;
        }

        if(player.goBack()) {
            printLocationInfo();
        }
        else {
            System.out.println("You can't go back any further.");
        }
    }

    /**
     * "look" command: print the description of the current room again,
     * which is handy after dropping or picking up items.
     */
    private void look()
    {
        printLocationInfo();
    }

    /**
     * "take" command: pick up an item lying in the current room and
     * carry it. The item is only picked up if it is really here and the
     * player is strong enough to carry it on top of everything else.
     * (Exercises 8.29 - 8.31)
     */
    private void take(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getSecondWord();
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItem(itemName);

        if(item == null) {
            System.out.println("There is no " + itemName + " here.");
        }
        else if(!player.canCarry(item)) {
            System.out.println("The " + itemName + " is too heavy. You are carrying "
                               + player.getTotalWeight() + " and can carry "
                               + player.getMaxWeight() + " at most.");
        }
        else {
            // move the item from the room to the player
            currentRoom.removeItem(item);
            player.addItem(item);
            System.out.println("You pick up the " + itemName + ".");
        }
    }

    /**
     * "drop" command: put down an item the player is carrying. It is
     * left in the current room, where it can be picked up again later.
     * (Exercises 8.29 and 8.30)
     */
    private void drop(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("Drop what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.getItem(itemName);

        if(item == null) {
            System.out.println("You are not carrying the " + itemName + ".");
        }
        else {
            // move the item from the player to the room
            player.removeItem(item);
            player.getCurrentRoom().addItem(item);
            System.out.println("You drop the " + itemName + ".");
        }
    }

    /**
     * "items" command: print everything the player is carrying and the
     * total weight. (Exercise 8.32)
     */
    private void printItems()
    {
        System.out.println(player.getItemString());
    }

    /**
     * "eat" command: eat the magic cookie, which makes the player able
     * to carry more. The player has to be carrying the cookie to eat it.
     * Trying to eat anything else gives a suitable message. (Exercise 8.33)
     */
    private void eat(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to eat...
            System.out.println("Eat what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.getItem(itemName);

        if(item == null) {
            System.out.println("You are not carrying the " + itemName + ".");
        }
        else if(!itemName.equals(MAGIC_COOKIE)) {
            System.out.println("You can't eat the " + itemName + "!");
        }
        else {
            player.removeItem(item);                    // the cookie is gone once eaten
            player.increaseMaxWeight(COOKIE_STRENGTH);
            System.out.println("You eat the magic cookie. You feel much stronger!");
            System.out.println("You can now carry up to " + player.getMaxWeight() + ".");
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
