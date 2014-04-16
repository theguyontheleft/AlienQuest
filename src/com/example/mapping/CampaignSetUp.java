package com.example.mapping;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.alienquest.GameActivity;
import com.example.alienquest.R;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 2, 2014
 * 
 */
public class CampaignSetUp extends GameActivity {
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
	public int getmNumberOfAlienShips() {
		return mNumberOfAlienShips;
	}

	/**
	 * @param mNumberOfAlienShips
	 */
	public void setmNumberOfAlienShips(int mNumberOfAlienShips) {
		this.mNumberOfAlienShips = mNumberOfAlienShips;
	}

	/**
	 * Look at the preference setting and store the values locally.
	 */
	public CampaignSetUp(SharedPreferences initPreferences) {
		initializePreferences(initPreferences);
		initializeTheNumberOfAliens();
	}

	private void initializePreferences(SharedPreferences preferences) {
		// Set the difficulty variable
		String currentDifficultySetting = preferences.getString(
				getString(R.string.pref_title_difficulty),
				getString(R.string.pref_title_difficulty));

		if (currentDifficultySetting
				.contentEquals(getString(R.string.pref_title_difficulty))
				|| currentDifficultySetting
						.contentEquals(getString(R.string.mediumDifficulty))) {
			// The preference hasnn't been set yet, so default it to medium
			gameDifficulty = 1;
		} else if (currentDifficultySetting
				.contentEquals(getString(R.string.easyDifficulty))) {
			// Easy
			gameDifficulty = 0;
		} else if (currentDifficultySetting
				.contentEquals(getString(R.string.hardDifficulty))) {
			// Hard
			gameDifficulty = 2;
		} else if (currentDifficultySetting
				.contentEquals(getString(R.string.extremeDifficulty))) {
			// Extreme
			gameDifficulty = 3;
		}

		// Set the difficulty variable
		String currentGameLengthSetting = preferences.getString(
				getString(R.string.pref_title_game_length),
				getString(R.string.pref_title_game_length));

		if (currentGameLengthSetting
				.contentEquals(getString(R.string.pref_title_game_length))
				|| currentDifficultySetting
						.contentEquals(getString(R.string.mediumLength))) {
			// The preference hasnn't been set yet, so default it to medium
			gameLengthInMinutes_ = 1;
		} else if (currentGameLengthSetting
				.contentEquals(getString(R.string.speedRound))) {
			// Easy
			gameLengthInMinutes_ = 0;
		} else if (currentGameLengthSetting
				.contentEquals(getString(R.string.shortLength))) {
			// Hard
			gameLengthInMinutes_ = 2;
		} else if (currentGameLengthSetting
				.contentEquals(getString(R.string.longLength))) {
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
	private int initializeTheNumberOfAliens() {
		randomNumberGenerator_ = new Random();
		int randomInt = 0;

		switch (gameDifficulty) {
		case 0:

			/**
			 * EASY
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

			if (gameLengthInMinutes_ == 0) {

				randomInt = randomNumberGenerator_.nextInt(10) + 5;

			} else if (gameLengthInMinutes_ == 1) {

				randomInt = randomNumberGenerator_.nextInt(10) + 10;

			} else if (gameLengthInMinutes_ == 2) {

				randomInt = randomNumberGenerator_.nextInt(10) + 15;

			} else {

				randomInt = randomNumberGenerator_.nextInt(10) + 30;

			}
			Log.i("Random int is ", "" + randomInt);
			break;
		case 1:
			/**
			 * MEDIUM
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

			if (gameLengthInMinutes_ == 0) {

				randomInt = randomNumberGenerator_.nextInt(10) + 15;

			} else if (gameLengthInMinutes_ == 1) {

				randomInt = randomNumberGenerator_.nextInt(10) + 30;

			} else if (gameLengthInMinutes_ == 2) {

				randomInt = randomNumberGenerator_.nextInt(10) + 45;

			} else {

				randomInt = randomNumberGenerator_.nextInt(10) + 90;

			}
			Log.i("Random int is ", "" + randomInt);
			break;
		case 2:

			/**
			 * HARD
			 * 
			 * 5 minutes: 25 - 34
			 * 
			 * 10 minutes: 50 - 59
			 * 
			 * 15 minutes: 75 - 84
			 * 
			 * 30 minutes: 150 - 159
			 * 
			 */

			if (gameLengthInMinutes_ == 0) {

				randomInt = randomNumberGenerator_.nextInt(10) + 25;

			} else if (gameLengthInMinutes_ == 1) {

				randomInt = randomNumberGenerator_.nextInt(10) + 50;

			} else if (gameLengthInMinutes_ == 2) {

				randomInt = randomNumberGenerator_.nextInt(10) + 75;

			} else {

				randomInt = randomNumberGenerator_.nextInt(10) + 150;

			}
			Log.i("Random int is ", "" + randomInt);

			break;
		case 3:
			/**
			 * EXTREME
			 * 
			 * 5 minutes: 30 - 39
			 * 
			 * 10 minutes: 60 - 69
			 * 
			 * 15 minutes: 90 - 99
			 * 
			 * 30 minutes: 180 - 189
			 * 
			 */

			if (gameLengthInMinutes_ == 0) {

				randomInt = randomNumberGenerator_.nextInt(10) + 30;

			} else if (gameLengthInMinutes_ == 1) {

				randomInt = randomNumberGenerator_.nextInt(10) + 60;

			} else if (gameLengthInMinutes_ == 2) {

				randomInt = randomNumberGenerator_.nextInt(10) + 90;

			} else {

				randomInt = randomNumberGenerator_.nextInt(10) + 180;

			}
			Log.i("Random int is ", "" + randomInt);
			break;
		default:
			break;
		}
		setmNumberOfAlienShips(randomInt);
		return getmNumberOfAlienShips();
	}
}
