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
    
    public ExpBottle(double capacity, double expRatio) {
        super(capacity);
        this.expRatio = expRatio;
    }
    
    //gets and sets
    public double getExpRatio() {
        return expRatio;
    }
    
    public void setExpRatio(double expRatio) {
        this.expRatio = expRatio;
    }
}
