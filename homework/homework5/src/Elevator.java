import java.util.Iterator;

// 电梯挂掉之前记得检查上一个输出是不是close or 停下
public class Elevator extends Thread {
    
    private static final int UP = 1;
    private static final int DOWN = 0;
    private static final int MAXNUM = 6;
    private static final int WAIT_TIME = 400;
    /*private static final int BASE_FLOOR = 1;
    private static final int TOP_FLOOR = 10;
    private static final int NONE = 0;*/
    private static final int OPEN = 1;
    private static final int CLOSE = 2;
    private static final int MOVE = 3;
    
    private final RequestQueue persons;
    private final RequestQueue processQueue;
    private final char building;
    private final int buildingNum; //此处a对应0，输出时要求a对应1，注意
    private int direction;
    private int nowFloor;
    private int numOfPeople;
    
    public Elevator(RequestQueue processQueue, char building) {
        this.processQueue = processQueue;
        this.building = building;
        this.buildingNum = building - 'A';
        
        this.persons = new RequestQueue(); // 实际上这个是这个电梯的私有对象，里面的人都在电梯上
        this.nowFloor = 1;
        this.direction = UP;
        this.numOfPeople = 0;
    }
    
    public void printInf(int opKind) {
        String outMsg;
        if (opKind == CLOSE) {
            outMsg = "CLOSE-" + building + "-" + nowFloor + "-" + (buildingNum + 1);
            OutputHandler.printInf(outMsg);
        }
        if (opKind == OPEN) {
            outMsg = "OPEN-" + building + "-" + nowFloor + "-" + (buildingNum + 1);
            OutputHandler.printInf(outMsg); // 开门
        }
        if (opKind == MOVE) {
            outMsg = "ARRIVE-" + building + "-" + nowFloor + "-" + (buildingNum + 1);
            OutputHandler.printInf(outMsg); // 没有延迟
        }
        
    }
    
    public void moveFloor() {
        if (direction == UP) {
            nowFloor++;
        } else {
            nowFloor--;
        }
        printInf(MOVE);
        
    }
    
    public boolean judgeNeedOut() {
        for (Request person : persons.getRequestsNotSafe()) {
            if (person.getDestination() == nowFloor) {
                return true;
            }
        }
        return false;
    }
    
    public void personGoOut() {
        String outMsg;
        Iterator<Request> it = persons.getRequestsNotSafe().iterator();
        while (it.hasNext()) {
            Request nowPerson = it.next();
            if (nowPerson.getDestination() == nowFloor) {
                outMsg = "OUT-" + nowPerson.getId() + "-" + building
                        + "-" + nowFloor + "-" + (buildingNum + 1);
                OutputHandler.printInf(outMsg);
                numOfPeople--;
                it.remove();
            }
        }
    }
    
    public boolean ifSbWantInAtThisFloor() {
        return processQueue.ifSbWantInAtThisFloor(nowFloor, direction);
    }
    
    public boolean ifRequestExistThisDirection() {
        return processQueue.isRequestExist(nowFloor, direction, numOfPeople);
    }
    
    public void personGoIn() {
        if (ifSbWantInAtThisFloor() && numOfPeople < MAXNUM) {
            while (true) {
                Request newPerson = processQueue.findInPerson(nowFloor, direction, numOfPeople);
                if (newPerson == null) {
                    break;
                }
                persons.addRequest(newPerson);
                numOfPeople++;
            }
        }
    }
    
    public boolean judgeNeedTurn() {
        if (!persons.isEmpty() || ifSbWantInAtThisFloor() || ifRequestExistThisDirection()) {
            return false;
        }
        return true;
    }
    
    public void sleepForUnitTime() {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            //OutputHandler.printInf("elevator " + (building + 1) + " is alive");
            boolean openFlag = false;
            if (processQueue.isEmpty() && processQueue.isEnd() && persons.isEmpty()) {
                return;
            }
            
            if (judgeNeedOut()) { // 下人
                // OutputHandler.printInf("xxxxxxxxxxxx   need out now");
                printInf(OPEN); // 开门
                openFlag = true;
                personGoOut();
                sleepForUnitTime();
            }
            
            if (judgeNeedTurn()) { // 转向
                if (direction == UP) {
                    direction = DOWN;
                } else {
                    direction = UP;
                }
            }
            
            // 上人判断
            if (openFlag) { // 之前开过门的话，默认直接调用上人函数，管它上不上
                personGoIn(); // 之前开门下人后已经等待过了
                printInf(CLOSE);
            } else { // 此处函数对应之前没开过门的情况
                if (ifSbWantInAtThisFloor() && numOfPeople < MAXNUM) {
                    printInf(OPEN); // 开门
                    sleepForUnitTime();
                    personGoIn();
                    printInf(CLOSE);
                }
            }
            
            // 走不走
            if (!persons.isEmpty() || ifRequestExistThisDirection()) {
                sleepForUnitTime();
                moveFloor();
            } else if (processQueue.isEnd()) {
                return;
            } else {
                processQueue.letItWait();
            }
        }
    }
}
