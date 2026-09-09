package edith.ui;

import java.util.Scanner;
import java.util.function.Consumer;

import edith.exception.EdithException;
import edith.task.Task;
import edith.task.TaskList;

/** Handles Edith's command input and formatted output. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Consumer<String> output;
    private final Scanner scanner;

    /** Creates a console interface that reads commands from standard input. */
    public Ui() {
        this(new Scanner(System.in), System.out::println);
    }

    /**
     * Creates an interface that sends each output line to the supplied destination.
     *
     * @param output destination that receives each line of chatbot output
     */
    public Ui(Consumer<String> output) {
        this(new Scanner(System.in), output);
    }

    private Ui(Scanner scanner, Consumer<String> output) {
        this.scanner = scanner;
        this.output = output;
    }

    /**
     * Returns whether another command is available to read.
     *
     * @return {@code true} if standard input has another line
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next complete command entered by the user.
     *
     * @return the next line from standard input
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays Edith's welcome message. */
    public void showWelcome() {
        String banner = """
                 _____ ____ ___ _____ _   _
                | ____|  _ \\_ _|_   _| | | |
                |  _| | | | | |  | | | |_| |
                | |___| |_| | |  | | |  _  |
                |_____|____/___| |_| |_| |_|
                """;
        output.accept(banner);
        output.accept("Hello! I'm EDITH.");
        output.accept("What can I do for you?");
        showDivider();
    }

    /** Displays the divider between command interactions. */
    public void showDivider() {
        output.accept(DIVIDER);
    }

    /** Displays the farewell message. */
    public void showGoodbye() {
        output.accept("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an application error to the user.
     *
     * @param exception the error containing the message to display
     */
    public void showError(EdithException exception) {
        output.accept("OOPS!!! " + exception.getMessage());
    }

    /** Displays a warning that saved tasks could not be restored. */
    public void showLoadingError() {
        output.accept("OOPS!!! I could not load your saved tasks. Starting with an empty list.");
    }

    /**
     * Displays every task currently stored in the task list.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        output.accept("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Displays the tasks that match a find command. */
    public void showMatchingTasks(TaskList tasks) {
        output.accept("Here are the matching tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Displays the supplied tasks as a one-based numbered list. */
    private void showNumberedTasks(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            output.accept((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        output.accept("Got it. I've added this task:");
        output.accept("  " + task);
        output.accept("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task's completion state changed.
     *
     * @param task the task whose completion state changed
     * @param isDone whether the task is now completed
     */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            output.accept("Nice! I've marked this task as done:");
        } else {
            output.accept("OK, I've marked this task as not done yet:");
        }
        output.accept("  " + task);
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task the task that was removed
     * @param taskCount the number of tasks after the removal
     */
    public void showTaskDeleted(Task task, int taskCount) {
        output.accept("Noted. I've removed this task:");
        output.accept("  " + task);
        output.accept("Now you have " + taskCount + " tasks in the list.");
    }
}
