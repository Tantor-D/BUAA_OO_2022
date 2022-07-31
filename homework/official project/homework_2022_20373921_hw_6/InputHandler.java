import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    
    private final ArrayList<AskQueue> verticalQueues;
    private final ArrayList<AskQueue> horizontalQueues;
    
    public InputHandler() {
        verticalQueues = new ArrayList<>();
        horizontalQueues = new ArrayList<>();
    }
    
    public void initial() {
        for (int i = 0; i < 5; i++) {
            verticalQueues.add(new AskQueue());
        }
        for (int i = 0; i <= 10; i++) { // 第一个列表为废的，表示第0层
            horizontalQueues.add(new AskQueue());
        }
        
        for (int i = 0; i < 5; i++) {
            ElevatorVertical elevatorVertical =
                    new ElevatorVertical(verticalQueues.get(i), i, i + 1);
            elevatorVertical.start();
        }
    }
    
    public void setQueuesEnd() {
        for (AskQueue askQueue : verticalQueues) {
            askQueue.setEnd(true);
        }
        for (AskQueue askQueue : horizontalQueues) {
            askQueue.setEnd(true);
        }
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        
        initial();
        
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                setQueuesEnd();
                break;
            }
            // 到这必然不是空，已经拿到了请求
            if (request instanceof PersonRequest) {
                PersonRequest person = (PersonRequest) request;
                if (person.getFromFloor() == person.getToFloor()) { // 水平移动
                    Ask newAsk = new Ask(person.getPersonId(), (person.getFromBuilding() - 'A'),
                            (person.getToBuilding() - 'A'), Const.HORIZONTAL);
                    horizontalQueues.get(person.getFromFloor()).addAsk(newAsk);
                    //OutputHandler.printInf("add a request successfully");
                } else { //竖直移动
                    Ask newAsk = new Ask(person.getPersonId(), person.getFromFloor(),
                            person.getToFloor(), Const.VERTICAL);
                    verticalQueues.get(person.getFromBuilding() - 'A').addAsk(newAsk);
                    //OutputHandler.printInf("add a request successfully");
                }
            } else if (request instanceof ElevatorRequest) {
                ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                if (elevatorRequest.getType().equals("building")) { // 纵向电梯
                    int buildingNum = elevatorRequest.getBuilding() - 'A';
                    ElevatorVertical elevatorVertical =
                            new ElevatorVertical(verticalQueues.get(buildingNum),
                                    buildingNum,
                                    elevatorRequest.getElevatorId());
                    elevatorVertical.start();
                } else { // 横向电梯
                    int floorNum = elevatorRequest.getFloor();
                    ElevatorHorizontal elevatorHorizontal =
                            new ElevatorHorizontal(horizontalQueues.get(floorNum),
                                    floorNum,
                                    elevatorRequest.getElevatorId());
                    elevatorHorizontal.start();
                }
            }
        }
        try {
            elevatorInput.close();
            //OutputHandler.printInf("Input Thread end now");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
