
/*
_____MET Value  x  3.5  x  _____kg body weight  �  200 = calories burned per minute.
    Running 6 MPH: 10 METS
     Sleeping 1 MET
 */
// This is my change for testing VCS integration


package com.example.wizart.mytrips;

import android.app.ActionBar;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wizart.mytrips.ActiveAndroidDb.Trip;
import com.example.wizart.mytrips.Parse.TripP;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;

public class MapsActivity extends ActionBarActivity implements //FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,SensorEventListener
{

    private static final long ACTIVITY_RECOGNITION_INTERVAL = 2000;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    //    private static String TAG="MapsActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = "MyTrips";

    private TextView mLocationView;

    private GoogleApiClient mGoogleApiClient;

    public LocationSource.OnLocationChangedListener mListener;

    private static boolean isTimerRunning;

//    public LocationClient mLocationClient=null;

    private LocationRequest mLocationRequest;
    public Polyline routePoints=null;
    private ArrayList<LatLng> arrayRoutePoints = null;
    PolylineOptions polylineOptions;
    public Location mCurrentLocation;

    public Location mLastLocation;
    public Location mStartLocation;


    private SensorManager sensorManager=null;

    private Marker markerStartRoute=null;
    private Marker markerEndRoute=null;


    static Timer oneSecTimer= null;
    private BitmapDescriptor  markerGreen;
    private BitmapDescriptor markerRed;
    private ImageButton imageViewMap;
    private ImageButton imageButtonCamera;
    private ImageButton imageButtonStatistics;
    private TextView textViewDistance;
    private TextView textViewTimeElapsed;
    private TextView textViewpace;
    private boolean mUpdatesRequested=false;
    private LocationManager locationManager=null;

    private final static int    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    public static String elapsedTime;


    public  static int rHours;

    public  static int rSeconds;

    public static int rMinutes;


    public static TripData routeData=null;


    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;

    // route data
    public static long routeDistance=0;
    public double routeSpeed=0;
    public double routeAverageSpeed=0;
    public double routeMaxSpeed=0;
    public int routeActivity=0;
    private LocationListener mLocationListener;
    private boolean routeRecordingStarted=false;
    private Location tempLocation;
    private float tempRouteDistance=0;
    private float minRegisteringDistance=0;
    private PolylineOptions lineOptions;
    private Location mBestLastLocation;
    private String[] routesList;
    private MenuItem playMenuActionBar=null;
    private PendingIntent mPendingIntent=null;
    private Date mStartTime,mEndTime;
    private String mElapsedTime;
    private long mTimeInMove=0;
    private long mDistance=0;
    private double mCalories=0;
    private int mActivity=0;
    private double mAverageSpeed=0;
    private double mMaxSpeed;

    public long mStepsStart=0;
    public long mstepsEnd=0,mSteps=0;

    public boolean stepsFirstTime=true;
    private boolean mRequestingLocationUpdates=false;
    private String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateValuesFromBundle(savedInstanceState);

        setContentView(R.layout.activity_google_maps_mytrips);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

//        setHasOptionsMenu(true);
        setUpMapIfNeeded();

        arrayRoutePoints = new ArrayList<LatLng>();

        markerGreen= BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        markerRed= BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

        imageViewMap= (ImageButton) findViewById(R.id.imageButton1);
        imageViewMap.setOnClickListener(onClickListenerMap);
        imageButtonCamera= (ImageButton) findViewById(R.id.imageButton2);
        imageButtonCamera.setOnClickListener(onClickListenerCamera);

        imageButtonStatistics= (ImageButton) findViewById(R.id.imageButton3);
        imageButtonStatistics.setOnClickListener(onClickListenerStat);

        textViewDistance=(TextView)findViewById(R.id.textDistance);
        textViewTimeElapsed=(TextView)findViewById(R.id.textTime);
        textViewpace=(TextView)findViewById(R.id.textPace);

