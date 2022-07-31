import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;
import java.util.ArrayList;

public class InputHandler extends Thread {
    
    private final ArrayList<RequestQueue> requestQueues;
    
    public InputHandler(ArrayList<RequestQueue> requestQueues) {
        this.requestQueues = requestQueues;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest inputRequst = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (inputRequst == null) {
                for (RequestQueue requestQueue : requestQueues) {
                    requestQueue.setEnd(true);
                }
                //System.out.println("Input end now"); // todo 最后删掉这一个
                break; // 输入结束，由后面的close来结束线程
            }
            Request newRequest = new Request(inputRequst.getPersonId(), inputRequst.getFromFloor(),
                    inputRequst.getToFloor(), inputRequst.getFromBuilding());
            //OutputHandler.printInf("ready to add a request");
            requestQueues.get((int) (newRequest.getBuilding() - 'A')).addRequest(newRequest);
            
        }
        
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
