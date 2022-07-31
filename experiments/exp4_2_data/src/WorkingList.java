import java.util.ArrayList;
import java.util.List;

public class WorkingList { //工序对应的队列
    private final List<Request> requestList;

    WorkingList() {
        requestList = new ArrayList<>();
    }

    public synchronized void addRequest(Request request) {
        requestList.add(request);
        notifyAll();
    }

    public synchronized Request getRequest() {
        Request request = null;
        while (!Controller.getInstance().getEndTag()) {
            if (requestList.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                request = requestList.get(0);
                requestList.remove(0);
                break;
            }
        }
        return request;
    }
}
