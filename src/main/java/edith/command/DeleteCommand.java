package edith.command;

import edith.exception.EdithException;
import edith.task.Task;
import edith.task.TaskList;
import edith.ui.Ui;

/** Removes one task from Edith's task list. */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /** Creates a command targeting the supplied one-based task number. */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws EdithException {
        if (tasks.isEmpty()) {
            throw new EdithException("There are no tasks to delete. Add a task first.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EdithException("Please provide a task number from 1 to " + tasks.size() + ".");
        }

        Task removedTask = tasks.remove(taskNumber - 1);
        saveTasks(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}
