package com.example.mapping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alienquest.R;

/**
 * @author Jimmy Dagres
 * @author 
 * @author 
 * @author 
 * 
 * @version Mar 30, 2014
 *
 */
public class MapFragment
    extends Fragment
{
    private GPSLocator gps_ = null;

   // private GoogleMap mMap;
    
// // Uses a custom icon.
//    mSydney = mMap.addMarker(new MarkerOptions()
//        .position(SYDNEY)
//        .title("Sydney")
//        .snippet("Population: 4,627,300")
//        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
    
    // this is the first callback method that is invoked.
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }
    
    

 // this is where the GUI is initialized
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_fragment,
                container, false);
        return view;
    }
}
