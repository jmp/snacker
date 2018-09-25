import javafx.stage.FileChooser;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExporterTest {
    private static String normalize(String string) {
        return string.replaceAll("\\r\\n", "\n")
                     .replaceAll("\\r", "\n");
    }

    @Test
    void writeNullWriter() {
        StringWriter writer = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> new Exporter().write(writer, null));
    }

    @Test
    void writeNullItems() {
        ArrayList<Hours> hours = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new Exporter().write(null, hours));
    }

    @Test
    void writeEmptyItems() {
        StringWriter writer = new StringWriter();
        ArrayList<Hours> hours = new ArrayList<>();
        new Exporter().write(writer, hours);
        assertEquals("", normalize(writer.toString()));
    }

    @Test
    void write() {
        StringWriter writer = new StringWriter();
        ArrayList<Hours> hours = new ArrayList<>();
        hours.add(new Hours("11:22:33", "11:22:44", "TASK-1"));
        hours.add(new Hours("22:33:44", "22:33:55", "TASK-2"));
        new Exporter().write(writer, hours);
        assertEquals("11:22:33;11:22:44;TASK-1\n22:33:44;22:33:55;TASK-2\n", normalize(writer.toString()));
    }

    @Test
    void getFileChooser() {
        Exporter exporter = new Exporter();
        FileChooser fileChooser = exporter.getFileChooser();
        assertNotNull(fileChooser);
        assertSame(fileChooser, exporter.getFileChooser());
        assertEquals("Export", fileChooser.getTitle());
        assertTrue(fileChooser.getExtensionFilters().size() > 0);
    }

    @Test
    void getPreviousDirectory() {
        assertEquals(System.getProperty("user.home"), new Exporter().getPreviousDirectory());
    }

    @Test
    void setPreviousDirectory() {
        Exporter exporter = new Exporter();
        assertNotEquals("test", exporter.getPreviousDirectory());
        exporter.setPreviousDirectory("test");
        assertEquals("test", exporter.getPreviousDirectory());
    }
}