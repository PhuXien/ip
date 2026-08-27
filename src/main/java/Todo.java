/** Represents a task with no associated date or time. */
public class Todo extends Task {
    /**
     * Creates a todo with the given description.
     *
     * @param description the text describing the todo
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the format used by the task list.
     *
     * @return the todo type, status icon, and description
     */
    @Override
    public String toString() {
        return "[T]" + getStatusIcon() + " " + getDescription();
    }

    @Override
    protected String getTaskType() {
        return "todo";
    }
}
