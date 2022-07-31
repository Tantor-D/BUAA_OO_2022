import java.util.ArrayList;
import java.math.BigInteger;

//完全可以优化代码，加上一个方法叫做findTheAdventurer()
public class Adventurer {
    private int id;
    private String name;
    private long maxPrice;
    private ArrayList<Bottle> bottles;
    private BigInteger totalPrice;
    
    public void findMaxPrice() {
        maxPrice = 0;
        for (Bottle bot : bottles) {
            if (bot.getPrice() > maxPrice) {
                maxPrice = bot.getPrice();
            }
        }
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
    
    public ArrayList<Bottle> getBottles() {
        return bottles;
    }
    
    public void setBottles(ArrayList<Bottle> bottles) {
        this.bottles = bottles;
    }
    
    public long getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }
    
}
