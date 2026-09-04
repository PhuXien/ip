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
                Command parsedCommand = Parser.parse(command);
                parsedCommand.execute(tasks, ui);
                if (parsedCommand.isExit()) {
                    ui.showDivider();
                    return;
                }
            } catch (EdithException e) {
                ui.showError(e);
            }
            ui.showDivider();
        }
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
}
