package components;
import base.*;

public class TruckDemo extends Truck {

    public TruckDemo() {
        super();
        this.timePassed = 0;
        this.currentHwy = null;
        this.inHub = false;
        // this.truckId = this.id;
        // this.id++;
    }

    @Override
    public String getTruckName() {
        // return "Truck19069" + this.truckId;
        return "Truck19069";
	}

    // if truck is on a highway, cuurentHwy is that highway
    // if truck is in a hub, currentHwy is the highway it came from
    // if truck is on an access road or in the first hub, currentHwy is null
    @Override
    public Hub getLastHub() {
        if(this.currentHwy != null) {
            return this.currentHwy.getStart();
        }
        return null;
    }

    // notifies truck that it has left a hub and entered highway
    @Override
	public void enter(Highway hwy) {
        this.currentHwy = hwy;
        this.inHub = false;
    }

    @Override
    protected void update(int deltaT) {
        this.timePassed += deltaT;
        if(this.timePassed < this.getStartTime()) {     // if the time passed from start is less than startTime, does nothing
            return;
        }
        if(this.getLoc().getX() == this.getDest().getX() && this.getLoc().getY() == this.getDest().getY()) {    // if truck has reached destHub
            return;
        }

        if(this.currentHwy == null) {       // if truck is on an access road or in the first hub

            Hub firstHub = Network.getNearestHub(this.getSource());
            Hub destHub = Network.getNearestHub(this.getDest());

            this.setLoc(firstHub.getLoc());
            if(firstHub.equals(destHub)) {  // if the first hub itself is the destination hub, truck should not be added to hub
                this.setLoc(this.getDest());
                return;
            }

            if(this.inHub == false) {       // if truck is on an access road and not in hub, it should not be added
                boolean added = firstHub.add(this);
                if(added) {
                    this.inHub = true;
                }
                return;
            }
            return;
        }


        if(this.inHub == false && this.currentHwy != null) {

            double dist = (this.currentHwy.getMaxSpeed() * deltaT) / 1000;  // distance the truck should move

            Location hwyP1 = this.currentHwy.getStart().getLoc();           // hwyP1 = start of hwy
            Location hwyP2 = this.currentHwy.getEnd().getLoc();             // hwyP2 = end of hwy
            double hwyLength = Math.sqrt(hwyP1.distSqrd(hwyP2));

            double cos = (hwyP2.getX() - hwyP1.getX()) / hwyLength;
            double sin = (hwyP2.getY() - hwyP1.getY()) / hwyLength;

            double P1toLoc = Math.sqrt(hwyP1.distSqrd(this.getLoc()));      // distance from start of hwy to current location of truck
            double computedX = (P1toLoc + dist) * cos;                      // x and y component of the total distance of new location from hwyP1
            double computedY = (P1toLoc + dist) * sin;

            int deltaX = (int) Math.round(computedX);
            int deltaY = (int) Math.round(computedY);

            if(deltaX == 0 && computedX != 0) {
                deltaX = 1;
            }
            if(deltaY == 0 && computedY != 0) {
                deltaY = 1;
            }

            int X = hwyP1.getX() + deltaX;
            int Y = hwyP1.getY() + deltaY;

            Location updated = new Location(X, Y);
            this.setLoc(updated);
            if(updated.distSqrd(hwyP1) > hwyP1.distSqrd(hwyP2)) {           // if new location crosses the end hub
                this.setLoc(hwyP2);
            }
        }

        Location endLoc = this.currentHwy.getEnd().getLoc();

        if(this.getLoc().getX() == endLoc.getX() && this.getLoc().getY() == endLoc.getY()) {    // if the truck has reached the end hub
            if(this.inHub == false) {       // if the truck in not in hub

                Hub nextHub = Network.getNearestHub(this.getLoc());
                Hub destHub = Network.getNearestHub(this.getDest());

                if(nextHub.equals(destHub)) {       // if the hub is the destination hub, truck should not be added to hub
                    if(this.currentHwy != null) {
                        this.currentHwy.remove(this);
                    }
                    this.setLoc(this.getDest());
                    return;
                }

                if(nextHub.add(this)) {             // adding truck to hub and removing it from highway
                    if(this.currentHwy != null) {
                        this.currentHwy.remove(this);
                    }
                    this.inHub = true;
                    return;
                }
            }
        }
    }

    private int timePassed;
    private Highway currentHwy;
    private boolean inHub;
    // private int truckId;
    // private static int id = 1;
}
