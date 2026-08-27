import java.util.Scanner;

/** Entry point for the Edith chatbot. */
public class Edith {
    private static final String DIVIDER = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(DIVIDER);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(DIVIDER);
                return;
            }

            try {
                if (command.equals("list")) {
                    printTaskList(tasks, taskCount);
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    markTask(tasks, taskCount, command, true);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    markTask(tasks, taskCount, command, false);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    String description = command.substring("todo".length()).trim();
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    taskCount = addDeadline(tasks, taskCount, command);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    taskCount = addEvent(tasks, taskCount, command);
                } else {
                    throw new EdithException("I don't know what that means. Use todo, deadline, event, list, mark, unmark, or bye.");
                }
            } catch (EdithException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
            System.out.println(DIVIDER);
        }
    }

    /**
     * Adds a task and prints the confirmation message, if the list has space.
     *
     * @param tasks the task list to update
     * @param taskCount the number of tasks currently in the list
     * @param task the task to add
     * @return the updated task count
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) throws EdithException {
        if (task.getDescription().isEmpty()) {
            throw new EdithException("The description of a " + task.getTaskType() + " cannot be empty.");
        }
        if (taskCount == MAX_TASKS) {
            throw new EdithException("I can only store up to " + MAX_TASKS + " tasks. Delete a task before adding another.");
        }

        tasks[taskCount] = task;
        taskCount++;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }

    /**
     * Parses and adds a deadline command in the form {@code deadline DESCRIPTION /by TIME}.
     *
     * @param tasks the task list to update
     * @param taskCount the number of tasks currently in the list
     * @param command the user's command
     * @return the updated task count
     */
    private static int addDeadline(Task[] tasks, int taskCount, String command) throws EdithException {
        String details = command.substring("deadline".length()).trim();
        int byMarker = details.indexOf("/by");
        if (byMarker < 0) {
            throw new EdithException("A deadline needs a description and a due time. Use: deadline DESCRIPTION /by TIME");
        }

        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + "/by".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of a deadline cannot be empty. Use: deadline DESCRIPTION /by TIME");
        }
        if (by.isEmpty()) {
            throw new EdithException("A deadline needs a due time after /by. Use: deadline DESCRIPTION /by TIME");
        }
        return addTask(tasks, taskCount, new Deadline(description, by));
    }

    /**
     * Parses and adds an event command in the form {@code event DESCRIPTION /from TIME /to TIME}.
     *
     * @param tasks the task list to update
     * @param taskCount the number of tasks currently in the list
     * @param command the user's command
     * @return the updated task count
     */
    private static int addEvent(Task[] tasks, int taskCount, String command) throws EdithException {
        String details = command.substring("event".length()).trim();
        int fromMarker = details.indexOf("/from");
        int toMarker = details.indexOf("/to");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            throw new EdithException("An event needs a description, start time, and end time. Use: event DESCRIPTION /from START /to END");
        }

        String description = details.substring(0, fromMarker).trim();
        String from = details.substring(fromMarker + "/from".length(), toMarker).trim();
        String to = details.substring(toMarker + "/to".length()).trim();
        if (description.isEmpty()) {
            throw new EdithException("The description of an event cannot be empty. Use: event DESCRIPTION /from START /to END");
        }
        if (from.isEmpty()) {
            throw new EdithException("An event needs a start time after /from. Use: event DESCRIPTION /from START /to END");
        }
        if (to.isEmpty()) {
            throw new EdithException("An event needs an end time after /to. Use: event DESCRIPTION /from START /to END");
        }
        return addTask(tasks, taskCount, new Event(description, from, to));
    }

    /** Prints every task currently stored in the task list. */
    private static void printTaskList(Task[] tasks, int taskCount) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks the requested task as complete or incomplete.
     *
     * @param tasks the task list
     * @param taskCount the number of tasks currently stored
     * @param command the user's mark or unmark command
     * @param shouldMarkDone whether to mark the task complete
     * @throws EdithException if the supplied task number is invalid
     */
    private static void markTask(Task[] tasks, int taskCount, String command, boolean shouldMarkDone)
            throws EdithException {
        String commandWord = shouldMarkDone ? "mark" : "unmark";
        String taskNumberText = command.substring(commandWord.length()).trim();
        try {
            int taskNumber = Integer.parseInt(taskNumberText);
            if (taskCount == 0) {
                throw new EdithException("There are no tasks to " + commandWord + ". Add a task first.");
            }
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new EdithException("Please provide a task number from 1 to " + taskCount + ".");
            }
            Task task = tasks[taskNumber - 1];
            if (shouldMarkDone) {
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
            } else {
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + task);
        } catch (NumberFormatException e) {
            throw new EdithException("Please provide a whole-number task number. Use: " + commandWord + " NUMBER");
        }
    }
}
