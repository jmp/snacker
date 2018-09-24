import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class Export {
    private static final String CSV_SEPARATOR = ";";

    /**
     * Export the given hours into the given file.
     *
     * @param writer the writer to write to
     * @param items items to write into the file
     */
    static void write(Writer writer, List<Hours> items) {
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
}
