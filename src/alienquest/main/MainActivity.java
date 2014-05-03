package alienquest.main;

import alienquest.settings.SettingsPreferenceActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Jimmy Dagres
 * @author Garrett Moran
 * @author Tiffany Tuan
 * 
 * @version Mar 30, 2014
 * 
 */
public class MainActivity extends Activity {
	/**
	 * Used for the settings
	 */
	public static boolean inputCorrect;

	/**
	 * determines if the app is in its initial setup
	 */
	private static boolean initialSetup;

	/**
	 * settings menu Intent, used to access settings and preference information
	 */
	private Intent settingsIntention;

	/**
	 * start game intent
	 */
	private Intent startGameIntention;

	private Intent startScoreIntention;
	/**
	 * instance of the settings task
	 */
	private static SharedPreferences preference_;

	// This variable is updated from the settings and determines if there will
	// be sound in the game
	private boolean soundEnabled = false;

	// Button instances
	private Button startGameButton_;
	private Button instructionsButton_;
	private Button aboutGameButton_;
	private Button scoresButton_;

	private TextView welcomeMsg_;
	private TextView welcomeMsg2_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Opens alert dialog if user GPS is not enabled
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

			// Build the alert dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Location Services Not Active");
			builder.setMessage("Please enable Location Services and GPS");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int i) {
							// Show location settings when the user acknowledges
							// the alert dialog
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});
			Dialog alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		}

		setContentView(R.layout.activity_main);

		Typeface tf = Typeface.createFromAsset(getAssets(), "Fonts/Molot.otf");
		Typeface main = Typeface
				.createFromAsset(getAssets(), "Fonts/OpTic.ttf");

		settingsIntention = new Intent(MainActivity.this,
				SettingsPreferenceActivity.class);
		startGameIntention = new Intent(MainActivity.this, GameActivity.class);
		startScoreIntention = new Intent(MainActivity.this,
				CompletionActivity.class);

		// Set up the start game, instructions and about buttons.
		startGameButton_ = (Button) findViewById(R.id.startGameButton);
		instructionsButton_ = (Button) findViewById(R.id.instructionsButton);
		aboutGameButton_ = (Button) findViewById(R.id.aboutButton);
		scoresButton_ = (Button) findViewById(R.id.scoresButton);

		welcomeMsg_ = (TextView) findViewById(R.id.textView1);
		welcomeMsg2_ = (TextView) findViewById(R.id.textView2);

		startGameButton_.setTypeface(tf);
		instructionsButton_.setTypeface(tf);
		aboutGameButton_.setTypeface(tf);
		scoresButton_.setTypeface(tf);

		welcomeMsg_.setTypeface(tf);
		welcomeMsg2_.setTypeface(main);

		// Set up the preference
		preference_ = getSharedPreferences(getString(R.string.pref_title_file),
				Context.MODE_PRIVATE);

		// Set up sound
		initializeSound();

		// Start game
		startGameButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Vibrator earthShaker = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				earthShaker.vibrate(200);
				// startGameIntention.putExtra("Difficulty",
				// settingsIntention.getStringExtra("Difficulty"));
				MainActivity.this.startActivity(startGameIntention);
			}
		});

		// Display instructions fragment
		instructionsButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInstructionsDialog();
			}
		});

		// Display about fragment
		aboutGameButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayAboutDialog();
			}
		});

		scoresButton_.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(startScoreIntention);
			}
		});

		// CUSTOM
		// Test the map fragment
		// mapFragment_ = new MapFragmentClass();
		//
		// FragmentManager fragmentManager = getFragmentManager();
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		//
		// fragmentTransaction.add( R.id.googleMapFragment, mapFragment_ );
		// fragmentTransaction.commit();

		// mMap_ = new GoogleMap( null ); TODO

	}

	/**
	 * Gets the sound preference and initialize the variable
	 */
	private void initializeSound() {
		String soundSring = preference_.getString(
				getString(R.string.pref_title_sound),
				getString(R.string.pref_title_sound));

		soundEnabled = soundSring.toUpperCase().contains("TRUE");
	}

	/**
	 * Adds the instructions fragment to the view
	 */
	private void displayInstructionsDialog() {
		// prepare the instructions box
		Dialog instructionsBox = new Dialog(MainActivity.this);
		instructionsBox.setContentView(R.layout.dialog_instructions);
		instructionsBox.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		// set the message to display
		instructionsBox.setTitle("Instructions Menu");

		instructionsBox.show();
	}

	/**
	 * Adds the about fragment to the view
	 */
	private void displayAboutDialog() {
		// prepare the instructions box
		Dialog aboutBox = new Dialog(MainActivity.this);
		aboutBox.setContentView(R.layout.dialog_about);

		// TextView aboutTxt = (TextView) aboutBox
		// .findViewById(android.R.id.message);

		// set the message to display
		aboutBox.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		aboutBox.setTitle("About Alien Quest");
		aboutBox.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			this.startActivity(settingsIntention);

			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * @return
	 * @returns the preferences
	 */
	public static SharedPreferences getPreference_() {
		return preference_;
	}
}
