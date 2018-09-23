import javafx.beans.property.SimpleStringProperty;

/**
 * Time tracking entry.
 *
 * It consists of a start time, end time, and the task name,
 * each represented as strings.
 */
@SuppressWarnings("WeakerAccess")
public class Hours {
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private SimpleStringProperty task;

    protected Hours(String startTime, String endTime, String task) {
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.task = new SimpleStringProperty(task);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String time) {
        startTime.set(time);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String time) {
        endTime.set(time);
    }

    public void setTask(String name) {
        task.set(name);
    }

    public String getTask() {
        return task.get();
    }
}
