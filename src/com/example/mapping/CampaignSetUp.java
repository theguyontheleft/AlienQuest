package com.example.mapping;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.alienquest.GameActivity;
import com.example.alienquest.R;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 2, 2014
 * 
 */
public class CampaignSetUp extends GameActivity
{
    private static int mNumberOfAlienShips;

    /**
     * There are currently four difficulty options
     * 
     * 0 = easyDifficulty
     * 
     * 1 = mediumDifficulty (Default)
     * 
     * 2 = hardDifficulty
     * 
     * 3 = extremeDifficulty
     */
    private static int gameDifficulty = 1;

    /**
     * There are currently four campaign length options
     * 
     * 0 = speedRound (5 minutes)
     * 
     * 1 = shortLength (10 minutes)
     * 
     * 2 = mediumLength (15 minutes) (Default)
     * 
     * 3 = longLength (30 minutes)
     */
    private static int gameLengthInMinutes_ = 2;

    private static Random randomNumberGenerator_;

    /**
     * @return NumberOfAlienShips
     */
    public int getmNumberOfAlienShips()
    {
        return mNumberOfAlienShips;
    }

    /**
     * @param mNumberOfAlienShips
     */
    public void setmNumberOfAlienShips( int mNumberOfAlienShips )
    {
        this.mNumberOfAlienShips = mNumberOfAlienShips;
    }

    /**
     * Look at the preference setting and store the values locally.
     */
    private CampaignSetUp()
    {

    }

    private void intializePreferences( SharedPreferences prefernces )
    {
        // Set the difficulty variable
        String currentDifficultySetting = prefernces.getString(
                getString( R.string.pref_title_difficulty ),
                getString( R.string.pref_title_difficulty ) );

        if ( currentDifficultySetting
                .contentEquals( getString( R.string.pref_title_difficulty ) )
                || currentDifficultySetting
                        .contentEquals( getString( R.string.mediumDifficulty ) ) )
        {
            // The preference hasnn't been set yet, so default it to medium
            gameDifficulty = 1;
        }
        else if ( currentDifficultySetting
                .contentEquals( getString( R.string.easyDifficulty ) ) )
        {
            // Easy
            gameDifficulty = 0;
        }
        else if ( currentDifficultySetting
                .contentEquals( getString( R.string.hardDifficulty ) ) )
        {
            // Hard
            gameDifficulty = 2;
        }
        else if ( currentDifficultySetting
                .contentEquals( getString( R.string.extremeDifficulty ) ) )
        {
            // Extreme
            gameDifficulty = 3;
        }

        
        
        // Set the difficulty variable 
        String currentGameLengthSetting = prefernces.getString(
                getString( R.string.pref_title_game_length ),
                getString( R.string.pref_title_game_length ) );

        if ( currentGameLengthSetting
                .contentEquals( getString( R.string.pref_title_game_length ) )
                || currentDifficultySetting
                        .contentEquals( getString( R.string.mediumLength ) ) )
        {
            // The preference hasnn't been set yet, so default it to medium
            gameLengthInMinutes_ = 1;
        }
        else if ( currentGameLengthSetting
                .contentEquals( getString( R.string.speedRound ) ) )
        {
            // Easy
            gameLengthInMinutes_ = 0;
        }
        else if ( currentGameLengthSetting
                .contentEquals( getString( R.string.shortLength ) ) )
        {
            // Hard
            gameLengthInMinutes_ = 2;
        }
        else if ( currentGameLengthSetting
                .contentEquals( getString( R.string.longLength ) ) )
        {
            // Extreme
            gameLengthInMinutes_ = 3;
        }
    }

    /**
     * There are two main things taken into account here. The first is the time
     * of the campaign and the second is the selected difficulty.
     * 
     * @return the number of aliens created
     */
    private int initializeTheNumberOfAliens()
    {
        return -1;
    }
}
