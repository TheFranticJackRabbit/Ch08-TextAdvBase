import java.util.HashMap;
import java.util.Stack;

/**
 * Class Player - the person playing the "World of Zuul" adventure game.
 *
 * Before this class existed, the Game class kept track of the current
 * room itself. Following responsibility-driven design (section 8.7), the
 * things that are really about the player now live here:
 *   - where the player is (the current room);
 *   - where the player has been, so that the "back" command can retrace
 *     their steps one room at a time (exercises 8.23 - 8.26);
 *   - what the player is carrying (exercises 8.29 and 8.30);
 *   - how much weight the player can carry in total (exercise 8.31).
 *
 * The Game class asks the player questions (where are you? can you carry
 * this?) and tells the player what happened (you entered this room, you
 * picked this up). This class never prints anything itself; it returns
 * values and strings and leaves the printing to Game, like Room does.
 *
 * Exercises 8.28 - 8.33, completed 2026.11.04.
 *
 * @author Ashton Harbison
 * @version 2026.11.04
 */
public class Player
{
    // The total weight a player can carry at the start of the game.
    private static final int STARTING_MAX_WEIGHT = 10;

    private String name;                    // the player's name
    private Room currentRoom;               // where the player is right now
    private Stack<Room> previousRooms;      // where the player has been, most recent on top
    private HashMap<String, Item> items;    // what the player is carrying, by item name
    private int maxWeight;                  // the most total weight the player can carry

    /**
     * Create a player with the given name, standing in the given room,
     * carrying nothing and with nowhere to go "back" to yet.
     * @param name The player's name.
     * @param startRoom The room the player starts in.
     */
    public Player(String name, Room startRoom)
    {
        this.name = name;
        currentRoom = startRoom;
        previousRooms = new Stack<>();
        items = new HashMap<>();
        maxWeight = STARTING_MAX_WEIGHT;
    }

    /**
     * @return The player's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The room the player is in right now.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Move the player into a new room. The room they are leaving is
     * remembered on the stack of previous rooms, so "back" can return
     * to it later. (Exercise 8.26)
     * @param room The room the player is moving into.
     */
    public void enterRoom(Room room)
    {
        previousRooms.push(currentRoom);    // remember where we came from
        currentRoom = room;
    }

    /**
     * Move the player back to the room they came from. Because the
     * previous rooms are kept on a stack, calling this again and again
     * retraces the player's steps all the way to the starting room.
     * (Exercises 8.23 and 8.26)
     * @return true if the player went back one room, false if there was
     *         no previous room to go back to (the player is where they
     *         started, or has already gone back all the way).
     */
    public boolean goBack()
    {
        if(previousRooms.isEmpty()) {
            return false;                   // nowhere to go back to
        }
        currentRoom = previousRooms.pop();  // the most recent room comes off the top
        return true;
    }

    /**
     * Work out whether the player is strong enough to pick up an item
     * on top of everything they are already carrying. (Exercise 8.31)
     * @param item The item the player wants to pick up.
     * @return true if the item can be carried, false if it would push the
     *         total weight over the player's maximum.
     */
    public boolean canCarry(Item item)
    {
        return getTotalWeight() + item.getWeight() <= maxWeight;
    }

    /**
     * Add an item to what the player is carrying. The game is expected to
     * check canCarry first. (Exercises 8.29 and 8.30)
     * @param item The item that was picked up.
     */
    public void addItem(Item item)
    {
        items.put(item.getName(), item);
    }

    /**
     * Look for an item the player is carrying, by its name.
     * @param name The name of the item, for example "key".
     * @return The item with that name, or null if the player does not have it.
     */
    public Item getItem(String name)
    {
        return items.get(name);
    }

    /**
     * Take an item away from the player, for example because they
     * dropped it or ate it. Nothing happens if the player did not have it.
     * @param item The item to remove.
     */
    public void removeItem(Item item)
    {
        items.remove(item.getName());
    }

    /**
     * Add up the weight of everything the player is carrying.
     * @return The total weight of all carried items (0 if none).
     */
    public int getTotalWeight()
    {
        int total = 0;
        for(Item item : items.values()) {
            total = total + item.getWeight();
        }
        return total;
    }

    /**
     * @return The most total weight the player can carry right now.
     */
    public int getMaxWeight()
    {
        return maxWeight;
    }

    /**
     * Make the player stronger, so they can carry more. This is what
     * eating the magic cookie does. (Exercise 8.33)
     * @param amount How much more weight the player can carry from now on.
     */
    public void increaseMaxWeight(int amount)
    {
        maxWeight = maxWeight + amount;
    }

    /**
     * Return a string listing everything the player is carrying and the
     * total weight, for the "items" command (exercise 8.32). For example:
     *     You are carrying:
     *       key - a small brass key (weight 1)
     *       laptop - an old laptop (weight 6)
     *     Total weight: 7 (you can carry up to 10)
     * @return Details of the player's items and their total weight.
     */
    public String getItemString()
    {
        String returnString;
        if(items.isEmpty()) {
            returnString = "You are not carrying anything.";
        }
        else {
            returnString = "You are carrying:";
            for(Item item : items.values()) {
                returnString += "\n  " + item.getLongDescription();
            }
        }
        returnString += "\nTotal weight: " + getTotalWeight()
                        + " (you can carry up to " + maxWeight + ")";
        return returnString;
    }
}
