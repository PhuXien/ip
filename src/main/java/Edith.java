import java.io.IOException;
import java.util.Scanner;

/** Entry point for the Edith chatbot. */
public class Edith {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        TaskList tasks = loadTasks(ui);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            ui.showDivider();

            try {
                CommandType commandType = Parser.parseCommandType(command);
                if (commandType == null) {
                    throw new EdithException("I don't know what that means. Use todo, deadline, event, list, mark, unmark, delete, or bye.");
                }

                Command parsedCommand = Parser.parseCommand(command, commandType);
                if (parsedCommand != null) {
                    parsedCommand.execute(tasks, ui);
                    if (parsedCommand.isExit()) {
                        ui.showDivider();
                        return;
                    }
                    ui.showDivider();
                    continue;
                }

                switch (commandType) {
                case TODO:
                    addTask(tasks, Parser.parseTodo(command), ui);
                    break;
                case DEADLINE:
                    addTask(tasks, Parser.parseDeadline(command), ui);
                    break;
                case EVENT:
                    addTask(tasks, Parser.parseEvent(command), ui);
                    break;
                default:
                    throw new IllegalStateException("Command should already have been handled: " + commandType);
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
    private static void addTask(TaskList tasks, Task task, Ui ui) throws EdithException {
        if (task.getDescription().isEmpty()) {
            throw new EdithException("The description of a " + task.getTaskType() + " cannot be empty.");
        }
        tasks.add(task);
        saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Loads saved tasks, starting with an empty list if the file does not exist or cannot be read.
     *
     * @param ui the user interface used to show a loading error
     * @return the restored tasks, or an empty list after a loading error
     */
    private static TaskList loadTasks(Ui ui) {
        try {
            return new TaskList(Storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Saves task changes and converts storage errors into a message suitable for the command loop. */
    private static void saveTasks(TaskList tasks) throws EdithException {
        try {
            Storage.saveTasks(tasks.asList());
        } catch (IOException e) {
            throw new EdithException("I could not save your tasks to disk.");
        }
    }
}
