package mycode;

import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;

import java.util.HashMap;
import java.util.HashSet;

// todo 方法一定有返回值吗？包括void
// todo 本次作业中参数只有in和return，迭代时要注意那个parameterSign2Times，暂时只分为return和非return
public class MyOperation {
    private final String name;
    private final String id;
    private final String parentId;
    private final Visibility visibility;
    
    private int coupling;
    private final HashMap<String, MyParameter> id2Parameter;
    private final HashMap<String, Integer> parameterSign2Times; // 注意，对于类型为return的，不要记录
    private final HashSet<String> referencedParameterId;
    private boolean hasWrongType;
    
    public MyOperation(String name, String id, String parentId, Visibility visibility) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        id2Parameter = new HashMap<>();
        hasWrongType = false;
        parameterSign2Times = new HashMap<>();
        coupling = 0;
        referencedParameterId = new HashSet<>();
    }
    
    public void renewHasWrongType() {
        for (MyParameter parameter : id2Parameter.values()) {
            if (parameter.judgeIsWrong()) { // 存在一个参数有错
                hasWrongType = true;
                return;
            }
        }
    }
    
    public void renewCoupling() {
        for (MyParameter myParameter : id2Parameter.values()) {
            if (myParameter.getRealType() == EnumsType.referenceType &&
                    !myParameter.getNamedTypeOrReferencedId().equals(parentId)) {
                // 方法的某个参数为引用类型，且引用的类不是当前方法所属的类
                referencedParameterId.add(myParameter.getNamedTypeOrReferencedId());
            }
        }
        coupling = referencedParameterId.size();
    }
    
    public boolean judgeHasSameParameterList(MyOperation op) {
        if (this.parameterSign2Times.size() != op.getParameterSign2Times().size()) {
            return false;
        }
        HashMap<String, Integer> opPara2TimeMap = op.getParameterSign2Times();
        
        for (String key : parameterSign2Times.keySet()) {
            if (opPara2TimeMap.containsKey(key)) {
                if (!(parameterSign2Times.get(key).equals(opPara2TimeMap.get(key)))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
    
    public void addParameter(MyParameter myParameter) {
        id2Parameter.put(myParameter.getId(), myParameter);
        if (myParameter.getDirection() == Direction.RETURN) { // return类型的，不计入方法的参数表中
            return;
        }
        String parameterSign = myParameter.getNamedTypeOrReferencedId();
        if (parameterSign2Times.containsKey(parameterSign)) {
            int prev = parameterSign2Times.get(parameterSign);
            parameterSign2Times.put(parameterSign, prev + 1); // 多出现了一次
        } else {
            parameterSign2Times.put(parameterSign, 1);
        }
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
    
    public HashMap<String, Integer> getParameterSign2Times() {
        return parameterSign2Times;
    }
}
