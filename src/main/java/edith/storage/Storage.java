package edith.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import edith.task.Deadline;
import edith.task.Event;
import edith.task.Task;
import edith.task.Todo;
import edith.util.DateFormatter;

/** Saves tasks to, and restores tasks from, Edith's project-relative data file. */
public class Storage {
    /** The heading used by both the console task list and the saved file. */
    private static final String LIST_HEADING = "Here are the tasks in your list:";

    /** A portable path relative to the directory from which Edith is started. */
    private static final Path DATA_FILE = Path.of("data", "edith.txt");

    /**
     * Loads saved tasks, returning an empty list when no data file has been created yet.
     *
     * @return the saved tasks
     * @throws IOException if the data file cannot be read or has an invalid task line
     */
    public static List<Task> loadTasks() throws IOException {
        return loadTasks(DATA_FILE);
    }

    /**
     * Loads tasks from a specified file so persistence can be tested without using the application data file.
     *
     * @param dataFile the file from which tasks are loaded
     * @return the tasks stored in the file, or an empty list if the file does not exist
     * @throws IOException if the file cannot be read or contains an invalid task
     */
    static List<Task> loadTasks(Path dataFile) throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(dataFile)) {
            return tasks;
        }

        for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
            if (!line.isBlank() && !line.equals(LIST_HEADING)) {
                tasks.add(parseTask(line));
            }
        }
        return tasks;
    }

    /**
     * Writes the task list in exactly the format displayed by the {@code list} command.
     *
     * @param tasks the current tasks to save
     * @throws IOException if the data directory or file cannot be written
     */
    public static void saveTasks(List<Task> tasks) throws IOException {
        saveTasks(tasks, DATA_FILE);
    }

    /**
     * Saves tasks to a specified file so persistence can be tested without using the application data file.
     *
     * @param tasks the tasks to save in their current order
     * @param dataFile the file to which tasks are written
     * @throws IOException if the file or its parent directory cannot be written
     */
    static void saveTasks(List<Task> tasks, Path dataFile) throws IOException {
        Files.createDirectories(dataFile.getParent());
        List<String> lines = new ArrayList<>();
        lines.add(LIST_HEADING);
        for (int i = 0; i < tasks.size(); i++) {
            lines.add((i + 1) + "." + tasks.get(i));
        }
        Files.write(dataFile, lines, StandardCharsets.UTF_8);
    }

    /**
     * Recreates one task from its numbered list representation.
     *
     * @param savedLine one line from the data file
     * @return the restored task
     * @throws IOException if the line is not a task representation Edith understands
     */
    private static Task parseTask(String savedLine) throws IOException {
        int numberSeparator = savedLine.indexOf('.');
        if (numberSeparator < 1) {
            throw new IOException("Invalid saved task: " + savedLine);
        }
        String taskText = savedLine.substring(numberSeparator + 1);
        if (taskText.length() < 7 || taskText.charAt(0) != '[' || taskText.charAt(2) != ']'
                || taskText.charAt(3) != '[' || taskText.charAt(5) != ']'
                || taskText.charAt(6) != ' ') {
            throw new IOException("Invalid saved task: " + savedLine);
        }

        Task task;
        String descriptionAndTime = taskText.substring(7);
        switch (taskText.charAt(1)) {
            case 'T':
                task = new Todo(descriptionAndTime);
                break;
            case 'D':
                task = parseDeadline(descriptionAndTime, savedLine);
                break;
            case 'E':
                task = parseEvent(descriptionAndTime, savedLine);
                break;
            default:
                throw new IOException("Invalid saved task: " + savedLine);
        }
        if (taskText.charAt(4) == 'X') {
            task.markAsDone();
        } else if (taskText.charAt(4) != ' ') {
            throw new IOException("Invalid saved task: " + savedLine);
        }
        return task;
    }

    /** Restores the description and due date-time from a saved deadline. */
    private static Task parseDeadline(String text, String savedLine) throws IOException {
        int byMarker = text.lastIndexOf(" (by: ");
        if (byMarker < 0 || !text.endsWith(")")) {
            throw new IOException("Invalid saved task: " + savedLine);
        }
        try {
            DateFormatter.ParsedDateTime dueDateTime =
                    DateFormatter.parseDisplayedDateTime(text.substring(byMarker + 6, text.length() - 1));
            return new Deadline(text.substring(0, byMarker), dueDateTime.value(), dueDateTime.hasTime());
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid saved task: " + savedLine, e);
        }
    }

    /** Restores the description, start date-time, and end date-time from a saved event. */
    private static Task parseEvent(String text, String savedLine) throws IOException {
        int fromMarker = text.lastIndexOf(" (from: ");
        int toMarker = text.lastIndexOf(" to: ");
        if (fromMarker < 0 || toMarker < fromMarker || !text.endsWith(")")) {
            throw new IOException("Invalid saved task: " + savedLine);
        }
        try {
            DateFormatter.ParsedDateTime startDateTime =
                    DateFormatter.parseDisplayedDateTime(text.substring(fromMarker + 8, toMarker));
            DateFormatter.ParsedDateTime endDateTime =
                    DateFormatter.parseDisplayedDateTime(text.substring(toMarker + 5, text.length() - 1));
            return new Event(text.substring(0, fromMarker), startDateTime.value(), startDateTime.hasTime(),
                    endDateTime.value(), endDateTime.hasTime());
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid saved task: " + savedLine, e);
        }
    }
}
