import java.time.LocalDateTime;

/** Represents a task with start and end dates, each optionally with a time. */
public class Event extends Task {
    /** The date and optional time at which this event starts. */
    private final LocalDateTime from;
    /** Whether the event start includes a time. */
    private final boolean fromHasTime;
    /** The date and optional time at which this event ends. */
    private final LocalDateTime to;
    /** Whether the event end includes a time. */
    private final boolean toHasTime;

    /**
     * Creates an event with its description, start date-time, and end date-time.
     *
     * @param description the text describing the event
     * @param from the start date-time
     * @param fromHasTime whether the start includes a time
     * @param to the end date-time
     * @param toHasTime whether the end includes a time
     */
    public Event(String description, LocalDateTime from, boolean fromHasTime, LocalDateTime to, boolean toHasTime) {
        super(description);
        this.from = from;
        this.fromHasTime = fromHasTime;
        this.to = to;
        this.toHasTime = toHasTime;
    }

    /**
     * Returns this event in the format used by the task list.
     *
     * @return the event type, status icon, description, start date-time, and end date-time
     */
    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " " + getDescription()
                + " (from: " + DateFormatter.formatForDisplay(from, fromHasTime)
                + " to: " + DateFormatter.formatForDisplay(to, toHasTime) + ")";
    }

    @Override
    protected String getTaskType() {
        return "event";
    }
}
