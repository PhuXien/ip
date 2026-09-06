package edith.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edith.exception.EdithException;
import edith.task.TaskList;
import edith.task.Todo;

/** Tests safety checks applied before task-mutating commands execute. */
public class TaskCommandValidationTest {
    @Test
    public void execute_emptyTaskList_reportsCommandSpecificError() {
        TaskList tasks = new TaskList();

        assertEquals("There are no tasks to mark. Add a task first.",
                assertThrows(EdithException.class, () -> new MarkCommand(1).execute(tasks, null)).getMessage());
        assertEquals("There are no tasks to unmark. Add a task first.",
                assertThrows(EdithException.class, () -> new UnmarkCommand(1).execute(tasks, null)).getMessage());
        assertEquals("There are no tasks to delete. Add a task first.",
                assertThrows(EdithException.class, () -> new DeleteCommand(1).execute(tasks, null)).getMessage());
    }

    @Test
    public void execute_taskNumberOutsideList_reportsValidRange() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertEquals("Please provide a task number from 1 to 1.",
                assertThrows(EdithException.class, () -> new MarkCommand(0).execute(tasks, null)).getMessage());
        assertEquals("Please provide a task number from 1 to 1.",
                assertThrows(EdithException.class, () -> new UnmarkCommand(2).execute(tasks, null)).getMessage());
        assertEquals("Please provide a task number from 1 to 1.",
                assertThrows(EdithException.class, () -> new DeleteCommand(-1).execute(tasks, null)).getMessage());
    }
}
