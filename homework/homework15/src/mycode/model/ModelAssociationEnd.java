package mycode.model;

import com.oocourse.uml3.models.elements.UmlAssociationEnd;

public class ModelAssociationEnd {
    private final UmlAssociationEnd umlAssociationEnd;
    
    public ModelAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        this.umlAssociationEnd = umlAssociationEnd;
    }
    
    public String getReference() {
        return umlAssociationEnd.getReference();
    }
    
    public String getName() {
        return umlAssociationEnd.getName();
    }
}
