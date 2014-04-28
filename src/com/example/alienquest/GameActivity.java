package com.example.alienquest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mapping.CampaignSetUp;
import com.example.mapping.GPSLocator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Jimmy Dagres
 * @author Garrett Moran
 * 
 * @version Mar 31, 2014
 * 
 * 
 *          This activity will display the game mode
 */
@SuppressLint( "NewApi" )
public class GameActivity extends Activity implements SensorEventListener
{
    public final static String FRAG1_TAG = "FRAG1";
    public final static String FRAG2_TAG = "FRAG2";

    private String userName_ = "";

    private int fragCounter = 0; // 0 if currently on map, 1 if on camera
    private CameraFragment cameraFragment;
    private Intent settings;

    /**
     * The google map
     */
    protected static GoogleMap mMap_;
    protected static MapFragment mMapFrag_;

    // Stores a GPS variable
    protected GPSLocator gps_ = null;

    /**
     * The current Aliens Location
     */
    private static Marker currentAlienShipsMarker_;
    private static Marker currentUserMarker_;
    private boolean alienShipsInitialized_ = false;
    private int numberOfShipsDestroyed = 0;

    // Store the width and height in pixels, this will be usefull in
    // calculations throughout the application
    private int screenWidthPixels_;
    private int screenHeightPixels_;

    // Time in ms that the game started
    private static long questStartTime_;

    /**
     * When the game first starts the user is prompted of the map where the
     * aliens are landing
     */
    private boolean userPrompted_ = false;

    // GPS location values
    protected double longitude_;
    protected double latitude_;
    protected CampaignSetUp setUp;
    private String difficulty_;
    private String gameLength_;

    // Device sensor manager
    private SensorManager mSensorManager;

