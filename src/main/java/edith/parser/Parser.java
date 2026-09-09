package edith.parser;

import java.time.format.DateTimeParseException;

import edith.command.AddCommand;
import edith.command.Command;
import edith.command.DeleteCommand;
import edith.command.ExitCommand;
import edith.command.FindCommand;
import edith.command.ListCommand;
import edith.command.MarkCommand;
import edith.command.UnmarkCommand;
import edith.exception.EdithException;
import edith.task.Deadline;
import edith.task.Event;
import edith.task.Task;
import edith.task.Todo;
import edith.util.DateFormatter;

/** Interprets user commands and creates the tasks described by them. */
public class Parser {
    /**
     * Parses complete user input into an executable command.
     *
     * @param command the complete user input
     * @return the command represented by the input
     * @throws EdithException if the command or its arguments are invalid
     */
    public static Command parse(String command) throws EdithException {
        CommandType commandType = CommandType.fromInput(command);
        if (commandType == null) {
            throw new EdithException("I don't know what that means. Use todo, deadline, event, list, find, "
                    + "mark, unmark, delete, or bye.");
        }

        return switch (commandType) {
            case BYE -> new ExitCommand();
            case LIST -> new ListCommand();
            case FIND -> new FindCommand(parseFindKeyword(command));
            case MARK -> new MarkCommand(parseTaskNumber(command, commandType));
            case UNMARK -> new UnmarkCommand(parseTaskNumber(command, commandType));
            case DELETE -> new DeleteCommand(parseTaskNumber(command, commandType));
            case TODO -> new AddCommand(parseTodo(command));
            case DEADLINE -> new AddCommand(parseDeadline(command));
            case EVENT -> new AddCommand(parseEvent(command));
        };
    }

    /** Parses and validates the search text supplied to a find command. */
    private static String parseFindKeyword(String command) throws EdithException {
        String keyword = command.substring(CommandType.FIND.getKeyword().length()).trim();
        if (keyword.isEmpty()) {
            throw new EdithException("Please provide a word or phrase to find. Use: find KEYWORD");
        }
        return keyword;
    }

    /** Parses a todo command into a task. */
    private static Task parseTodo(String command) throws EdithException {
        String description = command.substring(CommandType.TODO.getKeyword().length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /** Parses a deadline command into a task. */
    private static Task parseDeadline(String command) throws EdithException {
        String details = command.substring(CommandType.DEADLINE.getKeyword().length()).trim();
        int byMarker = details.indexOf("/by");
        if (byMarker < 0) {
            throw new EdithException("A deadline needs a description and due date. Use: deadline DESCRIPTION "
                    + "/by yyyy-MM-dd [HHmm]");
        }

        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a deadline cannot be empty. Use: deadline DESCRIPTION "
                    + "/by yyyy-MM-dd [HHmm]");
        }
        if (by.isEmpty()) {
            throw new EdithException("A deadline needs a due date after /by. Use: deadline DESCRIPTION "
                    + "/by yyyy-MM-dd [HHmm]");
        }
        DateFormatter.ParsedDateTime dueDateTime = parseDateTime(by, "deadline");
        return new Deadline(description, dueDateTime.value(), dueDateTime.hasTime());
    }

    /** Parses an event command into a task. */
    private static Task parseEvent(String command) throws EdithException {
        String details = command.substring(CommandType.EVENT.getKeyword().length()).trim();
        int fromMarker = details.indexOf("/from");
        int toMarker = details.indexOf("/to");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            throw new EdithException("An event needs a description, start date, and end date. Use: event DESCRIPTION "
                    + "/from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }

        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + "/from".length(), toMarker).trim();
        String to = details.substring(toMarker + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of an event cannot be empty. Use: event DESCRIPTION "
                    + "/from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        if (from.isEmpty()) {
            throw new EdithException("An event needs a start date after /from. Use: event DESCRIPTION "
                    + "/from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        if (to.isEmpty()) {
            throw new EdithException("An event needs an end date after /to. Use: event DESCRIPTION "
                    + "/from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]");
        }
        DateFormatter.ParsedDateTime startDateTime = parseDateTime(from, "event start");
        DateFormatter.ParsedDateTime endDateTime = parseDateTime(to, "event end");
        return new Event(description, startDateTime.value(), startDateTime.hasTime(),
                endDateTime.value(), endDateTime.hasTime());
    }

    /** Parses the one-based task number supplied to a mark, unmark, or delete command. */
    private static int parseTaskNumber(String command, CommandType commandType) throws EdithException {
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
