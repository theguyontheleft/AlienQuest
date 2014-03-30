package com.example.alienquest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.mapping.GPSLocator;

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
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
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
