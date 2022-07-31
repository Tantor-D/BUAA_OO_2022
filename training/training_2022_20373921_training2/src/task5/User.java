package src.task5;

public class User implements Observer {
    private final String name;
    
    public User(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String msg) {
        System.out.println(name + ": " + msg);
    }
    
    @Override
    public String getName() {
        return name;
    }
}
