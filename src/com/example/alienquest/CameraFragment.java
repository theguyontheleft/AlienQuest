package com.example.alienquest;

import java.io.IOException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint( "NewApi" )
public class CameraFragment extends Fragment implements SurfaceTextureListener
{

    private RelativeLayout cameraLayout;
    private ListView objectives;
    private ArrayAdapter<String> itemAdapter;
    private ArrayList<String> itemList;
    private Camera mCamera;
    private TextureView mTextureView;
    private double userLat;
    private double userLong;
    static private boolean alienDrawn;
    private ImageView alien;

    // Keeps track of the number of hits the alien ship takes
    private int scarCraftDamageCounter = 0;

    // A reference to the GameActivity
    GameActivity context_;

    // The layout of the alien currently being displayed (or hidden)
    RelativeLayout.LayoutParams alienParams_;

    // Store the bundle
    private Bundle data;

    /**
     * Basic Constructor
     * 
     * @param context
     */
    public CameraFragment( GameActivity context )
    {
        context_ = context;
    }

    // this is the first callback method that is invoked.
    public void onCreate( Bundle state )
    {
        super.onCreate( state );
        itemList = new ArrayList<String>();
        itemAdapter = new ArrayAdapter<String>( this.getActivity(),
                android.R.layout.simple_list_item_1, itemList );
        itemList.add( 0 + " , " + 0 );
        data = new Bundle();
        alienDrawn = false;
    }

