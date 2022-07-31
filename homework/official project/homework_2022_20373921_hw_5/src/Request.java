public class Request {
    
    private final int id;
    private final int from;
    private final int destination;
    private final char building;
    private final int direction;
    
    public Request(int id, int from, int destination, char building) {
        this.id = id;
        this.from = from;
        this.destination = destination;
        this.building = building;
        if (destination > from) {
            direction = 1; //UP为1, down为0
        } else {
            direction = 0;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public int getFrom() {
        return from;
    }
    
    public int getDestination() {
        return destination;
    }
    
    public char getBuilding() {
        return building;
    }
    
    public int getDirection() {
        return direction;
    }
}
