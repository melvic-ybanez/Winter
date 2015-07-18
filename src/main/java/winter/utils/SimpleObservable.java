package winter.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybamelcash on 7/18/2015.
 */
public class SimpleObservable implements Observable {
    private List<Observer> observers;
    
    public SimpleObservable() {
        observers = new ArrayList<>();
    }
        
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
