public class HealingPotion extends Bottle {
    private double efficiency;
    
    @Override
    public void useIt(Adventurer user) throws Exception {
        super.useIt(user);
        user.setHealth(user.getHealth() + getCapacity() * getEfficiency());
        System.out.println(user.getName() + " recovered extra "
                + getCapacity() * getEfficiency() + ".");
    }
    
    @Override
    public void printInf() {
        System.out.println("The healingPotion's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + isFilled() +
                ", efficiency is " + getEfficiency() + ".");
    }
    
    public HealingPotion(double capacity, double efficiency) {
        super(capacity);
        this.efficiency = efficiency;
    }
    
    public double getEfficiency() {
        return efficiency;
    }
    
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
