import java.util.ArrayList;
import java.util.HashMap;

public class HorizontalElevatorsMap {
    private final HashMap<Integer, ArrayList<HorizontalElevator>> horizontalElevatorsMap;
    
    public HorizontalElevatorsMap() {
        this.horizontalElevatorsMap = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            horizontalElevatorsMap.put(i, new ArrayList<>());
        }
    }
    
    public synchronized void addHorizontalElevator(int floor, HorizontalElevator thisElevator) {
        horizontalElevatorsMap.get(floor).add(thisElevator);
    }
    
    public synchronized double getStress(
            int floor,
            int fromBuilding,
            int destinationBuilding,
            AskQueue horizontalProcessQueue) {
        ArrayList<HorizontalElevator> horizontalElevators = horizontalElevatorsMap.get(floor);
        for (HorizontalElevator thisElevator : horizontalElevators) {
            if (thisElevator.judgeCanOpen(fromBuilding, destinationBuilding)) { // 有一个电梯可以就可以，返回送人压力
                return (double) horizontalProcessQueue.getAskNum() / horizontalElevators.size();
            }
        }
        return -1;
    }
    
    public synchronized ArrayList<HorizontalElevator> getHorizontalElevators(int floor) {
        return horizontalElevatorsMap.get(floor);
    }
}
