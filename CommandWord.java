/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 *
 * Exercises 8.23 - 8.33: the new commands BACK, LOOK, TAKE, DROP, ITEMS
 * and EAT were added here. This is the only change needed for the parser
 * to understand the new words, because the CommandWords class builds its
 * list of valid words from the values of this enumerated type (see the
 * CommandWords constructor). The Game class decides what each one does.
 *
 * Completed 2026.11.04.
 *
 * @author  Michael Kölling and David J. Barnes
 * @author  Ashton Harbison
 * @version 2026.11.04
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    GO("go"), BACK("back"), LOOK("look"),
    TAKE("take"), DROP("drop"), ITEMS("items"), EAT("eat"),
    QUIT("quit"), HELP("help"), UNKNOWN("?");

    // The command string.
    private String commandString;

    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }

    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}
