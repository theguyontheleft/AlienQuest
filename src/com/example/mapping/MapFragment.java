package com.example.mapping;

import com.example.alienquest.R;
import com.example.alienquest.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
