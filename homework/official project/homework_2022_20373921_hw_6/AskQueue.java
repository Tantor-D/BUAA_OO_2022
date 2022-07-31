import java.util.ArrayList;
import java.util.Iterator;

public class AskQueue {
    private final ArrayList<Ask> asks;
    private boolean isEnd;
    
    ////////////////////////////////////////////////////////////////
    // 构造函数
    public AskQueue() {
        isEnd = false;
        asks = new ArrayList<>();
    }
    
    public synchronized boolean isEmpty() {
        return asks.isEmpty();
    }
    
    public synchronized void setEnd(boolean end) {
        isEnd = end;
        notifyAll();
    }
    
    public synchronized void addAsk(Ask ask) { // 保证按照距离单调递增
        for (int i = 0; i < asks.size(); i++) {
            if (asks.get(i).getDistance() >= ask.getDistance()) {
                asks.add(i, ask);
                notifyAll();
                return;
            }
        }
        asks.add(ask);
        notifyAll();
    }
    
    public synchronized boolean isEnd() {
        return isEnd;
    }
    
    public ArrayList<Ask> getAsksUnsafe() {
        return asks;
    }
    
    public synchronized boolean isRequestExitUpOrDown(int nowFloor, int direction) {
        if (direction == Const.UP) {
            for (Ask ask : asks) {
                if (ask.getFrom() > nowFloor) {
                    return true;
                }
            }
            return false;
        }
        // down
        for (Ask ask : asks) {
            if (ask.getFrom() < nowFloor) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean isRequestExitLeftOrRight(int nowBuilding, int direction) {
        if (direction == Const.LEFT) { // 不包括本身所出的位置
            for (Ask ask : asks) {
                if ((nowBuilding - ask.getFrom() + 5) % 5 <= 2 && nowBuilding != ask.getFrom()) {
                    return true;
                }
            }
            return false;
        }
        // Right
        for (Ask ask : asks) {
            if ((ask.getFrom() - nowBuilding + 5) % 5 <= 2 && nowBuilding != ask.getFrom()) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean isRequestExitLeftOrRightWithSameDirection(
            int nowBuilding, int direction) {
        if (direction == Const.LEFT) {
            for (Ask ask : asks) {
                if ((nowBuilding - ask.getFrom() + 5) % 5 <= 2 &&
                        ask.getDirection() == direction) { // 包括了本地
                    return true;
                }
            }
            return false;
        }
    
        for (Ask ask : asks) {
            if ((ask.getFrom() - nowBuilding + 5) % 5 <= 2 && ask.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized void letItWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public synchronized boolean isPersonWantIn(int place, int direction) {
        for (Ask ask : asks) {
            if (ask.getFrom() == place && ask.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized void personGoIn(AskQueue persons, int direction, int nowPlace,
                                        int buildingOrFloor, int elevatorId) {
        int allowNum = Const.MAXNUM - persons.getAsksUnsafe().size();
        if (allowNum == 0) {
            return;
        }
        Iterator<Ask> it = asks.iterator();
        while (it.hasNext()) {
            Ask thisAsk = it.next();
            if (thisAsk.getFrom() == nowPlace && thisAsk.getDirection() == direction) {
                persons.addAsk(thisAsk);
                allowNum--;
                it.remove();
                
                if (direction == Const.UP || direction == Const.DOWN) {
                    thisAsk.printPersonInf(Const.IN, buildingOrFloor, nowPlace, elevatorId);
                } else {
                    thisAsk.printPersonInf(Const.IN, nowPlace, buildingOrFloor, elevatorId);
                }
                if (allowNum == 0) {
                    //notifyAll();
                    return;
                }
            }
        }
        //notifyAll();
    }
}
