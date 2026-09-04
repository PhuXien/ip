/** Displays all tasks currently stored by Edith. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showTaskList(tasks);
    }
}
