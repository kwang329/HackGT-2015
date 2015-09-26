package hackgt.directionapp;

import android.graphics.Point;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Ojan on 9/26/2015.
 */
public class WaypointQueue {
    Queue<Point> pointQueue;

    public WaypointQueue() {
        pointQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Advances the queue.
     * @return  the waypoint that was just reached.
     */
    public Point goToNextWaypoint() {
        return null;
    }

    /**
     * Determines if the given location is within the bounds for
     * the next waypoint.
     * @param location      the query location.
     * @return              true if the location is within the waypoint; flase otherwise.
     */
    public boolean hasReachedNextWaypoint(Point location) {
        return false;
    }

    /**
     * Determines if all waypoints are reached.
     * @return  true if all waypoints are reached (queue is empty); false otherwise;
     */
    public boolean finishedPath() {
        return false;
    }
}
