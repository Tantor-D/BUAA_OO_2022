package mycode;

import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlTransition;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlEndpoint;

import mycode.model.ModelAttribute;
import mycode.model.ModelClass;
import mycode.model.ModelInterface;
import mycode.model.ModelOperation;
import mycode.model.ModelParameter;

import mycode.statemachine.SmRegion;
import mycode.statemachine.SmState;
import mycode.statemachine.SmStateMachine;
import mycode.statemachine.SmTransition;
import mycode.statemachine.SmEvent;

import mycode.sequence.SequenceInteraction;
import mycode.sequence.SequenceLifeline;

import java.util.HashMap;
import java.util.HashSet;

public class Tools {
    private final HashMap<String, String> name2ClassId;
    private final HashMap<String, ModelClass> id2Class;
    private final HashMap<String, ModelInterface> id2Interface;
    private final HashMap<String, ModelOperation> id2Operation;
    private final HashMap<String, ModelAttribute> id2Attribute;
    private static final HashSet<String> RIGHT_NAMED_TYPE_SET = new HashSet<>();
    private static final String DUPLICATED = "duplicated";
    
    // 以下为这次加的
    private final HashMap<String, String> name2InteractionId;
    private final HashMap<String, SequenceInteraction> id2Interaction;
    private final HashMap<String, String> name2StateMachineId;
    private final HashMap<String, SmStateMachine> id2StateMachine;
    private final HashMap<String, SmRegion> id2Region;
    private final HashMap<String, SmState> id2State;
    private final HashMap<String, SmTransition> id2Transition;
    
    public Tools(HashMap<String, String> name2ClassId,
                 HashMap<String, ModelClass> id2Class,
                 HashMap<String, ModelInterface> id2Interface,
                 HashMap<String, ModelOperation> id2Operation,
                 HashMap<String, ModelAttribute> id2Attribute,
                 HashMap<String, String> name2InteractionId,
                 HashMap<String, SequenceInteraction> id2Interaction,
                 HashMap<String, String> name2StateMachineId,
                 HashMap<String, SmStateMachine> id2StateMachine,
                 HashMap<String, SmRegion> id2Region) {
        this.name2ClassId = name2ClassId;
        this.id2Class = id2Class;
        this.id2Interface = id2Interface;
        this.id2Operation = id2Operation;
        this.id2Attribute = id2Attribute;
        initialRightNameTypeSet();
        // 以下为状态图和顺序图相关的
        this.name2InteractionId = name2InteractionId;
        this.id2Interaction = id2Interaction;
        this.name2StateMachineId = name2StateMachineId;
        this.id2StateMachine = id2StateMachine;
        this.id2Region = id2Region;
        this.id2State = new HashMap<>();
        this.id2Transition = new HashMap<>();
    }
    
    public void initialRightNameTypeSet() {
        RIGHT_NAMED_TYPE_SET.add("byte");
        RIGHT_NAMED_TYPE_SET.add("short");
        RIGHT_NAMED_TYPE_SET.add("int");
        RIGHT_NAMED_TYPE_SET.add("long");
        RIGHT_NAMED_TYPE_SET.add("float");
        RIGHT_NAMED_TYPE_SET.add("double");
        RIGHT_NAMED_TYPE_SET.add("char");
        RIGHT_NAMED_TYPE_SET.add("boolean");
        RIGHT_NAMED_TYPE_SET.add("String");
    }
    
    public static boolean judgeIsWrongNamedType(String str) {
        if (RIGHT_NAMED_TYPE_SET.contains(str)) {
            return false;
        }
        return true;
    }
    
    public static String getDuplicated() {
        return DUPLICATED;
    }
    
    public void renewClassDepthOfInheritance() {
        for (ModelClass modelClass : id2Class.values()) {
            if (modelClass.getFaClassId() == null) { // 顶层父类
                modelClass.setInheritanceDepthAndRenewSonDepth(0); // 父类的继承深度为0
            }
        }
    }
    
    public void renewOperationWrongType() { // 对所有操作都进行一次hasWrongType的初始化
        for (ModelOperation op : id2Operation.values()) {
            op.renewHasWrongType();
        }
    }
    
    public void renewOperationCoupling() {
        for (ModelOperation op : id2Operation.values()) {
            op.renewCoupling();
        }
    }
    
    public void interfaceGetAllInterface() {
        for (ModelInterface modelInterface : id2Interface.values()) {
            if (modelInterface.isTopInterface()) {
                modelInterface.getAllInterfaceAndRenewSon(new HashSet<>());
            }
        }
    }
    
    public void classGetAllInterface() {
        for (ModelClass modelClass : id2Class.values()) {
            if (modelClass.getFaClassId() == null) {
                modelClass.getAllInterfaceAndRenewSon(new HashSet<>(), id2Interface);
            }
        }
    }
    
