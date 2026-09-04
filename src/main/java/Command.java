/** Represents an executable instruction understood by Edith. */
public abstract class Command {
    /**
     * Performs this command using the current task list and user interface.
     *
     * @param tasks the task list on which the command operates
     * @param ui the user interface used to display the result
     * @throws EdithException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui) throws EdithException;

    /**
     * Returns whether Edith should stop after executing this command.
     *
     * @return {@code true} only for an exit command
     */
    public boolean isExit() {
        return false;
    }
}
