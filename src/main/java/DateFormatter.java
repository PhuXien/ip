import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Converts task dates between Edith's command, display, and saved-file formats. */
public final class DateFormatter {
    /** The ISO date format accepted in commands. */
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    /** The readable date format used in task lists and saved tasks. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);

    private DateFormatter() {
    }

    /**
     * Parses a date supplied in a command.
     *
     * @param input a date in {@code yyyy-MM-dd} format
     * @return the parsed date
     */
    public static LocalDate parseInput(String input) {
        return LocalDate.parse(input, INPUT_FORMAT);
    }

    /**
     * Parses a date displayed in a saved task.
     *
     * @param displayedDate a date in {@code MMM dd yyyy} format
     * @return the parsed date
     */
    public static LocalDate parseDisplayedDate(String displayedDate) {
        return LocalDate.parse(displayedDate, DISPLAY_FORMAT);
    }

    /**
     * Formats a date for task-list output.
     *
     * @param date the date to format
     * @return the readable date
     */
    public static String formatForDisplay(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
