package mycode.model;

import com.oocourse.uml2.models.common.Visibility;

import java.util.HashMap;
import java.util.HashSet;

public class ModelInterface {
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    private final HashMap<String, ModelInterface> id2FaInterface;  // 只包括直接继承的接口
    private final HashMap<String, ModelInterface> id2SubInterface;
    private final HashMap<String, ModelAttribute> id2Attribute;
    private final HashMap<String, ModelClass> id2RealizedClass;
    private final HashSet<String> inheritedInterfaceIdSet; // 包括直接或间接继承的接口
    
    public ModelInterface(String name, String id, String parentId, Visibility visibility) {
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
    
    public void addAttribute(ModelAttribute modelAttribute) {
        id2Attribute.put(modelAttribute.getId(), modelAttribute);
    }
    
    public void addFaInterface(ModelInterface modelInterface) {
        id2FaInterface.put(modelInterface.getId(), modelInterface);
        inheritedInterfaceIdSet.add(modelInterface.getId());
    }
    
    public void addSubInterface(ModelInterface modelInterface) {
        id2SubInterface.put(modelInterface.getId(), modelInterface);
    }
    
    public void addRealizedClass(ModelClass modelClass) {
        id2RealizedClass.put(modelClass.getId(), modelClass);
    }
    
    public boolean isTopInterface() {
        return id2FaInterface.isEmpty();
    }
    
    public void getAllInterfaceAndRenewSon(HashSet<String> faInheritedInterfaceIdSet) {
        // 将父接口所继承的全部加到当前接口这里
        inheritedInterfaceIdSet.addAll(faInheritedInterfaceIdSet);
        
        // 更新子类的情况
        for (ModelInterface subInterface: id2SubInterface.values()) {
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
