package mycode.statemachine;

import com.oocourse.uml3.models.elements.UmlRegion;
import mycode.EnumsStateType;
import mycode.ToolStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SmRegion {
    private final UmlRegion umlRegion;
    private int numOfState;
    private int numOfFinalState;
    private SmState initialState;
    private boolean canReachFinalStateWithNoChange;
    private final ArrayList<SmState> finalStateList;
    private final HashMap<String, String> name2StateId;
    private final HashMap<String, SmState> id2State;
    
    public SmRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
        numOfFinalState = 0;
        numOfState = 0;
        canReachFinalStateWithNoChange = false;
        initialState = null;
        finalStateList = new ArrayList<>();
        name2StateId = new HashMap<>();
        id2State = new HashMap<>();
    }
    
    public void addState(SmState smState) {
        numOfState++;
        if (name2StateId.containsKey(smState.getName())) {
            name2StateId.put(smState.getName(), ToolStatic.getDuplicated());
        } else {
            name2StateId.put(smState.getName(), smState.getId());
        }
        id2State.put(smState.getId(), smState);
        if (smState.getStateType() == EnumsStateType.finalState) {
            numOfFinalState++;
            finalStateList.add(smState);
        } else if (smState.getStateType() == EnumsStateType.initialState) {
            initialState = smState; // 输入保证只有一个起始状态
        }
    }
    
    public boolean isStateExist(String stateName) {
        // System.out.println("to search this name:" + stateName);
        // System.out.println("name2StateId's keys:" + name2StateId.keySet());
        return name2StateId.containsKey(stateName);
    }
    
    public boolean isStateDuplicate(String stateName) {
        if (!name2StateId.containsKey(stateName)) {
            return false;
        }
        return name2StateId.get(stateName).equals(ToolStatic.getDuplicated());
    }
    
    public List<String> getTransitionTrigger(String sourceStateName, String targetStateName) {
        SmState sourceState = id2State.get(name2StateId.get(sourceStateName));
        SmState targetState = id2State.get(name2StateId.get(targetStateName));
        if (!sourceState.judgeReachStateDirectly(targetState)) {
            return null; // 返回mull表示不可以直接到达
        } else {
            return sourceState.getTransitionTrigger(targetState);
        }
    }
    
    public void initialCanReachFinalState() { // 初始条件下能否到终止状态
        canReachFinalStateWithNoChange = initialState.judgeCanReachFinalState(new HashSet<>());
    }
    
    public boolean getStateIsCriticalPoint(String stateName) {
        // 先看看不对图做改变能不能跑到终止状态
        if (!canReachFinalStateWithNoChange) {
            return false;
        }
        
        // 特判起始状态和终止状态
        SmState thisState = id2State.get(name2StateId.get(stateName));
        if (initialState.equals(thisState)) {
            return false;
        }
        for (SmState smState : finalStateList) {
            if (smState.equals(thisState)) {
                return false;
            }
        }
        
        // 一般情况
        HashSet<SmState> alreadyReachedStateSet = new HashSet<>();
        alreadyReachedStateSet.add(initialState);
        alreadyReachedStateSet.add(thisState);
        return !initialState.judgeCanReachFinalState(alreadyReachedStateSet);
    }
    
    ////////////////////////////////////////////////////
    // gets
    
    public int getNumOfState() {
        return numOfState;
    }
    
}
