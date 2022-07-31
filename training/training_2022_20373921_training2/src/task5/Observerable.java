package src.task5;

public interface Observerable {
    void addObserver(Observer observer);
    
    void removeObserver(Observer observer);
    
    void notifyObserver(String msg); //打印msg并调用每个注册过的观察者的update方法
}
