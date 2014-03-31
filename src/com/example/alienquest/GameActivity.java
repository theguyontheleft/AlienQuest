package com.example.alienquest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.TextureView;
import android.widget.RelativeLayout;

/**
 * @author Jimmy Dagres
 *
 * @version Mar 31, 2014
 *
 *
 *          This activity will display the game mode
 */
@SuppressLint( "NewApi" )
public class GameActivity
        extends Activity
{
    private Camera mCamera;
    private TextureView mTextureView;

    private RelativeLayout mRelativeLayout;

    // Store the width and height in pixels, this will be usefull in
    // calculations throughout the application
    private int screenWidthPixels_;
    private int screenHeightPixels_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        // Load the preference values

        // Start off by displaying a google map and inserting the aliens

        // Store the screen width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );
        screenWidthPixels_ = metrics.widthPixels;
        screenHeightPixels_ = metrics.heightPixels;

        // displayCamera();
    }

    /**
     * This method will display the bulk of the screen as the camera
     */
    private void displayCamera()
    {
        mTextureView = new TextureView( this );
        // mTextureView.setSurfaceTextureListener( this );

        // mTextureView = (TextureView) findViewById( R.id.textureView1 );

        RelativeLayout.LayoutParams params;

        params =
                new RelativeLayout.LayoutParams( screenWidthPixels_,
                        screenHeightPixels_ );
        mRelativeLayout = new RelativeLayout( this );

        mRelativeLayout.addView( mTextureView, params );
        setContentView( mRelativeLayout );


    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }
}
