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
import android.widget.TextView;

/**
 * Contains the methods for continuous location updates
 * Created by Kaiwen on 9/26/2015.
 */
public class LocUpdater extends Activity implements GoogleApiClient.ConnectionCallbacks, LocationListener,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private double lat = 31.5; //Our default location is now the Dead Sea. Because I feel like it.
    private double lng = 35.5;
    private LocationRequest mLocationRequest = new LocationRequest();
    private int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();
        createLocationRequest();
        setContentView(R.layout.activity_loc_updater);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        TextView locationLabel = (TextView) findViewById(R.id.locationLabel);
        locationLabel.setText("{" + lat + ", " + lng + ")");
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
        TextView locationLabel = (TextView) findViewById(R.id.locationLabel);
        locationLabel.setText(locationLabel.getText() + "\n(" + location.getLatitude()
                + ", " + location.getLongitude() + ")");
        TextView counterLabel = (TextView) findViewById(R.id.counter);
        counter++;
        counterLabel.setText("" + counter);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
