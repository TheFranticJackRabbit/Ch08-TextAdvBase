import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * Exercises 8.20 - 8.22: a room can now also hold items. The items are
 * kept in a HashMap that works exactly like the one for the exits: the
 * key is the item's name and the value is the Item object. So a room can
 * hold any number of items (exercise 8.22), including none at all, and
 * getItem("key") finds an item the same way getExit("north") finds an
 * exit. The room's long description lists every item it holds, so the
 * player sees the items as soon as they walk in (exercise 8.21).
 *
 * Completed 2026.11.04.
 *
 * @author  Michael Kölling and David J. Barnes
 * @author  Ashton Harbison
 * @version 2026.11.04
 */
public class Room
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> items;        // the items lying in this room, by name

    /**
     * Create a room described "description". Initially, it has
     * no exits and no items. "description" is something like "a kitchen"
     * or "an open court yard".
     * @param description The room's description.
     */
    public Room(String description)
    {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items here:
     *       key - a small brass key (weight 1)
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString()
               + "\n" + getItemString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return a string listing the room's items, one per line, for example
     *     Items here:
     *       key - a small brass key (weight 1)
     *       safe - an enormous steel safe (weight 100)
     * If the room is empty the string is "Items here: none".
     * @return Details of the room's items.
     */
    private String getItemString()
    {
        if(items.isEmpty()) {
            return "Items here: none";
        }
        String returnString = "Items here:";
        for(Item item : items.values()) {
            returnString += "\n  " + item.getLongDescription();
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }

    /**
     * Put an item into this room. (Exercises 8.20 and 8.22)
     * Every item in a room needs a different name, because the name is
     * what the room uses to find the item again.
     * @param item The item to put in the room.
     */
    public void addItem(Item item)
    {
        items.put(item.getName(), item);
    }

    /**
     * Look for an item in this room by its name. This works like getExit:
     * the HashMap gives back the item for that name, or null if there is
     * no item with that name in the room.
     * @param name The name of the item, for example "key".
     * @return The item with that name, or null if it is not here.
     */
    public Item getItem(String name)
    {
        return items.get(name);
    }

    /**
     * Take an item out of this room, for example because the player has
     * picked it up. Nothing happens if the item was not in this room.
     * @param item The item to remove.
     */
    public void removeItem(Item item)
    {
        items.remove(item.getName());
    }
}
