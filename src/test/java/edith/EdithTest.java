package edith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests command-response behavior exposed to the graphical interface. */
public class EdithTest {
    @Test
    public void getResponse_unknownCommand_returnsCorrectiveMessage() {
        Edith edith = Edith.createForGui();

        String response = edith.getResponse("unknown");

        assertEquals("OOPS!!! I don't know what that means. Use todo, deadline, event, list, find, mark, "
                + "unmark, delete, or bye.", response);
        assertFalse(edith.isExit());
    }

    @Test
    public void getResponse_bye_returnsGoodbyeAndEndsSession() {
        Edith edith = Edith.createForGui();

        String response = edith.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!", response);
        assertTrue(edith.isExit());
    }
}
