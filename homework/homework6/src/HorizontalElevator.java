public class HorizontalElevator extends Elevator {
    
    private final int id;
    private final AskQueue persons;
    private final AskQueue askQueue;
    private int numOfPeople;
    private final int floor;
    private int direction;
    private int nowBuilding;
    
    public HorizontalElevator(AskQueue askQueue, int floor, int id) {
        this.id = id;
        this.askQueue = askQueue;
        this.floor = floor;
        
        persons = new AskQueue();
        numOfPeople = 0;
        direction = Const.RIGHT;
        nowBuilding = 0; // 默认在0座,A座
    }
    
    @Override
    public void run() {
        while (true) {
            boolean openFlag = false;
            if (askQueue.isEnd() && askQueue.isEmpty() && persons.isEmpty()) {
                //OutputHandler.printInf(id + " horizontal thread is end");
                return;
            }
            
            // 开门下人
            if (judgeNeedOut(persons.getAsksUnsafe(), nowBuilding)) {
                printElevatorInf(Const.OPEN, floor, nowBuilding, id);
                openFlag = true;
                personsGoOut(persons, nowBuilding, floor, id);
                numOfPeople = persons.getAsksUnsafe().size();
                sleepForUnitTime();
            }
            
            // 转向
            if (isNeedTurn()) {
                direction = opposeDirection(direction);
            }
            
            if (openFlag) {
                personsGoIn();
                printElevatorInf(Const.CLOSE, floor, nowBuilding, id);
            } else {
                if (isPersonWantIn() && numOfPeople < Const.MAXNUM) {
                    printElevatorInf(Const.OPEN, floor, nowBuilding, id);
                    personsGoIn();
                    sleepForUnitTime();
                    personsGoIn();
                    printElevatorInf(Const.CLOSE, floor, nowBuilding, id);
                }
            }
            
            if (numOfPeople > 0 || isAskExistThisDirection()) {
                sleepForUnitTime();
                move();
            } else if (askQueue.isEnd()) {
                //OutputHandler.printInf(id + " horizontal thread is end");
                return;
            } else {
                // OutputHandler.printInf(id + " wait succeed!!");
                askQueue.letItWait();
            }
            
        }
    }
    
    @Override
    public boolean isNeedTurn() {
        if (numOfPeople > 0 || isRequestExitThisDirectionWithSameDirection()) {
            // 以上的函数包括了当前所处位置也想上电梯的人
            return false;
        }
        // 此时，此地和前方必然不存在同向请求 且 电梯里没人
        if (isRequestExitOpposeDirectionWithOpposeDirection()) {
            return true;
        }
        // 此时，电梯没人， 当前位置没请求，转不转向都没有顺着的请求
        if (isAskExistThisDirection()) { // 前方有请求，那就不转向
            return false;
        }
        
        return true;
    }
    
    public void move() {
        if (direction == Const.LEFT) {
            nowBuilding = (nowBuilding + 4) % 5;
        } else {
            nowBuilding = (nowBuilding + 1) % 5;
        }
        printElevatorInf(Const.MOVE, floor, nowBuilding, id);
    }
    
    public void personsGoIn() {
        askQueue.personGoIn(persons, direction, nowBuilding, floor, id);
        numOfPeople = persons.getAsksUnsafe().size();
    }
    
    public boolean isPersonWantIn() {
        return askQueue.isPersonWantIn(nowBuilding, direction);
    }
    
    public boolean isAskExistThisDirection() {
        return askQueue.isRequestExitLeftOrRight(nowBuilding, direction);
    }
    
    public boolean isRequestExitThisDirectionWithSameDirection() { // 这个函数包括了对当前位置的判断
        return askQueue.isRequestExitLeftOrRightWithSameDirection(nowBuilding, direction);
    }
    
    public boolean isRequestExitOpposeDirectionWithOpposeDirection() { // 这个函数包括了对当前位置的判断
        return askQueue.isRequestExitLeftOrRightWithSameDirection(
                nowBuilding, opposeDirection(direction));
    }
}
