package mycode.model;

import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import mycode.EnumsType;
import mycode.Tools;

public class ModelParameter {
    private final String name;
    private final String id;
    private final String parentId;
    private final NameableType type;
    private final Direction direction;
    private final EnumsType realType;
    
    public ModelParameter(
            String name, String id, String parentId, NameableType type, Direction direction) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.type = type;
        this.direction = direction;
        if (type instanceof NamedType) {
            realType = EnumsType.namedType;
        } else {
            realType = EnumsType.referenceType;
        }
    }
    
    public boolean judgeIsWrong() {
        if (realType == EnumsType.referenceType) {
            return false; // referenceType 题目保证没有错
        }
        String strType = ((NamedType) type).getName();
        if (direction == Direction.RETURN) {
            if (strType.equals("void")) {
                return false;
            }
            return Tools.judgeIsWrongNamedType(strType);
        } else {
            return Tools.judgeIsWrongNamedType(strType);
        }
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
    
    public NameableType getType() {
        return type;
    }
    
    public String getNamedTypeOrReferencedId() {
        if (realType == EnumsType.namedType) {
            return ((NamedType) type).getName();
        } else {
            return ((ReferenceType) type).getReferenceId();
        }
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public EnumsType getRealType() {
        return realType;
    }
    
}
