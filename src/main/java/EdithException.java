/** Represents an error caused by invalid input to the Edith chatbot. */
public class EdithException extends Exception {
    /**
     * Creates an exception with a message explaining how the user can correct the input.
     *
     * @param message the user-facing explanation of the error
     */
    public EdithException(String message) {
        super(message);
    }
}
