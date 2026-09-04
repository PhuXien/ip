import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Entry point for the Edith chatbot. */
public class Edith {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = loadTasks(ui);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ui.showDivider();

            try {
                CommandType commandType = CommandType.fromInput(command);
                if (commandType == null) {
                    throw new EdithException("I don't know what that means. Use todo, deadline, event, list, mark, unmark, delete, or bye.");
                }

                switch (commandType) {
                case BYE:
                    ui.showGoodbye();
                    ui.showDivider();
                    return;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                case UNMARK:
                    markTask(tasks, command, commandType, ui);
                    break;
                case DELETE:
                    deleteTask(tasks, command, ui);
                    break;
                case TODO:
                    String description = command.substring(commandType.getKeyword().length()).trim();
                    addTask(tasks, new Todo(description), ui);
                    break;
                case DEADLINE:
                    addDeadline(tasks, command, ui);
                    break;
                case EVENT:
                    addEvent(tasks, command, ui);
                    break;
                }
            } catch (EdithException e) {
                ui.showError(e);
            }
            ui.showDivider();
        }
    }

    /**
     * Adds a task and prints the confirmation message.
     *
     * @param tasks the task list to update
     * @param task the task to add
     * @param ui the user interface used to show the confirmation
     */
    private static void addTask(List<Task> tasks, Task task, Ui ui) throws EdithException {
        if (task.getDescription().isEmpty()) {
            throw new EdithException("The description of a " + task.getTaskType() + " cannot be empty.");
        }
        tasks.add(task);
        saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Parses and adds a deadline command with an optional {@code HHmm} time after its date.
     *
     * @param tasks the task list to update
     * @param command the user's command
     * @param ui the user interface used to show the confirmation
     */
    private static void addDeadline(List<Task> tasks, String command, Ui ui) throws EdithException {
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
        addTask(tasks, new Deadline(description, dueDateTime.value(), dueDateTime.hasTime()), ui);
    }

    /**
     * Parses and adds an event command with optional {@code HHmm} times after each date.
     *
     * @param tasks the task list to update
     * @param command the user's command
     * @param ui the user interface used to show the confirmation
     */
    private static void addEvent(List<Task> tasks, String command, Ui ui) throws EdithException {
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
        addTask(tasks, new Event(description, startDateTime.value(), startDateTime.hasTime(),
                endDateTime.value(), endDateTime.hasTime()), ui);
    }

    /**
     * Parses a command date-time and gives the user a corrective message when it is invalid.
     *
     * @param dateText the date supplied by the user
     * @param dateRole the role of the date in the command
     * @return the parsed date-time
     * @throws EdithException if the value is not in a supported ISO format
     */
    private static DateFormatter.ParsedDateTime parseDateTime(String dateText, String dateRole) throws EdithException {
        try {
            return DateFormatter.parseInput(dateText);
        } catch (DateTimeParseException e) {
            throw new EdithException("The " + dateRole
                    + " date must use yyyy-MM-dd, optionally followed by a time in the format HHmm.");
        }
    }

    /**
     * Marks the requested task as complete or incomplete.
     *
     * @param tasks the task list
     * @param command the user's mark or unmark command
     * @param commandType whether to mark the task complete or incomplete
     * @param ui the user interface used to show the confirmation
     * @throws EdithException if the supplied task number is invalid
     */
    private static void markTask(List<Task> tasks, String command, CommandType commandType, Ui ui)
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
            } else {
                task.markAsNotDone();
            }
            saveTasks(tasks);
            ui.showTaskMarked(task, shouldMarkDone);
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: " + commandWord + " NUMBER");
        }
    }

    /**
     * Removes the requested task and prints a confirmation message.
     *
     * @param tasks the task list to update
     * @param command the user's delete command
     * @param ui the user interface used to show the confirmation
     * @throws EdithException if the supplied task number is invalid
     */
    private static void deleteTask(List<Task> tasks, String command, Ui ui) throws EdithException {
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
            ui.showTaskDeleted(removedTask, tasks.size());
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: delete NUMBER");
        }
    }

    /**
     * Loads saved tasks, starting with an empty list if the file does not exist or cannot be read.
     *
     * @param ui the user interface used to show a loading error
     * @return the restored tasks, or an empty list after a loading error
     */
    private static List<Task> loadTasks(Ui ui) {
        try {
            return Storage.loadTasks();
        } catch (IOException e) {
            ui.showLoadingError();
            return new java.util.ArrayList<>();
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
