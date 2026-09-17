package edith.gui;

import edith.Edith;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controls the main chatbot window defined in {@code MainWindow.fxml}. */
public class MainWindow extends AnchorPane {
    private static final String WELCOME_MESSAGE = String.join("\n",
            "Hello! I'm EDITH. You can use:",
            "",
            "Add tasks:",
            "  • todo DESCRIPTION  [/tags #TAG...]",
            "  • deadline DESCRIPTION /by yyyy-MM-dd [HHmm] [/tags #TAG...]",
            "  • event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]  [/tags #TAG...]",
            "View and manage tasks:",
            "  • list",
            "  • find KEYWORD",
            "  • mark NUMBER",
            "  • unmark NUMBER",
            "  • delete NUMBER",
            "Tags:",
            "  • tag NUMBER #TAG...",
            "  • untag NUMBER #TAG...",
            "",
            "Exit:",
            "• bye - end the chat");

    @FXML
    private VBox dialogContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button sendButton;
    @FXML
    private TextField userInput;

    private Edith edith;

    /** Configures behavior that depends only on FXML controls. */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener(observable -> scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the chatbot that handles commands entered in this window.
     *
     * @param edith chatbot instance to connect to the interface
     */
    public void setEdith(Edith edith) {
        this.edith = edith;
        dialogContainer.getChildren().add(
                DialogBox.getEdithDialog(WELCOME_MESSAGE));
        if (!edith.getStartupError().isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getEdithDialog(edith.getStartupError()));
        }
    }

    /** Sends nonblank input to Edith and appends both sides of the exchange. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty() || edith == null || edith.isExit()) {
            return;
        }

        String response = edith.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getEdithDialog(response));
        userInput.clear();

        if (edith.isExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
