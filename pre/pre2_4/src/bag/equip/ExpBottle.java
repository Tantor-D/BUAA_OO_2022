package bag.equip;

import others.Adventurer;

import java.util.Scanner;

public class ExpBottle extends Bottle {
    private double expRatio;
    
    @Override
    public void useIt(Adventurer user) throws Exception {
        super.useIt(user);
        user.setExp(user.getExp() * getExpRatio());
        System.out.println(user.getName() + "'s exp became " + user.getExp() + ".");
    }
    
    @Override
    public void printInf() {
        System.out.println("The expBottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", expRatio is " + getExpRatio() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        super.resetVal(equType, input);
        //Scanner input = new Scanner(System.in);
        setExpRatio(input.nextDouble());
    }
    
    public double getExpRatio() {
        return expRatio;
    }
    
    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }
}
