import java.math.BigInteger;

public class Bottle extends ValuableStuff { // todo 要在main函数中实现输入信息
    private double capacity;
    private boolean filled;
    
    @Override
    void printInf() {
        System.out.println("The bottle's id is " + getId() + ", name is " + getName()
                + ", capacity is " + getCapacity() + ", filled is " + isFilled() + ".");
    }
    
    @Override
    BigInteger calPrice() {
        return getPrice();
    }
    
    @Override
    void useIt(Adventurer user) throws Exception {
        if (!isFilled()) {
            throw new Exception("Failed to use " + getName() + " because it is empty.");
        }
        user.setHealth(user.getHealth() + getCapacity() / 10.0);//加生命
        setFilled(false);
        //System.out.print("price is " + getPrice() + "   ");
        setPrice(getPrice().divide(new BigInteger("10")));
        System.out.println(user.getName() + " drank " + getName() +
                " and recovered " + getCapacity() / 10 + ".");
    }
    
    //构造函数
    public Bottle() {  //构造函数,默认是满的
        filled = true;
    }
    
    public Bottle(double capacity) {
        this.capacity = capacity;
        filled = true;
    }
    
    // gets and sets
    public double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
