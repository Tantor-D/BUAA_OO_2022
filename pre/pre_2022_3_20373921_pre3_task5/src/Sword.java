import java.math.BigInteger;

public class Sword extends ValuableStuff {
    private double sharpness;
    
    @Override
    void printInf() {
        System.out.println("The sword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ".");
    }
    
    @Override
    BigInteger calPrice() {
        return getPrice();
    }
    
    @Override
    void useIt(Adventurer user) throws Exception {
        user.setHealth(user.getHealth() - 10);
        user.setExp(user.getExp() + 10);
        user.setMoney(user.getMoney() + getSharpness());
        //System.out.print("price is " + getPrice() + "   ");
        System.out.println(user.getName() + " used " + getName()
                + " and earned " + getSharpness() + ".");
    }
    
    //构造函数
    public Sword(double sharpness) {
        this.sharpness = sharpness;
    }
    
    //get and sets
    public double getSharpness() {
        return sharpness;
    }
    
    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }
}
