package winter.utils;

/**
 * Created by ybamelcash on 7/16/2015.
 */
public interface Observable {
    public void registerObserver(Observer observer);
    
    public void removeObserver(Observer observer);
    
    public void notifyObservers();
}
