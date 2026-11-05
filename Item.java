/**
 * Class Item - an object that can be found lying in a room of the
 * "World of Zuul" adventure game, and picked up and carried by the player.
 *
 * Every item has three pieces of information:
 *   - a name: ONE word, which is what the player types in commands such
 *     as "take key" or "drop key" (the parser only reads two words, so
 *     the name must not contain spaces);
 *   - a description: a few words saying what the item looks like;
 *   - a weight: a whole number. The player can only carry a limited
 *     total weight, so heavy items matter (see the Player class).
 *
 * An Item never changes after it has been created. There are methods to
 * ask for its details, but no methods to change them.
 *
 * Exercises 8.20 - 8.22 (items in rooms), completed 2026.11.04.
 *
 * @author Ashton Harbison
 * @version 2026.11.04
 */
public class Item
{
    private String name;          // one word, used in commands: "take key"
    private String description;   // what the item looks like
    private int weight;           // how heavy the item is

    /**
     * Create a new item.
     * @param name  A single word that names the item, for example "key".
     * @param description  A short description, for example "a small brass key".
     * @param weight  The weight of the item (a whole number, 1 or more).
     */
    public Item(String name, String description, int weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * @return The one-word name of this item, as typed in commands.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The description of this item.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return The weight of this item.
     */
    public int getWeight()
    {
        return weight;
    }

    /**
     * Return everything about this item on one line, in the form
     *     key - a small brass key (weight 1)
     * Rooms and the player both use this when they list their items,
     * so the format is written here once instead of in two places.
     * @return A one-line description of this item.
     */
    public String getLongDescription()
    {
        return name + " - " + description + " (weight " + weight + ")";
    }
}
