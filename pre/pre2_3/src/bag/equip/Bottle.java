package bag.equip;

import java.util.Scanner;

public class Bottle extends Equip {
    
    private double capacity;
    private boolean filled;
    
    public Bottle() {  //构造函数,默认是满的
        filled = true;
    }
    
    @Override
    public void printInf() {
        System.out.println("The bottle's id is " + getId() + ", name is " + getName()
                + ", capacity is " + getCapacity() + ", filled is " + isFilled() + ".");
    }
    
    @Override
    public void resetVal(int equType, Scanner input) {
        setEquipType(equType);//设置type
        //Scanner input = new Scanner(System.in);
        setId(input.nextInt());
        setName(input.next());
        setPrice(input.nextLong());
        setCapacity(input.nextDouble());
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    
}
