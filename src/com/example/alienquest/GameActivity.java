package com.example.alienquest;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mapping.GPSLocator;
import com.example.mapping.MapFragmentClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

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
public class GameActivity extends Activity implements SurfaceTextureListener
{
    public final static String FRAG1_TAG = "FRAG1";
    public final static String FRAG2_TAG = "FRAG2";

    private String userName_ = "";

    private Camera mCamera;
    private TextureView mTextureView;

    private RelativeLayout mRelativeLayout;

    private MapFragmentClass mapFragement_;
    private ListView objectives;
    private ArrayAdapter<String> itemAdapter;
    private ArrayList<String> itemList;
    private FrameLayout objectivesFrame;

    /**
     * The google map
     */
    private GoogleMap mMap_;
    private MapFragment mMapFrag_;

    private GPSLocator gps_ = null;

    // Store the width and height in pixels, this will be usefull in
    // calculations throughout the application
    private int screenWidthPixels_;
    private int screenHeightPixels_;

    // Time in ms that the game started
    private static long questStartTime_;

    // GPS location values
    protected double longitude_;
    protected double latitude_;

    /**
     * instance of the settings task
     */
    private static SharedPreferences preference_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );
        objectives = (ListView) findViewById( R.id.objectivesFrame )
                .findViewById( R.id.listView1 );
        itemList = new ArrayList<String>();
        itemList.clear();
        itemAdapter =
                new ArrayAdapter<String>( this, R.id.listView1, itemList );
        objectives.setAdapter( itemAdapter );
        objectives.bringToFront();
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

        // Start off by displaying a google map and inserting the aliens

        // Store the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );
        screenWidthPixels_ = metrics.widthPixels;
        screenHeightPixels_ = metrics.heightPixels;

        displayCamera();
        // displayMapFragment();
        displayObjectives();

        // Record the start time of the game
        questStartTime_ = System.currentTimeMillis();
    }

    /**
     * Adds the map fragment to the view
     */
    private void displayMapFragment()
    {
        // Add the map fragment
        mMapFrag_ = new MapFragment();
        mMap_ = mMapFrag_.getMap();

        FragmentTransaction fragMan = getFragmentManager().beginTransaction();
        fragMan.add( R.id.mapFrame, mMapFrag_, FRAG2_TAG );
        fragMan.commit();

        LatLng HAMBURG = new LatLng( 53.558, 9.927 ); // TODO remove
        LatLng KIEL = new LatLng( 53.551, 9.993 );

        // Zoom in on the current location
        getGPSLocation(); // Get the location first

        // Initialize the camera position
        MapsInitializer.initialize( GameActivity.this );
        CameraPosition mCameraPosition =
                new CameraPosition.Builder()
                        .target( gps_.getLatLongVariable() ).bearing( 0 )
                        .tilt( 30 ).build();

        // TODO causes null pointer exception
        // Marker hamburg =
        // mMap_.addMarker( new MarkerOptions().position(
        // gps_.getLatLongVariable() )
        // .title( "Hamburg" ) );
        // Marker kiel = mMap_.addMarker( new MarkerOptions()
        // .position( gps_.getLatLongVariable() )
        // .title( "Kiel" )
        // .snippet( "Kiel is cool" )
        // .icon( BitmapDescriptorFactory
        // .fromResource( R.drawable.ic_launcher ) ) );

        // Move the camera instantly to hamburg with a zoom of 15.
        // mMap_.moveCamera( CameraUpdateFactory.newLatLngZoom(
        // HAMBURG, 15 ) );

        // Zoom in, animating the camera.
        // mMap_.animateCamera( CameraUpdateFactory

        // .newCameraPosition( mCameraPosition )
        // );
    }

    /**
     * Try's to get the GPS location
     */
    private void getGPSLocation()
    {
        gps_ = new GPSLocator( getApplicationContext() );

        // check if GPS enabled
        if ( gps_.canGetLocation() )
        {
            latitude_ = gps_.getLatitude();
            longitude_ = gps_.getLongitude();

            // Raises a toast to show the location
            Toast.makeText(
                    getApplicationContext(),
                    "Your Location is - \nLat: " + latitude_ + "\nLong: "
                            + longitude_, Toast.LENGTH_LONG ).show();
        }
        else
        {
            // There was an error getting the gps information
            gps_.showSettingsAlert();
        }
    }

    /**
     * displays the objectives component to the game screen
     */
    private void displayObjectives()
    {
        // TODO: change this to actually do what it should do
        itemAdapter.add( "1.......200" );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    public long getQuestStartTime()
    {
        return questStartTime_;
    }

    // ---- Following Methods handle camera implementation -----//

    /**
     * This method will display the bulk of the screen as the camera
     */
    private void displayCamera()
    {
        mTextureView = new TextureView( this );
        mTextureView.setSurfaceTextureListener( this );
        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams( screenWidthPixels_,
                screenHeightPixels_ );
        mRelativeLayout = new RelativeLayout( this );
        mRelativeLayout.addView( mTextureView, params );
        setContentView( mRelativeLayout );
    }

    @Override
    public void onSurfaceTextureAvailable( SurfaceTexture surface, int width,
            int height )
    {
        mCamera = Camera.open();
        try
        {
            mCamera.setPreviewTexture( surface );
            mCamera.startPreview();

        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged( SurfaceTexture surface,
            int width,
            int height )
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSurfaceTextureDestroyed( SurfaceTexture surface )
    {
        if ( null != mCamera )
        {
            mCamera.stopPreview();
            mCamera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated( SurfaceTexture surface )
    {
        // TODO Auto-generated method stub
    }
    // --------------------End of Section-----------------------//

}