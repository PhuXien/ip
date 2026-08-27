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

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println("Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (command.startsWith("unmark ")) {
                String taskNumberText = command.substring("unmark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println("Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (command.startsWith("todo ")) {
                String description = command.substring("todo ".length()).trim();
                if (description.isEmpty()) {
                    System.out.println("Please provide a description for the todo.");
                } else {
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                }
            } else if (command.startsWith("deadline ")) {
                taskCount = addDeadline(tasks, taskCount, command);
            } else if (command.startsWith("event ")) {
                taskCount = addEvent(tasks, taskCount, command);
            } else {
                System.out.println("I don't understand that command.");
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
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount == MAX_TASKS) {
            System.out.println("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            return taskCount;
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
    private static int addDeadline(Task[] tasks, int taskCount, String command) {
        String details = command.substring("deadline ".length()).trim();
        int byMarker = details.indexOf(" /by ");
        if (byMarker < 0) {
            System.out.println("Please use: deadline DESCRIPTION /by TIME");
            return taskCount;
        }

        String description = details.substring(0, byMarker).trim();
        String by = details.substring(byMarker + " /by ".length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            System.out.println("Please use: deadline DESCRIPTION /by TIME");
            return taskCount;
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
    private static int addEvent(Task[] tasks, int taskCount, String command) {
        String details = command.substring("event ".length()).trim();
        int fromMarker = details.indexOf(" /from ");
        int toMarker = details.indexOf(" /to ");
        if (fromMarker < 0 || toMarker < 0 || toMarker < fromMarker) {
            System.out.println("Please use: event DESCRIPTION /from START /to END");
            return taskCount;
        }

        String description = details.substring(0, fromMarker).trim(); //trims from beginning to fromMarker
        String from = details.substring(fromMarker + " /from ".length(), toMarker).trim();
        String to = details.substring(toMarker + " /to ".length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            System.out.println("Please use: event DESCRIPTION /from START /to END");
            return taskCount;
        }
        return addTask(tasks, taskCount, new Event(description, from, to));
    }
}
