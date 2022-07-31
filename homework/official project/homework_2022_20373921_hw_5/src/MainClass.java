import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        
        ArrayList<RequestQueue> processQueues = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) { // 新建processQueue并绑到电梯上，同时电梯线程开始执行
            RequestQueue requestQueue = new RequestQueue();
            processQueues.add(requestQueue);
            Elevator elevator = new Elevator(requestQueue, (char) (i + 'A'));
            elevator.start();
        }
        
        //这个inputHandler要放在最后初始化出来
        InputHandler inputHandler = new InputHandler(processQueues);
        inputHandler.start();
        
    }
}
