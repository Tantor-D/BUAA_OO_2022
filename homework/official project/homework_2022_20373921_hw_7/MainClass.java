import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        
        // 请求队列
        ArrayList<AskQueue> verticalAskQueues = new ArrayList<>();
        ArrayList<AskQueue> horizontalAskQueues = new ArrayList<>();
        // 纵向请求队列
        for (int i = 0; i < 5; i++) {
            verticalAskQueues.add(new AskQueue());
        }
        // 横向
        for (int i = 0; i <= 10; i++) {
            horizontalAskQueues.add(new AskQueue());
            // 横向电梯的请求队列多加一个，要有11个，以直接get(i)得i楼的请求队列
        }
        
        // 加电梯
        HorizontalElevatorsMap horizontalElevatorsMap = new HorizontalElevatorsMap();
        for (int i = 0; i < 5; i++) {
            VerticalElevator verticalElevator = new VerticalElevator(
                    i + 1, verticalAskQueues.get(i), i, 8, 600);
            verticalElevator.start();
        }
        HorizontalElevator horizontalElevator = new HorizontalElevator(
                6, horizontalAskQueues.get(1), 1, 8, 600, 31);
        horizontalElevatorsMap.addHorizontalElevator(1, horizontalElevator);
        horizontalElevator.start();
        
        // 输入类 和 调度器
        AskQueue waitQueue = new AskQueue();
        InputHandler inputHandler = new InputHandler(waitQueue,
                horizontalElevatorsMap, verticalAskQueues, horizontalAskQueues);
        Scheduler scheduler = new Scheduler(waitQueue,
                horizontalElevatorsMap, verticalAskQueues, horizontalAskQueues);
        inputHandler.start();
        scheduler.start();
    }
}
/*
1-FROM-B-1-TO-B-2
2-FROM-B-1-TO-B-2

ADD-floor-7-3-6-0.4-5
1-FROM-A-4-TO-C-3
*/