    public void classGetAllAttribute() {
        for (ModelClass modelClass : id2Class.values()) {
            if (modelClass.getFaClassId() == null) { // 父类仅用考虑自身属性，其不考虑接口那的Attribute
                modelClass.getAllAttributeAndRenewSon(new HashSet<>(), id2Attribute);
            }
        }
    }
    
    public void initialStateMachineCanReachFinalState() {
        for (SmStateMachine smStateMachine : id2StateMachine.values()) {
            smStateMachine.initialCanReachFinalState();
        }
    }
    
    //////////////////////////////////////////////////////////////////////////
    // 以下为analyse，解析输入
    
    // 以下为解析状态图
    public void analyseStateMachine(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlStateMachine) {
                UmlStateMachine umlStateMachine = (UmlStateMachine) element;
                SmStateMachine smStateMachine = new SmStateMachine(umlStateMachine);
                id2StateMachine.put(umlStateMachine.getId(), smStateMachine);
                if (name2StateMachineId.containsKey(umlStateMachine.getName())) {
                    name2StateMachineId.put(umlStateMachine.getName(), DUPLICATED);
                } else {
                    name2StateMachineId.put(umlStateMachine.getName(), umlStateMachine.getId());
                }
            }
        }
    }
    
    public void analyseRegion(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlRegion) {
                UmlRegion umlRegion = (UmlRegion) element;
                SmRegion smRegion = new SmRegion(umlRegion);
                id2Region.put(umlRegion.getId(), smRegion);
                id2StateMachine.get(umlRegion.getParentId()).addRegion(smRegion);
            }
        }
    }
    
    public void analyse3kindsState(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlState) {
                UmlState umlState = (UmlState) element;
                SmState smState = new SmState(umlState, EnumsStateType.normalState);
                SmRegion smRegion = id2Region.get(umlState.getParentId());
                smRegion.addState(smState);
                id2State.put(smState.getId(), smState);
            } else if (element instanceof UmlFinalState) {
                UmlFinalState umlFinalState = (UmlFinalState) element;
                SmState smState = new SmState(umlFinalState, EnumsStateType.finalState);
                SmRegion smRegion = id2Region.get(umlFinalState.getParentId());
                smRegion.addState(smState);
                id2State.put(smState.getId(), smState);
            } else if (element instanceof UmlPseudostate) {
                UmlPseudostate umlPseudostate = (UmlPseudostate) element;
                SmState smState = new SmState(umlPseudostate, EnumsStateType.initialState);
                SmRegion smRegion = id2Region.get(umlPseudostate.getParentId());
                smRegion.addState(smState);
                id2State.put(smState.getId(), smState);
            }
        }
    }
    
    public void analyseTransition(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlTransition) {
                UmlTransition umlTransition = (UmlTransition) element;
                SmState targetState = id2State.get(umlTransition.getTarget());
                SmState sourceState = id2State.get(umlTransition.getSource());
                SmTransition smTransition = new SmTransition(umlTransition, targetState);
                id2Transition.put(umlTransition.getId(), smTransition);
                sourceState.addTransitionAndNextState(smTransition);
            }
        }
    }
    
    public void analyseEvent(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlEvent) {
                UmlEvent umlEvent = (UmlEvent) element;
                SmEvent smEvent = new SmEvent(umlEvent);
                SmTransition smTransition = id2Transition.get(umlEvent.getParentId());
                smTransition.addEvent(smEvent);
            }
        }
    }
    
    // 以下为解析顺序图
    public void analyseInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlInteraction) {
                UmlInteraction thisInteraction = (UmlInteraction) element;
                SequenceInteraction newSequenceInteraction =
                        new SequenceInteraction(thisInteraction);
                id2Interaction.put(thisInteraction.getId(), newSequenceInteraction);
                if (name2InteractionId.containsKey(thisInteraction.getName())) {
                    name2InteractionId.put(thisInteraction.getName(), DUPLICATED);
                } else {
                    name2InteractionId.put(thisInteraction.getName(), thisInteraction.getId());
                }
            }
        }
    }

    public void analyseLifeline(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlLifeline) {
                UmlLifeline thisLifeline = (UmlLifeline) element;
                SequenceLifeline newLifeline = new SequenceLifeline(thisLifeline);
                id2Interaction.get(thisLifeline.getParentId()).addLifeline(newLifeline);
            }
        }
    }
    
    public void analyseEndpoint(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlEndpoint) {
                UmlEndpoint thisEndpoint = (UmlEndpoint) element;
                id2Interaction.get(thisEndpoint.getParentId()).addEndpointId(thisEndpoint.getId());
            }
        }
    }
    
    public void analyseMessage(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlMessage) {
                UmlMessage thisMessage = (UmlMessage) element;
                id2Interaction.get(thisMessage.getParentId()).dealMessage(thisMessage);
            }
        }
    }
    
    // 以下为解析类图
    public void analyseClass(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlClass) {
                UmlClass thisClass = (UmlClass) element;
                ModelClass newModelClass = new ModelClass(thisClass.getName(), thisClass.getId(),
                        thisClass.getParentId(), thisClass.getVisibility());
                id2Class.put(newModelClass.getId(), newModelClass);
                if (name2ClassId.containsKey(newModelClass.getName())) {
                    name2ClassId.put(newModelClass.getName(), DUPLICATED);
                } else {
                    name2ClassId.put(newModelClass.getName(), newModelClass.getId());
                }
            }
        }
    }
    
    public void analyseInterface(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlInterface) {
                UmlInterface thisInterface = (UmlInterface) element;
                ModelInterface myNewInterface = new ModelInterface(thisInterface.getName(),
                        thisInterface.getId(),
                        thisInterface.getParentId(),
                        thisInterface.getVisibility());
                id2Interface.put(myNewInterface.getId(), myNewInterface);
            }
        }
    }
    
    public void analyseAttribute(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlAttribute) {
                UmlAttribute thisAttribute = (UmlAttribute) element;
                
                ModelAttribute modelAttribute = new ModelAttribute(
                        thisAttribute.getName(),
                        thisAttribute.getId(),
                        thisAttribute.getParentId(),
                        thisAttribute.getVisibility(),
                        thisAttribute.getType());
                id2Attribute.put(modelAttribute.getId(), modelAttribute);
                if (id2Class.containsKey(thisAttribute.getParentId())) { // 其为类的属性
                    id2Class.get(modelAttribute.getParentId()).addAttribute(modelAttribute);
                } else if (id2Interface.containsKey(thisAttribute.getParentId())) {
                    id2Interface.get(modelAttribute.getParentId()).addAttribute(modelAttribute);
                }
            }
        }
    }
    
    public void analyseOperation(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlOperation) {
                UmlOperation thisOperation = (UmlOperation) element;
                ModelOperation modelOperation = new ModelOperation(
                        thisOperation.getName(),
                        thisOperation.getId(),
                        thisOperation.getParentId(),
                        thisOperation.getVisibility());
                id2Operation.put(modelOperation.getId(), modelOperation);
                id2Class.get(modelOperation.getParentId()).addOperation(modelOperation);
            }
        }
    }
    
    public void analyseParameter(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlParameter) {
                UmlParameter thisParameter = (UmlParameter) element;
                ModelParameter modelParameter = new ModelParameter(
                        thisParameter.getName(),
                        thisParameter.getId(),
                        thisParameter.getParentId(),
                        thisParameter.getType(),
                        thisParameter.getDirection());
                id2Operation.get(modelParameter.getParentId()).addParameter(modelParameter);
            }
        }
    }
    
    public void analyseGeneralization(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlGeneralization) {
                UmlGeneralization thisGeneralization = (UmlGeneralization) element;
                String sourceId = thisGeneralization.getSource();
                String targetId = thisGeneralization.getTarget();
                if (id2Class.containsKey(sourceId)) { // 是class间的继承关系
                    id2Class.get(sourceId).addFaClass(id2Class.get(targetId));
                    id2Class.get(targetId).addSubClass(id2Class.get(sourceId));
                } else { // interface间的继承关系
                    id2Interface.get(sourceId).addFaInterface(id2Interface.get(targetId));
                    id2Interface.get(targetId).addSubInterface(id2Interface.get(sourceId));
                }
            }
        }
    }
    
    public void analyseInterfaceRealization(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlInterfaceRealization) {
                UmlInterfaceRealization thisRealization = (UmlInterfaceRealization) element;
                
                ModelClass sourceClass = id2Class.get(thisRealization.getSource());
                ModelInterface targetInterface = id2Interface.get(thisRealization.getTarget());
                sourceClass.addRealizedInterface(targetInterface);
                targetInterface.addRealizedClass(sourceClass);
            }
        }
    }
}

/*
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlTransition;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlEndpoint;

import mycode.model.ModelAttribute;
import mycode.model.ModelClass;
import mycode.model.ModelInterface;
import mycode.model.ModelOperation;
import mycode.model.ModelParameter;

import mycode.statemachine.SmRegion;
import mycode.statemachine.SmState;
import mycode.statemachine.SmStateMachine;
import mycode.statemachine.SmTransition;
import mycode.statemachine.SmEvent;
*/
