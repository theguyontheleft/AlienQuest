package alienquest.main;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.TextView;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 1, 2014
 * 
 */
public class CompletionActivity extends Activity
{

    private static final String PREF_NAME = "myScores";
    protected static SharedPreferences preference_;
    private Intent settings;
    private String userName_;
    private TextView congratulationsName_;

    private String topFiveScoreValues_;
    private String topFiveScoreUsernames_;

    // Array map that contains the top 5 scores as keys, and the assosiated
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

        // Format string to include username TODO: Jimmy: I'm not sure what this
        // is?
        congratulationsName_ =
                (TextView) findViewById( R.id.congratulations_name );
        String nameMsg = getString( R.string.congratulations_name, userName_ );
        congratulationsName_.setText( nameMsg );
        
        getTopFiveScores();
    }

    /**
     * @param score
     * @param completed
     *            whether or not the game was successful beat
     */
    public void gameEnded( int score, boolean completed )
    {
        congratulationsName_.setText( userName_ );
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
    private void saveScores()
    {
        // setting preferences
        // SharedPreferences prefs = this.getSharedPreferences("myPrefsKey",
        // Context.MODE_PRIVATE);
        // Editor editor = prefs.edit();
        // editor.putInt("key", score);
        // editor.commit();

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
            highscores_.put( Integer.parseInt(arrayOfScores[j]), arrayOfNames[j] );
        }

        // If there isn't five scores saved then make them blank
        for ( ; j < 5; j++ )
        {
            highscores_.put( 0, "Empty" );
        }
    }
    
    /**
     * Updates the score in the saved string.
     * @param newScore
     * @param userName
     */
    private void updateScore(int newScore, String userName)
    {
        
    }

    private int getScores()
    {
        // getting preferences
        return settings.getIntExtra( "score", 0 ); // 0 is the default value
    }
}
