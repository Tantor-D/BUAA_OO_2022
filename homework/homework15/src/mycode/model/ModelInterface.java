package mycode.model;

import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;*/

public class ModelInterface {
    private final UmlInterface umlInterface;
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    private final HashMap<String, ModelInterface> id2FaInterface;  // 只包括直接继承的接口
    private final HashMap<String, ModelInterface> id2SubInterface;
    private final HashMap<String, ModelAttribute> id2Attribute;
    private final HashMap<String, ModelClass> id2RealizedClass;
    // R003不需要考虑多继承，恰恰需要hashMap来消除相同的继承关系
    private final ArrayList<String> subInterfaceIdList; // 这个是为了R004的多继承特地加的
    private final HashSet<String> inheritedInterfaceIdSet; // 包括直接或间接继承的接口
    
    public ModelInterface(UmlInterface umlInterface, String name,
                          String id, String parentId, Visibility visibility) {
        this.umlInterface = umlInterface;
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        id2FaInterface = new HashMap<>();
        id2SubInterface = new HashMap<>();
        id2Attribute = new HashMap<>();
        id2RealizedClass = new HashMap<>();
        inheritedInterfaceIdSet = new HashSet<>();
        subInterfaceIdList = new ArrayList<>();
    }
    
    public void calForR004(HashSet<UmlClassOrInterface> retSet,
                           HashMap<String, Integer> beenFoundId2Times) {
        for (String subId : subInterfaceIdList) {
            if (beenFoundId2Times.containsKey(subId)) { // 遍历时曾经找到过
                if (beenFoundId2Times.get(subId) == 1) { // 之前搜到过一次，现在是第二次找到，需要重新搜索，把子类加进来
                    retSet.add(id2SubInterface.get(subId).getUmlInterface());
                    beenFoundId2Times.put(subId,2); // 标记为已经搜索两次了，之后都不要再搜索了
                    id2SubInterface.get(subId).calForR004(retSet, beenFoundId2Times);
                }
            } else { // 没被找到过
                beenFoundId2Times.put(subId, 1); // 现在被找到过一次了
                id2SubInterface.get(subId).calForR004(retSet, beenFoundId2Times);
            }
        }
    }
    
    /*public void calForR004(HashSet<UmlClassOrInterface> retSet, HashSet<String> beenFoundSet) {
        for (String subId : subInterfaceIdList) {
            if (beenFoundSet.contains(subId)) { // 遍历时曾经找到过
                retSet.add(id2SubInterface.get(subId).getUmlInterface());
            } else {
                beenFoundSet.add(subId);
            }
            System.out.println("nowId = " + id + "  subId = " + subId);
            id2SubInterface.get(subId).calForR004(retSet, beenFoundSet);
        }
    }*/
    
    public void dfsForR003(
            HashSet<UmlClassOrInterface> retSet,
            HashSet<String> visitedIdSet,
            ArrayList<String> pathIdList,
            HashMap<String, ModelInterface> id2Interface) {
        visitedIdSet.add(id);
        pathIdList.add(id);
        
        for (String faId : id2FaInterface.keySet()) {
            if (!visitedIdSet.contains(faId)) {
                id2FaInterface.get(faId).dfsForR003(retSet, visitedIdSet, pathIdList, id2Interface);
            } else if (pathIdList.contains(faId)) {
                for (int i = pathIdList.size() - 1; i >= 0; i--) {
                    retSet.add(id2Interface.get(pathIdList.get(i)).getUmlInterface());
                    if (pathIdList.get(i).equals(faId)) {
                        break;
                    }
                }
            }
        }
        
        pathIdList.remove(id);
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
        subInterfaceIdList.add(modelInterface.getId());
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
        for (ModelInterface subInterface : id2SubInterface.values()) {
            subInterface.getAllInterfaceAndRenewSon(inheritedInterfaceIdSet);
        }
    }
    
    /*************************************************************************/
    //gets
    public UmlInterface getUmlInterface() {
        return umlInterface;
    }
    
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
    
    public int getSubInterfaceNum() {
        return id2SubInterface.size();
    }
}
