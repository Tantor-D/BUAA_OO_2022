package others;

import bag.equip.Equip;
import java.util.Collections;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Scanner;

//完全可以优化代码，加上一个方法叫做findTheAdventurer()
public class Adventurer {
    private int id;
    private String name;
    private long maxPrice;
    private double health;
    private double exp;
    private double money;
    private BigInteger totalPrice;
    private ArrayList<Equip> equips;
    // todo adventures 的 totalPrice 还有 maxPrice 改成问一次就刷新一次，因为现在瓶子要时常改参数
    
    public void useAllEquip() {
        Collections.sort(equips);
        /*for (Equip equ : equips) {
            System.out.println("   " + equ.getPrice());
        }*/
        for (Equip equ : equips) {
            try {
                equ.useIt(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void printAllInf() {
        System.out.println("The adventurer's id is " + getId() +
                ", name is " + getName() + ", health is " + getHealth() +
                ", exp is " + getExp() + ", money is " + getMoney() + ".");
    }
    
    public void renewTotalPrice() {
        setTotalPrice(new BigInteger("0"));
        for (Equip equ : equips) {
            BigInteger needToAdd = new BigInteger(String.valueOf(equ.getPrice()));
            setTotalPrice(totalPrice.add(needToAdd));
        }
    }
    
    public void renewMaxPrice() {
        maxPrice = 0;
        for (Equip equ : equips) {
            if (equ.getPrice() > maxPrice) {
                maxPrice = equ.getPrice();
            }
        }
    }
    
    public void resetVal(Scanner input) {
        id = input.nextInt();
        name = input.next();
    }
    
    public void addEquip(Equip equ) {
        //加到列表中
        equips.add(equ);
        //维护totalPrice
        BigInteger priceNeedAdd = new BigInteger(String.valueOf(equ.getPrice()));
        totalPrice = totalPrice.add(priceNeedAdd);
        //维护MaxPrice
        if (equ.getPrice() > maxPrice) {
            maxPrice = equ.getPrice();
        }
    }
    
    public void deleteEquip(int equId) {
        for (Equip equ : equips) {
            if (equ.getId() == equId) { // 找到要删的这个了
                //维护totalPrice，减去它
                BigInteger priceNeedSub = new BigInteger(String.valueOf(equ.getPrice()));
                totalPrice = totalPrice.subtract(priceNeedSub);
                //删去这个装备
                equips.remove(equ);
                break;
            }
            //删完后维护MaxPrice
            renewMaxPrice();
        }
    }
    
    public int calNumOfEquips() {
        return equips.size();
    }
    
    public void printEquInf(int equId) {
        for (Equip equ : equips) {
            if (equ.getId() == equId) {
                equ.printInf();
            }
        }
    }
    
    public void addHealth(double added) {
        health = health + added;
    }
    
    public Adventurer() {
        health = 100.0;
        this.equips = new ArrayList<>();
        this.totalPrice = new BigInteger("0");
    }
    
    /**
     * gets and sets
     */
    public double getHealth() {
        return health;
    }
    
    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getExp() {
        return exp;
    }
    
    public void setExp(double exp) {
        this.exp = exp;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    public ArrayList<Equip> getEquips() {
        return equips;
    }
    
    public void setEquips(ArrayList<Equip> equips) {
        this.equips = equips;
    }
    
    public BigInteger getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigInteger totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }
    
}
