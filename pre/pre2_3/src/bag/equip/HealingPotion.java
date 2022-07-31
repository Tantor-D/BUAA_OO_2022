package bag.equip;

import java.util.Scanner;

public class HealingPotion extends Bottle {
    private double efficiency;
    
    @Override
    public void printInf() {
        System.out.println("The healingPotion's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", efficiency is " + getEfficiency() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        super.resetVal(equType, input);
        //Scanner input = new Scanner(System.in);
        setEfficiency(input.nextDouble());
    }
    
    public double getEfficiency() {
        return efficiency;
    }
    
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
