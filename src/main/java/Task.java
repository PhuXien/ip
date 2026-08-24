/** Represents a task and whether it has been completed. */
public class Task {
    /** The text describing what the user needs to do. */
    private final String description;

    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates a new incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code [X]} for a completed task, or {@code [ ]} otherwise
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns this task in the format used by the task list.
     *
     * @return the status icon followed by the task description
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
