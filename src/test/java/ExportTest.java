import javafx.stage.FileChooser;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExportTest {
    private static String normalize(String string) {
        return string.replaceAll("\\r\\n", "\n")
                     .replaceAll("\\r", "\n");
    }

    @Test
    void writeNullWriter() {
        StringWriter writer = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> Export.write(writer, null));
    }

    @Test
    void writeNullItems() {
        ArrayList<Hours> hours = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> Export.write(null, hours));
    }

    @Test
    void writeEmptyItems() {
        StringWriter writer = new StringWriter();
        ArrayList<Hours> hours = new ArrayList<>();
        Export.write(writer, hours);
        assertEquals("", normalize(writer.toString()));
    }

    @Test
    void write() {
        StringWriter writer = new StringWriter();
        ArrayList<Hours> hours = new ArrayList<>();
        hours.add(new Hours("11:22:33", "11:22:44", "TASK-1"));
        hours.add(new Hours("22:33:44", "22:33:55", "TASK-2"));
        Export.write(writer, hours);
        assertEquals("11:22:33;11:22:44;TASK-1\n22:33:44;22:33:55;TASK-2\n", normalize(writer.toString()));
    }

    @Test
    void getFileChooser() {
        FileChooser fileChooser = Export.getFileChooser();
        assertNotNull(fileChooser);
        assertSame(fileChooser, Export.getFileChooser());
        assertEquals("Export", fileChooser.getTitle());
        assertTrue(fileChooser.getExtensionFilters().size() > 0);
    }

    @Test
    void setPreviousDirectory() {
        assertNotEquals("test", Export.getPreviousDirectory());
        Export.setPreviousDirectory("test");
        assertEquals("test", Export.getPreviousDirectory());
    }


    @Test
    void getPreviousDirectory() {
        assertEquals(System.getProperty("user.home"), Export.getPreviousDirectory());
    }
}