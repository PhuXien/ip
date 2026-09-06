package edith.command;

import edith.exception.EdithException;
import edith.task.Task;
import edith.task.TaskList;
import edith.ui.Ui;

/** Adds one parsed task to Edith's task list. */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     *
     * @param task the fully parsed task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws EdithException {
        tasks.add(task);
        saveTasks(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
