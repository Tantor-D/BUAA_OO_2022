package bag.equip;

import java.util.Scanner;

public class RareSword extends Sword {
    private double extraExpBonus;
    
    @Override
    public void printInf() {
        System.out.println("The rareSword's id is " + getId() +
                ", name is " + getName() + ", sharpness is " + getSharpness() +
                ", extraExpBonus is " + getExtraExpBonus() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        super.resetVal(equType, input);
        //Scanner input = new Scanner(System.in);
        setExtraExpBonus(input.nextDouble());
    }
    
    //gets and sets
    public double getExtraExpBonus() {
        return extraExpBonus;
    }
    
    public void setExtraExpBonus(double extraExpBonus) {
        this.extraExpBonus = extraExpBonus;
    }
}
