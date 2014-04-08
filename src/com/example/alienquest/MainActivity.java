package com.example.alienquest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapping.GPSLocator;
import com.example.mapping.MapFragmentClass;
import com.example.settings.SettingsPreferenceActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Jimmy Dagres
 * @author Garrett Moran
 * @author Tiffany Tuan
 * 
 * @version Mar 30, 2014
 * 
 */
public class MainActivity extends Activity
{
    /**
     * Used for the settings
     */
    public static boolean inputCorrect;

    /**
     * determines if the app is in its initial setup
     */
    private static boolean initialSetup;

    /**
     * settings menu Intent, used to access settings and preference information
     */
    private Intent settingsIntention;

    /**
     * start game intent
     */
    private Intent startGameIntention;

    /**
     * instance of the settings task
     */
    private static SharedPreferences preference_;

    // Button instances
    private Button startGameButton_;
    private Button instructionsButton_;
    private Button aboutGameButton_;

    private TextView welcomeMsg_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Typeface tf =
                Typeface.createFromAsset( getAssets(), "Fonts/Molot.otf" );

        settingsIntention = new Intent( MainActivity.this,
                SettingsPreferenceActivity.class );
        startGameIntention =
                new Intent( MainActivity.this, GameActivity.class );

        // Set up the start game, instructions and about buttons.
        startGameButton_ = (Button) findViewById( R.id.startGameButton );
        instructionsButton_ = (Button) findViewById( R.id.instructionsButton );
        aboutGameButton_ = (Button) findViewById( R.id.aboutButton );

        welcomeMsg_ = (TextView) findViewById( R.id.textView1 );

        startGameButton_.setTypeface( tf );
        instructionsButton_.setTypeface( tf );
        aboutGameButton_.setTypeface( tf );

        welcomeMsg_.setTypeface( tf );

        // Set up the preference
        preference_ =
                getSharedPreferences( getString( R.string.pref_title_file ),
                        Context.MODE_PRIVATE );

        // Start game
        startGameButton_.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                Vibrator earthShaker =
                        (Vibrator) getSystemService( Context.VIBRATOR_SERVICE );
                earthShaker.vibrate( 200 );

                MainActivity.this.startActivity( startGameIntention );
            }
        } );

        // Display instructions fragment
        instructionsButton_.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                displayInstructionsDialog();
            }
        } );

        // Display about fragment
        aboutGameButton_.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                displayAboutDialog();
            }
        } );

        // CUSTOM
        // Test the map fragment
        // mapFragment_ = new MapFragmentClass();
        //
        // FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction =
        // fragmentManager.beginTransaction();
        //
        // fragmentTransaction.add( R.id.googleMapFragment, mapFragment_ );
        // fragmentTransaction.commit();

        // mMap_ = new GoogleMap( null ); TODO

    }

    /**
     * Adds the instructions fragment to the view
     */
    private void displayInstructionsDialog()
    {
        // prepare the instructions box
        Dialog instructionsBox = new Dialog( MainActivity.this );
        instructionsBox.setContentView( R.layout.dialog_instructions );
        // set the message to display
        instructionsBox.setTitle( "Instructions Menu" );

        instructionsBox.show();
    }

    /**
     * Adds the about fragment to the view
     */
    private void displayAboutDialog()
    {
        // prepare the instructions box
        Dialog aboutBox = new Dialog( MainActivity.this );
        aboutBox.setContentView( R.layout.dialog_about );

        // set the message to display
        aboutBox.setTitle( "About Alien Quest" );

        aboutBox.show();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
        case R.id.action_settings:
            this.startActivity( settingsIntention );
            break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.settings, menu );
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected( int featureId, MenuItem item )
    {
        // TODO Auto-generated method stub
        return super.onMenuItemSelected( featureId, item );
    }

    /**
     * @return
     * @returns the preferences
     */
    public static SharedPreferences getPreference_()
    {
        return preference_;
    }
}
