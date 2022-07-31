package mycode.statemachine;

import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.List;

public class SmStateMachine {
    private final UmlStateMachine umlStateMachine;
    private SmRegion smRegion;
    
    public SmStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }
    
    public void addRegion(SmRegion smRegion) {
        this.smRegion = smRegion;
    }
    
    public boolean isStateExist(String stateName) {
        return smRegion.isStateExist(stateName);
    }
    
    public boolean isStateDuplicate(String stateName) {
        return smRegion.isStateDuplicate(stateName);
    }
    
    public List<String> getTransitionTrigger(String sourceStateName, String targetStateName) {
        return smRegion.getTransitionTrigger(sourceStateName, targetStateName);
    }
    
    public boolean getStateIsCriticalPoint(String stateName) {
        return smRegion.getStateIsCriticalPoint(stateName);
    }
    
    public void initialCanReachFinalState() {
        smRegion.initialCanReachFinalState();
    }
    
    /////////////////////////////////////////////////////////
    // gets
    public int getNumOfState() {
        return smRegion.getNumOfState();
    }
}
