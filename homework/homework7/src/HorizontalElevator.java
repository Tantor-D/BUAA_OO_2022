import java.util.ArrayList;

public class HorizontalElevator extends Elevator {
    private final int id;
    private final AskQueue persons;
    private final AskQueue askQueue;
    private final int floor;
    private final int maxPersonNum;
    private final int sleepTime;
    private final int openCondition;
    private int numOfPeople;
    private int direction;
    private int nowBuilding;
    
    public HorizontalElevator(int id,
                              AskQueue askQueue,
                              int floor,
                              int maxPersonNum,
                              int sleepTime,
                              int openCondition) {
        this.id = id;
        this.askQueue = askQueue;
        this.floor = floor;
        this.maxPersonNum = maxPersonNum;
        this.sleepTime = sleepTime;
        this.openCondition = openCondition;
        
        persons = new AskQueue();
        numOfPeople = 0;
        direction = Const.RIGHT;
        nowBuilding = 0;
    }
    
    public boolean judgeCanOpen(int fromBuilding, int destinationBuilding) {
        return (((openCondition >> fromBuilding) & 1) &
                ((openCondition >> destinationBuilding) & 1)) == 1;
    }
    
    public boolean judgeCanOpen(int nowBuilding) {
        return ((openCondition >> nowBuilding) & 1) == 1;
    }
    
    /*public synchronized void printForDebug(int nowFinishNum) {
        for (Ask ask : persons.getAsksUnsafe()) {
            OutputHandler.printInf("   nowFinishNum:" + nowFinishNum
                    + " person " + ask.getId() + " , is in horizontal ele " + id);
        }
    }*/
    
    @Override
    public void run() {
        while (true) {
            //OutputHandler.printInf("        Horizontal
            // Elevator " + id + " , is alive . " + persons.size() + "people is in it");
            boolean openFlag = false;
            if (askQueue.isEnd() && askQueue.isEmpty() && persons.isEmpty()) {
                //OutputHandler.printInf("        Horizontal Elevator " + id + " , is dead");
                return;
            }
            
            if (judgeNeedOut(persons.getAsksUnsafe(), nowBuilding)) {
                printElevatorInf(Const.OPEN, floor, nowBuilding, id);
                openFlag = true;   // ??????personGoOut?????????????????????????????????
                personsGoOut(persons, nowBuilding, floor, id, Const.HORIZONTAL);
                numOfPeople = persons.getAsksUnsafe().size();
                personsGoIn();
                sleepForSomeTime(400);
            }
            
            if (isNeedTurn()) {
                direction = opposeDirection(direction);
            }
            
            if (openFlag) {
                personsGoIn();
                printElevatorInf(Const.CLOSE, floor, nowBuilding, id);
            } else {
                if (numOfPeople < maxPersonNum && isPersonsWantAndCanIn()) {
                    printElevatorInf(Const.OPEN, floor, nowBuilding, id);
                    personsGoIn();
                    sleepForSomeTime(400);
                    personsGoIn();
                    printElevatorInf(Const.CLOSE, floor, nowBuilding, id);
                }
            }
            
            if (numOfPeople > 0 || isAskExistThisDirection()) {
                sleepForSomeTime(sleepTime);
                move();
            } else if (askQueue.isEnd()) {
                //OutputHandler.printInf("        Horizontal
                // Elevator " + id + " , is dead");
                return;
            } else {
                //OutputHandler.printInf("        Horizontal
                // Elevator " + id + " , wait successfully");
                askQueue.letItWait();
            }
        }
    }
    
    public boolean isAskExistThisDirection() {
        return askQueue.isAskExistThisDirectionHorizontal(nowBuilding, direction, openCondition);
    }
    
    public boolean isPersonsWantAndCanIn() {
        return askQueue.isPersonCanAndWantInHorizontal(nowBuilding, direction, openCondition);
    }
    
    public void personsGoIn() {
        askQueue.personsGoInHorizontal(persons, direction,
                floor, nowBuilding, maxPersonNum, id, openCondition);
        numOfPeople = persons.size();
    }
    
    public boolean isNeedTurn() {
        if (numOfPeople > 0 || isAskExistThisDirectionWithSameDirection()) {
            // ???????????????????????????????????????????????????????????????
            return false;
        }
        // ??????????????????????????????????????????????????? ??? ???????????????
        if (isAskExistOpposeDirectionWithOpposeDirection()) {
            return true;
        }
        // ???????????????????????? ????????????????????????????????????????????????????????????
        if (isAskExistThisDirection()) {
            return false;
        }
        return true;
    }
    
    public boolean isAskExistThisDirectionWithSameDirection() {
        // ?????????????????????????????????????????????
        return askQueue.isAskExistThisDirectionWithSameDirectionHorizontal(
                nowBuilding, direction, openCondition);
        
    }
    
    public boolean isAskExistOpposeDirectionWithOpposeDirection() {
        // ?????????????????????????????????????????????
        return askQueue.isAskExistThisDirectionWithSameDirectionHorizontal(
                nowBuilding, opposeDirection(direction), openCondition);
    }
    
    public boolean judgeNeedOut(ArrayList<Ask> asks, int nowBuilding) {
        if (!judgeCanOpen(nowBuilding)) {
            return false;
        }
        for (Ask ask : asks) {
            if (ask.getNowDestination() == nowBuilding) {
                return true;
            }
        }
        return false;
    }
    
    public void move() {
        if (direction == Const.LEFT) {
            nowBuilding = (nowBuilding + 4) % 5;
        } else {
            nowBuilding = (nowBuilding + 1) % 5;
        }
        printElevatorInf(Const.MOVE, floor, nowBuilding, id);
    }
    
}
