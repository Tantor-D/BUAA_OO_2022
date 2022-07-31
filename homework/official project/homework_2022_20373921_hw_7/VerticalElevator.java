import java.util.ArrayList;

public class VerticalElevator extends Elevator {
    private final int id;
    private final AskQueue persons;
    private final AskQueue askQueue;
    private final int building;
    private final int maxPersonNum;
    private final int sleepTime;
    
    private int numOfPeople;
    private int direction;
    private int nowFloor;
    
    public VerticalElevator(int id,
                            AskQueue askQueue, int building, int maxPersonNum, int sleepTime) {
        this.id = id;
        this.askQueue = askQueue;
        this.building = building;
        this.maxPersonNum = maxPersonNum;
        this.sleepTime = sleepTime;
        
        persons = new AskQueue();
        numOfPeople = 0;
        direction = Const.UP;
        nowFloor = 1;
    }
    
    @Override
    public void run() {
        while (true) {
            //OutputHandler.printInf("        Vertical Elevator " + id + " ,
            // is alive ." + persons.size() + " people is in it");
            boolean openFlag = false;
            if (persons.isEmpty() && askQueue.isEnd() && askQueue.isEmpty()) {
                //OutputHandler.printInf("        Vertical Elevator   " + id + " , is dead");
                return;
            }
            
            // 开门放人
            if (judgeNeedOut(persons.getAsksUnsafe(), nowFloor)) {
                printElevatorInf(Const.OPEN, nowFloor, building, id);
                openFlag = true;
                personsGoOut(persons, nowFloor, building, id, Const.VERTICAL);
                numOfPeople = persons.getAsksUnsafe().size();
                personsGoIn(); // 先in一次，不影响转向的判断
                sleepForSomeTime(400);
            }
            
            if (isNeedTurn()) {
                direction = opposeDirection(direction);
            }
            
            //开门上人
            if (openFlag) {
                personsGoIn();
                printElevatorInf(Const.CLOSE, nowFloor, building, id);
            } else {
                if (isPersonWantIn() && numOfPeople < maxPersonNum) {
                    printElevatorInf(Const.OPEN, nowFloor, building, id);
                    personsGoIn();
                    sleepForSomeTime(400);
                    personsGoIn();
                    printElevatorInf(Const.CLOSE, nowFloor, building, id);
                }
            }
            
            // 走不走
            if (!persons.isEmpty() || isAskExistThisDirection()) {
                sleepForSomeTime(sleepTime);
                move();
            } else if (askQueue.isEnd()) {
                //OutputHandler.printInf("        Vertical Elevator   " + id + " , is dead");
                return;
            } else {
                //OutputHandler.printInf("        VerticalElevator " + id + " , wait successfully");
                askQueue.letItWait();
            }
        }
    }
    
    public boolean judgeNeedOut(ArrayList<Ask> asks, int nowFloor) {
        for (Ask person : asks) {
            if (person.getNowDestination() == nowFloor) {
                return true;
            }
        }
        return false;
    }
    
    public void move() {
        if (direction == Const.UP) {
            nowFloor++;
        } else {
            nowFloor--;
        }
        printElevatorInf(Const.MOVE, nowFloor, building, id);
    }
    
    public boolean isNeedTurn() {
        if (numOfPeople > 0 || isPersonWantIn() || isAskExistThisDirection()) {
            return false;
        }
        return true;
    }
    
    public void personsGoIn() {
        askQueue.personsGoInVertical(persons, direction, nowFloor, building, maxPersonNum, id);
        numOfPeople = persons.size();
    }
    
    public boolean isPersonWantIn() {
        return askQueue.isPersonWantInVertical(nowFloor, direction);
    }
    
    public boolean isAskExistThisDirection() {
        return askQueue.isRequestExitUpOrDown(nowFloor, direction);
    }
    
}
