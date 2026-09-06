package edith.command;

import edith.task.TaskList;
import edith.ui.Ui;

/** Displays all tasks currently stored by Edith. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showTaskList(tasks);
    }
}
