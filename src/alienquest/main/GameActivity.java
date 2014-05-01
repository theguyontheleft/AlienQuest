package alienquest.main;

import alienquest.mapping.CampaignSetUp;
import alienquest.mapping.GPSLocator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    private Intent settings;
    private int playerScore;

    // Stores track of the time
    private CountDownTimer timer;
    private int gameTime;

    /**
     * The google map
     */
    protected static GoogleMap mMap_;
    protected static MapFragment mMapFrag_;
    private int fragCounter = 0; // 0 if currently on map, 1 if on camera
    private static CameraFragment cameraFragment;

    // Stores the previous degree of the direction the device is facing
    private static double previousXDegree_;
    private static double previousYDegree_;

    // Stores a GPS variable
    protected GPSLocator gps_ = null;

    /**
     * The current Aliens Location
     */
    private Marker currentAlienShipsMarker_;
    private Marker currentUserMarker_;
    private boolean alienShipsInitialized_ = false;
    private int numberOfShipsDestroyed = 0;

    // Store the previous latitude and longitude values so that you can only
    // update when there's a significant change in location.
    private double storedLatitude_;
    private double storedLongitude_;

    // Stores the current ship type.
    // Type 0: alien_battleship1_large
    // Type 1: alien_cruiser_carrier_large
    // Type 2: alien_cruiser1_large (Left orientation)
    // Type 3: alien_cruiser2_large (Right orientation)
    private int alienShipType;

    // GPS location values
    protected CampaignSetUp setUp;
    private String difficulty_;
    private String gameLength_;

    // Device sensor manager
    private SensorManager mSensorManager;
    private Bundle cameraFragmentData;

    // This variable is updated from the settings and determines if there will
    // be sound in the game
    private boolean soundEnabled = false;

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

        // Set up sound
        initializeSound();

        userName_ =
                preference_.getString( getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) );

        // Initialize the bundle to keep fragments through on pause and on
        // resume
        cameraFragmentData = savedInstanceState;

        if ( 1 <= userName_.length() )
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Welcome "
                            + userName_
                            + " to the quest. The human race is counting on you"
                            + " to fend off the alien spaceships attempting to land on earth.",
                    Toast.LENGTH_SHORT ).show();
        }
        else
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Welcome to the quest. The human race is counting on you"
                            + " to fend off the alien spaceships attempting to land on earth.",
                    Toast.LENGTH_SHORT ).show();
        }

        getGPSLocation();

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService( SENSOR_SERVICE );

        // Start off by displaying a google map and inserting the aliens

        // Store the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );

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

        // Set initial score and retrieve time of session
        playerScore = 0;
        switch ( setUp.getGameLength() )
        {
        case 0:
            gameTime = 5;
            break;
        case 1:
            gameTime = 10;
            break;
        case 2:
            gameTime = 15;
            break;
        case 3:
            gameTime = 30;
            break;
        default:
            gameTime = 1;
        }
        gameTime = gameTime * 60000; // converting from minutes to milliseconds
    }

    /**
     * Gets the sound preference and initialize the variable
     */
    private void initializeSound()
    {
        String soundSring = preference_.getString(
                getString( R.string.pref_title_sound ),
                getString( R.string.pref_title_sound ) );

        soundEnabled = soundSring.toUpperCase().contains( "TRUE" );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setTimer();
    }

    /************************************* Game Time Handler *************************************/
    private void setTimer()
    {
        timer = new CountDownTimer( gameTime, 5000 )
        {
            public void onTick( long millisUntilFinished )
            {
                toastTimeRemaining( millisUntilFinished );
            }

            public void onFinish()
            {
                timesUp( false );
            }
        }.start();
    }

    /**
     * This method is mostly for debugging
     * 
     * @param millisUntilFinished
     */
    private void toastTimeRemaining( long millisUntilFinished )
    {
        /*
         * Toast.makeText(this, String.format("%d:%d remaining",
         * TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
         * TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
         * TimeUnit.MINUTES
         * .toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))),
         * 2000).show();
         */
    }

    /************************************* Sensor Listeners *************************************/

    @Override
    public void onSensorChanged( SensorEvent event )
    {

        // angle between the magnetic north direction
        // 0=North, 90=East, 180=South, 270=West
        float azimuth = event.values[0];

        // The pitch, x direction, but for landscape mode it is y
        float pitch = event.values[1];

        if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
        {
            float degree = Math.round( azimuth );
            Toast.makeText(
                    getApplicationContext(), "Type " + event.sensor.getType() +
                            " Acc Current Degrees " + degree,
                    Toast.LENGTH_SHORT ).show();
        }
        else if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
        {
            float degree = Math.round( azimuth );
            Toast.makeText(
                    getApplicationContext(), "Type " + event.sensor.getType() +
                            " Mag Current Degrees " + degree,
                    Toast.LENGTH_SHORT ).show();
        }
        else if ( event.sensor.getType() == Sensor.TYPE_ORIENTATION )
        {

            float xDegree = Math.round( azimuth );
            float yDegree = Math.round( pitch );

            // This if statement ensures it doesn't update too much when the
            // changes are minimal
            if ( Math.abs( xDegree - previousXDegree_ ) > 2
                    || Math.abs( yDegree - previousYDegree_ ) > 2 )
            {
                /*
                 * // Toast for debugging Toast.makeText(
                 * getApplicationContext(), "Bearing between two points " +
                 * bearing() + "\n Orientation Current Degrees " + xDegree,
                 * Toast.LENGTH_SHORT ).show();
                 */

                previousXDegree_ = xDegree;
                previousYDegree_ = yDegree;

                // Update the picture of the alien on the screen if the device
                // is pointing in a relatively similar direction and the ship is
                // located nearby
                double differenceInDegrees = bearing() - previousXDegree_;

                if ( Math.abs( differenceInDegrees ) < 20 ) // &&
                                                            // isAlienNearby() )
                                                            // // TODO test the
                                                            // alien nearby
                {

                    if ( fragCounter == 0 )
                    {
                        switchFragment();
                    }

                    cameraFragment.drawAlien( differenceInDegrees, yDegree );
                }
                else
                {
                    if ( cameraFragment.isAlienDrawn() )
                    {
                        // Hide the spaceship
                        cameraFragment.removeAlienFromRadar();
                    }

                }

            }
        }
    }

    /**
     * @return the type of alien ship
     */
    public int getShipOnRadar()
    {
        return alienShipType;
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
    protected double bearing()
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

        if ( cameraFragment == null )
        {
            displayCameraFragment();
            fragCounter++;
        }
        else if ( fragCounter == 0 )
        {
            // Hide mapfragment from view and show the camera
            xact = fragMgr.beginTransaction();

            // Replace destroys the fragment and all of its views
            // xact.replace( R.id.mapFrame, cameraFragment, FRAG1_TAG );

            if ( null != mMapFrag_ )
            {
                xact.hide( mMapFrag_ );
            }

            if ( null != cameraFragment )
            {
                xact.show( cameraFragment );
            }

            xact.commit();
            fragMgr.executePendingTransactions();

            // increment to 1
            fragCounter++;
        }
        else
        {
            // hide camerafragment from view show with map fragment
            xact = fragMgr.beginTransaction();

            // Replace destroys the fragment and all of its views
            // xact.replace( R.id.mapFrame, mMapFrag_, FRAG2_TAG );

            if ( null != mMapFrag_ )
            {
                xact.show( mMapFrag_ );
            }

            if ( null != cameraFragment )
            {
                xact.hide( cameraFragment );
            }

            xact.commit();
            // xact.hide( cameraFragment );
            fragMgr.executePendingTransactions();

            setUpMapIfNeeded();

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
        cameraFragment = new CameraFragment( this );
        FragmentTransaction fragMan = getFragmentManager().beginTransaction();
        fragMan.add( R.id.mapFrame, cameraFragment, FRAG1_TAG );
        fragMan.commit();
    }

    /**
     * If the map isn't set up then initialize it, this function is important
     * cause the map needs to be initialized before this activities oncreate
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
            storedLatitude_ = gps_.getLatitude();
            storedLongitude_ = gps_.getLongitude();
        }
        else
        {
            // There was an error getting the gps information
            gps_.showSettingsAlert();
        }
    }

    @SuppressWarnings( "static-access" )
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

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {
        if ( (keyCode == KeyEvent.KEYCODE_BACK) )
        {
            AlertDialog.Builder alertbox =
                    new AlertDialog.Builder( this )
                            .setTitle( "End Campaign" )
                            .setMessage(
                                    "Are you sure you want to end your campaign?" )
                            .setPositiveButton( android.R.string.yes,
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which )
                                        {
                                            // End the timer
                                            timer.cancel();

                                            // End the campaign
                                            finish();
                                        }
                                    } )
                            .setNegativeButton( android.R.string.no,
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which )
                                        {
                                            // do nothing
                                            return;
                                        }
                                    } );
            alertbox.setIcon( android.R.drawable.ic_dialog_alert );
            alertbox.show();
        }

        return super.onKeyDown( keyCode, event );

    }

    /************************************* Map and Alien Ship Methods *************************************/

    /**
     * This function is called whenever the location of the device is changed,
     * it updates the objectives screen and the map.
     */
    public void locationChanged()
    {

        cameraFragment
                .updateLocation( gps_.getLatitude(), gps_.getLongitude() );
        centerOnCurrentLocation();

        // Update the new values
        storedLatitude_ = gps_.getLatitude();
        storedLongitude_ = gps_.getLongitude();
    }

    /**
     * @return if the alien is close to the users current location
     */
    private boolean isAlienNearby()
    {
        double distance =
                Math.sqrt( Math.pow( gps_.getLongitude() -
                        currentAlienShipsMarker_.getPosition().longitude, 2 )
                        +
                        Math.pow(
                                gps_.getLatitude()
                                        - currentAlienShipsMarker_
                                                .getPosition().latitude,
                                2 ) );
        if ( distance < 1 )
        {
            return true;
        }
        return false;
    }

    /**
     * Focuses the google map on the current GPS location
     */
    private void centerOnCurrentLocation()
    {
        if ( null != mMap_ )
        {
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
                    .title( "Your Location" ) );

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
     * @param alienShipType
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
                                    .icon( BitmapDescriptorFactory
                                            .fromResource( R.drawable.alien_ship_map_marker_small ) ) );

            cameraFragment.insertAlienLocation(
                    randomNewAlienLocation.latitude,
                    randomNewAlienLocation.longitude );

            // Ship was initialized
            alienShipsInitialized_ = true;

            // Get the random ship type
            alienShipType = setUp.getTypeOfAlienCraft();

            // Set the title of the current spacecraft type
            switch ( alienShipType )
            {
            case 0:
                currentAlienShipsMarker_
                        .setSnippet( "Alien Battleship is landing!" );
                currentAlienShipsMarker_.setTitle( "Alien Battleship" );
                break;
            case 1:
                currentAlienShipsMarker_
                        .setSnippet( "Alien Starcraft Carrier is landing!" );
                currentAlienShipsMarker_.setTitle( "Alien Starcraft Carrier" );

                break;
            case 2:
                currentAlienShipsMarker_
                        .setSnippet( "Alien Cruiser is wrecking havoc!" );
                currentAlienShipsMarker_.setTitle( "Alien Cruiser" );

                break;
            case 3:
                currentAlienShipsMarker_
                        .setSnippet( "Alien Cruiser is wrecking havoc!" );
                currentAlienShipsMarker_.setTitle( "Alien Cruiser" );
                break;
            default:
                currentAlienShipsMarker_
                        .setSnippet( "This should not be here!" );
                currentAlienShipsMarker_.setTitle( "UFO?!" );
                // alien.setImageResource(
                // R.drawable.alien_cruiser_carrier_large );
            }

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
    public void alienShipDestroyed()
    {
        // Increment the ship destroyed count
        numberOfShipsDestroyed++;

        String shipType;
        // Set the title of the current spacecraft type
        switch ( alienShipType )
        {
        case 0:
            shipType = "Alien Battleship";
            break;
        case 1:
            shipType = "Alien Starcraft Carrier";
            break;
        case 2:
            shipType = "Alien Cruiser";
            break;
        case 3:
            shipType = "Alien Cruiser";
            break;
        default:
            shipType = "UNKNOWN UFO";
        }

        Toast.makeText(
                getApplicationContext(),
                shipType + " " + numberOfShipsDestroyed + " of "
                        + setUp.getmNumberOfAlienShips() +
                        " destroyed!",
                Toast.LENGTH_LONG )
                .show();

        // First remove the current marker from the map.
        currentAlienShipsMarker_.remove();
        alienShipsInitialized_ = false;

        // TODO update the completion activity with a score based on time and
        // difficulty
        updateScore();

        // Reset the alien ship type
        alienShipType = -1;

        if ( numberOfShipsDestroyed >= setUp.getmNumberOfAlienShips() )
        {
            // Game is finished!
            campaignFinished();
        }
        else
        {
            // Switch the view to the map
            if ( fragCounter == 1 )
            {
                switchFragment();
            }

            // Place another alien ship
            setUpMapIfNeeded();

            centerOnCurrentLocation();
        }
    }

    /**
     * Updates the score based on difficulty and time
     */
    private void updateScore()
    {
        int difficultyModifier = setUp.getDifficulty() + 1;
        int timeModifier;
        switch ( setUp.getGameLength() )
        {
        case 0:
            timeModifier = 5;
            break;
        case 1:
            timeModifier = 10;
            break;
        case 2:
            timeModifier = 15;
            break;
        case 3:
            timeModifier = 30;
            break;
        default:
            timeModifier = 1;
        }

        playerScore = playerScore +
                (10000 * difficultyModifier) / timeModifier;
    }

    /************************************* End Game Event Handlers *************************************/
    /**
     * This function is called when all of the alien ships are destroyed, it
     * wraps up the campaign accordingly
     */
    private void campaignFinished()
    {
        // TODO: implement this

        // Update the scores
        timesUp( true );
    }

    /**
     * This function is called when the game ends.
     */
    private void timesUp( boolean compaignSuccessful )
    {
        Intent completionActivityIntent =
                new Intent( this, CompletionActivity.class );

        // Uses the string keys to pass info to the CompletionActivity
        completionActivityIntent.putExtra( getString( R.string.key_score ),
                playerScore );
        completionActivityIntent.putExtra(
                getString( R.string.key_completed ), compaignSuccessful );

        // Pass whether or not to call the completion method
        completionActivityIntent.putExtra(
                getString( R.string.key_display_completed ), true );

        Vibrator earthShaker =
                (Vibrator) getSystemService( Context.VIBRATOR_SERVICE );
        earthShaker.vibrate( 300 );
        this.startActivity( completionActivityIntent );
    }

    /************************************* Camera Fragment Data Getter/Setter *************************************/
    /**
     * @return the bundle
     */
    public Bundle getCameraFragmentData()
    {
        return cameraFragmentData;
    }

    /**
     * @param cameraFragmentData
     */
    public void setCameraFragmentData( Bundle cameraFragmentData )
    {
        this.cameraFragmentData = cameraFragmentData;
    }

    /**
     * @return the current sound setting
     */
    public boolean getSoundSetting()
    {
        return soundEnabled;
    }
}