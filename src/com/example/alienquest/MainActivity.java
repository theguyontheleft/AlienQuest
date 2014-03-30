package com.example.alienquest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mapping.GPSLocator;
import com.example.settings.SettingsPreferenceActivity;

/**
 * @author Jimmy Dagres
 * @author
 * @author
 * @author
 * 
 * @version Mar 30, 2014
 * 
 */
public class MainActivity
        extends Activity
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
     * settings menu Intent
     */
    private Intent settingsIntention;

    // GPS location values
    protected double longitude_;
    protected double latitude_;
    private GPSLocator gps_ = null;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        getGPSLocation();

        SettingsPreferenceActivity settings_ =
                new SettingsPreferenceActivity();
        settingsIntention =
                new Intent( MainActivity.this,
                        SettingsPreferenceActivity.class );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch ( item.getItemId() )
        {
        case R.id.action_settings:
            // MainActivity.this.startActivity( settingsIntention ); // Causes
            // it to crash

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
     * Try's to get the GPS location
     */
    private void getGPSLocation()
    {
        gps_ = new GPSLocator( MainActivity.this );

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

}
