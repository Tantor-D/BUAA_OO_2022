package mycode.statemachine;

import com.oocourse.uml2.models.elements.UmlElement;
import mycode.EnumsStateType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SmState {
    private final UmlElement umlElement; // 3种state只有这一公共祖先
    private final EnumsStateType stateType;
    private final ArrayList<SmTransition> transitionsList;
    private final ArrayList<SmState> nextStateList;
    
    public SmState(UmlElement umlElement, EnumsStateType stateType) {
        this.umlElement = umlElement;
        this.stateType = stateType;
        transitionsList = new ArrayList<>();
        nextStateList = new ArrayList<>();
    }
    
    public void addTransitionAndNextState(SmTransition smTransition) {
        transitionsList.add(smTransition);
        nextStateList.add(smTransition.getTargetState());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SmState)) {
            return false;
        }
        return getId().equals(((SmState) obj).getId());
    }
    
    @Override
    public int hashCode() { // 使用官方包中类的hashCode，因此
        return umlElement.hashCode(); // 会自行调用实际类型的hashcode，不用类型转化
    }
    
    public boolean judgeReachStateDirectly(SmState nextState) {
        //System.out.println("search the state" + nextState);
        //System.out.println(nextStateList);
        if (nextStateList.contains(nextState)) {
            
            return true;
        }
        /*for (SmState smState : nextStateList) {
            if (smState.equals(nextState)) {
                return true;
            }
        }*/
        //System.out.println("xxxxxxxxxxxxxxxxxx  return false");
        return false;
    }
    
    public List<String> getTransitionTrigger(SmState nextState) {
        ArrayList<String> retEventNameList = new ArrayList<>();
        /*if (this.getName().equals("State3745") && nextState.getName().equals("State782")) {
            for (int i = 0; i < nextStateList.size(); i++) {
                SmState smState = nextStateList.get(i);
                if (smState.equals(nextState)) {
                    System.out.println("TransitionId: " + transitionsList.get(i).getId());
                    System.out.println("xxxxxxxxxx   " + transitionsList.get(i).getEventNameSet());
                }
            }
        }*/
        for (int i = 0; i < nextStateList.size(); i++) {
            SmState smState = nextStateList.get(i);
            if (smState.equals(nextState)) {
                retEventNameList.addAll(transitionsList.get(i).getEventNameSet());
            }
        }
        
        return retEventNameList;
    }
    
    public boolean judgeCanReachFinalState(HashSet<SmState> alreadyReachedStateSet) {
        for (SmState nextState : nextStateList) {
            if (nextState.getStateType() == EnumsStateType.finalState) {
                return true; // 到终点了
            }
            if (alreadyReachedStateSet.contains(nextState)) {
                continue;
            }
            alreadyReachedStateSet.add(nextState);
            if (nextState.judgeCanReachFinalState(alreadyReachedStateSet)) {
                return true;
            }
        }
        return false;
    }
    
    //////////////////////////////////////////////////////
    // gets
    public EnumsStateType getStateType() {
        return stateType;
    }
    
    public String getId() {
        return umlElement.getId();
    }
    
    public String getName() {
        return umlElement.getName();
    }
    
    @Override
    public String toString() {
        return "name: " + getName() + ", id: " + getId();
    }
}
