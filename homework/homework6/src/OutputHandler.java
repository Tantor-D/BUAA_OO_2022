import com.oocourse.TimableOutput;

public class OutputHandler {
    public static synchronized void printInf(String msg) {
        TimableOutput.println(msg);
    }
}
