package alienquest.mapping;

import java.util.Random;

import alienquest.main.R;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Jimmy Dagres, Tiffany Tuan, Garrett Moran
 * 
 * @version Apr 2, 2014
 * 
 */
public class CampaignSetUp
{

    /**
     * The number of alien ships
     */
    private int mNumberOfAlienShips;

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
    private int gameDifficulty = 1;

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
    private int gameLengthInMinutes_ = 2;

    // Used to get the random location and number of aliens
    private static Random randomNumGenerator_;

    // Stores the current or previously modified alien location
    protected Location currentAlienLocation_;

    /**
     * @return NumberOfAlienShips
     */
    public int getmNumberOfAlienShips()
    {
        return 3; // mNumberOfAlienShips; // TODO
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
     * 
     * @param context
     * @param gameLevel
     * @param gameLength
     */
    public CampaignSetUp( Context context, String gameLevel, String gameLength )
    {
        initializePreferences( context, gameLevel, gameLength );
        initializeNumberOfAliens();
    }

    private void initializePreferences( Context context,
            String currentGameLevel_, String currentGameLength_ )
    {
        if ( currentGameLevel_.equals( context
                .getString( R.string.mediumDifficulty ) ) )
        {
            // The preference hasnn't been set yet, so default it to medium
            gameDifficulty = 1;
        }
        else if ( currentGameLevel_.equals( context
                .getString( R.string.easyDifficulty ) ) )
        {
            // Easy
            gameDifficulty = 0;
        }
        else if ( currentGameLevel_.equals( context
                .getString( R.string.hardDifficulty ) ) )
        {
            // Hard
            gameDifficulty = 2;
        }
        else if ( currentGameLevel_.equals( context
                .getString( R.string.extremeDifficulty ) ) )
        {
            // Extreme
            gameDifficulty = 3;
        }

        if ( currentGameLength_.equals( context
                .getString( R.string.speedRound ) ) )
        {
            gameLengthInMinutes_ = 0;
        }
        else if ( currentGameLength_.equals( context
                .getString( R.string.shortLength ) ) )
        {
            // Easy
            gameLengthInMinutes_ = 1;
        }
        else if ( currentGameLength_.equals( context
                .getString( R.string.mediumLength ) ) )
        {
            // Hard
            gameLengthInMinutes_ = 2;
        }
        else if ( currentGameLength_.equals( context
                .getString( R.string.longLength ) ) )
        {
            // Extreme
            gameLengthInMinutes_ = 3;
        }

        Log.w( "Level", "" + gameDifficulty );
        Log.i( "Game length ", "" + gameLengthInMinutes_ );

        // gameLengthInMinutes_ = currentGameLengthSetting;
    }

    /**
     * There are two main things taken into account here. The first is the time
     * of the campaign and the second is the selected difficulty.
     * 
     */
    private void initializeNumberOfAliens()
    {
        randomNumGenerator_ = new Random();

        switch ( gameDifficulty )
        {
        case 0:

            /**
             * EASY
             * 
             * 5 minutes: 1 - 5
             * 
             * 10 minutes: 3 - 8
             * 
             * 15 minutes: 5 - 12
             * 
             * 30 minutes: 10 - 15
             * 
             */
            if ( gameLengthInMinutes_ == 0 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 4 ) + 1;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 3;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 7 ) + 5;
            }
            else
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 10;
            }
            break;
        case 1:
            /**
             * MEDIUM
             * 
             * 5 minutes: 3 - 8
             * 
             * 10 minutes: 5 - 10
             * 
             * 15 minutes: 7 - 12
             * 
             * 30 minutes: 9 - 15
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 3;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 5;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 7;
            }
            else
            {
                mNumberOfAlienShips = randomNumGenerator_.nextInt( 5 ) + 9;
            }
            break;
        case 2:

            /**
             * HARD
             * 
             * 5 minutes: 5 - 14
             * 
             * 10 minutes: 10 - 19
             * 
             * 15 minutes: 15 - 24
             * 
             * 30 minutes: 30 - 39
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 5;

            }
            else if ( gameLengthInMinutes_ == 1 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 10;

            }
            else if ( gameLengthInMinutes_ == 2 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 15;

            }
            else
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 30;

            }
            break;
        case 3:
            /**
             * EXTREME
             * 
             * 5 minutes: 15 - 24
             * 
             * 10 minutes: 30 - 39
             * 
             * 15 minutes: 45 - 54
             * 
             * 30 minutes: 90 - 99
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 15;

            }
            else if ( gameLengthInMinutes_ == 1 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 30;

            }
            else if ( gameLengthInMinutes_ == 2 )
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 45;

            }
            else
            {

                mNumberOfAlienShips = randomNumGenerator_.nextInt( 10 ) + 90;

            }
            break;
        default:
            break;
        }
        Log.i( "Random int is ", "" + mNumberOfAlienShips );
        setmNumberOfAlienShips( mNumberOfAlienShips );
    }

    /**
     * The radius is randomly determined based off of the difficulty and time
     * length
     * 
     * @return the random radius in meters
     */
    private double getRandomAlienShipRadius()
    {
        double radiusInMeters;
        switch ( gameDifficulty )
        {
        case 0:

            /**
             * EASY
             * 
             * 5 minutes: 20 - 80
             * 
             * 10 minutes: 30 - 90
             * 
             * 15 minutes: 40 - 100
             * 
             * 30 minutes: 50 - 110
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {
                radiusInMeters = randomInRange( 0.0, 60.0 ) + 20;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                radiusInMeters = randomInRange( 0.0, 60.0 ) + 30;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                radiusInMeters = randomInRange( 0.0, 60.0 ) + 40;
            }
            else
            {
                radiusInMeters = randomInRange( 0.0, 60.0 ) + 50;
            }
            break;
        case 1:
            /**
             * MEDIUM
             * 
             * 5 minutes: 20 - 100
             * 
             * 10 minutes: 35 - 115
             * 
             * 15 minutes: 50 - 130
             * 
             * 30 minutes: 75 - 155
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {
                radiusInMeters = randomInRange( 0.0, 80.0 ) + 20;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                radiusInMeters = randomInRange( 0.0, 80.0 ) + 35;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                radiusInMeters = randomInRange( 0.0, 80.0 ) + 50;
            }
            else
            {
                radiusInMeters = randomInRange( 0.0, 80.0 ) + 75;
            }
            break;
        case 2:

            /**
             * HARD
             * 
             * 5 minutes: 25 - 125
             * 
             * 10 minutes: 50 - 150
             * 
             * 15 minutes: 75 - 175
             * 
             * 30 minutes: 150 - 250
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {
                radiusInMeters = randomInRange( 0.0, 100.0 ) + 25;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                radiusInMeters = randomInRange( 0.0, 100.0 ) + 50;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                radiusInMeters = randomInRange( 0.0, 100.0 ) + 75;
            }
            else
            {
                radiusInMeters = randomInRange( 0.0, 100.0 ) + 150;
            }
            break;
        case 3:
            /**
             * EXTREME GORGIO's VACATION!!! TODO
             * 
             * 
             * 5 minutes: 25 - 135
             * 
             * 10 minutes: 50 - 160
             * 
             * 15 minutes: 75 - 185
             * 
             * 30 minutes: 150 - 260
             * 
             */

            if ( gameLengthInMinutes_ == 0 )
            {
                radiusInMeters = randomInRange( 0.0, 110.0 ) + 25;
            }
            else if ( gameLengthInMinutes_ == 1 )
            {
                radiusInMeters = randomInRange( 0.0, 110.0 ) + 50;
            }
            else if ( gameLengthInMinutes_ == 2 )
            {
                radiusInMeters = randomInRange( 0.0, 110.0 ) + 75;
            }
            else
            {
                radiusInMeters = randomInRange( 0.0, 110.0 ) + 150;
            }
            break;
        default:
            radiusInMeters = -1;
            break;
        }
        return radiusInMeters;
    }