        mUpdatesRequested = false;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        buildGoogleApiClient();

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
//        saveToParse();
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                //setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the
            // UI to show the correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that
                // mCurrentLocationis not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(
                        LAST_UPDATED_TIME_STRING_KEY);
            }
            //updateUI();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        playMenuActionBar = menu.findItem(R.id.action_play);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Take appropriate action for each action item click
        switch (menu.getItemId()) {
            case R.id.action_play:
                ActionBar actionBar = getActionBar();
                if (!routeRecordingStarted) {
                    RemoveExistingRoute();
                    routeRecordingStarted = true;
                    routeDistance = 0;
                    startLocationUpdates();
                    StartActivityRecognition();
                    playMenuActionBar.setIcon(R.drawable.ic_action_stop);
//                    actionBar.setTitle("MyTrips(Registering)");
                    markerStartRoute = setMarker(mCurrentLocation, markerGreen, "");
                    startRouteData();

                } else {
                    routeRecordingStarted = false;
                    markerEndRoute = setMarker(mCurrentLocation, markerRed, "");
                    stopLocationUpdates();
                    playMenuActionBar.setIcon(R.drawable.ic_action_play);
//                    actionBar.setTitle("MyTrips");
                    endRouteData();
                    saveToParse();
                    saveDataToDB();
                }
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(menu);
        }
        return super.onOptionsItemSelected(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();

//        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//        AppEventsLogger.activateApp(this);
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        activityRunning = false;
//        AppEventsLogger.deactivateApp(this);
        // if you unregister the last listener, the hardware will stop detecting step events
        // sensorManager.unregisterListener(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    public void onDestroy(){
        super.onDestroy();
//        mGoogleApiClient.disconnect();
        //mPlusClient.disconnect();
        //super.onDestroy();
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        centerMapOnMyLocation();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }




    private void centerMapOnMyLocation() {

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {

            LatLng pos = null;
            pos= new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pos)      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(pos).title("Marker"));
        }
    }



/*
    @Override
    public void onConnected(Bundle dataBundle) {
        // Get first reading. Get additional location updates if necessary
        if (servicesAvailable()) {
            // Get best last location measurement meeting criteria
            mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);

            if (null == mBestReading
                    || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                    || mBestReading.getTime() < System.currentTimeMillis() - TWO_MIN) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                // Schedule a runnable to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {

                    @Override
                    public void run() {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MainActivity.this);
                    }

                }, ONE_MIN, TimeUnit.MILLISECONDS);
            }
        }
    }
*/


    protected void startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


    protected void stopLocationUpdates() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        //startLocationUpdates();

//        Intent intent = new Intent(getApplicationContext(),  ActivityRecognitionIntentService.class); // your custom ARS class
//        mPendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, ACTIVITY_RECOGNITION_INTERVAL, mPendingIntent);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }


    View.OnClickListener onClickListenerCamera= new View.OnClickListener() {

        public void onClick(final View v) {
            //imageViewMap.setImageResource(R.drawable.android3d);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }
    };


    View.OnClickListener onClickListenerStat= new View.OnClickListener() {

        public void onClick(final View v) {
            //imageViewMap.setImageResource(R.drawable.android3d);
//            Intent intent = new Intent(MapsActivity.this,RouteStatisticsActivity.class);
            Intent intent = new Intent(MapsActivity.this,TripTableActivity.class);
            startActivity(intent);
        }
    };


    View.OnClickListener onClickListenerMap = new View.OnClickListener() {

        public void onClick(final View v) {
            //imageViewMap.setImageResource(R.drawable.android3d);
            int mapType=mMap.getMapType();
            if(mapType==GoogleMap.MAP_TYPE_NORMAL) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (mapType==GoogleMap.MAP_TYPE_SATELLITE) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            } else if (mapType==GoogleMap.MAP_TYPE_HYBRID) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }

        }
    };



    private void startRouteData() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String localTime = date.format(currentLocalTime);
        routeData=new TripData(currentLocalTime);
        mStartTime=new Date();
        routeData.setStartTime(mStartTime);
        routeData.setsStartTime(mStartTime);
        textViewDistance.setText("  0m");
        textViewTimeElapsed.setText("00:00:00");
        textViewpace.setText("0 km/h");
        startTimer();
    }

    private void endRouteData() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        String localTime = date.format(currentLocalTime);
        mDistance=(long)routeDistance;
        mActivity=routeActivity;
