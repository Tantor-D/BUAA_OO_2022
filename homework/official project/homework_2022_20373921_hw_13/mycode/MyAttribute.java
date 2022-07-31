package mycode;

import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;

public class MyAttribute {
    private final String name;
    private final String id;
    private final String parentId;
    private final NameableType type;
    private final Visibility visibility;
    private final EnumsType realType;
    
    public MyAttribute(
            String name, String id, String parentId, Visibility visibility, NameableType type) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.visibility = visibility;
        this.type = type;
        if (type instanceof NamedType) {
            realType = EnumsType.namedType;
        } else {
            realType = EnumsType.referenceType;
        }
    }
    
    public String getNamedTypeOrReferencedId() {
        if (realType == EnumsType.namedType) {
            return ((NamedType)type).getName();
        } else {
            return ((ReferenceType)type).getReferenceId();
        }
    }
    
    /////////////////////////////////////////////////////////////
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
    
    public NameableType getType() {
        return type;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }
    
    public EnumsType getRealType() {
        return realType;
    }
}
