import java.util.ArrayList;

public class Scheduler extends Thread {
    private final AskQueue waitQueue;
    
    private final HorizontalElevatorsMap horizontalElevatorsMap;
    private final ArrayList<AskQueue> verticalProcessQueues;
    private final ArrayList<AskQueue> horizontalProcessQueues;
    private static int finishedNum;
    private static int totalAskNum;
    private static AskQueue staticWaitQueue;
    //public static HorizontalElevatorsMap staticHorizontalElevatorsMap;
    
    /*public static synchronized int getFinishedNum() {
        return finishedNum;
    }*/
    
    /*public static synchronized int getTotalAskNum() {
        return totalAskNum;
    }*/
    
    public Scheduler(
            AskQueue waitQueue,
            HorizontalElevatorsMap horizontalElevatorsMap,
            ArrayList<AskQueue> verticalProcessQueues,
            ArrayList<AskQueue> horizontalProcessQueues) {
        this.waitQueue = waitQueue;
        this.horizontalElevatorsMap = horizontalElevatorsMap;
        this.verticalProcessQueues = verticalProcessQueues;
        this.horizontalProcessQueues = horizontalProcessQueues;
        Scheduler.staticWaitQueue = waitQueue;
        //Scheduler.staticHorizontalElevatorsMap = horizontalElevatorsMap;
        Scheduler.finishedNum = 0;
        Scheduler.totalAskNum = 0;
    }
    
    public static synchronized void addFinishedAskNum() {
        finishedNum++;
    }
    
    public static synchronized void addTotalAskNum() {
        totalAskNum++;
    }
    
    public static AskQueue getStaticWaitQueue() {
        return Scheduler.staticWaitQueue;
    }
    
    public double getStress(int floor, int fromBuilding, int destinationBuilding) {
        return horizontalElevatorsMap.getStress(floor, fromBuilding,
                destinationBuilding, horizontalProcessQueues.get(floor));
    }
    
    public static synchronized void tryToEndScheduler() {
        if (staticWaitQueue.isEnd() && finishedNum == totalAskNum) {
            staticWaitQueue.addAskToWaitQueue(new Ask(0, 0, -1, 0, 0));
        }
    }
    
    /*public static synchronized void printForDebug(int nowFinishNum) {
        for (int floor = 1; floor <= 10; floor++) {
            ArrayList<HorizontalElevator> horizontalElevators =
            staticHorizontalElevatorsMap.getHorizontalElevators(floor);
            for (HorizontalElevator ele : horizontalElevators) {
                ele.printForDebug(nowFinishNum);
            }
        }
    }*/
    
    @Override
    public void run() {
        while (true) { // todo 记得在最后一个任务完成后新加一个非法的请求来唤醒此线程,同时不加请求总数
            //OutputHandler.printInf("   Scheduler is alive");
            if (waitQueue.isEmpty()) {
                if (waitQueue.isEnd() && finishedNum == totalAskNum) { // 当前分配完了，输入完了，所有的请求处理完了
                    for (AskQueue askQueue : horizontalProcessQueues) {
                        askQueue.setEnd(true);
                    }
                    for (AskQueue askQueue : verticalProcessQueues) {
                        askQueue.setEnd(true);
                    }
                    //OutputHandler.printInf("   Scheduler Thread is dead");
                    return;
                } else {
                    //OutputHandler.printInf("   Scheduler wait successfully");
                    waitQueue.letItWait();
                }
            } else { // 保证取得出东西，开始处理
                Ask thisAsk = waitQueue.getAskFromWaitQueue();
                int nowFloor = thisAsk.getNowFloor();
                int nowBuilding = thisAsk.getNowBuilding();
                int destinationFloor = thisAsk.getDestinationFloor();
                int destinationBuilding = thisAsk.getDestinationBuilding();
                if (nowFloor == -1) {
                    continue; // 非法的那个请求，实际上多余，因为非法请求必然是最后一个，且或返回empty
                }
                
                synchronized (horizontalElevatorsMap) { // 分配某个请求时，不允许添加电梯，电梯情况保持不变
                    // 无换乘
                    if (nowBuilding == destinationBuilding) { // 竖向一次
                        allocPerson(Const.VERTICAL, nowFloor,
                                destinationFloor, nowBuilding, thisAsk);
                        //OutputHandler.printInf("   alloc " +
                        // thisAsk.getId() + " , 0 trans , vertical");
                        continue;
                    }
                    if (nowFloor == destinationFloor &&
                            getStress(nowFloor, nowBuilding, destinationBuilding) >= 0) { // 横向一次
                        allocPerson(Const.HORIZONTAL, nowBuilding,
                                destinationBuilding, nowFloor, thisAsk);
                        //OutputHandler.printInf("   alloc " +
                        // thisAsk.getId() + " , 0 trans , horizontal");
                        continue;
                    }
                    
                    // 一次换乘
                    int transferFloor = calTransFloorWithOneTrans(nowFloor,
                            nowBuilding, destinationFloor, destinationBuilding);
                    if (transferFloor != -1) {
                        if (transferFloor == nowFloor) { // 横向走
                            //OutputHandler.printInf("   alloc " + thisAsk.getId() + " ,
                            // 1 trans, horizontal, transFloor:" + transferFloor);
                            allocPerson(Const.HORIZONTAL, nowBuilding,
                                    destinationBuilding, nowFloor, thisAsk);
                        } else {
                            allocPerson(Const.VERTICAL, nowFloor,
                                    transferFloor, nowBuilding, thisAsk);
                            //OutputHandler.printInf("   alloc " + thisAsk.getId() + " ,
                            // 1 trans, vertical, transFloor:" + transferFloor);
                        }
                        continue;
                    }
                    
                    // 两次换乘
                    transferFloor = calTransFloorWithTwoTrans(nowFloor,
                            nowBuilding, destinationFloor, destinationBuilding);
                    allocPerson(Const.VERTICAL, nowFloor, transferFloor, nowBuilding, thisAsk);
                    //OutputHandler.printInf("   alloc " + thisAsk.getId() + " ,
                    // 2 trans , " + "transferFloor : " + transferFloor);
                }
            }
            
            
        }
    }
    