//        mElapsedTime=elapsedTime;
        mEndTime=currentLocalTime;
        String strElTime=dateDifference(mStartTime,mEndTime);
        mElapsedTime=strElTime;

        routeData.setDistance(routeDistance);
        routeData.setActivity(routeActivity);
        routeData.setElapsedTime(elapsedTime);
        routeData.setEndTime(currentLocalTime);
        routeData.setsEndTime(new String(localTime));
        oneSecTimer.cancel();
//        writeRouteToFile();
    }


    protected void startTimer() {
        isTimerRunning = true;
        rSeconds=0;
        rMinutes=0;
        rHours=0;
        oneSecTimer = new Timer();
        oneSecTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(TAG, "Timer tick !");
                        rSeconds++;
                        if (rSeconds > 59) {
                            rMinutes++;
                            rSeconds = 0;
                            if (rMinutes > 59) {
                                rMinutes = 0;
                                rHours++;
                            }
                        }
                        String str = String.format("%02d:%02d:%02d", rHours, rMinutes, rSeconds);
                        textViewTimeElapsed.setText(str);
                    }

                });
            }

        }, 0, 1000);

    }

    public void activate(LocationSource.OnLocationChangedListener listener){
        Log.d(TAG, "activate");
        mListener = listener;
    }

    public void deactivate(){
        Log.d(TAG, "de-activate");
        mListener = null;
    }




    @Override
    public void onLocationChanged(Location location) {
        float tempDist = 0;
        String msg="";

        if (servicesAvailable()) {
            // Get best last location measurement meeting criteria
            mBestLastLocation= bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);

            if (null == mBestLastLocation
                    || mBestLastLocation.getAccuracy() > MIN_LAST_READ_ACCURACY
                    || mBestLastLocation.getTime() < System.currentTimeMillis() - TWO_MIN) {

            return;
            }
        }
        mCurrentLocation=location; //mBestLastLocation

        msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());



        if(mListener != null){
            mListener.onLocationChanged(location);
        }

        float accuracy=location.getAccuracy();
        double speed= location.getSpeed();
        String msgs = "speed : " +Double.toString(speed);
        Log.d(TAG, msgs);
        String sSpeed=formatSpeed(speed);

        textViewpace.setText(sSpeed);

        msg="acuracy:  "+ Float.toString(accuracy)+"  meters";
        Log.d(TAG, msg);


        if(!routeRecordingStarted) return;
        float dist=location.distanceTo(mLastLocation);

        tempRouteDistance = tempRouteDistance + dist;
        if(tempRouteDistance>minRegisteringDistance) {
            tempLocation=location;
            tempDist=location.distanceTo(tempLocation);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);
            LatLng mapPoint = new LatLng(location.getLatitude(), location.getLongitude());
            arrayRoutePoints.add(mapPoint);
            polylineOptions.addAll(arrayRoutePoints);
            mMap.addPolyline(polylineOptions);

            //CameraPosition. a = googleMap.getCameraPosition();

            //routeDistance+=distFrom(lastLocation, location);
            routeDistance+=tempRouteDistance;
            ActionBar actionBar = getActionBar();
            String sDist= formatDistance(routeDistance);

            textViewDistance.setText("  "+sDist);
            //actionBar.setTitle("MyTrips-"+ sDist);

            msg=" tempDistance:  "+ Float.toString(tempDist)+"  meters beetween last temp ";
            Log.d(TAG, msg);

            tempRouteDistance = 0;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapPoint));
        }
        msg=" distance:  "+ Float.toString(routeDistance)+"  meters";
        Log.d(TAG, msg);
        msg=" tempRouteDistance:  "+ Float.toString(tempRouteDistance)+"  meters";
        Log.d(TAG, msg);

        mLastLocation=location;

        // Read more: http://www.androidhub4you.com/2013/07/draw-polyline-in-google-map-version-2.html#ixzz2orucMqqW


        //		LatLng mapPoint = new LatLng(location.getLatitude(), location.getLongitude());
        //		List<LatLng> points = routePoints.getSerPoints();
        //		points.add(mapPoint);
        //
        //		Polyline route = googleMap.addPolyline(new PolylineOptions());
        //		routePoints.setSerPoints(routePoints);


        // Zoom in the Google Map
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        //		googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

    }


    private void initializeDraw() {
        lineOptions = new PolylineOptions().width(5).color(Color.RED);
        routePoints= mMap.addPolyline(lineOptions);
    }



    public static String formatDistance(long distance) {
        String res = null;
        String unit = "m";
        //		distance /= 1000;
        //		unit = "km";
        //
        //		res=String.format("%4.2f%s", distance, unit);

        if (distance < 1000) {
            //			distance *= 1000;
            unit = "m";
            int d=(int)distance;
            res = String.format("%d %s", d, unit);
        } else if (distance >= 1000) {
            distance /= 1000;
            unit = "km";
            res=String.format("%4.2f %s", distance, unit);
        }

        return res;
    }


    public static String formatSpeed(double speed) {
        String res = null;
        String unit = "km/h";

        double sp=(3600*speed)/1000;

        res=String.format("%.2f %s", sp, unit);
        return res;
    }


    public double getDistance( ArrayList<LatLng> pointsList) {
        //double distance1 = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerB.getPosition());
        double distance2 = SphericalUtil.computeLength(pointsList);
        return distance2;

    }

    public String getDistanceString( ArrayList<LatLng> pointsList) {
        //double distance1 = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerB.getPosition());
        double distance = SphericalUtil.computeLength(pointsList);
        String sDist= formatDistance((long) distance);
        return sDist;
    }

    public double getDistanceMarker( Marker mark1, Marker mark2) {
        double distance = SphericalUtil.computeDistanceBetween(mark1.getPosition(), mark2.getPosition());
        //        double distance2 = SphericalUtil.computeLength(pointsList);
        return distance;

    }

    public String getDistanceMarkerString( Marker mark1, Marker mark2) {
        double distance = SphericalUtil.computeDistanceBetween(mark1.getPosition(), mark2.getPosition());
        String sDist= formatDistance((long) distance);
        return sDist;

    }


    public Marker setMarker(Location loc, BitmapDescriptor icon, String title){
        MarkerOptions marker=new MarkerOptions();
        LatLng mapPoint = new LatLng(loc.getLatitude(), loc.getLongitude());
        marker.position(mapPoint);
        marker.icon(icon);
        if(title!="") marker.title(title);
        Marker mark=mMap.addMarker(marker);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mapPoint, 17);
        mMap.animateCamera(cameraUpdate);
        return mark;
    }


    public void RemoveExistingRoute() {
        if(markerStartRoute!=null)
        {
            markerStartRoute.remove();
            markerStartRoute=null;
        }
        if(markerEndRoute!=null) {
            markerEndRoute.remove();
            markerEndRoute=null;
        }
        if(routePoints!=null) {
            routePoints.remove();
            routePoints=null;
        }
//        googleMap.clear();
    }

    private boolean writeRouteToFile() {
        Log.d(TAG, "writeRouteToFile");
        String filename = "mt"+System.currentTimeMillis();
        //    	String filename = "tttwb_game"+"_"+dt.toString();

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try{
            File root = Environment.getExternalStorageDirectory();

            if( root.canWrite() ){
                Log.d(TAG, "writeRouteToFile- can write to root: "+ root);
                File mtDirectory = new File(root+"/MyTrips/");
                Log.d(TAG, "writeRouteToFile- mtDirectory: "+ root+"/MyTrips/"+ " filename: "+filename);
                if(!mtDirectory.exists()){
                    mtDirectory.mkdirs();
                }

                File outputFile = new File(mtDirectory, filename);
                fos = new FileOutputStream(outputFile);
                out = new ObjectOutputStream( fos );
                out.writeObject(routeData);
                out.close();
                return true;
            }else{
                return false;
            }
        }catch( IOException e ){
            e.printStackTrace();
            Log.e(TAG, "exception "+e.getMessage());
            return false;
        }


        //		String fileName="";
        //
        //		if(null == FileName)
        //			throw new RuntimeException ("FileName is null!");
        //
        //		File myfile = getFileStreamPath(fileName);
        //		try {
        //			if(myfile.exists() || myfile.createNewFile()){
        //				FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
        //				ObjectOutputStream oos = new ObjectOutputStream(fos);
        //				oos.writeObject(aList);
        //				fos.close();
        //			}
        //		} catch (Exception e) {
        //			e.printStackTrace();
        //		}
        //
    }

    private ArrayList<LatLng> readRouteFromFile(ArrayList<LatLng> aList, String fileName) {
        if(null == fileName)
            return null;


        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        File root = Environment.getExternalStorageDirectory();

        if( root.canRead() ){
            File mytripsDirectory = new File(root+"/MyTrips/");
            if(!mytripsDirectory.exists()){
                return null;
            }


            File myfile = getFileStreamPath(fileName);
            try {
                if(myfile.exists()){
                    FileInputStream fis = openFileInput(fileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    aList= (ArrayList<LatLng>) ois.readObject();
                    fis.close();
                }else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return aList;
        }
        return null;
    }


    public String[] readStoredRoutes(Context ctx ) throws IOException, ClassNotFoundException{

        Date dt = new Date();
        int day= dt.getDay();
        int month= dt.getMonth();
        int year = dt.getYear();
        int hours = dt.getHours();
        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();
        long milis;
        String mt="mt";
        String[] list =null;
        Date myDate; //= new Date(milis);
        String curTime = hours + ":"+minutes + ":"+ seconds;

        //            String myStringDate = DateFormat.getDateInstance().format(new myDate(milis));

        //        	String filename = "tttwb_game"+System.currentTimeMillis();
        String filename = "mt"+"_"+dt.toString();

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }



        FileInputStream fos = null;
        ObjectInputStream in = null;

        File root = Environment.getExternalStorageDirectory();

        if( root.canRead() ){
            File mtDirectory = new File(root+"/MyTrips/");
            if(!mtDirectory.exists()){
                return null;
            }
            File[] FileList=mtDirectory.listFiles();
            int s =FileList.length;
            list = new String[s];
            routesList= new String[s];
            for(int i=0;i<s;i++) {

                String fname=FileList[i].getName();


                filename=FileList[i].getName();

                //        			File inputFile = new File(tttwbDirectory, filename);
                fos = new FileInputStream(FileList[i]);
                in = new ObjectInputStream( fos );
                //    				GameMoves = (TGameMove)in.readObject();

                try {
                    routeData= (TripData)in.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    Log.e(TAG,"DESERIALIZATION FAILED (CLASS NOT FOUND):"+e.getMessage());
                    e.printStackTrace();
                    return null;
                }
                catch (IOException e) {
                    android.util.Log.e(TAG,"DESERIALIZATION FAILED (IO EXCEPTION):"+e.getMessage());
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }



                String ss=fname.substring(mt.length(),fname.length());
                list[i]=ss;
                Date d=new Date((long)Long.parseLong(ss));
                routesList[i]=d.toLocaleString();
                //					list[i]=d.toLocaleString()+"  ("+Integer.toString(ListGameMoves.size())+")";
                String winner=null;
                //list[i]=d.toLocaleString()+"\n Moves: "+Integer.toString(GameMoves.NumberOfMoves)+"  " +winner;
                in.close();
            }
            return list;
        }
        else{
            return null;
        }
    }


    private Location bestLastKnownLocation(float minAccuracy, long minTime) {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Get the best most recent location currently available
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mCurrentLocation != null) {
            float accuracy = mCurrentLocation.getAccuracy();
            long time = mCurrentLocation.getTime();

            if (accuracy < bestAccuracy) {
                bestResult = mCurrentLocation;
                bestAccuracy = accuracy;
                bestTime = time;
            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy || bestTime < minTime) {
            return null;
        }
        else {
            return bestResult;
        }
    }


    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0).show();
            return false;
        }
    }


    public void saveDataToDB(){
            Trip trip = new Trip(mDistance,mElapsedTime,mAverageSpeed,mCalories,
                    mSteps,mStartTime,mEndTime,mAverageSpeed,mMaxSpeed,mActivity);

            trip.save();

        }


    public static String dateDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String delta = String.format(
                "%s%s%s%s"
                ,(elapsedDays>0?String.format("%d  d%s "    ,elapsedDays,(elapsedDays!=1?"s":"")):"")
                ,(elapsedHours>0?String.format("%2d  h%s "  ,elapsedHours,(elapsedHours!=1?"s":" ")):"")
                ,(elapsedMinutes>0?String.format("%2d  m%s ",elapsedMinutes,(elapsedMinutes!=1?"s":" ")):"")
//                ,(elapsedSeconds>0?String.format("%2d seconds%s ",elapsedSeconds,(elapsedSeconds!=1?"s":" ")):"")
                ,String.format("%2d  s%s ",elapsedSeconds,(elapsedSeconds!=1?"s":" "))
        );


        String delta1 = String.format(
                "%s%s%s%s"
                ,(elapsedDays>0?String.format("%d d"    ,elapsedDays):"")
                ,(elapsedHours>0?String.format("%2d  h"  ,elapsedHours):"")
                ,(elapsedMinutes>0?String.format("%2d  m",elapsedMinutes):"")
                ,String.format("%2d  s",elapsedSeconds)
        );


        Log.d(TAG,"datediff delta="+delta1);
        return delta1;

    }


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation != null) {
//            mLatitudeTextView.setText(String.valueOf(mCurrentLocation.getLatitude()));
//            mLongitudeTextView.setText(String.valueOf(mCurrentLocation.getLongitude()));
//            mLastUpdateTimeTextView.setText(mLastUpdateTime);
        }
    }


    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    void StartActivityRecognition() {
//        Intent intent = new Intent(getApplicationContext(),  ActivityRecognitionIntentService.class); // your custom ARS class
//        mPendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, ACTIVITY_RECOGNITION_INTERVAL, mPendingIntent);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
// Sets the desired interval for active location updates. This interval is
// inexact. You may not receive updates at all if no location sources are available, or
// you may receive them slower than requested. You may also receive updates faster than
// requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
// Sets the fastest rate for active location updates. This interval is exact, and your
// application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long steps= (int) sensorEvent.values[0];;
        if(stepsFirstTime) {
            mStepsStart=steps;
            stepsFirstTime=false;
        }
        mSteps=steps-mStepsStart;
        if(mSteps%10==0) {
            Toast.makeText(this, "Count os steps = " + mSteps, Toast.LENGTH_LONG).show();
        }
        Log.i(TAG, "onSensorChanged   steps=" + steps + " mSteps=" + mSteps);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void saveToParse() {
//        ParseObject testObject = new ParseObject("TestObject");
        TripP tripp = new TripP(mDistance,mElapsedTime,mAverageSpeed,mCalories,
                mSteps,mStartTime,mEndTime,mAverageSpeed,mMaxSpeed,mActivity);

        tripp.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
//                    setResult(Activity.RESULT_OK);
                   Toast.makeText(getApplicationContext(), "Data saved to parse: ", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),"Error saving: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        });
    }


}
