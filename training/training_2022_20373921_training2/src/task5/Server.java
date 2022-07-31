package src.task5;

import java.util.ArrayList;

public class Server implements Observerable {
    private final ArrayList<Observer> observers;
    
    public Server() {
        observers = new ArrayList<>();
    }
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        for (Observer obsver : observers) {
            if (obsver.getName().equals(observer.getName())) {
                observers.remove(obsver);
                return;
            }
        }
    }
    
    @Override
    public void notifyObserver(String msg) {
        System.out.println("Server: " + msg);
        for (Observer obsver : observers) {
            obsver.update(msg);
        }
    }
}
