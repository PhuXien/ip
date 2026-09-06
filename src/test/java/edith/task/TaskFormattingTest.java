package edith.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests task status transitions and storage-compatible string representations. */
public class TaskFormattingTest {
    @Test
    public void todo_markAndUnmark_updatesStatusInStringRepresentation() {
        Todo todo = new Todo("read book");

        assertEquals("[T][ ] read book", todo.toString());
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
        todo.markAsNotDone();
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void deadline_withAndWithoutTime_formatsExpectedDateDetails() {
        assertEquals("[D][ ] submit work (by: Sep 06 2026)",
                new Deadline("submit work", LocalDateTime.of(2026, 9, 6, 0, 0), false).toString());
        assertEquals("[D][ ] submit work (by: Sep 06 2026 14:30)",
                new Deadline("submit work", LocalDateTime.of(2026, 9, 6, 14, 30), true).toString());
    }

    @Test
    public void event_mixedOptionalTimes_formatsEachEndpointIndependently() {
        Event event = new Event("conference", LocalDateTime.of(2026, 9, 6, 0, 0), false,
                LocalDateTime.of(2026, 9, 7, 17, 30), true);

        assertEquals("[E][ ] conference (from: Sep 06 2026 to: Sep 07 2026 17:30)", event.toString());
    }
}
