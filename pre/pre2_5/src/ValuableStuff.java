import java.math.BigInteger;

public abstract class ValuableStuff implements Comparable<ValuableStuff> {
    private int id;
    private String name;
    private BigInteger price;
    
    abstract void printInf();
    
    abstract BigInteger calPrice(); // 实际在main函数中，是不需要使用getprice的，每次使用calprice时自动实现对price的刷新
    
    abstract void useIt(Adventurer user) throws Exception;
    
    public ValuableStuff() {
        price = new BigInteger("0");
    }
    
    @Override
    public int compareTo(ValuableStuff other) {
        if (this.getPrice().min(other.getPrice()).equals(getPrice())
                && !this.getPrice().equals(other.getPrice())) {
            return 1;
        } else if (this.getPrice().max(other.getPrice()).equals(getPrice())
                && !this.getPrice().equals(other.getPrice())) {
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
    
    //getter and setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigInteger getPrice() {
        // 一定要有getPrice，这是用于装备直接获取price的。
        // 对于adventurer而言，只有calPrice这一种要求
        // 对于Equip而言，getPrice和calPrice是一个东西
        // 因为要使用这个抽象类对人和装备进行统一的调用，所以一个都不能少
        return price;
    }
    
    public void setPrice(BigInteger price) {
        this.price = price;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
