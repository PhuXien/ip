import java.time.format.DateTimeParseException;

/** Interprets user commands and creates the tasks described by them. */
public class Parser {
    /** Identifies the command type represented by the complete user input. */
    public static CommandType parseCommandType(String command) {
        return CommandType.fromInput(command);
    }

    /**
     * Creates a command object for commands that do not require arguments.
     *
     * <p>This temporary migration method returns {@code null} for commands that have not yet been
     * converted to command objects.</p>
     *
     * @param commandType the recognized command type
     * @return a command object, or {@code null} when that command is not yet converted
     */
    public static Command parseSimpleCommand(CommandType commandType) {
        return switch (commandType) {
        case BYE -> new ExitCommand();
        case LIST -> new ListCommand();
        default -> null;
        };
    }

    /** Parses a todo command into a task. */
    public static Task parseTodo(String command) throws EdithException {
        String description = command.substring(CommandType.TODO.getKeyword().length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /** Parses a deadline command into a task. */
    public static Task parseDeadline(String command) throws EdithException {
        String details = command.substring(CommandType.DEADLINE.getKeyword().length()).trim();
        int byMarker = details.indexOf("/by");
        if (byMarker < 0) {
            throw new EdithException("A deadline needs a description and due date. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]");
        }

        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a deadline cannot be empty. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]");
        }
        if (by.isEmpty()) {
            throw new EdithException("A deadline needs a due date after /by. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]");
        }
        DateFormatter.ParsedDateTime dueDateTime = parseDateTime(by, "deadline");
        return new Deadline(description, dueDateTime.value(), dueDateTime.hasTime());
    }

    /** Parses an event command into a task. */
    public static Task parseEvent(String command) throws EdithException {
        String details = command.substring(CommandType.EVENT.getKeyword().length()).trim();
        int fromMarker = details.indexOf("/from");
        int toMarker = details.indexOf("/to");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            throw new EdithException("An event needs a description, start date, and end date. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }

        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + "/from".length(), toMarker).trim();
        String to = details.substring(toMarker + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of an event cannot be empty. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        if (from.isEmpty()) {
            throw new EdithException("An event needs a start date after /from. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        if (to.isEmpty()) {
            throw new EdithException("An event needs an end date after /to. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        DateFormatter.ParsedDateTime startDateTime = parseDateTime(from, "event start");
        DateFormatter.ParsedDateTime endDateTime = parseDateTime(to, "event end");
        return new Event(description, startDateTime.value(), startDateTime.hasTime(),
                endDateTime.value(), endDateTime.hasTime());
    }

    /** Parses the one-based task number supplied to a mark, unmark, or delete command. */
    public static int parseTaskNumber(String command, CommandType commandType) throws EdithException {
        String commandWord = commandType.getKeyword();
        String taskNumberText = command.substring(commandWord.length()).trim();
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: " + commandWord + " NUMBER");
        }
    }

    /** Parses a command date-time and gives the user a corrective message when it is invalid. */
    private static DateFormatter.ParsedDateTime parseDateTime(String dateText, String dateRole) throws EdithException {
        try {
            return DateFormatter.parseInput(dateText);
        } catch (DateTimeParseException e) {
            throw new EdithException("The " + dateRole
                    + " date must use yyyy-MM-dd, optionally followed by a time in the format HHmm.");
        }
    }
}