    // Initializing the views and listeners
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState )
    {

        // linking together the xml and the view object
        // Note that each fragment has its own layout file
        RelativeLayout view = (RelativeLayout) inflater.inflate( R.layout.
                camera_fragment, container, false );
        objectives = (ListView) view.findViewById( R.id.listView1 );
        objectives.setAdapter( itemAdapter );
        alien = new ImageView( this.getActivity() );

        alien.setOnClickListener( new View.OnClickListener()
        {
            int numberOfHitsToCrashShip = -1;

            @Override
            public void onClick( View v )
            {
                Vibrator earthShaker =
                        (Vibrator) context_
                                .getSystemService( Context.VIBRATOR_SERVICE );
                earthShaker.vibrate( 200 );

                // TODO: Implement a health bar
                scarCraftDamageCounter++;

                // be harder/easier to destroy
                switch ( context_.getShipOnRadar() )
                {
                case 0:
                    // alien_battleship1_large ); // Medium
                    numberOfHitsToCrashShip = 7;
                    break;
                case 1:
                    // alien_cruiser_carrier_large ); // Hardest
                    numberOfHitsToCrashShip = 15;
                    break;
                case 2:
                    // alien_cruiser1_large // Easiest
                    numberOfHitsToCrashShip = 5;
                    break;
                case 3:
                    // alien_cruiser1_large // Easiest
                    numberOfHitsToCrashShip = 5;
                    break;
                default:
                    numberOfHitsToCrashShip = 10;
                }

                if ( scarCraftDamageCounter >= numberOfHitsToCrashShip )
                {
                    alienShipDestroyed();
                }
            }
        } );

        mTextureView = new TextureView( this.getActivity() );
        mTextureView.setSurfaceTextureListener( this );
        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams( view.getHeight(),
                view.getWidth() );
        cameraLayout = (RelativeLayout) view.findViewById( R.id.cameraLayout );
        view.addView( mTextureView );
        objectives.bringToFront();
        return view;
    }

    /**
     * @param latitude
     * @param longitude
     */
    public void updateLocation( double latitude, double longitude )
    {
        userLat = latitude;
        userLong = longitude;
        itemList.set( 0, "User Loc: " + Math.round( userLong * 1e2 ) / 1e2
                + " , " + Math.round( userLat * 1e2 ) / 1e2 );
        itemAdapter.notifyDataSetChanged();
        /*
         * if(isAlienNearby()) { placeAlienOnScreen(); }
         */
    }

    /**
     * @param latitude
     * @param longitude
     */
    public void insertAlienLocation( double latitude, double longitude )
    {
        itemList.add( "Alien Loc: " + Math.round( longitude * 1e2 ) / 1e2
                + " , " + Math.round( latitude * 1e2 ) / 1e2 );
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Draws Alien at specified coordinates
     * 
     * @param x
     *            the position of the alien ship along the x-axis view (note:
     *            use alienParams.leftMargin and/or alienParams.rightMargin to
     *            correctly place)
     * @param y
     *            the position of the alien ship along the y-axis view (note:
     *            use alienParams.topMargin and/or alienParams.bottomMargin to
     *            correctly place)
     * 
     * @param shipType
     *            Type 0: alien_battleship1_large
     * 
     *            Type 1: alien_cruiser_carrier_large
     * 
     *            Type 2: alien_cruiser1_large (Left orientation)
     * 
     *            Type 3: alien_cruiser2_large (Right orientation)
     * 
     */
    public void drawAlien( double x, double y )
    {
        // Calculate the relative difference between the current screen size
        // and the 360 degree difference
        double xRatioToMultiply =
                (double) cameraLayout.getWidth() / 360;
        x = (double) x * xRatioToMultiply;

        double yRatioToMultiply = (double) cameraLayout.getHeight() / y;
        // y = (double) y * yRatioToMultiply;

        // If the ship is drawn then it's type has already been defined
        if ( !alienDrawn )
        {

            // be harder/easier to destroy
            switch ( context_.getShipOnRadar() )
            {
            case 0:
                alien.setImageResource( R.drawable.alien_battleship1_large ); // Medium
                break;
            case 1:
                alien.setImageResource( R.drawable.alien_cruiser_carrier_large ); // Hardest
                break;
            case 2:
                alien.setImageResource( R.drawable.alien_cruiser1_large ); // Easiest
                break;
            case 3:
                alien.setImageResource( R.drawable.alien_cruiser2_large ); // Easiest
                break;
            default:
                alien.setImageResource( R.drawable.alien_cruiser_carrier_large );
            }
        }
        else
        {
            // Update the ships position on the screen so remove the old
            // position from the cameraLayout
            removeAlienFromRadar();
        }

        alienParams_ =
                new RelativeLayout.LayoutParams(
                        cameraLayout.getWidth() / 4,
                        cameraLayout.getHeight() / 4 );

        // Place the starcraft at the proper position on the screen
        if ( x < 0 )
        {
            // If it's negative take the abs of it and offset it from the right
            // side.
            alienParams_.rightMargin =
                    Math.abs( (int) (((double) cameraLayout.getWidth() / 2) + x) );
        }
        else
        {
            alienParams_.leftMargin =
                    (int) (((double) cameraLayout.getWidth() / 2) + x);
        }

        alienParams_.topMargin = cameraLayout.getHeight() / 2; // (int) y;

        Toast.makeText(
                context_.getApplicationContext(),
                "Width: " + cameraLayout.getWidth() + " Height: "
                        + cameraLayout.getHeight() + "\n" +
                        "Drawing alien of type: " + context_.getShipOnRadar()
                        + " at (" + x
                        + ", " + y
                        + ")",
                Toast.LENGTH_SHORT )
                .show();

        cameraLayout.addView( alien, alienParams_ );
        alien.bringToFront();
        alienDrawn = true;
    }

    /**
     * Removes the alien layout param from the camera fragment
     */
    public void removeAlienFromRadar()
    {
        if ( null != alien )
        {
            cameraLayout.removeView( alien );
        }
        alienDrawn = false;
    }

    /**
     * Hides the alien starcraft
     */
    public void hideAlien()
    {
        alien.setVisibility( View.INVISIBLE );
    }

    /**
     * Unhides the alien starcraft
     */
    public void unhideAlien()
    {
        alien.setVisibility( View.VISIBLE );
    }

    /**
     * @return
     */
    public boolean isAlienDrawn()
    {
        return alienDrawn;
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
        // Does nothing
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
        // Does Nothing
    }

    /**
     * This method is called when the alien ship is destroyed and handles all
     * the game transition
     */
    private void alienShipDestroyed()
    {
        Toast.makeText( // For debugging
                context_.getApplicationContext(),
                "Alien Ship destroyed!",
                Toast.LENGTH_SHORT )
                .show();

        removeAlienFromRadar();

        context_.alienShipDestroyed();

        // Reset the damage counter
        scarCraftDamageCounter = 0;
    }

    /************************************* Pausing and Resuming *************************************/
    @Override
    public void onPause()
    {
        super.onPause();
        saveData();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if ( ((GameActivity) this.getActivity()).getCameraFragmentData() != null )
        {
            if ( ((GameActivity) this.getActivity()).getCameraFragmentData()
                    .getStringArrayList( "locations" ) != null )
            {
                itemList =
                        ((GameActivity) this.getActivity())
                                .getCameraFragmentData().getStringArrayList(
                                        "locations" );
                update();
            }
        }
    }

    // updates the list and saves the state inside the activity
    public void update()
    {
        // setting up an interface for converting string array list into
        // listview items
        itemAdapter = new ArrayAdapter<String>( this.getActivity(),
                android.R.layout.simple_list_item_1, itemList );
        objectives.setAdapter( itemAdapter );
        // saving now resumed state
        saveData();
    }

    private void saveData()
    {
        if ( itemList.size() > 0 )
        {
            data.putStringArrayList( "locations", itemList );
            ((GameActivity) this.getActivity()).setCameraFragmentData( data );
        }
    }
}
