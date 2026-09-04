import java.io.IOException;

/** Entry point for the Edith chatbot. */
public class Edith {
    private final Ui ui;
    private TaskList tasks;

    /** Creates an Edith application with a console user interface. */
    public Edith() {
        this.ui = new Ui();
    }

    /** Starts Edith's command-processing loop. */
    public void run() {
        ui.showWelcome();
        tasks = loadTasks();
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();

            try {
                Command parsedCommand = Parser.parse(command);
                parsedCommand.execute(tasks, ui);
                isExit = parsedCommand.isExit();
            } catch (EdithException e) {
                ui.showError(e);
            } finally {
                ui.showDivider();
            }
        }
    }

    /** Starts Edith using its default console and storage configuration. */
    public static void main(String[] args) {
        new Edith().run();
    }

    /**
     * Loads saved tasks, starting with an empty list if the file does not exist or cannot be read.
     *
     * @return the restored tasks, or an empty list after a loading error
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(Storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }
}
