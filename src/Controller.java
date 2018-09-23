import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final String CSV_SEPARATOR = ";";
    private static final String NEW_TASK_NAME = "New task";
    private static final String SWITCH_BUTTON_TEXT = "Switch";
    private static final String START_BUTTON_TEXT = "Start";
    private static boolean isTrackingStarted = false;
    private static FileChooser fileChooser;

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

    private void createContextMenu() {
        final MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction((ActionEvent event) -> removeSelectedHours());
        final ContextMenu menu = new ContextMenu(removeItem);
        menu.setOnShown((WindowEvent event) -> removeItem.setDisable(hoursTable.getSelectionModel().getSelectedItems().isEmpty()));
        hoursTable.setContextMenu(menu);
    }

    private void initializeSelectionModel() {
        hoursTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void onStartClick() {
        updateLastTaskEndTime();
        startTask();
        editLastTask();
    }

    private void startTask() {
        final String startTime = getCurrentTimeAsString();
        final String taskName = getLastTaskName();
        hoursTable.getItems().add(new Hours(startTime, "", taskName));
        startButton.setText(SWITCH_BUTTON_TEXT);
        stopButton.setDisable(false);
        isTrackingStarted = true;
    }

    private void stopTask() {
        final ObservableList<Hours> data = hoursTable.getItems();
        final int lastIndex = data.size() - 1;
        Hours lastHours = data.get(lastIndex);
        lastHours.setEndTime(getCurrentTimeAsString());
        data.set(lastIndex, lastHours);
        isTrackingStarted = false;
    }

    private String getLastTaskName() {
        final ObservableList<Hours> data = hoursTable.getItems();
        if (!data.isEmpty()) {
            return data.get(data.size() - 1).getTask();
        }
        return NEW_TASK_NAME;
    }

    private void updateLastTaskEndTime() {
        final ObservableList<Hours> data = hoursTable.getItems();
        final String currentTime = getCurrentTimeAsString();
        if (!data.isEmpty()) {
            final int lastIndex = data.size() - 1;
            final Hours lastHours = data.get(lastIndex);
            if (isTrackingStarted) {
                data.set(lastIndex, new Hours(lastHours.getStartTime(), currentTime, lastHours.getTask()));
            }
        }
    }

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

    public void onStopClick() {
        stopTask();
        resetButtonStates();
    }

    public void onExportClick(ActionEvent event) {
        final Window window = ((Node) event.getTarget()).getScene().getWindow();
        final File file = getFileChooser().showSaveDialog(window);
        if (file != null) {
            exportToFile(file);
        }
    }

    public void onStartTimeEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setStartTime(event.getNewValue());
    }

    public void onEndTimeEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setEndTime(event.getNewValue());
    }

    public void onTaskEditCommit(TableColumn.CellEditEvent<Hours, String> event) {
        event.getRowValue().setTask(event.getNewValue());
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        final ObservableList<Hours> items = hoursTable.getSelectionModel().getSelectedItems();
        final boolean isDeletePressed = keyEvent.getCode().equals(KeyCode.DELETE);
        if (!items.isEmpty() && isDeletePressed) {
            removeSelectedHours();
        }
    }

    private void exportToFile(File file) {
        ObservableList<Hours> allItems = hoursTable.getItems();
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Hours hours : allItems) {
                writer.println(hours.getStartTime() + CSV_SEPARATOR + hours.getEndTime() + CSV_SEPARATOR + hours.getTask());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTimeAsString() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }

    private static FileChooser getFileChooser() {
        if (fileChooser != null) {
            return fileChooser;
        }
        fileChooser = createFileChooser();
        return fileChooser;
    }

    private static FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        ObservableList<FileChooser.ExtensionFilter> filters = fileChooser.getExtensionFilters();
        filters.add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        filters.add(new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt"));
        filters.add(new FileChooser.ExtensionFilter("All Files (*.*)", "*.*"));
        fileChooser.setTitle("Export");
        return fileChooser;
    }

    private void removeSelectedHours() {
        final ObservableList<Hours> itemsToDelete = hoursTable.getSelectionModel().getSelectedItems();
        final ObservableList<Hours> allItems = hoursTable.getItems();
        final boolean lastDeleted = !itemsToDelete.isEmpty() && itemsToDelete.contains(allItems.get(allItems.size() - 1));
        allItems.removeAll(itemsToDelete);
        if (lastDeleted || allItems.isEmpty()) {
            resetButtonStates();
            isTrackingStarted = false;
        }
    }

    private void resetButtonStates() {
        startButton.setText(START_BUTTON_TEXT);
        startButton.setDisable(false);
        startButton.requestFocus();
        stopButton.setDisable(true);
        exportButton.setDisable(hoursTable.getItems().isEmpty());
    }
}
