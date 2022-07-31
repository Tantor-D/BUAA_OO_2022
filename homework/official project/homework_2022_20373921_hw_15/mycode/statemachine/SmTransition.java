package mycode.statemachine;

import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashSet;

public class SmTransition {
    private final UmlTransition umlTransition;
    private final SmState targetState;
    private final ArrayList<SmEvent> eventList;
    private final HashSet<String> eventNameSet; // 数据保证两两不同且不为空
    
    public SmTransition(UmlTransition umlTransition, SmState targetState) {
        this.umlTransition = umlTransition;
        this.targetState = targetState;
        eventList = new ArrayList<>();
        eventNameSet = new HashSet<>();
    }
    
    public void addEvent(SmEvent smEvent) {
        eventList.add(smEvent);
        eventNameSet.add(smEvent.getName());
    }
    
    public boolean isHasSameEventName(SmTransition otherTransition) {
        HashSet<String> otherNameSet = otherTransition.getEventNameSet();
        for (String name:otherNameSet) {
            if (eventNameSet.contains(name)) { // 有相同名字的event
                return true;
            }
        }
        return false;
    }
    
    /********************************************************************/
    // gets
    public HashSet<String> getEventNameSet() {
        return eventNameSet;
    }
    
    public SmState getTargetState() {
        return targetState;
    }
    
    @Override
    public String toString() {
        return "  YYY transition:  name: " + umlTransition.getName() + ", id: "
                + umlTransition.getId() + ", eventNameSet: " + eventNameSet;
    }
    
    public String getId() {
        return umlTransition.getId();
    }
    
    public String getGuard() {
        return umlTransition.getGuard();
    }
}
