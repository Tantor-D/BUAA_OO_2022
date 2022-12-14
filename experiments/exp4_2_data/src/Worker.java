public class Worker implements Runnable {
    private final WorkingList workingList;
    private final int workerId;
    private final WorkingStage workingStage;

    Worker(int workerId, WorkingList workingList, WorkingStage workingStage) {
        this.workingList = workingList;
        this.workerId = workerId;
        this.workingStage = workingStage;
    }

    private void solveRequest(Request request) { //处理一个任务，并在任务完成后
        Printer.println("REQUEST: " + request.getRequestCode()
                + ", SOLVE STAGE: " + workingStage + ", WorkerId: " + workerId);
        try {
            Thread.sleep(workingStage.getWorkingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Printer.println("REQUEST: " + request.getRequestCode()
                + ", FINISH STAGE: " + workingStage + ", WorkerId: " + workerId);
        request.finishStage(workingStage);
        if (request.allStagesFinished()) { //当前任务完成，输出信息
            Printer.println("REQUEST FINISH: " + request.getRequestCode());
            RequestCounter.getInstance().release();

        } else { //当前任务没有结束，需要传递给下一个人
            Controller.getInstance().addRequest(request);
        }
    }

    @Override
    public void run() {
        while (true) {
            Request request = workingList.getRequest();
            if (request == null) {
                break;
            }
            solveRequest(request);
        }
    }
}
