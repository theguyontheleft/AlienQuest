package alienquest.mapping;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;

/**
 * @author Jimmy Dagres
 * @author
 * @author
 * @author
 * 
 * @version Mar 30, 2014
 * 
 */
public class MapFragmentClass extends Fragment
{
    /**
     * This arrayList will store the markets
     */
    private ArrayList<Marker> alienShips_;

    // this is the first callback method that is invoked.
    public void onCreate( Bundle state )
    {
        super.onCreate( state );
        alienShips_ = new ArrayList<Marker>();

    }

    /**
     * @param numberOfAlienShips
     */
    public void createAliens( int numberOfAlienShips )
    {
        /*
        for ( int j = 0; j < numberOfAlienShips; j++ )
        {
            Marker newAlienShip =
                    mMap_
                            .addMarker( new MarkerOptions()
                                    .position(
                                            setUp.getRandomLatLongVariable() )
                                    .title( "AlienShip " + j )
                                    .snippet( "Ship " + j + " is landing!" )
                                    .icon( BitmapDescriptorFactory
                                            .fromResource( R.drawable.alien_ship_map_marker_small ) ) );
            alienShips_.add( newAlienShip );
        }*/
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

        // TODO is this needed?

        // // // Uses a custom icon.
        // MarkerOptions newMarker =
        // new MarkerOptions()
        // .title( shipType + " " + shipID )
        // .icon( BitmapDescriptorFactory
        // .fromResource( R.drawable.alien_ship_map_marker_large ) );
        //
        // // Add the marker to the map and the array list
        // // mMap_.addMarker( newMarker ); TODO
        // alienShips_.add( newMarker );
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
