package mycode;

import com.oocourse.uml1.models.common.Visibility;

import java.util.HashMap;
import java.util.HashSet;

public class MyInterface {
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    private final HashMap<String, MyInterface> id2FaInterface;  // 只包括直接继承的接口
    private final HashMap<String, MyInterface> id2SubInterface;
    private final HashMap<String, MyAttribute> id2Attribute;
    private final HashMap<String, MyClass> id2RealizedClass;
    private final HashSet<String> inheritedInterfaceIdSet; // 包括直接或间接继承的接口
    
    public MyInterface(String name, String id, String parentId, Visibility visibility) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        id2FaInterface = new HashMap<>();
        id2SubInterface = new HashMap<>();
        id2Attribute = new HashMap<>();
        id2RealizedClass = new HashMap<>();
        inheritedInterfaceIdSet = new HashSet<>();
    }
    
    public void addAttribute(MyAttribute myAttribute) {
        id2Attribute.put(myAttribute.getId(), myAttribute);
    }
    
    public void addFaInterface(MyInterface myInterface) {
        id2FaInterface.put(myInterface.getId(), myInterface);
        inheritedInterfaceIdSet.add(myInterface.getId());
    }
    
    public void addSubInterface(MyInterface myInterface) {
        id2SubInterface.put(myInterface.getId(), myInterface);
    }
    
    public void addRealizedClass(MyClass myClass) {
        id2RealizedClass.put(myClass.getId(), myClass);
    }
    
    public boolean isTopInterface() {
        return id2FaInterface.isEmpty();
    }
    
    public void getAllInterfaceAndRenewSon(HashSet<String> faInheritedInterfaceIdSet) {
        // 将父接口所继承的全部加到当前接口这里
        inheritedInterfaceIdSet.addAll(faInheritedInterfaceIdSet);
        
        // 更新子类的情况
        for (MyInterface subInterface: id2SubInterface.values()) {
            subInterface.getAllInterfaceAndRenewSon(inheritedInterfaceIdSet);
        }
    }
    
    /////////////////////////////////////////////////////////
    //gets
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public HashSet<String> getInheritedInterfaceIdSet() {
        return inheritedInterfaceIdSet;
    }
}
