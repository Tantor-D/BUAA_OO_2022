import java.util.BitSet;

public class Request {
    private final String requestCode;
    private final BitSet requestStages;
    private final BitSet finishedStages;

    public Request(String requestCode, String[] workingStages, int pipelineLength) { //将多个工序按层次封装起来
        this.requestCode = requestCode;
        finishedStages = new BitSet(WorkingStage.STAGE_NUM);
        requestStages = new BitSet(WorkingStage.STAGE_NUM);
        for (int i = 0; i < pipelineLength; ++i) {
            requestStages.or(WorkingStage.valueOf(workingStages[i]).getMask());
        }
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void finishStage(WorkingStage workingStage) {
        finishedStages.or(workingStage.getMask());
    }

    public BitSet getUnfinishedStages() {
        BitSet unfinishedStages = (BitSet) requestStages.clone();
        unfinishedStages.xor(finishedStages);
        return  unfinishedStages;
    }

    public boolean allStagesFinished() {
        return getUnfinishedStages().isEmpty();
    }
}
