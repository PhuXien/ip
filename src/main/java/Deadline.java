import java.time.LocalDate;

/** Represents a task that must be completed by a specified date. */
public class Deadline extends Task {
    /** The date by which this task must be completed. */
    private final LocalDate by;

    /**
     * Creates a deadline with its description and due date.
     *
     * @param description the text describing the task
     * @param by the due date
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline in the format used by the task list.
     *
     * @return the deadline type, status icon, description, and due date
     */
    @Override
    public String toString() {
        return "[D]" + getStatusIcon() + " " + getDescription()
                + " (by: " + DateFormatter.formatForDisplay(by) + ")";
    }

    @Override
    protected String getTaskType() {
        return "deadline";
    }
}
