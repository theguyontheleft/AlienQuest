package com.example.alienquest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Jimmy Dagres
 * 
 * @version Mar 31, 2014
 * 
 *          This fragment tells the users a little about us and who we are.
 *          ALIENS!
 * 
 */
public class AboutFragment extends Fragment
{
    // this is the first callback method that is invoked.
    @Override
    public void onCreate( Bundle state )
    {
        super.onCreate( state );
    }

    // this is where the GUI is initialized
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState )
    {

        View view = inflater.inflate( R.layout.dialog_instructions,
                container, false );
        return view;
    }

}
