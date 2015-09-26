package hackgt.directionapp;

import android.location.Location;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Ojan on 9/26/2015.
 */
public class WaypointQueue {
    Queue<Location> pointQueue;
    Location current, previous;

    public WaypointQueue() {
        pointQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Advances the queue.
     * @return  the waypoint that was just reached.
     */
    public Location goToNextWaypoint() {
        previous = current;
        current = pointQueue.remove();
        return current;
    }

    /**
     * Determines if the given location is within 1/2500 of a degree of
     * the next waypoint.
     * @param location      the query location.
     * @return              true if the location is within the waypoint; false otherwise.
     */
    public boolean hasReachedNextWaypoint(Location location) {
        double distance = Math.sqrt(Math.pow(current.getLatitude() - location.getLatitude(), 2)
                + Math.pow(current.getLongitude() - location.getLongitude(), 2));
        return (distance < (1 / 2500));
    }

    /**
     * Determines if all waypoints are reached.
     * @return  true if all waypoints are reached (queue is empty); false otherwise;
     */
    public boolean finishedPath() {
        return pointQueue.isEmpty();
    }
}