    /**
     * This sets the currentAlienLocation_ = to a random location
     * 
     * @param latitude_
     * @param longitude_
     * @param radius
     */
    private void setRandomAlienLocation( double latitude_, double longitude_ )
    {
        double radius = getRandomAlienShipRadius();
        if ( -1 != radius )
        {
            radius = getRandomAlienShipRadius();
        }

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = randomNumGenerator_.nextDouble();
        double v = randomNumGenerator_.nextDouble();
        double w = radiusInDegrees * Math.sqrt( u );
        double t = 2 * Math.PI * v;
        double x = w * Math.cos( t );
        double y = w * Math.sin( t );

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos( latitude_ );

        double alienLongitude = new_x + longitude_;
        double alienLatitude = y + latitude_;

        currentAlienLocation_ = new Location( "GPS" );

        currentAlienLocation_.setLongitude( alienLongitude );
        currentAlienLocation_.setLatitude( alienLatitude );

    }

    /**
     * @param latitude_
     * @param longitude_
     * @return the latLong of random alien location
     */
    public LatLng
            getRandomLatLongVariable( double latitude_, double longitude_ )
    {
        setRandomAlienLocation( latitude_, longitude_ );

        return new LatLng( currentAlienLocation_.getLatitude(),
                currentAlienLocation_.getLongitude() );
    }

    /**
     * The standard java getDouble, does not have range specifications so this
     * function generates a random double within the specified range
     * 
     * @param min
     * @param max
     * @return a random double inside the range
     */
    public double randomInRange( double min, double max )
    {
        double range = max - min;
        double scaled = randomNumGenerator_.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }

    /**
     * Type 0: alien_battleship1_large
     * 
     * Type 1: alien_cruiser_carrier_large
     * 
     * Type 2: alien_cruiser1_large (Left orientation)
     * 
     * Type 3: alien_cruiser2_large (Right orientation)
     * 
     * TODO: need to implement this
     * 
     * @return the type in the form of an integer
     */
    public int getTypeOfAlienCraft()
    {
        int shipType = randomNumGenerator_.nextInt( 3 );
        return shipType;
    }

    public int getDifficulty()
    {
        return gameDifficulty;
    }

    public int getGameLength()
    {
        return gameLengthInMinutes_;
    }
}
