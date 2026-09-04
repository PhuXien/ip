import java.time.LocalDate;

/** Represents a task with a start date and an end date. */
public class Event extends Task {
    /** The date on which this event starts. */
    private final LocalDate from;
    /** The date on which this event ends. */
    private final LocalDate to;

    /**
     * Creates an event with its description, start date, and end date.
     *
     * @param description the text describing the event
     * @param from the start date
     * @param to the end date
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the format used by the task list.
     *
     * @return the event type, status icon, description, start date, and end date
     */
    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " " + getDescription()
                + " (from: " + DateFormatter.formatForDisplay(from)
                + " to: " + DateFormatter.formatForDisplay(to) + ")";
    }

    @Override
    protected String getTaskType() {
        return "event";
    }
}
