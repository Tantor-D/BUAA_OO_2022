import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    private final AskQueue waitQueue;
    private final HorizontalElevatorsMap horizontalElevatorsMap;
    private final ArrayList<AskQueue> verticalProcessQueues;
    private final ArrayList<AskQueue> horizontalProcessQueues;
    
    public InputHandler(AskQueue waitQueue,
                        HorizontalElevatorsMap horizontalElevatorsMap,
                        ArrayList<AskQueue> verticalProcessQueues,
                        ArrayList<AskQueue> horizontalProcessQueues) {
        this.waitQueue = waitQueue;
        this.horizontalElevatorsMap = horizontalElevatorsMap;
        this.verticalProcessQueues = verticalProcessQueues;
        this.horizontalProcessQueues = horizontalProcessQueues;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                waitQueue.setEnd(true);
                //OutputHandler.printInf("InputHandler is dead");
                break;
            }
            
            // 加请求
            if (request instanceof PersonRequest) {
                Scheduler.addTotalAskNum(); // 总请求数增加
                PersonRequest person = (PersonRequest) request;
                Ask newAsk = new Ask(person.getPersonId(), person.getFromBuilding() - 'A',
                        person.getFromFloor(), person.getToBuilding() - 'A', person.getToFloor());
                waitQueue.addAskToWaitQueue(newAsk);
            } else if (request instanceof ElevatorRequest) {
                ElevatorRequest elevator = (ElevatorRequest) request;
                
                // 对电梯的操作：新建，加到表中，start
                if (elevator.getType().equals("building")) { // 纵向电梯
                    VerticalElevator thisElevator = new VerticalElevator(
                            elevator.getElevatorId(),
                            verticalProcessQueues.get(elevator.getBuilding() - 'A'),
                            elevator.getBuilding() - 'A',
                            elevator.getCapacity(),
                            (int) (elevator.getSpeed() * 1000));
                    thisElevator.start();
                } else { // 横向电梯
                    HorizontalElevator thisElevator = new HorizontalElevator(
                            elevator.getElevatorId(),
                            horizontalProcessQueues.get(elevator.getFloor()),
                            elevator.getFloor(),
                            elevator.getCapacity(),
                            (int) (elevator.getSpeed() * 1000),
                            elevator.getSwitchInfo());
                    horizontalElevatorsMap.addHorizontalElevator(elevator.getFloor(), thisElevator);
                    thisElevator.start();
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
