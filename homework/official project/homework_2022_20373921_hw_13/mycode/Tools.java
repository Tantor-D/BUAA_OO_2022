package mycode;

/*
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
*/

import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;

import java.util.HashMap;
import java.util.HashSet;

public class Tools {
    private final HashMap<String, String> name2ClassId;
    private final HashMap<String, MyClass> id2Class;
    private final HashMap<String, MyInterface> id2Interface;
    private final HashMap<String, MyOperation> id2Operation;
    private final HashMap<String, MyAttribute> id2Attribute;
    private static final HashSet<String> RIGHT_NAMED_TYPE_SET = new HashSet<>();
    private static final String DUPLICATED = "duplicated";
    
    public Tools(HashMap<String, String> name2ClassId,
                 HashMap<String, MyClass> id2Class,
                 HashMap<String, MyInterface> id2Interface,
                 HashMap<String, MyOperation> id2Operation,
                 HashMap<String, MyAttribute> id2Attribute) {
        this.name2ClassId = name2ClassId;
        this.id2Class = id2Class;
        this.id2Interface = id2Interface;
        this.id2Operation = id2Operation;
        this.id2Attribute = id2Attribute;
        initialRightNameTypeSet();
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
    
    public void renewClassDepthOfInheritance() {
        for (MyClass myClass : id2Class.values()) {
            if (myClass.getFaClassId() == null) { // 顶层父类
                myClass.setInheritanceDepthAndRenewSonDepth(0); // 父类的继承深度为0
            }
        }
    }
    
    public void renewOperationWrongType() { // 对所有操作都进行一次hasWrongType的初始化
        for (MyOperation op : id2Operation.values()) {
            op.renewHasWrongType();
        }
    }
    
    public void renewOperationCoupling() {
        for (MyOperation op : id2Operation.values()) {
            op.renewCoupling();
        }
    }
    
    public void interfaceGetAllInterface() {
        for (MyInterface myInterface : id2Interface.values()) {
            if (myInterface.isTopInterface()) {
                myInterface.getAllInterfaceAndRenewSon(new HashSet<>());
            }
        }
    }
    
    public void classGetAllInterface() {
        for (MyClass myClass : id2Class.values()) {
            if (myClass.getFaClassId() == null) {
                myClass.getAllInterfaceAndRenewSon(new HashSet<String>(), id2Interface);
            }
        }
    }
    
    public void classGetAllAttribute() {
        for (MyClass myClass : id2Class.values()) {
            if (myClass.getFaClassId() == null) { // 父类仅用考虑自身属性，其不考虑接口那的Attribute
                myClass.getAllAttributeAndRenewSon(new HashSet<>(), id2Attribute);
            }
        }
    }
    
    //////////////////////////////////////////////////////////////////////////
    // 以下为analyse，解析输入
    public void analyseClass(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlClass) {
                UmlClass thisClass = (UmlClass) element;
                MyClass newMyClass = new MyClass(thisClass.getName(), thisClass.getId(),
                        thisClass.getParentId(), thisClass.getVisibility());
                id2Class.put(newMyClass.getId(), newMyClass);
                if (name2ClassId.containsKey(newMyClass.getName())) {
                    name2ClassId.put(newMyClass.getName(), DUPLICATED);
                } else {
                    name2ClassId.put(newMyClass.getName(), newMyClass.getId());
                }
            }
        }
    }
    
    public void analyseInterface(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlInterface) {
                UmlInterface thisInterface = (UmlInterface) element;
                MyInterface myNewInterface = new MyInterface(thisInterface.getName(),
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
                
                MyAttribute myAttribute = new MyAttribute(
                        thisAttribute.getName(),
                        thisAttribute.getId(),
                        thisAttribute.getParentId(),
                        thisAttribute.getVisibility(),
                        thisAttribute.getType());
                id2Attribute.put(myAttribute.getId(), myAttribute);
                if (id2Class.containsKey(thisAttribute.getParentId())) { // 其为类的属性
                    id2Class.get(myAttribute.getParentId()).addAttribute(myAttribute);
                } else {
                    id2Interface.get(myAttribute.getParentId()).addAttribute(myAttribute);
                }
            }
        }
    }
    
    public void analyseOperation(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlOperation) {
                UmlOperation thisOperation = (UmlOperation) element;
                MyOperation myOperation = new MyOperation(
                        thisOperation.getName(),
                        thisOperation.getId(),
                        thisOperation.getParentId(),
                        thisOperation.getVisibility());
                id2Operation.put(myOperation.getId(), myOperation);
                id2Class.get(myOperation.getParentId()).addOperation(myOperation);
            }
        }
    }
    
    public void analyseParameter(UmlElement... elements) {
        for (UmlElement element : elements) {
            if (element instanceof UmlParameter) {
                UmlParameter thisParameter = (UmlParameter) element;
                MyParameter myParameter = new MyParameter(
                        thisParameter.getName(),
                        thisParameter.getId(),
                        thisParameter.getParentId(),
                        thisParameter.getType(),
                        thisParameter.getDirection());
                id2Operation.get(myParameter.getParentId()).addParameter(myParameter);
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
                
                MyClass sourceClass = id2Class.get(thisRealization.getSource());
                MyInterface targetInterface = id2Interface.get(thisRealization.getTarget());
                sourceClass.addRealizedInterface(targetInterface);
                targetInterface.addRealizedClass(sourceClass);
            }
        }
    }
    
}
