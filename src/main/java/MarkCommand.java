/** Marks one task as completed. */
public class MarkCommand extends Command {
    private final int taskNumber;

    /** Creates a command targeting the supplied one-based task number. */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) throws EdithException {
        if (tasks.isEmpty()) {
            throw new EdithException("There are no tasks to mark. Add a task first.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new EdithException("Please provide a task number from 1 to " + tasks.size() + ".");
        }

        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        saveTasks(tasks);
        ui.showTaskMarked(task, true);
    }
}
