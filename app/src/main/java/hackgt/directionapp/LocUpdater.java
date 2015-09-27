package hackgt.directionapp;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Contains the methods for continuous location updates
 * Created by Kaiwen on 9/26/2015.
 */
public class LocUpdater extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private double lat = 31.5; //Our default location is now the Dead Sea. Because I feel like it.
    private double lng = 35.5;
    private LocationRequest mLocationRequest = new LocationRequest();
    WaypointQueue queue;
    private Location currentLocation;
    private TurnArrow arrow;

    @Override
    public void onCreate() {
        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    public void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }
        currentLocation = mLastLocation;
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    //Should be accurate enough for real time data
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
            if (queue.hasReachedNextWaypoint(location)) {
                queue.goToNextWaypoint();
            }
            if (queue.finishedPath()) {
                stopLocationRequest();
            }
            arrow.approachArrow(currentLocation);
        }
    }

    public void stopLocationRequest() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class LocationUpdaterBinder extends Binder {
        public LocUpdater getService() {
            return LocUpdater.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocationUpdaterBinder();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setWaypointQueue(WaypointQueue queue) {
        this.queue = queue;
    }

    public void setTurnArrow(TurnArrow arrow) {
        this.arrow = arrow;
    }
}
