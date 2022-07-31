package mycode.statemachine;

import com.oocourse.uml2.models.elements.UmlEvent;

public class SmEvent {
    private final UmlEvent umlEvent;
    
    public SmEvent(UmlEvent umlEvent) {
        this.umlEvent = umlEvent;
    }
    
    public String getName() {
        return umlEvent.getName();
    }
    
    public String getId() {
        return umlEvent.getId();
    }
    
}
