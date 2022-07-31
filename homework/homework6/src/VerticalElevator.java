public class VerticalElevator extends Elevator {
    
    private final int id;
    private final AskQueue persons;
    private final AskQueue askQueue;
    private int numOfPeople;
    private final int building;
    private int direction;
    private int nowFloor;
    
    public VerticalElevator(AskQueue askQueue, int building, int id) {
        this.id = id;
        this.askQueue = askQueue;
        this.building = building;
        
        persons = new AskQueue();
        numOfPeople = 0;
        direction = Const.UP;
        nowFloor = 1;
    }
    
    @Override
    public void run() {
        while (true) {
            //OutputHandler.printInf(id + " vertical is alive now");
            boolean openFlag = false;
            if (persons.isEmpty() && askQueue.isEnd() && askQueue.isEmpty()) {
                //OutputHandler.printInf(id + " vertical thread is end");
                return;
            }
            
            if (judgeNeedOut(persons.getAsksUnsafe(), nowFloor)) { // 下人
                //System.out.println(id + "下人");
                printElevatorInf(Const.OPEN, nowFloor, building, id);
                openFlag = true;
                personsGoOut(persons, nowFloor, building, id);
                numOfPeople = persons.getAsksUnsafe().size();
                sleepForUnitTime();
            }
            
            if (isNeedTurn()) { // 转向
                //System.out.println(id + "转向");
                direction = opposeDirection(direction);
            }
            
            if (openFlag) { // 上人，已开门则无条件上人
                personsGoIn();
                printElevatorInf(Const.CLOSE, nowFloor, building, id);
            } else {
                if (isPersonWantIn() && numOfPeople < Const.MAXNUM) {
                    printElevatorInf(Const.OPEN, nowFloor, building, id);
                    personsGoIn(); // 先进一次人，不等，一个优化，反正不会影响其它电梯
                    sleepForUnitTime();
                    personsGoIn();
                    printElevatorInf(Const.CLOSE, nowFloor, building, id);
                }
            }
            
            // 走不走
            if (!persons.isEmpty() || isRequestExistThisDirection()) {
                sleepForUnitTime();
                move();
            } else if (askQueue.isEnd()) {
                //OutputHandler.printInf(id + " vertical thread is end");
                return;
            } else {
                // OutputHandler.printInf(id + "wait succeed!!");
                askQueue.letItWait();
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean isNeedTurn() {
        if (numOfPeople > 0 || isPersonWantIn() || isRequestExistThisDirection()) {
            return false;
        }
        return true;
    }
    
    public void personsGoIn() {
        askQueue.personGoIn(persons, direction, nowFloor, building, id);
        numOfPeople = persons.getAsksUnsafe().size();
    }
    
    public boolean isPersonWantIn() {
        return askQueue.isPersonWantIn(nowFloor, direction);
    }
    
    public boolean isRequestExistThisDirection() {
        return askQueue.isRequestExitUpOrDown(nowFloor, direction);
    }
    
    public void move() {
        if (direction == Const.UP) {
            nowFloor++;
        } else {
            nowFloor--;
        }
        printElevatorInf(Const.MOVE, nowFloor, building, id);
    }
}
