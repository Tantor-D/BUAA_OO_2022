import java.util.Iterator;

// 抽象类中所包含的方法仅限于电梯本身的一些公共行为，例如开关门、输出、至于控制手段，全部放到相应电梯的run方法中
public abstract class Elevator extends Thread {
    public void sleepForSomeTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    // 此函数仅仅负责输出，能不能开门不由其判断
    public void personsGoOut(AskQueue persons, int nowPlace, int belong, int elevatorId, int kind) {
        Iterator<Ask> it = persons.getAsksUnsafe().iterator();
        while (it.hasNext()) {
            Ask thisPerson = it.next();
            if (thisPerson.getNowDestination() == nowPlace) {
                if (kind == Const.VERTICAL) {
                    thisPerson.printPersonInf(Const.OUT, belong, nowPlace, elevatorId);
                    thisPerson.renewNowPlace(belong, nowPlace);
                } else {
                    thisPerson.printPersonInf(Const.OUT, nowPlace, belong, elevatorId);
                    thisPerson.renewNowPlace(nowPlace, belong);
                }
                if (thisPerson.isReachDestination()) { // 人到终点了就尝试杀掉调度器线程
                    Scheduler.addFinishedAskNum();
                    //Scheduler.printForDebug(Scheduler.getFinishedNum());
                    //OutputHandler.printInf("  finishNum is " + Scheduler.getFinishedNum());
                    //OutputHandler.printInf("xxxxxxxxx    finishedNum
                    // : " + Scheduler.getFinishedNum() + " ,
                    // totalNum: " + Scheduler.getTotalAskNum());
                    Scheduler.tryToEndScheduler();
                } else { // 没到终点
                    Scheduler.getStaticWaitQueue().addAskToWaitQueue(thisPerson); // 没到终点就回去
                }
                it.remove();
            }
        }
        
    }
    
    public int opposeDirection(int direction) {
        if (direction == Const.UP) {
            return Const.DOWN;
        }
        if (direction == Const.DOWN) {
            return Const.UP;
        }
        if (direction == Const.LEFT) {
            return Const.RIGHT;
        }
        if (direction == Const.RIGHT) {
            return Const.LEFT;
        }
        return 0;
    }
    
    public void printElevatorInf(int op, int floor, int building, int elevatorId) {
        if (op == Const.OPEN) {
            String outMsg = "OPEN-" + (char) (building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.CLOSE) {
            String outMsg = "CLOSE-" + (char) (building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
        if (op == Const.MOVE) {
            String outMsg = "ARRIVE-" + (char) (building + 'A') + "-" + floor + "-" + elevatorId;
            OutputHandler.printInf(outMsg);
        }
    }
    
}
