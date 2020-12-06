package components;

import base.*;
import java.util.*;

public class NetworkDemo extends Network {
    public NetworkDemo() {
        super();
        this.hubs = new ArrayList<Hub> ();
        this.highways = new ArrayList<Highway> ();
        this.trucks = new ArrayList<Truck> ();
    }

    @Override
    public void add(Hub hub) {
        this.hubs.add(hub);
    }

    @Override
	public void add(Highway hwy) {
        this.highways.add(hwy);
    }

    @Override
    public void add(Truck truck) {
        this.trucks.add(truck);
    }

    @Override
	public void start() {
        for(Hub hub: this.hubs) {
            hub.start();
        }
        for(Truck truck: this.trucks) {
            truck.start();
        }
    }

    @Override
	public void redisplay(Display disp) {
        for(Hub hub: this.hubs) {
            hub.draw(disp);
        }
        for(Highway hwy: this.highways) {
            hwy.draw(disp);
        }
        for(Truck truck: this.trucks) {
            truck.draw(disp);
        }
    }

    @Override
	protected Hub findNearestHubForLoc(Location loc) {
        try {
            int minDistSqrd = loc.distSqrd(this.hubs.get(0).getLoc());
            Hub nearestHub = this.hubs.get(0);
            for(Hub hub: this.hubs) {
                if(loc.distSqrd(hub.getLoc()) < minDistSqrd) {
                    minDistSqrd = loc.distSqrd(hub.getLoc());
                    nearestHub = hub;
                }
            }
            return nearestHub;
        }
        catch(Exception e) {
            return null;
        }
    }

    private ArrayList<Hub> hubs;
    private ArrayList<Highway> highways;
    private ArrayList<Truck> trucks;
}
