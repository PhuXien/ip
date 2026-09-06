package edith.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edith.command.AddCommand;
import edith.command.DeleteCommand;
import edith.command.ExitCommand;
import edith.command.FindCommand;
import edith.command.ListCommand;
import edith.command.MarkCommand;
import edith.command.UnmarkCommand;
import edith.exception.EdithException;

/** Tests translation of user input into executable commands. */
public class ParserTest {
    @Test
    public void parse_validCommands_returnsExpectedCommandTypes() throws EdithException {
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline submit work /by 2026-09-06"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event meeting /from 2026-09-06 1400 /to 2026-09-06 1500"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    public void parse_unknownCommand_exceptionThrown() {
        EdithException exception = assertThrows(EdithException.class, () -> Parser.parse("remind me"));

        assertEquals("I don't know what that means. Use todo, deadline, event, list, find, "
                        + "mark, unmark, delete, or bye.",
                exception.getMessage());
    }

    @Test
    public void parse_findWithoutKeyword_exceptionThrown() {
        assertEquals("Please provide a word or phrase to find. Use: find KEYWORD",
                assertThrows(EdithException.class, () -> Parser.parse("find")).getMessage());
    }

    @Test
    public void parse_todoWithoutDescription_exceptionThrown() {
        assertEquals("The description of a todo cannot be empty.",
                assertThrows(EdithException.class, () -> Parser.parse("todo")).getMessage());
    }

    @Test
    public void parse_invalidDeadlineForms_exceptionThrown() {
        assertThrows(EdithException.class, () -> Parser.parse("deadline submit work"));
        assertThrows(EdithException.class, () -> Parser.parse("deadline /by 2026-09-06"));
        assertThrows(EdithException.class, () -> Parser.parse("deadline submit work /by"));
        assertThrows(EdithException.class, () -> Parser.parse("deadline submit work /by tomorrow"));
    }

    @Test
    public void parse_invalidEventForms_exceptionThrown() {
        assertThrows(EdithException.class, () -> Parser.parse("event meeting"));
        assertThrows(EdithException.class, () -> Parser.parse("event /from 2026-09-06 /to 2026-09-07"));
        assertThrows(EdithException.class, () -> Parser.parse("event meeting /from /to 2026-09-07"));
        assertThrows(EdithException.class, () -> Parser.parse("event meeting /from 2026-09-06 /to"));
        assertThrows(EdithException.class,
                () -> Parser.parse("event meeting /to 2026-09-07 /from 2026-09-06"));
        assertThrows(EdithException.class,
                () -> Parser.parse("event meeting /from tomorrow /to 2026-09-07"));
        assertThrows(EdithException.class,
                () -> Parser.parse("event meeting /from 2026-09-06 /to tomorrow"));
    }

    @Test
    public void parse_taskCommandWithoutWholeNumber_exceptionThrown() {
        assertEquals("Please provide a whole-number task number. Use: mark NUMBER",
                assertThrows(EdithException.class, () -> Parser.parse("mark two")).getMessage());
        assertEquals("Please provide a whole-number task number. Use: unmark NUMBER",
                assertThrows(EdithException.class, () -> Parser.parse("unmark 1.5")).getMessage());
        assertEquals("Please provide a whole-number task number. Use: delete NUMBER",
                assertThrows(EdithException.class, () -> Parser.parse("delete")).getMessage());
    }
}
