package com.example.mapping;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.alienquest.GameActivity;
import com.example.alienquest.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Jimmy Dagres
 * 
 * @version Apr 2, 2014
 * 
 */
public class CampaignSetUp {

	int randomInt;
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

	protected Location alienLocation_;

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
	public CampaignSetUp(String gameLevel, String gameLength) {
		initializePreferences(gameLevel, gameLength);
		initializeNumberOfAliens();
	}

	private void initializePreferences(String currentGameLevel_,
			String currentGameLength_) {
		if (currentGameLevel_.equals("mediumDifficulty")) {
			// The preference hasnn't been set yet, so default it to medium
			gameDifficulty = 1;
		} else if (currentGameLevel_.equals("easyDifficulty")) {
			// Easy
			gameDifficulty = 0;
		} else if (currentGameLevel_.equals("hardDifficulty")) {
			// Hard
			gameDifficulty = 2;
		} else if (currentGameLevel_.equals("extremeDifficulty")) {
			// Extreme
			gameDifficulty = 3;
		}

		if (currentGameLength_.equals("speedRound")) {
			// The preference hasnn't been set yet, so default it to medium
			gameLengthInMinutes_ = 0;
		} else if (currentGameLength_.equals("shortLength")) {
			// Easy
			gameLengthInMinutes_ = 1;
		} else if (currentGameLength_.equals("mediumLength")) {
			// Hard
			gameLengthInMinutes_ = 2;
		} else if (currentGameLength_.equals("longLength")) {
			// Extreme
			gameLengthInMinutes_ = 3;
		}

		Log.w("Level", "" + gameDifficulty);

		// gameLengthInMinutes_ = currentGameLengthSetting;
	}

	/**
	 * There are two main things taken into account here. The first is the time
	 * of the campaign and the second is the selected difficulty.
	 * 
	 * @return the number of aliens created
	 */
	private void initializeNumberOfAliens() {
		randomNumberGenerator_ = new Random();

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
			break;
		default:
			break;
		}
		Log.i("Game length ", "" + gameLengthInMinutes_);
		Log.i("Random int is ", "" + randomInt);
		setmNumberOfAlienShips(randomInt);
	}

	private void getRandomAlienLocation(double latitude_, double longitude_,
			int radius) {
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(latitude_);

		double alienLongitude = new_x + longitude_;
		double alienLatitude = y + latitude_;

		alienLocation_ = new Location("GPS");

		alienLocation_.setLongitude(alienLongitude);
		alienLocation_.setLatitude(alienLatitude);

	}

	public LatLng getRandomLatLongVariable() {
		return new LatLng(alienLocation_.getLatitude(),
				alienLocation_.getLongitude());
	}
}