    public void allocPerson(int kind, int from, int destination, int belongs, Ask thisAsk) {
        if (kind == Const.VERTICAL) {
            thisAsk.renewDirectionAndDistance(Const.VERTICAL, from, destination);
            verticalProcessQueues.get(belongs).addAskToProcessQueue(thisAsk);
        } else {
            thisAsk.renewDirectionAndDistance(Const.HORIZONTAL, from, destination);
            horizontalProcessQueues.get(belongs).addAskToProcessQueue(thisAsk);
        }
    }
    
    public int calTransFloorWithOneTrans(
            int nowFloor,
            int nowBuilding,
            int destinationFloor,
            int destinationBuilding) {
        double stressNowFloor = getStress(nowFloor, nowBuilding, destinationBuilding);
        double stressDestinationFloor = getStress(
                destinationFloor, nowBuilding, destinationBuilding);
        int transferFloor = -1;
        if (stressNowFloor >= 0 || stressDestinationFloor >= 0) {
            if (stressNowFloor >= 0 && stressDestinationFloor >= 0) {
                if (stressNowFloor < stressDestinationFloor) {
                    transferFloor = nowFloor;
                } else {
                    transferFloor = destinationFloor;
                }
            } else if (stressNowFloor >= 0) {
                transferFloor = nowFloor;
            } else {
                transferFloor = destinationFloor;
            }
        }
        return transferFloor;
    }
    
    public int calTransFloorWithTwoTrans(
            int nowFloor,
            int nowBuilding,
            int destinationFloor,
            int destinationBuilding) {
        
        int lowFloor = (nowFloor < destinationBuilding) ? nowFloor : destinationFloor;
        int highFloor = (nowFloor > destinationBuilding) ? nowFloor : destinationFloor;
        int transferFloor = 1;
        double minStress = 1000000;
        
        // 两楼层之间找换乘
        for (int floor = lowFloor + 1; floor < highFloor; floor++) {
            double nowStress = getStress(floor, nowBuilding, destinationBuilding);
            if (nowStress >= 0 && nowStress < minStress) {
                minStress = nowStress;
                transferFloor = floor;
            }
        }
        if (minStress != 1000000) {
            return transferFloor;
        }
        
        // 往外扩着找
        int flag;
        for (int i = 1; i <= 8; i++) {
            flag = 0; //没找到
            int floor = lowFloor - i;
            if (floor >= 1) {
                double nowStress = getStress(floor, nowBuilding, destinationBuilding);
                if (nowStress >= 0 && nowStress < minStress) {
                    flag = 1;
                    minStress = nowStress;
                    transferFloor = floor;
                }
            }
            floor = highFloor + 1;
            if (floor <= 10) {
                double nowStress = getStress(floor, nowBuilding, destinationBuilding);
                if (nowStress >= 0 && nowStress < minStress) {
                    flag = 1;
                    minStress = nowStress;
                    transferFloor = floor;
                }
            }
            
            if (flag == 1) {
                return transferFloor;
            }
        }
        
        return 1;
    }
    
}
