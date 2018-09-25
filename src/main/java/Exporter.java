import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class Exporter {
    private static final String CSV_SEPARATOR = ";";
    private static final String FILE_CHOOSER_TITLE = "Export";
    private FileChooser fileChooser;
    private String previousDirectory = System.getProperty("user.home");

    /**
     * Export the given hours into the given file.
     *
     * @param writer the writer to write to
     * @param items items to write into the file
     */
    void write(Writer writer, List<Hours> items) {
        if (writer == null || items == null) {
            throw new IllegalArgumentException("Writer or items must not be null!");
        }
        try (PrintWriter printWriter = new PrintWriter(writer)) {
            for (Hours hours : items) {
                printWriter.print(hours.getStartTime());
                printWriter.print(CSV_SEPARATOR);
                printWriter.print(hours.getEndTime());
                printWriter.print(CSV_SEPARATOR);
                printWriter.print(hours.getTask());
                printWriter.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a new FileChooser object for choosing a file for export.
     *
     * If a file chooser does not exist yet, it will be created.
     * Otherwise the existing one will be returned.
     *
     * @return a FileChooser object
     */
    public FileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = createFileChooser();
        }
        fileChooser.setInitialDirectory(new File(getPreviousDirectory()));
        return fileChooser;
    }

    /**
     * Creates a new FileChooser object for choosing a file for export.
     * @return a newly created FileChooser object
     */
    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        ObservableList<FileChooser.ExtensionFilter> filters = fileChooser.getExtensionFilters();
        filters.add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        filters.add(new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt"));
        filters.add(new FileChooser.ExtensionFilter("All Files (*.*)", "*.*"));
        fileChooser.setTitle(FILE_CHOOSER_TITLE);
        return fileChooser;
    }

    /**
     * Returns the previous directory opened with the file chooser.
     * @return previous directory as a File object
     */
    public String getPreviousDirectory() {
        return previousDirectory;
    }

    /**
     * Sets the previous directory.
     * @param path directory path
     */
    public void setPreviousDirectory(String path) {
        previousDirectory = path;
    }
}
