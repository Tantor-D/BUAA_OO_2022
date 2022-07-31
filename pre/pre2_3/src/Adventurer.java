import bag.equip.Equip;

import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Scanner;

//完全可以优化代码，加上一个方法叫做findTheAdventurer()
public class Adventurer {
    private int id;
    private String name;
    private long maxPrice;
    private BigInteger totalPrice;
    private ArrayList<Equip> equips;
    
    public void renewMaxPrice() {
        maxPrice = 0;
        for (Equip equ : equips) {
            if (equ.getPrice() > maxPrice) {
                maxPrice = equ.getPrice();
            }
        }
    }
    
    public void resetVal(Scanner input) {
        //System.out.println("adventurer is resetVal ing");
        //Scanner input = new Scanner(System.in);
        id = input.nextInt();
        //System.out.println("id = " + id);
        name = input.next();
        //System.out.println("name = " + name);
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
    
    public int findNumOfEquips() {
        return equips.size();
    }
    
    public void printEquInf(int equId) {
        for (Equip equ : equips) {
            if (equ.getId() == equId) {
                equ.printInf();
            }
        }
    }
    
    public Adventurer() {
        this.equips = new ArrayList<>();
        this.totalPrice = new BigInteger("0");
    }
    
    /**
     * gets and sets
     */
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
