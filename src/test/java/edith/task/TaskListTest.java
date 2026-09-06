package edith.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task ordering, mutation, and snapshot isolation. */
public class TaskListTest {
    @Test
    public void addGetAndRemove_multipleTasks_preservesOrder() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("first");
        Todo second = new Todo("second");

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(first, tasks.remove(0));
        assertSame(second, tasks.get(0));
        assertEquals(1, tasks.size());
    }

    @Test
    public void constructor_sourceListChanged_taskListUnaffected() {
        List<Task> source = new ArrayList<>();
        source.add(new Todo("original"));
        TaskList tasks = new TaskList(source);

        source.clear();

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] original", tasks.get(0).toString());
    }

    @Test
    public void asList_returnedSnapshotCannotBeModified() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));
        List<Task> snapshot = tasks.asList();

        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Todo("write book")));
        assertEquals(1, tasks.size());
    }

    @Test
    public void isEmpty_emptyThenPopulated_returnsExpectedState() {
        TaskList tasks = new TaskList();

        assertTrue(tasks.isEmpty());
        tasks.add(new Todo("read book"));
        assertFalse(tasks.isEmpty());
    }
}
