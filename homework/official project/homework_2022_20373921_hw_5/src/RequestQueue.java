import java.util.ArrayList;

public class RequestQueue {
    private static final int UP = 1;
    private static final int DOWN = 0;
    private static final int MAXNUM = 6;
    
    private boolean isEnd;
    private final ArrayList<Request> requests;
    
    //////////////////////////////////////////////////////////////////////////////
    // 构造函数
    public RequestQueue() {
        isEnd = false;
        requests = new ArrayList<>();
    }
    
    /////////////////////////////////////////////////////////////////////////////
    public synchronized void addRequest(Request request) {
        requests.add(request);
        this.notifyAll();
    }
    
    public synchronized void letItWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized boolean isEmpty() {
        notifyAll();
        return requests.isEmpty();
    }
    
    public synchronized boolean isEnd() {
        notifyAll(); // todo 需要吗？ 这样会不会错
        return isEnd;
    }
    
    public synchronized void setEnd(boolean end) {
        isEnd = end;
        notifyAll();
    }
    
    public synchronized boolean isRequestExist(int nowFloor, int direction, int nowNumOfPerson) {
        // 不包括本楼层，在电梯运行方向上有无请求
        if (direction == UP) {
            for (Request request : requests) {
                if (request.getFrom() > nowFloor) {
                    return true;
                }
            }
        } else {
            for (Request request : requests) {
                if (request.getFrom() < nowFloor) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public synchronized boolean ifSbWantInAtThisFloor(int nowFloor, int direction) {
        for (Request request : requests) {
            if (request.getFrom() == nowFloor && request.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized Request findInPerson(int nowFloor, int direction, int nowNumOfPerson) {
        if (nowNumOfPerson == MAXNUM) {
            return null;
        }
        Request retRequest = null;
        for (Request request : requests) {
            if (request.getFrom() == nowFloor && request.getDirection() == direction) {
                if (retRequest == null) {
                    retRequest = request;
                } else {
                    if (Math.abs(request.getFrom() - request.getDestination())
                            < Math.abs(request.getFrom() - retRequest.getDestination())) {
                        retRequest = request;
                    }
                }
            }
        }
        if (retRequest != null) {
            requests.remove(retRequest);
            String outMsg;
            outMsg = "IN-" + retRequest.getId() + "-" + retRequest.getBuilding() +
                    "-" + nowFloor + "-" +  (retRequest.getBuilding() - 'A' + 1);
            OutputHandler.printInf(outMsg);
        }
        return retRequest;
    }
    
    // 这个方法是给person使用的，绝对不能对共享对象使用
    public ArrayList<Request> getRequestsNotSafe() {
        return requests;
    }
}
