public class Ask {
    private final int id;
    private final int fromBuilding;
    private final int fromFloor;
    private final int destinationBuilding;
    private final int destinationFloor;
    
    private int nowFloor;
    private int nowBuilding;
    private int nowDistance;
    private int nowDirection;
    private int nowDestination;
    
    public Ask(int id, int fromBuilding, int fromFloor,
               int destinationBuilding, int destinationFloor) {
        this.id = id;
        this.fromBuilding = fromBuilding;
        this.fromFloor = fromFloor;
        this.destinationBuilding = destinationBuilding;
        this.destinationFloor = destinationFloor;
        nowBuilding = fromBuilding;
        nowFloor = fromFloor;
    }
    
    public void renewNowPlace(int nowBuilding, int nowFloor) {
        this.nowFloor = nowFloor;
        this.nowBuilding = nowBuilding;
    }
    
    public void renewDirectionAndDistance(int kind, int from, int destination) {
        nowDestination = destination;
        if (kind == Const.HORIZONTAL) {
            if ((destination - from + 5) % 5 <= 2) {
                nowDirection = Const.RIGHT;
                nowDistance = (destination - from + 5) % 5;
            } else {
                nowDirection = Const.LEFT;
                nowDistance = (from - destination + 5) % 5;
            }
        } else { // 纵向
            if (destination > from) {
                nowDirection = Const.UP;
                nowDistance = destination - from;
            } else {
                nowDirection = Const.DOWN;
                nowDistance = from - destination;
            }
        }
    }
    
    public boolean isReachDestination() {
        return (nowFloor == destinationFloor) && (nowBuilding == destinationBuilding);
    }
    
    public void printPersonInf(int op, int building, int floor, int elevatorId) {
        if (op == Const.IN) {
            String outMsg = "IN-" + this.id + "-" + (char) (building + 'A') +
                    "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.OUT) {
            String outMsg = "OUT-" + this.id + "-" + (char) (building + 'A') +
                    "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
    }
    
    /////////////////////////////////////////////////////////////////
    // get \ set
    public int getId() {
        return id;
    }
    
    public int getFromBuilding() {
        return fromBuilding;
    }
    
    public int getFromFloor() {
        return fromFloor;
    }
    
    public int getDestinationBuilding() {
        return destinationBuilding;
    }
    
    public int getDestinationFloor() {
        return destinationFloor;
    }
    
    public int getNowFloor() {
        return nowFloor;
    }
    
    public int getNowBuilding() {
        return nowBuilding;
    }
    
    public int getNowDistance() {
        return nowDistance;
    }
    
    public int getNowDirection() {
        return nowDirection;
    }
    
    public void setNowFloor(int nowFloor) {
        this.nowFloor = nowFloor;
    }
    
    public void setNowBuilding(int nowBuilding) {
        this.nowBuilding = nowBuilding;
    }
    
    public void setNowDistance(int nowDistance) {
        this.nowDistance = nowDistance;
    }
    
    public void setNowDirection(int nowDirection) {
        this.nowDirection = nowDirection;
    }
    
    public int getNowDestination() {
        return nowDestination;
    }
    
}
