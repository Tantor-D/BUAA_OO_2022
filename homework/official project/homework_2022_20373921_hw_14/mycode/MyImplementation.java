package mycode;

import com.oocourse.uml2.interact.common.Pair;
/*
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
*/

import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;

import com.oocourse.uml2.interact.format.UserApi;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlLifeline;
import mycode.model.ModelAttribute;
import mycode.model.ModelClass;
import mycode.model.ModelInterface;
import mycode.model.ModelOperation;
import mycode.sequence.SequenceInteraction;
import mycode.sequence.SequenceLifeline;
import mycode.statemachine.SmRegion;
import mycode.statemachine.SmStateMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    
    private final HashMap<String, String> name2ClassId;
    private final HashMap<String, ModelClass> id2Class;
    // 有一些属性起始顶层MyImplementation用不上，只是以防万一写在这
    private final HashMap<String, ModelInterface> id2Interface;
    private final HashMap<String, ModelOperation> id2Operation;
    private final HashMap<String, ModelAttribute> id2Attribute;
    private final HashMap<String, String> name2InteractionId;
    private final HashMap<String, SequenceInteraction> id2Interaction;
    private final HashMap<String, String> name2StateMachineId;
    private final HashMap<String, SmStateMachine> id2StateMachine;
    private final HashMap<String, SmRegion> id2Region;
    private static final String DUPLICATED = "duplicated";
    
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
        // 类图的分析
        tool.analyseClass(elements);
        tool.analyseInterface(elements);
        tool.analyseAttribute(elements);
        tool.analyseOperation(elements);
        tool.analyseParameter(elements);
        tool.analyseGeneralization(elements);
        tool.analyseInterfaceRealization(elements);
        
        // 顺序图的分析
        tool.analyseInteraction(elements);
        tool.analyseLifeline(elements);
        tool.analyseEndpoint(elements);
        tool.analyseMessage(elements);
        
        // 状态图的分析
        tool.analyseStateMachine(elements);
        tool.analyseRegion(elements);
        tool.analyse3kindsState(elements);
        tool.analyseTransition(elements);
        tool.analyseEvent(elements);
        
        // 以下为类图的静态图建立
        tool.classGetAllAttribute();     // 找到类所有的attribute,同时计算属性耦合度
        tool.interfaceGetAllInterface(); // 处理接口间的继承关系
        tool.renewClassDepthOfInheritance(); // 处理类之间的继承关系，刷新类的继承深度
        tool.classGetAllInterface();    // 处理类对接口的实现，找到所有类实现的接口
        tool.renewOperationWrongType(); // 刷新所有的operation是否存在错误类型,后来直接得结果
        tool.renewOperationCoupling();  // 刷新所有类的操作耦合度
        
        // 状态图的静态图建立
        tool.initialStateMachineCanReachFinalState();
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    // 以下为状态图
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
    
    // 以下为类图
    public int getClassCount() { // 获取类数量
        return id2Class.size();
    }
    
    public int getClassSubClassCount(String className) // 获取类的子类数量
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getSubClassCount();
    }
    
    public int getClassOperationCount(String className) // 获取类操作数量
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        return id2Class.get(name2ClassId.get(className)).getClassOperationCount();
    }
    
    // 统计类操作可见性
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
        if (modelClass.judgeMethodWrongType(methodName)) { // true则说明次方法名对应的方法存在错误
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
    
    ////////////////////////////////////////////////////////////////////////////
    // 以下为顺序图
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
