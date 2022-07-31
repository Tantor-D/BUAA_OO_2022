public class Ask {
    private final int id;
    private final int from;
    private final int destination;
    private final int distance;
    private final int kind;
    private final int direction;
    
    public int getId() {
        return id;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public int getFrom() {
        return from;
    }
    
    public int getDestination() {
        return destination;
    }
    
    public int getKind() {
        return kind;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public Ask(int id, int from, int destination, int kind) {
        this.id = id;
        this.from = from;
        this.destination = destination;
        this.kind = kind;
        
        if (kind == Const.VERTICAL) {
            if (destination > from) {
                direction = Const.UP;
                distance = destination - from;
            } else {
                direction = Const.DOWN;
                distance = from - destination;
            }
            
        } else { // 此为水平方向
            if ((destination - from + 5) % 5 <= 2) {
                direction = Const.RIGHT;
                distance = (destination - from + 5) % 5;
            } else {
                direction = Const.LEFT;
                distance = (from - destination + 5) % 5;
            }
        }
    }
    
    public void printPersonInf(int op, int building, int floor, int elevatorId) { // todo 试试双引号改单引号
        if (op == Const.IN) {
            String outMsg = "IN-" + this.id + "-" + (char)(building + 'A') +
                    "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.OUT) {
            String outMsg = "OUT-" + this.id + "-" + (char)(building + 'A') +
                    "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
    }
}