    /**
     * instance of the settings task
     */
    protected static SharedPreferences preference_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );
        settings = getIntent();
        // Load the preference values
        // Set up the preference
        preference_ =
                getSharedPreferences( getString( R.string.pref_title_file ),
                        Context.MODE_PRIVATE );

        userName_ =
                preference_.getString( getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) );
        if ( 1 <= userName_.length() )
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Welcome "
                            + userName_
                            + " to the quest. The human race is counting on you"
                            + " to fend off the alien spaceships attempting to land on earth.",
                    Toast.LENGTH_LONG * 2 ).show();
        }

        getGPSLocation();

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService( SENSOR_SERVICE );

        // Start off by displaying a google map and inserting the aliens

        // Store the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );
        screenWidthPixels_ = metrics.widthPixels;
        screenHeightPixels_ = metrics.heightPixels;

        difficulty_ = preference_.getString(
                getString( R.string.pref_title_difficulty ),
                getString( R.string.pref_title_difficulty ) );

        gameLength_ = preference_.getString(
                getString( R.string.pref_title_game_length ),
                getString( R.string.pref_title_game_length ) );

        // Default the preferences if they aren't set yet
        setUp = new CampaignSetUp( this, difficulty_, gameLength_ );
        Toast.makeText(
                getApplicationContext(),
                "Difficulty is " + difficulty_ + " and length is "
                        + gameLength_, Toast.LENGTH_LONG ).show();

        displayCameraFragment();
        displayMapFragment();

        // Record the start time of the game
        questStartTime_ = System.currentTimeMillis();
    }

    /************************************* Sensor Listeners *************************************/

    @Override
    public void onSensorChanged( SensorEvent event )
    {

        // angle between the magnetic north direction
        // 0=North, 90=East, 180=South, 270=West
        float azimuth = event.values[0];

        if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
        {

        }
        else if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
        {
            // get the angle around the z-axis rotated
            float degree = Math.round( azimuth );
            Toast.makeText(
                    getApplicationContext(),
                    "Current Degrees " + degree,
                    Toast.LENGTH_SHORT ).show();
        }

    }

    @Override
    public void onAccuracyChanged( Sensor sensor, int accuracy )
    {
        // Not used currently
    }

    /**
     * Point 1 is the users current position. Point 2 is the current alien ships
     * location
     * 
     * http://stackoverflow.com/questions/9457988/bearing-from-one-coordinate-to
     * -another
     * 
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    protected static double bearing()
    {
        // currentUserMarker_
        double longitude1 = currentUserMarker_.getPosition().longitude;
        double longitude2 = currentAlienShipsMarker_.getPosition().longitude;
        double latitude1 =
                Math.toRadians( currentUserMarker_.getPosition().latitude );
        double latitude2 =
                Math.toRadians( currentAlienShipsMarker_.getPosition().latitude );
        double longDiff = Math.toRadians( longitude2 - longitude1 );
        double y = Math.sin( longDiff ) * Math.cos( latitude2 );
        double x =
                Math.cos( latitude1 ) * Math.sin( latitude2 )
                        - Math.sin( latitude1 ) * Math.cos( latitude2 )
                        * Math.cos( longDiff );

        return (Math.toDegrees( Math.atan2( y, x ) ) + 360) % 360;
    }

    /************************************* End Sensor Listeners *************************************/

    /**
     * switches the view between the camera and the map fragments
     */
    private void switchFragment()
    {
        FragmentManager fragMgr = getFragmentManager();
        FragmentTransaction xact = fragMgr.beginTransaction();
        // TODO: implement this
        if ( cameraFragment == null )
        {
            displayCameraFragment();
            fragCounter++;
        }
        else if ( fragCounter == 0 )
        {
            // remove mapfragment from view
            xact.remove( mMapFrag_ );
            xact.commit();
            fragMgr.executePendingTransactions();

            // replace with camera fragment
            xact = fragMgr.beginTransaction();
            xact.replace( R.id.mapFrame, cameraFragment, FRAG1_TAG );
            xact.addToBackStack( null );
            xact.commit();
            fragMgr.executePendingTransactions();

            // increment to 1
            fragCounter++;
        }
        else
        {
            // remove camerafragment from view
            xact.remove( cameraFragment );
            xact.commit();
            fragMgr.executePendingTransactions();

            // replace with map fragment
            xact = fragMgr.beginTransaction();
            xact.replace( R.id.mapFrame, mMapFrag_, FRAG2_TAG );
            xact.addToBackStack( null );
            xact.commit();
            fragMgr.executePendingTransactions();

            // decrement to 0
            fragCounter--;
        }
    }

    /**
     * Adds the map fragment to the view
     */
    private void displayMapFragment()
    {
        // Add the map fragment
        mMapFrag_ = new MapFragment();

        FragmentTransaction fragMan = getFragmentManager().beginTransaction();
        fragMan.add( R.id.mapFrame, mMapFrag_, FRAG2_TAG );
        fragMan.commit();

        mMap_ = mMapFrag_.getMap(); // Custom MapFragment
        setUpMapIfNeeded(); // Dynamic
    }

    /**
     * displays the camera fragment
     */
    private void displayCameraFragment()
    {
        cameraFragment = new CameraFragment();
        FragmentTransaction fragMan = getFragmentManager().beginTransaction();
        fragMan.add( R.id.mapFrame, cameraFragment, FRAG1_TAG );
        fragMan.commit();
    }

    /**
     * If the map isn't set up then initialize it, this function is important
     * cause the map needs to be initiazlied before this activities oncreate
     * then the program will crash
     */
    private void setUpMapIfNeeded()
    {
        // Is the current location updated
        if ( null != mMap_ )
        {
            centerOnCurrentLocation();
        }
        else
        {
            mMap_ = mMapFrag_.getMap();
            centerOnCurrentLocation();
        }

        // Is the ships displayed
        if ( !alienShipsInitialized_ )
        {
            initializeNextAlienShip();
        }
        else
        {
            // redrawAlienShipsOnMap(); // TODO
        }
    }

    /**
     * Tries to get the GPS location
     */
    private void getGPSLocation()
    {
        gps_ = new GPSLocator( getApplicationContext(), this );

        // check if GPS enabled
        if ( gps_.canGetLocation() )
        {
            latitude_ = gps_.getLatitude();
            longitude_ = gps_.getLongitude();
        }
        else
        {
            // There was an error getting the gps information
            gps_.showSettingsAlert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        menu.add( menu.NONE, 1, menu.NONE, "Switch View" );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle item selection
        switch ( item.getItemId() )
        {
        case 1:
            switchFragment();
            return true;
        default:
            return super.onOptionsItemSelected( item );
        }
    }

    /**
     * @return the start time of the match
     */
    public long getQuestStartTime()
    {
        return questStartTime_;
    }

    // ---- Following Methods handle camera implementation -----//

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();

        if ( null != mSensorManager )
        {
            // for the system's orientation sensor registered listeners
            mSensorManager
                    .registerListener(
                            this,
                            mSensorManager
                                    .getDefaultSensor( Sensor.TYPE_ORIENTATION ),
                            SensorManager.SENSOR_DELAY_GAME );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        if ( null != mSensorManager )
        {
            // to stop the listener and save battery
            mSensorManager.unregisterListener( this );
        }

    }

    /************************************* Map and Alien Ship Methods *************************************/

    /**
     * This function is called whenever the location of the device is changed,
     * it updates the objectives screen and the map.
     */
    public void locationChanged()
    {
        // TODO:
        cameraFragment
                .updateLocation( gps_.getLatitude(), gps_.getLongitude() );
        centerOnCurrentLocation();
    }

    /**
     * Focuses the google map on the current GPS location
     */
    private void centerOnCurrentLocation()
    {
        if ( null != mMap_ )
        {
            // Get the location first
            getGPSLocation();

            // Remove the old marker
            if ( null != currentUserMarker_ )
            {
                currentUserMarker_.remove();
            }

            // Initialize the camera position
            MapsInitializer.initialize( GameActivity.this );
            CameraPosition mCameraPosition = new CameraPosition.Builder()
                    .zoom( 16.0f ).target( gps_.getLatCurrentLongVariable() )
                    .bearing( 0 ).tilt( 0 ).build();

            currentUserMarker_ = mMap_.addMarker( new MarkerOptions()
                    .position( gps_.getLatCurrentLongVariable() )
                    .title( "Hamburg" ) );

            // Move the camera instantly to hamburg with a zoom of 15.
            mMap_.moveCamera( CameraUpdateFactory.newLatLngZoom(
                    gps_.getLatCurrentLongVariable(), 15.0f ) );
            // Zoom in, animating the camera.
            mMap_.animateCamera( CameraUpdateFactory
                    .newCameraPosition( mCameraPosition ) );
        }
    }

    /**
     * This function is called to put the alien spaceship at their appropriate
     * spots on the map. It gets the number of ships to place, and places them
     * randomly.
     * 
     * @param longitude
     * @param latitude
     * @param shipID
     *            the number of alien ship to give the label
     * @param shipType
     *            the type of alien ship
     */
    public void initializeNextAlienShip()
    {
        if ( null != mMap_ )
        {
            if ( null != currentAlienShipsMarker_ )
            {
                currentAlienShipsMarker_.remove();
            }

            // First get a random location to place the alien ship
            LatLng randomNewAlienLocation =
                    setUp.getRandomLatLongVariable( gps_.getLatitude(),
                            gps_.getLongitude() );

            currentAlienShipsMarker_ =
                    mMap_
                            .addMarker( new MarkerOptions()
                                    .position(
                                            randomNewAlienLocation )
                                    .title( "AlienShip" )
                                    // TODO specify which ship
                                    .snippet( "Ship is landing!" )
                                    .icon( BitmapDescriptorFactory
                                            .fromResource( R.drawable.alien_ship_map_marker_small ) ) );

            alienShipsInitialized_ = true;
        }
    }

    /**
     * This function clears all of the markers on the current map
     */
    private void clearMap()
    {
        mMap_.clear();
    }

    /**
     * This function is called if the ships are shuffled or shot down
     * 
     * @param shipID
     */
    public void alienShipDestroyed( int shipID )
    {
        // First remove the current marker from the map.
        currentAlienShipsMarker_.remove();

        // TODO update the completion activity with a score based on time and
        // difficulty

        // Increment the ship destroyed count
        numberOfShipsDestroyed++;

        if ( numberOfShipsDestroyed == setUp.getmNumberOfAlienShips() )
        {
            // Game is finished!
            campaignFinished();
        }
        else
        {
            // Switch the view to the map
            switchFragment();

            // Place another alien ship
            initializeNextAlienShip();
        }
    }

    /**
     * This function is called when all of the alien ships are destroyed, it
     * wraps up the campaign accordingly
     */
    private void campaignFinished()
    {

    }
}