package mycode.statemachine;

import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashSet;

public class SmTransition {
    private final UmlTransition umlTransition;
    private final SmState targetState;
    private final ArrayList<SmEvent> eventList;
    private final HashSet<String> eventNameSet;
    
    public SmTransition(UmlTransition umlTransition, SmState targetState) {
        this.umlTransition = umlTransition;
        this.targetState = targetState;
        eventList = new ArrayList<>();
        eventNameSet = new HashSet<>();
    }
    
    public void addEvent(SmEvent smEvent) {
        eventList.add(smEvent);
        eventNameSet.add(smEvent.getName());
        /*if (smEvent.getName().equals("Event2") && umlTransition.getId().equals("T6770")) {
            System.out.println("eventName:" + smEvent.getName() + ", eventId: " + smEvent.getId());
            System.out.println("transitionName:" + umlTransition.getName() +
                    ", TransitionId: " + umlTransition.getId());
            System.out.println("eventList:" + eventList);
            System.out.println("eventNameSet:" + eventNameSet);
        }*/
    }
    
    //////////////////////////////////////////////////////////////////
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
}
