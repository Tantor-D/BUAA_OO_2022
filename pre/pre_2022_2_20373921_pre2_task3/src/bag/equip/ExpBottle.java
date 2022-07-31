package bag.equip;

import java.util.Scanner;

public class ExpBottle extends Bottle {
    private double expRatio;
    
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
