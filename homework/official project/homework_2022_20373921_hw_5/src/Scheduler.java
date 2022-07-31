import java.util.ArrayList;

public class Scheduler {
    private final ArrayList<RequestQueue> processQueues;
    
    public Scheduler(ArrayList<RequestQueue> processQueues) {
        this.processQueues = processQueues;
    }
}
