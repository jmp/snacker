import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timing {
    /**
     * Returns the current time.
     * @return current time as a String
     */
    public static String getCurrentTimeAsString() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }
}
