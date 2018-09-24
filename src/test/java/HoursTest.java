import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HoursTest {
    @Test
    void getStartTime() {
        Hours hours = new Hours("12:34:56", "", "");
        assertEquals("12:34:56", hours.getStartTime());
    }

    @Test
    void setStartTime() {
        Hours hours = new Hours("", "", "");
        hours.setStartTime("12:34:56");
        assertEquals("12:34:56", hours.getStartTime());
    }

    @Test
    void getEndTime() {
        Hours hours = new Hours("", "12:34:56", "");
        assertEquals("12:34:56", hours.getEndTime());
    }

    @Test
    void setEndTime() {
        Hours hours = new Hours("", "", "");
        hours.setEndTime("12:34:56");
        assertEquals("12:34:56", hours.getEndTime());
    }

    @Test
    void getTask() {
        Hours hours = new Hours("", "", "TASK-1");
        assertEquals("TASK-1", hours.getTask());
    }

    @Test
    void setTask() {
        Hours hours = new Hours("", "", "");
        hours.setTask("TASK-1");
        assertEquals("TASK-1", hours.getTask());
    }
}