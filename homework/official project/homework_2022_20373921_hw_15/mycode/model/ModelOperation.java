package mycode.model;

import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import mycode.EnumsType;

import java.util.HashMap;
import java.util.HashSet;

public class ModelOperation {
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    
    private int coupling;
    private final HashMap<String, ModelParameter> id2Parameter;
    //private final HashMap<String, Integer> parameterSign2Times; // 注意，对于类型为return的，不要记录
    private final HashMap<String, Integer> namedParameter2Times;
    private final HashMap<String, Integer> refParameter2Times;
    private final HashSet<String> referencedParameterId;
    private boolean hasWrongType;
    
    public ModelOperation(String name, String id, String parentId, Visibility visibility) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        id2Parameter = new HashMap<>();
        hasWrongType = false;
        //parameterSign2Times = new HashMap<>();
        namedParameter2Times = new HashMap<>();
        refParameter2Times = new HashMap<>();
        coupling = 0;
        referencedParameterId = new HashSet<>();
    }
    
    public void renewHasWrongType() {
        for (ModelParameter parameter : id2Parameter.values()) {
            if (parameter.judgeIsWrong()) { // 存在一个参数有错
                hasWrongType = true;
                return;
            }
        }
    }
    
    public void renewCoupling() {
        for (ModelParameter modelParameter : id2Parameter.values()) {
            if (modelParameter.getRealType() == EnumsType.referenceType &&
                    !modelParameter.getNamedTypeOrReferencedId().equals(parentId)) {
                // 方法的某个参数为引用类型，且引用的类不是当前方法所属的类
                referencedParameterId.add(modelParameter.getNamedTypeOrReferencedId());
            }
        }
        coupling = referencedParameterId.size();
    }
    
    public boolean judgeHasSameParameterList(ModelOperation op) {
        if (this.namedParameter2Times.size() != op.namedParameter2Times.size() ||
                this.refParameter2Times.size() != op.refParameter2Times.size()) {
            return false;
        }
        /*if (this.parameterSign2Times.size() != op.getParameterSign2Times().size()) {
            return false;
        }*/
        HashMap<String, Integer> opNamedPara2Times = op.namedParameter2Times;
        HashMap<String, Integer> opRefPara2Times = op.refParameter2Times;
        for (String key : namedParameter2Times.keySet()) {
            if (opNamedPara2Times.containsKey(key)) {
                if (!(namedParameter2Times.get(key).equals(opNamedPara2Times.get(key)))) {
                    return false;
                }
            } else {
                return false;
            }
        }
    
        for (String key : refParameter2Times.keySet()) {
            if (opRefPara2Times.containsKey(key)) {
                if (!(refParameter2Times.get(key).equals(opRefPara2Times.get(key)))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        
        
        /*HashMap<String, Integer> opPara2TimeMap = op.getParameterSign2Times();
        for (String key : parameterSign2Times.keySet()) {
            if (opPara2TimeMap.containsKey(key)) {
                if (!(parameterSign2Times.get(key).equals(opPara2TimeMap.get(key)))) {
                    return false;
                }
            } else {
                return false;
            }
        }*/
        return true;
    }
    
    public void addParameter(ModelParameter modelParameter) {
        id2Parameter.put(modelParameter.getId(), modelParameter);
        if (modelParameter.getDirection() == Direction.RETURN) { // return类型的，不计入方法的参数表中
            return;
        }
        String parameterSign = modelParameter.getNamedTypeOrReferencedId();
        
        if (modelParameter.getRealType() == EnumsType.namedType) { // namedType
            if (namedParameter2Times.containsKey(parameterSign)) {
                int prev = namedParameter2Times.get(parameterSign);
                namedParameter2Times.put(parameterSign, prev + 1);
            } else {
                namedParameter2Times.put(parameterSign, 1);
            }
        } else if (modelParameter.getRealType() == EnumsType.referenceType) { // referenceType
            if (refParameter2Times.containsKey(parameterSign)) {
                int prev = refParameter2Times.get(parameterSign);
                refParameter2Times.put(parameterSign, prev + 1);
            } else {
                refParameter2Times.put(parameterSign, 1);
            }
        }
        /*if (parameterSign2Times.containsKey(parameterSign)) {
            int prev = parameterSign2Times.get(parameterSign);
            parameterSign2Times.put(parameterSign, prev + 1); // 多出现了一次
        } else {
            parameterSign2Times.put(parameterSign, 1);
        }*/
    }
    
    ////////////////////////////////////////////////////////////
    // gets
    
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
    
    public int getCoupling() {
        return coupling;
    }
    
    public boolean isHasWrongType() {
        return hasWrongType;
    }
    
    /*public HashMap<String, Integer> getParameterSign2Times() {
        return parameterSign2Times;
    }*/
    
}
