import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the JavaFX components.
 *
 * Most of the application logic happens here.
 */
public class Controller implements Initializable {
    private static final String NEW_TASK_NAME = "New task";
    private static final String SWITCH_BUTTON_TEXT = "Switch";
    private static final String START_BUTTON_TEXT = "Start";
    private static boolean isTrackingStarted = false;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button exportButton;
    @FXML
    private TableView<Hours> hoursTable;
    @FXML
    private TableColumn<Hours, String> taskColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createContextMenu();
        initializeSelectionModel();
    }

    /**
     * Create and set a context menu for the hours table.
     */
    private void createContextMenu() {
        final MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction((ActionEvent event) -> removeSelectedHours());
        final ContextMenu menu = new ContextMenu(removeItem);
        menu.setOnShown((WindowEvent event) -> removeItem.setDisable(hoursTable.getSelectionModel().getSelectedItems().isEmpty()));
        hoursTable.setContextMenu(menu);
    }

    /**
     * Set the multiselection mode for the hours table.
     */
    private void initializeSelectionModel() {
        hoursTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Handle start button click event.
     */
    public void onStartClick() {
        updateLastTaskEndTime();
        startTask();
        editLastTask();
    }

    /**
     * Start tracking a new task.
     *
     * This inserts a new row to the hours table with the current time and a task name.
     * The task name is by default the same as the previous task name. If no previous
     * tasks exist, then it will be set to a dummy name. The end time of the new task
     * will be empty by default.
     */
    private void startTask() {
        final String startTime = Timing.getCurrentTimeAsString();
        final String taskName = getLastTaskName();
        hoursTable.getItems().add(new Hours(startTime, "", taskName));
        startButton.setText(SWITCH_BUTTON_TEXT);
        stopButton.setDisable(false);
        isTrackingStarted = true;
    }

    /**
     * Stop tracking the current task.
     *
     * This will update the end time of the current task to the current time and stop tracking.
     */
    private void stopTask() {
        updateLastTaskEndTime();
        isTrackingStarted = false;
    }

    /**
     * Returns the name of the last task.
     * @return the last task name as a String.
     */
    private String getLastTaskName() {
        final List<Hours> data = hoursTable.getItems();
        if (!data.isEmpty()) {
            return data.get(data.size() - 1).getTask();
        }
        return NEW_TASK_NAME;
    }

    /**
     * Sets the end time of the last task row to the current time.
     */
    private void updateLastTaskEndTime() {
        final List<Hours> data = hoursTable.getItems();
        if (!data.isEmpty()) {
            final int lastIndex = data.size() - 1;
            final Hours lastHours = data.get(lastIndex);
            if (isTrackingStarted) {
                lastHours.setEndTime(Timing.getCurrentTimeAsString());
                data.set(lastIndex, lastHours);
            }
        }
    }

    /**
     * Focus the last item and and activate its task cell for editing.
     */
    private void editLastTask() {
        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                final int row = hoursTable.getItems().size() - 1;
                hoursTable.getSelectionModel().select(row);
                hoursTable.getSelectionModel().focus(row);
                hoursTable.edit(row, taskColumn);
            });
        }).start();
    }

    /**
     * Stop button click handler.
     */
    public void onStopClick() {
        stopTask();
        resetButtonStates();
    }

    /**
     * Export button click handler.
     * @param event the event triggered when the button was clicked
     */
    public void onExportClick(ActionEvent event) {
        final Window window = ((Node) event.getTarget()).getScene().getWindow();
        final File file = Export.getFileChooser().showSaveDialog(window);
        if (file != null) {
            try {
                Export.write(new FileWriter(file), hoursTable.getItems());
                Export.setPreviousDirectory(file.getParent());
            } catch (IOException e) {
                e.printStackTrace(); // Write failed
            }
        }
    }

    /**
     * Handler for start time commit after editing.
     * @param event event for editing the table cell
     */
    public void onStartTimeEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setStartTime(event.getNewValue());
    }

    /**
     * Handler for end time commit after editing.
     * @param event event for editing the table cell
     */
    public void onEndTimeEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setEndTime(event.getNewValue());
    }

    /**
     * Handler for task name commit after editing.
     * @param event event for editing the table cell
     */
    public void onTaskEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setTask(event.getNewValue());
    }

    /**
     * Key press handler.
     * @param keyEvent event for the key press
     */
    public void onKeyPressed(KeyEvent keyEvent) {
        final List<Hours> items = hoursTable.getSelectionModel().getSelectedItems();
        final boolean isDeletePressed = keyEvent.getCode().equals(KeyCode.DELETE);
        if (!items.isEmpty() && isDeletePressed) {
            removeSelectedHours();
        }
    }

    /**
     * Removes the current selection from the hours.
     */
    private void removeSelectedHours() {
        final List<Hours> itemsToDelete = hoursTable.getSelectionModel().getSelectedItems();
        final List<Hours> allItems = hoursTable.getItems();
        final boolean lastDeleted = !itemsToDelete.isEmpty() && itemsToDelete.contains(allItems.get(allItems.size() - 1));
        allItems.removeAll(itemsToDelete);
        if (lastDeleted || allItems.isEmpty()) {
            resetButtonStates();
            isTrackingStarted = false;
        }
    }

    /**
     * Returns buttons (texts and disabled status) to their initial states.
     */
    private void resetButtonStates() {
        startButton.setText(START_BUTTON_TEXT);
        startButton.setDisable(false);
        startButton.requestFocus();
        stopButton.setDisable(true);
        exportButton.setDisable(hoursTable.getItems().isEmpty());
    }
}
