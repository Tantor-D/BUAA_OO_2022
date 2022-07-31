package src.task5;

public interface Observer {
    void update(String msg); //在该方法中打印msg
    
    String getName();
}
