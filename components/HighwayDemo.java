package components;

import base.*;
import java.util.*;

public class HighwayDemo extends Highway {

    public HighwayDemo() {
        super();
        this.trucks = new ArrayList<Truck> ();
    }

    @Override
    public synchronized boolean hasCapacity() {
        if(this.trucks.size() < this.getCapacity()) {
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean add(Truck truck) {  // adds truck to highway if it doesn't already exist
        if(this.hasCapacity()) {
            if(this.trucks.contains(truck)){
                return true;
            }
            this.trucks.add(truck);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void remove(Truck truck) {
        this.trucks.remove(truck);
    }

    private ArrayList<Truck> trucks;
}
