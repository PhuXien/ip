import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Entry point for the Edith chatbot. */
public class Edith {
    private static final String DIVIDER = "____________________________________________________________";

    public static void main(String[] args) {
        String banner = """
                 _____ ____ ___ _____ _   _
                | ____|  _ \\_ _|_   _| | | |
                |  _| | | | | |  | | | |_| |
                | |___| |_| | |  | | |  _  |
                |_____|____/___| |_| |_| |_|
                """;

        System.out.println(banner);
        System.out.println("Hello! I'm EDITH.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = loadTasks();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(DIVIDER);

            try {
                CommandType commandType = CommandType.fromInput(command);
                if (commandType == null) {
                    throw new EdithException("I don't know what that means. Use todo, deadline, event, list, mark, unmark, delete, or bye.");
                }

                switch (commandType) {
                case BYE:
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(DIVIDER);
                    return;
                case LIST:
                    printTaskList(tasks);
                    break;
                case MARK:
                case UNMARK:
                    markTask(tasks, command, commandType);
                    break;
                case DELETE:
                    deleteTask(tasks, command);
                    break;
                case TODO:
                    String description = command.substring(commandType.getKeyword().length()).trim();
                    addTask(tasks, new Todo(description));
                    break;
                case DEADLINE:
                    addDeadline(tasks, command);
                    break;
                case EVENT:
                    addEvent(tasks, command);
                    break;
                }
            } catch (EdithException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
            System.out.println(DIVIDER);
        }
    }

    /**
     * Adds a task and prints the confirmation message.
     *
     * @param tasks the task list to update
     * @param task the task to add
     */
    private static void addTask(List<Task> tasks, Task task) throws EdithException {
        if (task.getDescription().isEmpty()) {
            throw new EdithException("The description of a " + task.getTaskType() + " cannot be empty.");
        }
        tasks.add(task);
        saveTasks(tasks);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Parses and adds a deadline command in the form {@code deadline DESCRIPTION /by yyyy-MM-dd}.
     *
     * @param tasks the task list to update
     * @param command the user's command
     */
    private static void addDeadline(List<Task> tasks, String command) throws EdithException {
        String details = command.substring(CommandType.DEADLINE.getKeyword().length()).trim();
        int byMarker = details.indexOf("/by");
        if (byMarker < 0) {
            throw new EdithException("A deadline needs a description and due date. Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }

        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a deadline cannot be empty. Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
        if (by.isEmpty()) {
            throw new EdithException("A deadline needs a due date after /by. Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
        addTask(tasks, new Deadline(description, parseDate(by, "deadline")));
    }

    /**
     * Parses and adds an event command in the form {@code event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd}.
     *
     * @param tasks the task list to update
     * @param command the user's command
     */
    private static void addEvent(List<Task> tasks, String command) throws EdithException {
        String details = command.substring(CommandType.EVENT.getKeyword().length()).trim();
        int fromMarker = details.indexOf("/from");
        int toMarker = details.indexOf("/to");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            throw new EdithException("An event needs a description, start date, and end date. Use: event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd");
        }

        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + "/from".length(), toMarker).trim();
        String to = details.substring(toMarker + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of an event cannot be empty. Use: event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd");
        }
        if (from.isEmpty()) {
            throw new EdithException("An event needs a start date after /from. Use: event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd");
        }
        if (to.isEmpty()) {
            throw new EdithException("An event needs an end date after /to. Use: event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd");
        }
        addTask(tasks, new Event(description, parseDate(from, "event start"), parseDate(to, "event end")));
    }

    /**
     * Parses a command date and gives the user a corrective message when it is invalid.
     *
     * @param dateText the date supplied by the user
     * @param dateRole the role of the date in the command
     * @return the parsed date
     * @throws EdithException if the date is not in the required ISO format
     */
    private static LocalDate parseDate(String dateText, String dateRole) throws EdithException {
        try {
            return DateFormatter.parseInput(dateText);
        } catch (DateTimeParseException e) {
            throw new EdithException("The " + dateRole + " date must use yyyy-MM-dd.");
        }
    }

    /** Prints every task currently stored in the task list. */
    private static void printTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Marks the requested task as complete or incomplete.
     *
     * @param tasks the task list
     * @param command the user's mark or unmark command
     * @param commandType whether to mark the task complete or incomplete
     * @throws EdithException if the supplied task number is invalid
     */
    private static void markTask(List<Task> tasks, String command, CommandType commandType)
            throws EdithException {
        boolean shouldMarkDone = commandType == CommandType.MARK;
        // If input is e.g. mark 1, commandType = CommandType.MARK, the comparison is true; the method calls task.markAsDone() below.
        // If input is e.g. unmark 1,commandType = CommandType.UNMARK, the comparison is false; the method calls task.markAsNotDone() below.
        String commandWord = commandType.getKeyword();
        String taskNumberText = command.substring(commandWord.length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (tasks.isEmpty()) {
                throw new EdithException("There are no tasks to " + commandWord + ". Add a task first.");
            }
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new EdithException("Please provide a task number from 1 to " + tasks.size() + ".");
            }
            Task task = tasks.get(taskNumber - 1);
            if (shouldMarkDone) {
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
            } else {
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
            }
            saveTasks(tasks);
            System.out.println("  " + task);
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: " + commandWord + " NUMBER");
        }
    }

    /**
     * Removes the requested task and prints a confirmation message.
     *
     * @param tasks the task list to update
     * @param command the user's delete command
     * @throws EdithException if the supplied task number is invalid
     */
    private static void deleteTask(List<Task> tasks, String command) throws EdithException {
        String taskNumberText = command.substring(CommandType.DELETE.getKeyword().length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (tasks.isEmpty()) {
                throw new EdithException("There are no tasks to delete. Add a task first.");
            }
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new EdithException("Please provide a task number from 1 to " + tasks.size() + ".");
            }
            Task removedTask = tasks.remove(taskNumber - 1);
            saveTasks(tasks);
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: delete NUMBER");
        }
    }

    /** Loads saved tasks, starting with an empty list if the file does not exist or cannot be read. */
    private static List<Task> loadTasks() {
        try {
            return Storage.loadTasks();
        } catch (IOException e) {
            System.out.println("OOPS!!! I could not load your saved tasks. Starting with an empty list.");
            return new ArrayList<>();
        }
    }

    /** Saves task changes and converts storage errors into a message suitable for the command loop. */
    private static void saveTasks(List<Task> tasks) throws EdithException {
        try {
            Storage.saveTasks(tasks);
        } catch (IOException e) {
            throw new EdithException("I could not save your tasks to disk.");
        }
    }
}
