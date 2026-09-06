package edith.parser;

/** Represents the commands understood by Edith. */
public enum CommandType {
    /** Adds a task without a date or time. */
    TODO("todo", true),
    /** Adds a task with a due date and optional time. */
    DEADLINE("deadline", true),
    /** Adds a task with start and end dates and optional times. */
    EVENT("event", true),
    /** Displays the current task list. */
    LIST("list", false),
    /** Marks a task as completed. */
    MARK("mark", true),
    /** Marks a task as incomplete. */
    UNMARK("unmark", true),
    /** Removes a task from the task list. */
    DELETE("delete", true),
    /** Ends the current Edith session. */
    BYE("bye", false);

    private final String keyword;
    private final boolean acceptsArguments;

    /**
     * Creates a command type with its keyword and input form.
     *
     * @param keyword the word that identifies the command
     * @param acceptsArguments whether text may follow the command word
     */
    CommandType(String keyword, boolean acceptsArguments) {
        this.keyword = keyword;
        this.acceptsArguments = acceptsArguments;
    }

    /**
     * Returns the word used to identify this command in user input.
     *
     * @return the command keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Identifies a command from complete user input.
     *
     * @param input the complete user input
     * @return the matching command type, or {@code null} when the input is unknown
     */
    public static CommandType fromInput(String input) {
        for (CommandType commandType : values()) {
            if (input.equals(commandType.keyword)
                    || commandType.acceptsArguments && input.startsWith(commandType.keyword + " ")) {
                return commandType;
            }
        }
        return null;
    }
}
