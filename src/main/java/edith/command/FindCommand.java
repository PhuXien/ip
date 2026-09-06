package edith.command;

import edith.task.TaskList;
import edith.ui.Ui;

/** Displays tasks whose descriptions contain a given word or phrase. */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches task descriptions for the supplied text.
     *
     * @param keyword the word or phrase to search for
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui) {
        ui.showMatchingTasks(tasks.find(keyword));
    }
}
