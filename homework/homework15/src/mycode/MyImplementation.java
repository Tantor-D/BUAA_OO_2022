package mycode;

/*
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;

import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
*/

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.Pair;

import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;

import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;

import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlLifeline;
import mycode.model.ModelAttribute;
import mycode.model.ModelClass;
import mycode.model.ModelInterface;
import mycode.model.ModelOperation;
import mycode.sequence.SequenceInteraction;
import mycode.sequence.SequenceLifeline;
import mycode.statemachine.SmRegion;
import mycode.statemachine.SmStateMachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class MyImplementation implements UserApi {
    private final HashMap<String, String> name2ClassId;
    private final HashMap<String, ModelClass> id2Class;
    // ???????????????????????????MyImplementation???????????????????????????????????????
    private final HashMap<String, ModelInterface> id2Interface;
    private final HashMap<String, ModelOperation> id2Operation;
    private final HashMap<String, ModelAttribute> id2Attribute;
    private final HashMap<String, String> name2InteractionId;
    private final HashMap<String, SequenceInteraction> id2Interaction;
    private final HashMap<String, String> name2StateMachineId;
    private final HashMap<String, SmStateMachine> id2StateMachine;
    private final HashMap<String, SmRegion> id2Region;
    private static final String DUPLICATED = "duplicated";
    private Set<UmlClassOrInterface> r003Ans;
    private Set<UmlClassOrInterface> r004Ans;
    
    public MyImplementation(UmlElement... elements) {
        name2ClassId = new HashMap<>();
        id2Class = new HashMap<>();
        id2Interface = new HashMap<>();
        id2Operation = new HashMap<>();
        id2Attribute = new HashMap<>();
        name2InteractionId = new HashMap<>();
        id2Interaction = new HashMap<>();
        name2StateMachineId = new HashMap<>();
        id2StateMachine = new HashMap<>();
        id2Region = new HashMap<>();
        
        Tools tool = new Tools(name2ClassId, id2Class, id2Interface,
                id2Operation, id2Attribute, name2InteractionId, id2Interaction,
                name2StateMachineId, id2StateMachine, id2Region);
        // ???????????????
        tool.analyseClass(elements);
        tool.analyseAssociationEnd(elements);
        tool.analyseAssociation(elements);
        tool.analyseInterface(elements);
        tool.analyseAttribute(elements);
        tool.analyseOperation(elements);
        tool.analyseParameter(elements);
        tool.analyseGeneralization(elements);
        tool.analyseInterfaceRealization(elements);
        
        // ??????????????????
        tool.analyseInteraction(elements);
        tool.analyseLifeline(elements);
        tool.analyseEndpoint(elements);
        tool.analyseMessage(elements);
        
        // ??????????????????
        tool.analyseStateMachine(elements);
        tool.analyseRegion(elements);
        tool.analyse3kindsState(elements);
        tool.analyseTransition(elements);
        tool.analyseEvent(elements);
        
        r003Ans = tool.calForR003();
        if (r003Ans.isEmpty()) {
            r004Ans = tool.calForR004();
            // ?????????????????????????????????
            if (r004Ans.isEmpty()) {
                tool.classGetAllAttribute();     // ??????????????????attribute,???????????????????????????
                tool.interfaceGetAllInterface(); // ??????????????????????????????
                tool.renewClassDepthOfInheritance(); // ?????????????????????????????????????????????????????????
                tool.classGetAllInterface();    // ????????????????????????????????????????????????????????????
                tool.renewOperationWrongType(); // ???????????????operation????????????????????????,?????????????????????
                tool.renewOperationCoupling();  // ?????????????????????????????????
            }
        }
        
        // ???????????????????????????
        tool.initialStateMachineCanReachFinalState();
        tool.calForR009();
    }
    
    /********************************************************/
    //??????????????????
    public void checkForAllRules() throws PreCheckRuleException {
        checkForUml001();
        checkForUml002();
        checkForUml003();
        checkForUml004();
        checkForUml005();
        checkForUml006();
        checkForUml007();
        checkForUml008();
        checkForUml009();
    }
    
    public void checkForUml001() throws UmlRule001Exception {
        if (ToolStatic.isR001Fault()) {
            throw new UmlRule001Exception();
        }
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        Set<AttributeClassInformation> retSet = new HashSet<>();
        for (ModelClass modelClass : id2Class.values()) {
            retSet.addAll(modelClass.calForR002());
        }
        if (!retSet.isEmpty()) {
            throw new UmlRule002Exception(retSet);
        }
    }
    
    public void checkForUml003() throws UmlRule003Exception {
        if (!r003Ans.isEmpty()) {
            throw new UmlRule003Exception(r003Ans);
        }
    }
    
    /**
     * ?????? R004 ??????
     *
     * @throws UmlRule004Exception ????????????
     */
    public void checkForUml004() throws UmlRule004Exception {
        if (!r004Ans.isEmpty()) {
            throw new UmlRule004Exception(r004Ans);
        }
    }
    
    public void checkForUml005() throws UmlRule005Exception {
        if (ToolStatic.isR005Fault()) {
            throw new UmlRule005Exception();
        }
    }
    
    public void checkForUml006() throws UmlRule006Exception {
        if (ToolStatic.isR006Fault()) {
            throw new UmlRule006Exception();
        }
    }
    
    public void checkForUml007() throws UmlRule007Exception {
        if (ToolStatic.isR007Fault()) {
            throw new UmlRule007Exception();
        }
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        if (ToolStatic.isR008Fault()) {
            throw new UmlRule008Exception();
        }
    }
    
    public void checkForUml009() throws UmlRule009Exception {
        if (ToolStatic.isR009Fault()) {
            throw new UmlRule009Exception();
        }
    }
    
    /**************************************************************************************/
    // ??????????????????
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!name2StateMachineId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (name2StateMachineId.get(stateMachineName).equals(DUPLICATED)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        return id2StateMachine.get(name2StateMachineId.get(stateMachineName)).getNumOfState();
    }
    
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!name2StateMachineId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (name2StateMachineId.get(stateMachineName).equals(DUPLICATED)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        SmStateMachine smStateMachine =
                id2StateMachine.get(name2StateMachineId.get(stateMachineName));
        if (!smStateMachine.isStateExist(stateName)) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        if (smStateMachine.isStateDuplicate(stateName)) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        return smStateMachine.getStateIsCriticalPoint(stateName);
    }
    
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        if (!name2StateMachineId.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (name2StateMachineId.get(stateMachineName).equals(DUPLICATED)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        SmStateMachine smStateMachine =
                id2StateMachine.get(name2StateMachineId.get(stateMachineName));
        if (!smStateMachine.isStateExist(sourceStateName)) {
            throw new StateNotFoundException(stateMachineName, sourceStateName);
        }
        if (smStateMachine.isStateDuplicate(sourceStateName)) {
            throw new StateDuplicatedException(stateMachineName, sourceStateName);
        }
        if (!smStateMachine.isStateExist(targetStateName)) {
            throw new StateNotFoundException(stateMachineName, targetStateName);
        }
        if (smStateMachine.isStateDuplicate(targetStateName)) {
            throw new StateDuplicatedException(stateMachineName, targetStateName);
        }
        List<String> ansList =
                smStateMachine.getTransitionTrigger(sourceStateName, targetStateName);
        if (ansList == null) {
            throw new TransitionNotFoundException(
                    stateMachineName, sourceStateName, targetStateName);
        } else {
            return ansList;
        }
    }
    
    /****************************************************************************/
    // ???????????????
    public int getClassCount() { // ???????????????
        return id2Class.size();
    }
    
    public int getClassSubClassCount(String className) // ????????????????????????
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getSubClassCount();
    }
    
    public int getClassOperationCount(String className) // ?????????????????????
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getClassOperationCount();
    }
    
    // ????????????????????????
    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getClassOperationVisibility(methodName);
    }
    
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        ModelClass modelClass = id2Class.get(name2ClassId.get(className));
        if (modelClass.judgeMethodWrongType(methodName)) { // true????????????????????????????????????????????????
            throw new MethodWrongTypeException(className, methodName);
        }
        if (modelClass.judgeMethodDuplicated(methodName)) {
            throw new MethodDuplicatedException(className, methodName);
        }
        return id2Class.get(name2ClassId.get(className)).
                getClassOperationCouplingDegree(methodName);
    }
    
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getCouplingOfAttribute();
    }
    
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getRealizedInterfaceNameList();
    }
    
    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getDepthOfInheritance();
    }
    
    /**************************************************************************************/
    // ??????????????????
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!name2InteractionId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (name2InteractionId.get(interactionName).equals(DUPLICATED)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return id2Interaction.get(name2InteractionId.get(interactionName)).getNumOfLifeline();
    }
    
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!name2InteractionId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (name2InteractionId.get(interactionName).equals(DUPLICATED)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        SequenceInteraction sequenceInteraction =
                id2Interaction.get(name2InteractionId.get(interactionName));
        if (!sequenceInteraction.isLifelineExist(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        if (sequenceInteraction.isLifelineDuplicate(lifelineName)) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        SequenceLifeline sequenceLifeline = sequenceInteraction.getLifeline(lifelineName);
        if (sequenceLifeline.getNumOfCreateMessage() == 0) {
            throw new LifelineNeverCreatedException(interactionName, lifelineName);
        }
        if (sequenceLifeline.getNumOfCreateMessage() > 1) {
            throw new LifelineCreatedRepeatedlyException(interactionName, lifelineName);
        }
        return sequenceLifeline.getCreatorLifeline();
    }
    
    public Pair<Integer, Integer> getParticipantLostAndFound(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!name2InteractionId.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (name2InteractionId.get(interactionName).equals(DUPLICATED)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        SequenceInteraction sequenceInteraction =
                id2Interaction.get(name2InteractionId.get(interactionName));
        if (!sequenceInteraction.isLifelineExist(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        if (sequenceInteraction.isLifelineDuplicate(lifelineName)) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        SequenceLifeline sequenceLifeline = sequenceInteraction.getLifeline(lifelineName);
        
        return new Pair<>(sequenceLifeline.getNumOfFoundMessage(),
                sequenceLifeline.getNumOfLostMessage());
    }
    
}
