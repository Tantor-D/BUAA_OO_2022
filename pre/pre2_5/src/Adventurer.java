import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Adventurer extends ValuableStuff { // 有个renewPrice ，需要时刻保持price是对的。
    private double health;
    private double exp;
    private double money;
    private ArrayList<ValuableStuff> valuableStuffs;
    
    @Override
    void printInf() {
        System.out.println("The adventurer's id is " + getId() + ", name is " + getName() +
                ", health is " + health + ", exp is " + exp + ", money is " + money + ".");
    }
    
    @Override
    BigInteger calPrice() { // todo 记得自动实现对price的刷新
        BigInteger totalPrice = new BigInteger("0");
        for (ValuableStuff item : valuableStuffs) {
            totalPrice = totalPrice.add(item.calPrice());
        }
        
        this.setPrice(totalPrice);
        return getPrice();
    }
    
    @Override
    void useIt(Adventurer user) {
        for (ValuableStuff item : valuableStuffs) {
            item.calPrice();
        }
        Collections.sort(valuableStuffs);
        for (ValuableStuff item : valuableStuffs) {
            try {
                item.useIt(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    void printTheValuableStuff(int valId) {
        for (ValuableStuff val : valuableStuffs) {
            if (val.getId() == valId) {
                val.printInf();
                return;
            }
        }
    }
    
    BigInteger findMaxPrice() {
        BigInteger maxPrice = new BigInteger("0");
        for (ValuableStuff val : valuableStuffs) {
            maxPrice = maxPrice.max(val.calPrice());
        }
        return maxPrice;
    }
    
    public void deleteValuableStuff(int valId) {
        for (ValuableStuff valStu : valuableStuffs) {
            if (valStu.getId() == valId) {
                valuableStuffs.remove(valStu);
                return;
            }
        }
    }
    
    // 构造函数
    public Adventurer() {
        valuableStuffs = new ArrayList<>();
        health = 100;
    }
    
    // getter and setter
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
    
    public ArrayList<ValuableStuff> getValuableStuffs() {
        return valuableStuffs;
    }
    
    public void setValuableStuffs(ArrayList<ValuableStuff> valuableStuffs) {
        this.valuableStuffs = valuableStuffs;
    }
}
