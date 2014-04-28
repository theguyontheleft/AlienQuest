package com.example.alienquest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 1, 2014
 * 
 */
public class CompletionActivity extends Activity {

	private static final String PREF_NAME = "myScores";
	protected static SharedPreferences preference_;
	private Intent settings;
	private String userName_;
	private TextView congratulationsName_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completion);

		settings = getIntent();
		// Load the preference values
		// Set up the preference
		preference_ = getSharedPreferences(getString(R.string.pref_title_file),
				Context.MODE_PRIVATE);
		userName_ = preference_.getString(getString(R.string.pref_title_name),
				getString(R.string.pref_title_name));

		// Format string to include username
		congratulationsName_ = (TextView) findViewById(R.id.congratulations_name);
		String nameMsg = getString(R.string.congratulations_name, userName_);
		congratulationsName_.setText(nameMsg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void saveScores() {
		// setting preferences
		// SharedPreferences prefs = this.getSharedPreferences("myPrefsKey",
		// Context.MODE_PRIVATE);
		// Editor editor = prefs.edit();
		// editor.putInt("key", score);
		// editor.commit();

	}

	private void getScores() {
		// getting preferences
		// int score = prefs.getInt("key", 0); // 0 is the default value
	}
}
