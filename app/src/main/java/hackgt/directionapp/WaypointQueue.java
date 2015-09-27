package hackgt.directionapp;

import android.location.Location;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Ojan on 9/26/2015.
 */
public class WaypointQueue {
    public static final int WAYPOINT_RADIUS = 20;

    Queue<Location> pointQueue;
    Location current, previous;

    public WaypointQueue() {
        pointQueue = new ArrayDeque<>();
    }

    public WaypointQueue(Queue<Location> queue) {
        pointQueue = queue;
        current = pointQueue.remove();
    }

    public void add(Location location) {
        pointQueue.add(location);
        if (pointQueue.size() == 1 && current == null)  {
            current = pointQueue.remove();
        }
    }

    /**
     * Advances the queue.
     * @return  the waypoint that was just reached.
     */
    public Location goToNextWaypoint() {
        if (pointQueue.isEmpty()) {
            current = null;
        } else {
            previous = current;
            current = pointQueue.remove();
        }
        return current;
    }

    /**
     * Determines if the given location is within 1/2500 of a degree of
     * the next waypoint.
     * @param location      the query location.
     * @return              true if the location is within the waypoint; false otherwise.
     */
    public boolean hasReachedNextWaypoint(Location location) {
        if (current != null && location != null) {
            double distance = current.distanceTo(location);
            return (distance < WAYPOINT_RADIUS);
        } else {
            return false;
        }
    }

    public Location getCurrentWaypoint() {
        return current;
    }

    /**
     * Determines if all waypoints are reached.
     * @return  true if all waypoints are reached (queue is empty); false otherwise;
     */
    public boolean finishedPath() {
        return pointQueue.isEmpty() && current == null;
    }
}
