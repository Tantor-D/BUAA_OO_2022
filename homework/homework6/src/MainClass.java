import com.oocourse.TimableOutput;

public class MainClass {
    
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        
    }
}
/*
ADD-building-6-A
2-FROM-A-5-TO-A-3
3-FROM-A-2-TO-A-9


ADD-floor-10-7
1-FROM-C-7-TO-A-7


1-FROM-A-8-TO-A-7
ADD-floor-7-2
2-FROM-B-2-TO-C-2
ADD-floor-8-6
3-FROM-D-6-TO-A-6
ADD-floor-9-1
4-FROM-B-1-TO-D-1
 */