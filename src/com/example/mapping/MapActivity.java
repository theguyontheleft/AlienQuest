package com.example.mapping;

import com.example.alienquest.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 16, 2014
 * 
 *          During the initial start of the game a google map is displayed and
 *          shows where the aliens are.
 * 
 */
public class MapActivity extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_map );

    }
}
