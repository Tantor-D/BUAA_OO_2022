package bag.equip;

import others.Adventurer;

import java.util.Scanner;

public class Sword extends Equip {
    private double sharpness;
    
    @Override
    public void useIt(Adventurer user) throws Exception {
        // todo 万一生命值为负了咋办
        user.setHealth(user.getHealth() - 10);
        user.setExp(user.getExp() + 10);
        user.setMoney(user.getMoney() + getSharpness());
        System.out.println(user.getName() + " used " + getName()
                + " and earned " + getSharpness() + ".");
    }
    
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
