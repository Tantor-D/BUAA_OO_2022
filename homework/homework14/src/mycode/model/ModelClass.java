package mycode.model;

import com.oocourse.uml2.models.common.Visibility;
import mycode.EnumsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
/*
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
*/

public class ModelClass {
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    
    private ModelClass faClass;
    private String faClassId;
    
    private final HashMap<String, ModelClass> id2SubClass;
    private final HashMap<String, ModelOperation> id2Operation; // 直接实现的方法，不包括继承的
    private final HashMap<String, ModelInterface> id2DirectInterface; // 存的是直接实现的接口
    private final HashMap<String, ModelAttribute> id2Attribute;
    private final HashMap<String, String> name2OperationId;
    
    private final HashSet<String> realizedInterfaceIdSet; // 包括了通过继承间接实现的接口
    private final ArrayList<String> realizedInterfaceNameList;
    // 虽然最后询问的是接口的name，但是不能用hashSet存名字，可能出现接口同名的情况，id保证是唯一的
    
    private int couplingOfAttribute;
    private int depthOfInheritance;
    private final HashSet<String> referencedAttributeIdSet;
    
    private static final String DUPLICATED = "duplicated";
    
    public ModelClass(String name, String id, String parentId, Visibility visibility) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        faClass = null;
        faClassId = null;
        id2Attribute = new HashMap<>();
        id2SubClass = new HashMap<>();
        id2Operation = new HashMap<>();
        id2DirectInterface = new HashMap<>();
        name2OperationId = new HashMap<>();
        
        realizedInterfaceIdSet = new HashSet<>();
        realizedInterfaceNameList = new ArrayList<>();
        
        depthOfInheritance = 0;
        couplingOfAttribute = 0;
        referencedAttributeIdSet = new HashSet<>();
    }
    
    public void setInheritanceDepthAndRenewSonDepth(int depth) {
        depthOfInheritance = depth;
        for (ModelClass modelClass : id2SubClass.values()) {
            modelClass.setInheritanceDepthAndRenewSonDepth(depth + 1);
        }
    }
    
    public void addSubClass(ModelClass modelClass) {
        id2SubClass.put(modelClass.getId(), modelClass);
    }
    
    public void addFaClass(ModelClass modelClass) {
        faClassId = modelClass.getId();
        faClass = modelClass;
    }
    
    public void addRealizedInterface(ModelInterface modelInterface) {
        id2DirectInterface.put(modelInterface.getId(), modelInterface);
        realizedInterfaceIdSet.add(modelInterface.getId());
    }
    
    public void addOperation(ModelOperation modelOperation) {
        id2Operation.put(modelOperation.getId(), modelOperation);
        if (name2OperationId.containsKey(modelOperation.getName())) {
            name2OperationId.put(modelOperation.getName(), DUPLICATED);
        } else {
            name2OperationId.put(modelOperation.getName(), modelOperation.getId());
        }
    }
    
    public void addAttribute(ModelAttribute modelAttribute) {
        id2Attribute.put(modelAttribute.getId(), modelAttribute);
        if (modelAttribute.getRealType() == EnumsType.referenceType) {
            referencedAttributeIdSet.add(modelAttribute.getId());
        }
    }
    
    public void getAllAttributeAndRenewSon(
            HashSet<String> faAttributes, HashMap<String, ModelAttribute> id2AllAttribute) {
        // 得到所有的attribute
        referencedAttributeIdSet.addAll(faAttributes); // 注意使用addAll时，其对象不可为null，否则抛异常
        
        // 计算coupling。先把所有引用的对象给得出来，然后直接去size
        HashSet<String> referencedId = new HashSet<>();
        for (String attributeId : referencedAttributeIdSet) {
            if (!id2AllAttribute.get(attributeId).getNamedTypeOrReferencedId().equals(id)) {
                // 引用reference所引用的不是当前这个类
                //System.out.println("nowClassName :" + name + ", thisAttributeId :" + attributeId);
                referencedId.add(id2AllAttribute.get(attributeId).getNamedTypeOrReferencedId());
            }
        }
        couplingOfAttribute = referencedId.size();
        
        // 子类也要计算
        for (ModelClass subClass : id2SubClass.values()) {
            subClass.getAllAttributeAndRenewSon(referencedAttributeIdSet, id2AllAttribute);
        }
    }
    
    public List<Integer> getClassOperationCouplingDegree(String methodName) {
        List<Integer> retList = new ArrayList<>();
        for (ModelOperation op : id2Operation.values()) {
            if (op.getName().equals(methodName)) {
                retList.add(op.getCoupling());
            }
        }
        return retList;
    }
    
    public void getAllInterfaceAndRenewSon(
            HashSet<String> faInterface, HashMap<String, ModelInterface> id2AllInterface) {
        // 首先从直接实现的接口那里得到那个接口所继承的所有接口
        for (ModelInterface modelInterface : id2DirectInterface.values()) {
            realizedInterfaceIdSet.addAll(modelInterface.getInheritedInterfaceIdSet());
        }
        
        // 然后得到父类实现的所有接口，此时所有被实现的接口都被找到
        realizedInterfaceIdSet.addAll(faInterface);
        
        // 得到实现的接口的名称列表
        for (String interfaceId : realizedInterfaceIdSet) {
            realizedInterfaceNameList.add(id2AllInterface.get(interfaceId).getName());
        }
        
        // 刷新子类的接口情况
        for (ModelClass subClass : id2SubClass.values()) {
            subClass.getAllInterfaceAndRenewSon(realizedInterfaceIdSet, id2AllInterface);
        }
    }
    
    ////////////////////////////////////////////////////////
    // gets
    public int getSubClassCount() {
        return id2SubClass.size();
    }
    
    public int getClassOperationCount() { // 不包括继承自父类的操作
        return id2Operation.size(); // 无需考虑重复操作带来的影响。若有多个操作为重复操作，则这些操作都需要分别计入答案。
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(String methodName) {
        Map<Visibility, Integer> map = new HashMap<>();
        map.put(Visibility.PUBLIC, 0);
        map.put(Visibility.PROTECTED, 0);
        map.put(Visibility.PRIVATE, 0);
        map.put(Visibility.PACKAGE, 0);
        for (ModelOperation op : id2Operation.values()) {
            if (op.getName().equals(methodName)) {
                int prev = map.get(op.getVisibility());
                map.put(op.getVisibility(), prev + 1);
            }
        }
        return map;
    }
    
    public boolean judgeMethodWrongType(String methodName) {
        for (ModelOperation op : id2Operation.values()) {
            if (op.getName().equals(methodName) && op.isHasWrongType()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean judgeMethodDuplicated(String methodName) { // 这个方法用于判断是否存在重复函数
        ArrayList<ModelOperation> operations = new ArrayList<>();
        for (ModelOperation op : id2Operation.values()) {
            if (op.getName().equals(methodName)) {
                operations.add(op);
            }
        }
        
        for (int i = 0; i < operations.size(); i++) {
            for (int j = i + 1; j < operations.size(); j++) {
                if (operations.get(i).judgeHasSameParameterList(operations.get(j))) {
                    return true;
                }
            }
        }
        return false;
        
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public String getFaClassId() {
        return faClassId;
    }
    
    public int getDepthOfInheritance() {
        return depthOfInheritance;
    }
    
    public int getCouplingOfAttribute() {
        return couplingOfAttribute;
    }
    
    public ArrayList<String> getRealizedInterfaceNameList() {
        return realizedInterfaceNameList;
    }
}
