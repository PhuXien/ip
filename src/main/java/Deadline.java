/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    /** The deadline text supplied by the user. */
    private final String by;

    /**
     * Creates a deadline with its description and due time.
     *
     * @param description the text describing the task
     * @param by the due date or time, stored as text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the format used by the task list.
     *
     * @return the deadline type, status icon, description, and due time
     */
    @Override
    public String toString() {
        return "[D]" + getStatusIcon() + " " + getDescription() + " (by: " + by + ")";
    }
}
