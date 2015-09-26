package hackgt.directionapp;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import android.app.Activity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

/**
 * Contains the methods for continuous location updates
 * Created by Kaiwen on 9/26/2015.
 */
public class LocUpdater extends Activity {
    private GoogleApiClient mGoogleApiClient;
    private double lat = 31.5; //Our default location is now the Dead Sea. Because I feel like it.
    private double lng = 35.5;

    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }
    }

    LocationRequest mLocationRequest = new LocationRequest();

    //Should be accurate enough for real time data
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
}
