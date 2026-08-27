/** Represents a task with a start time and an end time. */
public class Event extends Task {
    /** The event start time supplied by the user. */
    private final String from;
    /** The event end time supplied by the user. */
    private final String to;

    /**
     * Creates an event with its description, start time, and end time.
     *
     * @param description the text describing the event
     * @param from the start date or time, stored as text
     * @param to the end date or time, stored as text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the format used by the task list.
     *
     * @return the event type, status icon, description, start time, and end time
     */
    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " " + getDescription()
                + " (from: " + from + " to: " + to + ")";
    }

    @Override
    protected String getTaskType() {
        return "event";
    }
}
