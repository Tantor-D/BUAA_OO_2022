package bag.equip;

import others.Adventurer;

import java.util.Scanner;

public class EpicSword extends Sword {
    private double evolveRatio;
    
    @Override
    public void useIt(Adventurer user) throws Exception {
        super.useIt(user);
        setSharpness(getSharpness() * getEvolveRatio());
        System.out.println(getName() + "'s sharpness became " + getSharpness() + ".");
    }
    
    @Override
    public void printInf() {
        System.out.println("The epicSword's id is " + getId() + ", name is "
                + getName() + ", sharpness is " + getSharpness() +
                ", evolveRatio is " + getEvolveRatio() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        super.resetVal(equType, input);
        //Scanner input = new Scanner(System.in);
        setEvolveRatio(input.nextDouble());
    }
    
    //get and set
    public double getEvolveRatio() {
        return evolveRatio;
    }
    
    public void setEvolveRatio(double evolveRatio) {
        this.evolveRatio = evolveRatio;
    }
}
