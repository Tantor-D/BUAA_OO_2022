import java.util.ArrayList;
import java.util.Iterator;

public class AskQueue {
    private final ArrayList<Ask> asks;
    private boolean isEnd;
    
    ///////////////////////////////////////////////////////////////////////////////////
    // 构造方法 、 getter 、 setter
    public AskQueue() {
        asks = new ArrayList<>();
        isEnd = false;
    }
    
    public synchronized boolean isEnd() {
        return isEnd;
    }
    
    public synchronized void setEnd(boolean end) {
        isEnd = end;
        notifyAll();
    }
    
    public synchronized int size() {
        return asks.size();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    // 其它方法
    public synchronized boolean isEmpty() {
        // 两种情况下认为等待调度器处理的队列为空，一个是真的空了，另一个是只有一个非法请求
        return asks.isEmpty() || (asks.size() == 1 && asks.get(0).getFromFloor() == -1);
    }
    
    public synchronized int getAskNum() {
        return asks.size();
    }
    
    public synchronized void addAskToWaitQueue(Ask ask) {
        asks.add(ask);
        notifyAll();
    }
    
    public synchronized Ask getAskFromWaitQueue() {
        Ask thisAsk = asks.get(0);
        asks.remove(0);
        return thisAsk;
        
    }
    
    public boolean judgeCanOpen(int fromBuilding, int destinationBuilding, int openCondition) {
        return (((openCondition >> fromBuilding) & 1) &
                ((openCondition >> destinationBuilding) & 1)) == 1;
    }
    
    public synchronized void addAskToProcessQueue(Ask ask) {
        for (int i = 0; i < asks.size(); i++) {
            if (asks.get(i).getNowDistance() >= ask.getNowDistance()) {
                asks.add(i, ask);
                notifyAll();
                return;
            }
        }
        asks.add(ask);
        notifyAll();
    }
    
    public synchronized void addAskToPersons(Ask ask) {
        asks.add(ask);
    }
    
    public synchronized void letItWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ArrayList<Ask> getAsksUnsafe() {
        return asks;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    // 给纵向电梯用的
    public synchronized boolean isRequestExitUpOrDown(int nowFloor, int direction) {
        if (direction == Const.UP) {
            for (Ask ask : asks) {
                if (ask.getNowFloor() > nowFloor) {
                    return true;
                }
            }
            return false;
        }
        // down
        for (Ask ask : asks) {
            if (ask.getNowFloor() < nowFloor) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized void personsGoInVertical(
            AskQueue persons,
            int direction,
            int nowFloor,
            int building,
            int maxPersonsNum,
            int elevatorId) {
        int allowNum = maxPersonsNum - persons.getAsksUnsafe().size();
        if (allowNum == 0) {
            return;
        }
        Iterator<Ask> it = asks.iterator();
        while (it.hasNext()) {
            Ask thisAsk = it.next();
            if (thisAsk.getNowFloor() == nowFloor && thisAsk.getNowDirection() == direction) {
                persons.addAskToPersons(thisAsk);
                allowNum--;
                it.remove();
                thisAsk.printPersonInf(Const.IN, building, nowFloor, elevatorId);
            }
            if (allowNum == 0) {
                return;
            }
        }
    }
    
    public synchronized boolean isPersonWantInVertical(int floor, int direction) {
        for (Ask ask : asks) {
            if (ask.getNowFloor() == floor && ask.getNowDirection() == direction) {
                return true;
            }
        }
        return false;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    // 横向电梯用的
    public synchronized void personsGoInHorizontal(
            AskQueue persons,
            int direction,
            int floor,
            int nowBuilding,
            int maxPersonNum,
            int elevatorId,
            int openCondition
    ) {
        int allowNum = maxPersonNum - persons.getAsksUnsafe().size();
        if (allowNum == 0) {
            return;
        }
        Iterator<Ask> it = asks.iterator();
        while (it.hasNext()) {
            Ask thisAsk = it.next();
            if (thisAsk.getNowBuilding() == nowBuilding && thisAsk.getNowDirection() == direction &&
                    judgeCanOpen(thisAsk.getNowBuilding(),
                            thisAsk.getNowDestination(), openCondition)) {
                persons.addAskToPersons(thisAsk);
                allowNum--;
                it.remove();
                thisAsk.printPersonInf(Const.IN, nowBuilding, floor, elevatorId);
            }
            if (allowNum == 0) {
                return;
            }
        }
    }
    
    public synchronized boolean isPersonCanAndWantInHorizontal(
            int eleNowBuilding,
            int direction,
            int openCondition) {
        for (Ask ask : asks) {
            if (ask.getNowBuilding() == eleNowBuilding && ask.getNowDirection() == direction &&
                    judgeCanOpen(eleNowBuilding, ask.getNowDestination(), openCondition)) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean isAskExistThisDirectionHorizontal(
            int elevatorNowBuilding,
            int direction,
            int openCondition) {
        if (direction == Const.LEFT) { // 不包括现在所处的位置
            for (Ask ask : asks) {
                if ((elevatorNowBuilding - ask.getNowBuilding() + 5) % 5 <= 2 &&
                        elevatorNowBuilding != ask.getNowBuilding() &&
                        judgeCanOpen(ask.getNowBuilding(),
                                ask.getNowDestination(), openCondition)) {
                    return true;
                }
            }
            return false;
        }
        for (Ask ask : asks) {
            if ((ask.getNowBuilding() - elevatorNowBuilding + 5) % 5 <= 2 &&
                    elevatorNowBuilding != ask.getNowBuilding() &&
                    judgeCanOpen(ask.getNowBuilding(), ask.getNowDestination(), openCondition)) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized boolean isAskExistThisDirectionWithSameDirectionHorizontal(
            int elevNowBuilding, int direction, int openCondition) {
        // 此函数包括了对当前电梯所处位置的请求的判断
        if (direction == Const.LEFT) {
            for (Ask ask : asks) {
                if ((elevNowBuilding - ask.getNowBuilding() + 5) % 5 <= 2 &&
                        ask.getNowDirection() == direction &&
                        judgeCanOpen(ask.getNowBuilding(),
                                ask.getDestinationBuilding(), openCondition)) {
                    return true;
                }
            }
            return false;
        }
        for (Ask ask : asks) {
            if ((ask.getNowBuilding() - elevNowBuilding + 5) % 5 <= 2 &&
                    ask.getNowDirection() == direction &&
                    judgeCanOpen(ask.getNowBuilding(),
                            ask.getDestinationBuilding(), openCondition)) {
                return true;
            }
        }
        return false;
    }
}

