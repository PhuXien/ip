package edith.command;

import edith.exception.EdithException;
import edith.task.Task;
import edith.task.TaskList;
import edith.ui.Ui;

/** Marks one task as incomplete. */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /** Creates a command targeting the supplied one-based task number. */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws EdithException {
        if (tasks.isEmpty()) {
            throw new EdithException("There are no tasks to unmark. Add a task first.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EdithException("Please provide a task number from 1 to " + tasks.size() + ".");
        }

        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        saveTasks(tasks);
        ui.showTaskMarked(task, false);
    }
}
