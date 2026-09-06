package edith.ui;

import java.util.Scanner;

import edith.exception.EdithException;
import edith.task.Task;
import edith.task.TaskList;

/** Handles all console input and output for Edith. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /** Creates a console interface that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Returns whether another command is available to read. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and returns the next complete command entered by the user. */
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
        System.out.println(banner);
        System.out.println("Hello! I'm EDITH.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /** Displays the divider between command interactions. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays the farewell message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /** Displays a user-correctable error. */
    public void showError(EdithException exception) {
        System.out.println("OOPS!!! " + exception.getMessage());
    }

    /** Displays a warning that saved tasks could not be restored. */
    public void showLoadingError() {
        System.out.println("OOPS!!! I could not load your saved tasks. Starting with an empty list.");
    }

    /** Displays every task currently stored in the task list. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Displays the tasks that match a find command. */
    public void showMatchingTasks(TaskList tasks) {
        System.out.println("Here are the matching tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Displays the supplied tasks as a one-based numbered list. */
    private void showNumberedTasks(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays confirmation that a task's completion state changed. */
    public void showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
