package bag.equip;

import java.util.Scanner;

public class Sword extends Equip {
    private double sharpness;
    
    @Override
    public void printInf() {
        System.out.println("The sword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        setEquipType(equType);
        //Scanner input = new Scanner(System.in);
        setId(input.nextInt());
        setName(input.next());
        setPrice(input.nextLong());
        setSharpness(input.nextDouble());
    }
    
    public double getSharpness() {
        return sharpness;
    }
    
    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }
}
