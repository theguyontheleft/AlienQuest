package com.example.mapping;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alienquest.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Jimmy Dagres
 * @author
 * @author
 * @author
 * 
 * @version Mar 30, 2014
 * 
 */
public class MapFragmentClass
        extends MapFragment
{
    private GPSLocator gps_ = null;

    private/**
            * This arrayList will store the markets
            */
    ArrayList<MarkerOptions> alienShips_;

    // this is the first callback method that is invoked.
    @Override
    public void onCreate( Bundle state )
    {
        super.onCreate( state );
        alienShips_ = new ArrayList<MarkerOptions>();

    }

    // this is where the GUI is initialized
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.id.mapFrame,
                container, false );
      

    
        return view;
    }

    /**
     * @param numberOfAlienShips
     */
    public void createAliens( int numberOfAlienShips )
    {
        for ( int j = 0; j < numberOfAlienShips; j++ )
        {
            // TODO create the marker and add it to the map
        }
    }

    /**
     * This function is called to put the alien spaceship at their appropriate
     * spots on the map
     * 
     * @param longitude
     * @param latitude
     * @param shipID
     *            the number of alien ship to give the label
     * @param shipType
     *            the type of alien ship
     */
    public void markAlienShipAtLocation( double longitude, double latitude,
            int shipID, String shipType )
    {
        // // Uses a custom icon.
        MarkerOptions newMarker =
                new MarkerOptions()
                        .title( shipType + " " + shipID )
                        .icon( BitmapDescriptorFactory
                                .fromResource( R.drawable.alien_ship_map_marker_large ) );

        // Add the marker to the map and the array list
        // mMap_.addMarker( newMarker ); TODO
        alienShips_.add( newMarker );
    }

    /**
     * This function is called if the ships are shuffled or shot down
     * 
     * @param shipID
     */
    public void alienShipDestroyed( int shipID )
    {
        // TODO
    }

    /**
     * Removes all of the items from the arrayList of markers and the map
     */
    public void removeMarkersOnMap()
    {
        // mMap_.setMap( null ); TODO

        // First erase from the map

        // Empty the array of markers
    }

}
