package com.example.alienquest;

import java.io.IOException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
    private boolean alienDrawn;
    private ImageView alien;

    // this is the first callback method that is invoked.
    public void onCreate( Bundle state )
    {
        super.onCreate( state );
        itemList = new ArrayList<String>();
        itemAdapter = new ArrayAdapter<String>( this.getActivity(),
                android.R.layout.simple_list_item_1, itemList );
        itemList.add( 0 + " , " + 0 );
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

    public void updateLocation( double latitude, double longitude )
    {
        userLat = latitude;
        userLong = longitude;
        itemList.set( 0, "User Loc: " + userLong + " , " + userLat );
        itemAdapter.notifyDataSetChanged();
        /*
         * if(isAlienNearby()) { placeAlienOnScreen(); }
         */
    }

    public void insertAlienLocation( double latitude, double longitude )
    {
        itemList.add( "Alien Loc: " + longitude + " , " + latitude );
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
    public void drawAlien( double x, double y, int shipType )
    {

        // switch (shipType)

        alien.setImageResource( R.drawable.alien_battleship1_large );
        RelativeLayout.LayoutParams alienParams =
                new RelativeLayout.LayoutParams( cameraLayout.getWidth() / 4,
                        cameraLayout.getHeight() / 4 );

        cameraLayout.addView( alien, alienParams );
        alien.bringToFront();
        alienDrawn = true;
    }

    public void hideAlien()
    {
        alien.setVisibility( View.INVISIBLE );
    }

    public void unhideAlien()
    {
        alien.setVisibility( View.VISIBLE );
    }

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

}
