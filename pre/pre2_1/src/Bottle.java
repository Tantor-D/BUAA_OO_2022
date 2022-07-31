import java.util.Scanner;

public class Bottle {
    
    private int id;
    private String name;
    private long price;
    private double capacity;
    private boolean filled;
    
    public Bottle() {  //构造函数
        filled = true;
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getPrice() {
        return price;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        Bottle myBottle = new Bottle();
        myBottle.setId(input.nextInt());
        myBottle.setName(input.next());
        myBottle.setPrice(input.nextLong());
        myBottle.setCapacity(input.nextDouble());
        int m = input.nextInt();
        
        for (int i = 0; i < m; i++) {
            int op = input.nextInt();
            if (op == 1) {
                System.out.println(myBottle.getName());
            }
            else if (op == 2) {
                System.out.println(myBottle.getPrice());
            }
            else if (op == 3) {
                System.out.println(myBottle.getCapacity());
            }
            else if (op == 4) {
                System.out.println(myBottle.isFilled());
            }
            else if (op == 5) {
                int newPrice = input.nextInt();
                myBottle.setPrice(newPrice);
            }
            else if (op == 6) {
                boolean newFilled = input.nextBoolean();
                myBottle.setFilled(newFilled);
            }
            else if (op == 7) {
                System.out.print("The bottle's id is " + myBottle.getId());
                System.out.print(", name is " + myBottle.getName() + ", capacity is ");
                System.out.print(myBottle.getCapacity() + ", filled is " + myBottle.isFilled());
                System.out.println(".");
            }
        }
        
    }
    
}
