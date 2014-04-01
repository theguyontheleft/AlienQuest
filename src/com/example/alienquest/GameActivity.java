package com.example.alienquest;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mapping.MapFragment;

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
public class GameActivity
        extends Activity implements SurfaceTextureListener
{
    public final static String FRAG1_TAG = "FRAG1";
    public final static String FRAG2_TAG = "FRAG2";

    private String userName_ = "";

    private Camera mCamera;
    private TextureView mTextureView;

    private RelativeLayout mRelativeLayout;

    private MapFragment mapFragement_;

    // Store the width and height in pixels, this will be usefull in
    // calculations throughout the application
    private int screenWidthPixels_;
    private int screenHeightPixels_;

    // Time in ms that the game started
    private static long questStartTime_;

    /**
     * instance of the settings task
     */
    private static SharedPreferences preference_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        // Load the preference values
        // Set up the preference
        preference_ = getSharedPreferences(
                getString( R.string.pref_title_file ),
                Context.MODE_PRIVATE );

        userName_ =
                preference_.getString(
                        getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) );
        if ( 1 <= userName_.length() )
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Welcome "
                            + userName_
                            + " to the quest. The human race is counting on you"
                            +
                            " to fend off the alien spaceships attempting to land on earth.",
                    Toast.LENGTH_LONG * 2 ).show();
        }

        // Start off by displaying a google map and inserting the aliens

        // Store the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );
        screenWidthPixels_ = metrics.widthPixels;
        screenHeightPixels_ = metrics.heightPixels;

        displayCamera();
        // displayMapFragment();

        // Record the start time of the game
        questStartTime_ = System.currentTimeMillis();
    }

    /**
     * This method will display the bulk of the screen as the camera
     */
    private void displayCamera()
    {
        mTextureView = new TextureView( this );

        mTextureView.setSurfaceTextureListener( this );

        // mTextureView = (TextureView) findViewById( R.id.textureView1 );

        RelativeLayout.LayoutParams params;

        params =
                new RelativeLayout.LayoutParams( screenWidthPixels_,
                        screenHeightPixels_ );
        mRelativeLayout = new RelativeLayout( this );

        mRelativeLayout.addView( mTextureView, params );
        setContentView( mRelativeLayout );

    }

    /**
     * Adds the accelerometer fragment to the view
     */
    private void displayMapFragment()
    {
        mapFragement_ = new MapFragment();
        getFragmentManager().beginTransaction()
                .add( R.id.mapFrame, mapFragement_, FRAG2_TAG ).commit();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    /**
     * @return the start time of the game
     */
    public static long getQuestStartTime()
    {
        return questStartTime_;
    }

    @Override
    public void onSurfaceTextureAvailable(
            SurfaceTexture surface,
            int width,
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
    public void onSurfaceTextureSizeChanged(
            SurfaceTexture surface,
            int width,
            int height )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSurfaceTextureDestroyed( SurfaceTexture surface )
    {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated( SurfaceTexture surface )
    {
        // TODO Auto-generated method stub

    }
}