package bag.equip;

import java.util.Scanner;

public class Equip {
    private int id;
    private int equipType;
    private long price;
    private String name;
    
    public void printInf() {
    
    }
    
    public void resetVal(int equType, Scanner input) {
    
    }
    
    public Equip() {} // 构造方法
    
    public long getPrice() {
        return price;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getEquipType() {
        return equipType;
    }
    
    public void setEquipType(int equipType) {
        this.equipType = equipType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
