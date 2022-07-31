package mycode;

public class ToolStatic {
    private static final String DUPLICATED = "duplicated";
    
    public static String getDuplicated() {
        return DUPLICATED;
    }
    
    public static boolean isKong(String str) {
        return (str == null || str.matches("[ \t]*"));
    }
    
    private static boolean r001Fault = false;
    private static boolean r005Fault = false;
    private static boolean r006Fault = false;
    private static boolean r007Fault = false;
    private static boolean r008Fault = false;
    private static boolean r009Fault = false;
    
    public static void setR001Fault(boolean bool) {
        r001Fault = bool;
    }

    public static boolean isR001Fault() {
        return r001Fault;
    }
  
    public static boolean isR005Fault() {
        return r005Fault;
    }
  
    public static boolean isR006Fault() {
        return r006Fault;
    }
  
    public static boolean isR007Fault() {
        return r007Fault;
    }
  
    public static void setR007Fault(boolean r007Fault) {
        ToolStatic.r007Fault = r007Fault;
    }
    
    public static boolean isR008Fault() {
        return r008Fault;
    }
    
    public static void setR008Fault(boolean r008Fault) {
        ToolStatic.r008Fault = r008Fault;
    }
    
    public static boolean isR009Fault() {
        return r009Fault;
    }
    
    public static void setR005Fault(boolean r005Fault) {
        ToolStatic.r005Fault = r005Fault;
    }
    
    public static void setR006Fault(boolean r006Fault) {
        ToolStatic.r006Fault = r006Fault;
    }
    
    public static void setR009Fault(boolean r009Fault) {
        ToolStatic.r009Fault = r009Fault;
    }
}
/*
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlTransition;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAssociation;

import mycode.model.ModelAttribute;
import mycode.model.ModelClass;
import mycode.model.ModelInterface;
import mycode.model.ModelOperation;
import mycode.model.ModelParameter;
import mycode.model.ModelAssociationEnd;

import mycode.statemachine.SmRegion;
import mycode.statemachine.SmState;
import mycode.statemachine.SmStateMachine;
import mycode.statemachine.SmTransition;
import mycode.statemachine.SmEvent;
*/