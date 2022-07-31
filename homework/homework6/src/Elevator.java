import java.util.ArrayList;
import java.util.Iterator;

public abstract class Elevator extends Thread {
    
    public Elevator() {}
    
    public abstract boolean isNeedTurn();
    
    public void sleepForUnitTime() {
        try {
            Thread.sleep(Const.SLEEP_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void personsGoOut(AskQueue persons, int nowPlace, int buildingOrFloor,int elevatorId) {
        Iterator<Ask> it = persons.getAsksUnsafe().iterator();
        while (it.hasNext()) {
            Ask thisPerson = it.next();
            if (thisPerson.getDestination() == nowPlace) {
                if (thisPerson.getDirection() == Const.UP ||
                        thisPerson.getDirection() == Const.DOWN) {
                    thisPerson.printPersonInf(Const.OUT, buildingOrFloor, nowPlace, elevatorId);
                } else {
                    thisPerson.printPersonInf(Const.OUT, nowPlace, buildingOrFloor, elevatorId);
                }
                it.remove();
            }
        }
    }
    
    public int opposeDirection(int direction) {
        if (direction == Const.UP) {
            return Const.DOWN;
        }
        if (direction == Const.DOWN) {
            return Const.UP;
        }
        if (direction == Const.LEFT) {
            return Const.RIGHT;
        }
        if (direction == Const.RIGHT) {
            return Const.LEFT;
        }
        return 0;
    }
    
    public boolean judgeNeedOut(ArrayList<Ask> persons, int nowPlace) {
        for (Ask person : persons) {
            if (person.getDestination() == nowPlace) {
                return true;
            }
        }
        return false;
    }
    
    public void printElevatorInf(int op, int floor, int building, int elevatorId) {
        if (op == Const.OPEN) {
            String outMsg = "OPEN-" + (char)(building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.CLOSE) {
            String outMsg = "CLOSE-" + (char)(building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.MOVE) {
            String outMsg = "ARRIVE-" + (char)(building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
    }
}
