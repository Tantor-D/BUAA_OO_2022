package mycode;

import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    
    private final HashMap<String, String> name2ClassId;
    private final HashMap<String, MyClass> id2Class;
    private final HashMap<String, MyInterface> id2Interface;
    private final HashMap<String, MyOperation> id2Operation;
    private final HashMap<String, MyAttribute> id2Attribute;
    
    private final Tools tool;
    private static final String DUPLICATED = "duplicated";
    
    public MyImplementation(UmlElement... elements) {
        name2ClassId = new HashMap<>();
        id2Class = new HashMap<>();
        id2Interface = new HashMap<>();
        id2Operation = new HashMap<>();
        id2Attribute = new HashMap<>();
        
        tool = new Tools(name2ClassId, id2Class, id2Interface, id2Operation, id2Attribute);
        tool.analyseClass(elements);
        tool.analyseInterface(elements);
        tool.analyseAttribute(elements);
        
        tool.analyseOperation(elements);
        tool.analyseParameter(elements);
        
        tool.analyseGeneralization(elements);
        tool.analyseInterfaceRealization(elements);
        
        // 以下为静态图的建立
        tool.classGetAllAttribute();    // 找到类所有的attribute,同时计算属性耦合度
        
        tool.interfaceGetAllInterface(); // 处理接口间的继承关系
        tool.renewClassDepthOfInheritance(); // 处理类之间的继承关系，刷新类的继承深度
        tool.classGetAllInterface();    // 处理类对接口的实现，找到所有类实现的接口
        
        tool.renewOperationWrongType(); // 刷新所有的operation是否存在错误类型,后来直接得结果
        tool.renewOperationCoupling();  // 刷新所有类的操作耦合度
    }
    
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
    
    /**
     * 查询类的操作的耦合度
     * 指令：CLASS_OPERATION_COUPLING_DEGREE
     *
     * @param className  类名
     * @param methodName 操作名
     * @return 类的操作的耦合度
     * @throws ClassNotFoundException    类未找到异常
     * @throws ClassDuplicatedException  类重复异常
     * @throws MethodWrongTypeException  存在错误类型
     * @throws MethodDuplicatedException 存在重复操作
     */
    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!name2ClassId.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (name2ClassId.get(className).equals(DUPLICATED)) {
            throw new ClassDuplicatedException(className);
        }
        MyClass myClass = id2Class.get(name2ClassId.get(className));
        if (myClass.judgeMethodWrongType(methodName)) { // true则说明次方法名对应的方法存在错误
            throw new MethodWrongTypeException(className, methodName);
        }
        if (myClass.judgeMethodDuplicated(methodName)) {
            throw new MethodDuplicatedException(className, methodName);
        }
        return id2Class.get(name2ClassId.get(className)).
                getClassOperationCouplingDegree(methodName);
    }
    
    /**
     * 查询类的属性的耦合度
     * 指令：CLASS_ATTR_COUPLING_DEGREE
     *
     * @param className 类名
     * @return 类的属性的耦合度
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
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
    
    /**
     * 获取实现的接口列表
     * 指令：CLASS_IMPLEMENT_INTERFACE_LIST
     *
     * @param className 类名
     * @return 实现的接口列表
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
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
    
    /**
     * 获取类的继承深度
     * 指令：CLASS_DEPTH_OF_INHERITANCE
     *
     * @param className 类名
     * @return 类的继承深度
     * @throws ClassNotFoundException   类未找到异常
     * @throws ClassDuplicatedException 类重复异常
     */
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
    
}
