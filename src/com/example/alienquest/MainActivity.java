package com.example.alienquest;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
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

    public final static String FRAG1_TAG = "FRAG1";
    public final static String FRAG2_TAG = "FRAG2";
    public final static String FRAG3_TAG = "FRAG3";
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

    // references to the fragments
    private InstructionsFragment instructionsFragment_;
    private AboutFragment aboutFragment_;

    // GPS location values
    protected double longitude_;
    protected double latitude_;
    private GPSLocator gps_ = null;

    // Button instances
    private Button startGameButton_;
    private Button instructionsButton_;
    private Button aboutGameButton_;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        getGPSLocation();

        settingsIntention =
                new Intent( MainActivity.this,
                        SettingsPreferenceActivity.class );
        startGameIntention =
                new Intent( MainActivity.this, GameActivity.class );

        // Set up the start game, instructions and about buttons.
        startGameButton_ = (Button) findViewById( R.id.startGameButton );
        instructionsButton_ = (Button) findViewById( R.id.instructionsButton );
        aboutGameButton_ = (Button) findViewById( R.id.aboutButton );

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
                // Vibrate the device on a successful shake
                Vibrator earthShaker =
                        (Vibrator) getSystemService( Context.VIBRATOR_SERVICE );
                earthShaker.vibrate( 200 );

                // To remove the fragment
                // getFragmentManager().beginTransaction().remove(
                // instructionsFragment_ )
                // .commit();

                displayInstructionsFragment();

            }
        } );

        // Display about fragment
        aboutGameButton_.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                displayAboutFragment();
            }
        } );

        // restoring references when the Activity is restoring itself
        if ( savedInstanceState != null )
        {

            instructionsFragment_ =
                    (InstructionsFragment) getFragmentManager()
                            .findFragmentByTag(
                                    FRAG1_TAG );
            aboutFragment_ =
                    (AboutFragment) getFragmentManager().findFragmentByTag(
                            FRAG2_TAG );
        }
    }

    /**
     * Adds the instructions fragment to the view
     */
    private void displayInstructionsFragment()
    {
        instructionsFragment_ = new InstructionsFragment();
        getFragmentManager()
                .beginTransaction()
                .add( R.id.mainActivityFragmentFrame1, instructionsFragment_,
                        FRAG1_TAG ).commit();
    }

    /**
     * Adds the about fragment to the view
     */
    private void displayAboutFragment()
    {
        // createGui();
        // aboutFragment_ = new AboutFragment(); TODO
        // getFragmentManager().beginTransaction()
        // .add( R.id.frame2, aboutFragment_, FRAG2_TAG ).commit();
    }

    /**
     * @return the view of a new GUI
     */
    private View createGui()
    {
        LinearLayout layout = new LinearLayout( this );
        //
        // layout.setOrientation( LinearLayout.HORIZONTAL );
        // layout.setLayoutParams( new LinearLayout.LayoutParams(
        // AbsListView.LayoutParams.FILL_PARENT,
        // AbsListView.LayoutParams.FILL_PARENT ) );
        //
        // LinearLayout innerLayout1 = new LinearLayout( this );
        // innerLayout1.setLayoutParams( new LinearLayout.LayoutParams( 300,
        // ViewGroup.LayoutParams.FILL_PARENT ) );
        // innerLayout1.setTag( FRAG1_TAG );
        // {
        // FragmentTransaction fragmentTransaction =
        // getFragmentManager().beginTransaction();
        // fragmentTransaction.add( FRAG1_TAG, new ItemsList() );
        // fragmentTransaction.commit();
        // }
        // layout.addView( innerLayout1 );
        //
        // LinearLayout innerLayout2 = new LinearLayout( this );
        // innerLayout2.setLayoutParams( new LinearLayout.LayoutParams( 300,
        // ViewGroup.LayoutParams.FILL_PARENT ) );
        // innerLayout2.setTag( FRAG1_TAG );
        // {
        // FragmentTransaction fragmentTransaction =
        // getFragmentManager().beginTransaction();
        // fragmentTransaction.add( FRAG1_TAG, new ItemDetails() );
        // fragmentTransaction.commit();
        // }
        // layout.addView( innerLayout2 );

        return layout;
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
