package components;

import base.*;
import java.util.*;

public class HubDemo extends Hub {

    public HubDemo(Location loc) {
        super(loc);
        this.trucks = new ArrayList<Truck> ();
    }

    // adds truck to hub if it doesn't already exist
    @Override
    public synchronized boolean add(Truck truck) {
        if(this.trucks.size() < this.getCapacity()) {
            if(this.trucks.contains(truck)) {
                return true;
            }
            this.trucks.add(truck);
            return true;
        }
        return false;
    }

    @Override
	protected synchronized void remove(Truck truck) {
        this.trucks.remove(truck);
    }

    @Override
	public Highway getNextHighway(Hub last, Hub dest) {

        Map<Hub, Boolean> visitedMap = new HashMap<Hub, Boolean> ();    // maps hub to its visited value
        Map<Hub, Highway> highwayMap = new HashMap<Hub, Highway> ();    // maps hub to its last highway visited in DFS

        visitedMap.put(this, false);
        visitedMap.put(last, true);     // as we cannot visit last hub, it's visited value is set true

        if(DFS(this, dest, visitedMap, highwayMap)) {
            return highwayMap.get(this);
        }
        return null;
    }

    private boolean DFS(Hub current, Hub dest, Map<Hub, Boolean> visitedMap, Map<Hub, Highway> highwayMap) {
        visitedMap.put(current, true);
        if(current.equals(dest)) {      // if the dest hub has been reached, returns true
            return true;
        }
        for(Highway hwy: current.getHighways()) {
                Hub next = hwy.getEnd();
                if(!visitedMap.containsKey(next)) {     // add "next" to the list of hubs traversed
                    visitedMap.put(next, false);
                }
                if(visitedMap.get(next).equals(false)) {    // if "next" has not been visited, call DFS on that
                    highwayMap.put(current, hwy);
                    if(DFS(next, dest, visitedMap, highwayMap)) {
                        return true;
                    }
                }
        }
        return false;
    }

    @Override
    protected void processQ(int deltaT) {
        Truck truckArr[] = this.trucks.toArray(new Truck[this.trucks.size()]);
        for(Truck truck: truckArr) {

            Hub destHub = Network.getNearestHub(truck.getDest());
            Highway nextHwy = getNextHighway(truck.getLastHub(), destHub);

            if(nextHwy.hasCapacity()) {     // if the next highway has capacity, performs necessary actions
                nextHwy.add(truck);
                truck.enter(nextHwy);
                this.remove(truck);
            }
        }
    }

    private ArrayList<Truck> trucks;
}
