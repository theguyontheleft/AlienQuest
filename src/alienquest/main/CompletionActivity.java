package alienquest.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jimmy Dagres
 * @author Garrett Moran
 * @author Tiffany Tuan
 * 
 * @version Apr 1, 2014
 * 
 * 
 *          I (Jimmy) chose to store the highscores in a string stored as a
 *          preference, rather than a database because I wanted to practice with
 *          the android favored data structure SparseArray's.
 */
public class CompletionActivity extends Activity
{

    private static final String PREF_NAME = "myScores";
    protected static SharedPreferences preference_;
    private Intent settings;
    private String userName_;
    private TextView highscore;
    private TextView score1;
    private TextView score2;
    private TextView score3;
    private TextView score4;
    private TextView score5;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView name4;
    private TextView name5;

    private TextView congratulationsName_;

    private String topFiveScoreValues_;
    private String topFiveScoreUsernames_;

    // Array map that contains the top 5 scores as keys, and the associated
    // usernames as values
    SparseArray<String> highscores_;

    /**
     * Delim used to separate the highscores in the preference string
     */
    static String delim = "/b";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_completion );

        settings = getIntent();

        highscores_ = new SparseArray<String>();

        int score = getScores(); // TODO: implement database of scores
        // Load the preference values
        // Set up the preference
        preference_ =
                getSharedPreferences( getString( R.string.pref_title_file ),
                        Context.MODE_PRIVATE );
        userName_ =
                preference_.getString( getString( R.string.pref_title_name ),
                        getString( R.string.pref_title_name ) );

        Typeface tf =
                Typeface.createFromAsset( getAssets(), "Fonts/Molot.otf" );
        highscore = (TextView) findViewById( R.id.textViewTopScores );
        score1 = (TextView) findViewById( R.id.textViewScore1 );
        score2 = (TextView) findViewById( R.id.TextViewScore2 );
        score3 = (TextView) findViewById( R.id.TextViewScore3 );
        score4 = (TextView) findViewById( R.id.TextViewScore4 );
        score5 = (TextView) findViewById( R.id.TextViewScore5 );
        name1 = (TextView) findViewById( R.id.textViewUserName1 );
        name2 = (TextView) findViewById( R.id.TextViewUserName2 );
        name3 = (TextView) findViewById( R.id.TextViewUserName3 );
        name4 = (TextView) findViewById( R.id.TextViewUserName4 );
        name5 = (TextView) findViewById( R.id.TextViewUserName5 );
        highscore.setTypeface( tf );
        score1.setTypeface( tf );
        score2.setTypeface( tf );
        score3.setTypeface( tf );
        score4.setTypeface( tf );
        score5.setTypeface( tf );
        name1.setTypeface( tf );
        name2.setTypeface( tf );
        name3.setTypeface( tf );
        name4.setTypeface( tf );
        name5.setTypeface( tf );
        getTopFiveScores();

        // Get the values sent in the bundle.
        Bundle b = settings.getExtras();
        if ( b != null )
        {
            int scoreTotal = Integer.parseInt( b.get(
                    getString( R.string.key_score ) ).toString() );
            boolean gameComplete = Boolean.parseBoolean( b.get(
                    getString( R.string.key_completed ) ).toString() );
            boolean callCompletedMethod = Boolean.parseBoolean( b.get(
                    getString( R.string.key_display_completed ) ).toString() );

            if ( callCompletedMethod )
            {
                gameEnded( scoreTotal, gameComplete );
            }

        }
    }

    /**
     * @param score
     * @param completed
     *            whether or not the game was successful beat
     */
    public void gameEnded( int score, boolean completed )
    {
        if ( completed )
        {
            congratulationsName_ =
                    (TextView) findViewById( R.id.textViewCongradulations );
            String nameMsg =
                    getString( R.string.congratulations_name, userName_ );

            nameMsg =
                    "Congratulations "
                            + userName_
                            +
                            " you completed the quest! Earth thanks you. \n Your score was "
                            + score + ".";
            congratulationsName_.setText( nameMsg );

            // See if the new score is higher than the 5th best score, if so
            // place it into the sparse array and remove the fifth value, then
            // call a function to update the preference settings
            if ( score > highscores_.keyAt( 4 ) ) // || highscores_.ketAt(4))
            {
                highscores_.remove( highscores_.keyAt( 4 ) );
                highscores_.put( score, userName_ );
                updatedSavedHighSchores();

                saveScores( score );

                // Update the edit texts with the new top 5 scores
                displayTopFiveScores();

                Toast.makeText(
                        getApplicationContext(),
                        "Congradulations " + userName_ + "! Your score of "
                                + score + " is a top five number!",
                        Toast.LENGTH_SHORT ).show();
            }
        }
        else
        {
            // Tell the user that time ran out
            // congratulationsName_.setText(userName_);
        }
    }

    /**
     * This method will put the sparseArray into a string with the proper delim
     * format and will save it as a preference
     */
    private void updatedSavedHighSchores()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    /**
     * This method saves the scores to the string in the preferences
     */
    private void saveScores( int score )
    {
        // setting preferences
        SharedPreferences prefs = this.getSharedPreferences( "myPrefsKey",
                Context.MODE_PRIVATE );
        Editor editor = prefs.edit();
        editor.putInt( "key", score );
        editor.commit();

    }

    /**
     * Parses through the top 5 names and scores and adds them to the map
     */
    private void getTopFiveScores()
    {

        topFiveScoreValues_ = preference_.getString(
                getString( R.string.pref_title_top_five_scores ),
                getString( R.string.pref_title_top_five_scores ) );

        topFiveScoreUsernames_ = preference_.getString(
                getString( R.string.pref_title_top_five_names ),
                getString( R.string.pref_title_top_five_names ) );

        String[] arrayOfScores = topFiveScoreValues_.split( delim );
        String[] arrayOfNames = topFiveScoreUsernames_.split( delim );

        int j = 0;
        for ( ; arrayOfNames.length < j && arrayOfScores.length < j; j++ )
        {
            highscores_
                    .put( Integer.parseInt( arrayOfScores[j] ),
                            arrayOfNames[j] );
        }

        // If there isn't five scores saved then make them blank
        for ( ; j < 5; j++ )
        {
            highscores_.put( 0, "Empty" );
        }

        // Call a function method to update the edit texts with the top 5 scores
        displayTopFiveScores();
    }

    /**
     * Update the edit texts with the top 5 scores
     */
    private void displayTopFiveScores()
    {
        if ( null != highscores_ && highscores_.size() >= 4 )
        {
            name1.setText( highscores_.get( 0 ) );
            name2.setText( highscores_.get( 1 ) );
            name3.setText( highscores_.get( 2 ) );
            name4.setText( highscores_.get( 3 ) );
            name4.setText( highscores_.get( 4 ) );

            score1.setText( highscores_.keyAt( 0 ) );
            score2.setText( highscores_.keyAt( 1 ) );
            score3.setText( highscores_.keyAt( 2 ) );
            score4.setText( highscores_.keyAt( 3 ) );
            score5.setText( highscores_.keyAt( 4 ) );
        }
    }

    /**
     * Updates the score in the saved string.
     * 
     * @param newScore
     * @param userName
     */
    private void updateScore( int newScore, String userName )
    {
        // TODO Auto-generated method stub
    }

    private int getScores()
    {
        // getting preferences
        return settings.getIntExtra( "score", 0 ); // 0 is the default value
    }
}
