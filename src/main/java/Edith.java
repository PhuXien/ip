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
        String[] tasks = new String[MAX_TASKS];
        boolean[] completedTasks = new boolean[MAX_TASKS];
        int taskCount = 0;
        while (true) {
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
                    String status = completedTasks[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + "." + status + " " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println("Please provide a valid task number.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        completedTasks[taskIndex] = true;
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            } else {
                System.out.println("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
            }
            System.out.println(DIVIDER);
        }
    }
}
