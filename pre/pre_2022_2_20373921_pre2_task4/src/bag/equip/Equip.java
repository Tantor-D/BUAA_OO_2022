package bag.equip;

import others.Adventurer;

import java.util.Scanner;

public class Equip implements Comparable<Equip> {
    private int id;
    private int equipType;
    private long price;
    private String name;
    
    public void printInf() {
    
    }
    
    public void resetVal(int equType, Scanner input) {
    
    }
    
    public void useIt(Adventurer user) throws Exception {
    
    }
    
    @Override
    public int compareTo(Equip other) {
        if (this.getPrice() < other.getPrice()) {
            return 1;
        } else if (this.getPrice() > other.getPrice()) {
            return -1;
        }
        
        if (this.getId() < other.getId()) {
            return 1;
        }
        else if (this.getId() > other.getId()) {
            return -1;
        }
        
        return 0;
    }
    
    public Equip() {
    } // 构造方法
    
    //gets and sets
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
