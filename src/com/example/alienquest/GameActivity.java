package com.example.alienquest;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mapping.CampaignSetUp;
import com.example.mapping.GPSLocator;
import com.example.mapping.MapFragmentClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Jimmy Dagres
 * @author Garrett Moran
 *
 * @version Mar 31, 2014
 *
 *
 *          This activity will display the game mode
 */
@SuppressLint("NewApi")
public class GameActivity extends Activity {
	public final static String FRAG1_TAG = "FRAG1";
	public final static String FRAG2_TAG = "FRAG2";

	private String userName_ = "";

	private Camera mCamera;
	private TextureView mTextureView;
	private Button cameraButton;
	private RelativeLayout mRelativeLayout;

	private MapFragmentClass mapFragment_;
	private CameraFragment cameraFragment;
	private ListView objectives;
	private ArrayAdapter<String> itemAdapter;
	private ArrayList<String> itemList;
	private FrameLayout objectivesFrame;
	private Intent settings;

	/**
	 * The google map
	 */
	protected static GoogleMap mMap_;
	protected static MapFragment mMapFrag_;

	protected GPSLocator gps_ = null;

	// Store the width and height in pixels, this will be usefull in
	// calculations throughout the application
	private int screenWidthPixels_;
	private int screenHeightPixels_;

	// Time in ms that the game started
	private static long questStartTime_;

	/**
	 * When the game first starts the user is prompted of the map where the
	 * aliens are landing
	 */
	private boolean userPrompted_ = false;

	// GPS location values
	protected double longitude_;
	protected double latitude_;
	protected CampaignSetUp setUp;
	private String difficulty_;
	private String gameLength_;

	/**
	 * instance of the settings task
	 */
	protected static SharedPreferences preference_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		settings = getIntent();
		cameraButton = (Button) findViewById(R.id.gameFrame)
		    .findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                displayCameraFragment();
            }
		});
		//objectives = (ListView) findViewById(R.id.objectivesFrame)
		//		.findViewById(R.id.listView1);
		//itemList = new ArrayList<String>();
		//itemList.clear();
		//itemAdapter = new ArrayAdapter<String>(this, R.id.listView1, itemList);
		//objectives.setAdapter(itemAdapter);
		//objectives.bringToFront();
		// Load the preference values
		// Set up the preference
		preference_ = getSharedPreferences(getString(R.string.pref_title_file),
				Context.MODE_PRIVATE);

		userName_ = preference_.getString(getString(R.string.pref_title_name),
				getString(R.string.pref_title_name));
		if (1 <= userName_.length()) {
			Toast.makeText(
					getApplicationContext(),
					"Welcome "
							+ userName_
							+ " to the quest. The human race is counting on you"
							+ " to fend off the alien spaceships attempting to land on earth.",
					Toast.LENGTH_LONG * 2).show();
		}

		getGPSLocation();

		// Start off by displaying a google map and inserting the aliens

		// Store the screen width and height
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidthPixels_ = metrics.widthPixels;
		screenHeightPixels_ = metrics.heightPixels;

		difficulty_ = preference_.getString(
				getString(R.string.pref_title_difficulty),
				getString(R.string.pref_title_difficulty));

		gameLength_ = preference_.getString(
				getString(R.string.pref_title_game_length),
				getString(R.string.pref_title_game_length));

		// Default the preferences if they aren't set yet
		setUp = new CampaignSetUp(this, difficulty_, gameLength_);
		Toast.makeText(
				getApplicationContext(),
				"Difficulty is " + difficulty_ + " and length is "
						+ gameLength_, Toast.LENGTH_LONG).show();

		// displayCamera();
		displayMapFragment();
		// displayObjectives();
		cameraButton.bringToFront();
		// Record the start time of the game
		questStartTime_ = System.currentTimeMillis();
	}

	/**
	 * Adds the map fragment to the view
	 */
	private void displayMapFragment() {
		// Add the map fragment
		mMapFrag_ = new MapFragment();

		FragmentTransaction fragMan = getFragmentManager().beginTransaction();
		fragMan.add(R.id.gameFrame, mMapFrag_, FRAG2_TAG);
		fragMan.commit();


		mMap_ = mMapFrag_.getMap(); // Custom MapFragment
		setUpMapIfNeeded(); // Dynamic

	}
	/**
	 * displays the camera fragment
	 */
	private void displayCameraFragment()
	{
	   cameraFragment = new CameraFragment();
	   FragmentTransaction fragMan = getFragmentManager().beginTransaction();
       fragMan.add(R.id.gameFrame, cameraFragment, FRAG2_TAG);
       fragMan.commit();
	}


	private void setUpMapIfNeeded() {
		if (null != mMap_) {
			centerOnCurrentLocation();
		} else {
			mMap_ = mMapFrag_.getMap();
			centerOnCurrentLocation();
		}
	}

	/**
	 * Tries to get the GPS location
	 */
	private void getGPSLocation() {
		gps_ = new GPSLocator(getApplicationContext());

		// check if GPS enabled
		if (gps_.canGetLocation()) {
			latitude_ = gps_.getLatitude();
			longitude_ = gps_.getLongitude();

			// Raises a toast to show the location
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude_ + "\nLong: "
							+ longitude_, Toast.LENGTH_LONG).show();
		} else {
			// There was an error getting the gps information
			gps_.showSettingsAlert();
		}
	}

	/**
	 * displays the objectives component to the game screen
	 *
	private void displayObjectives() {
		// TODO: change this to actually do what it should do
		itemAdapter.add("1.......200");
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public long getQuestStartTime() {
		return questStartTime_;
	}

	// ---- Following Methods handle camera implementation -----//

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Focuses the google map on the current GPS location
	 */
	private void centerOnCurrentLocation() {
		if (null != mMap_) {
			// Get the location first
			getGPSLocation();

			// Initialize the camera position
			MapsInitializer.initialize(GameActivity.this);
			CameraPosition mCameraPosition = new CameraPosition.Builder()
					.zoom(16.0f).target(gps_.getLatCurrentLongVariable())
					.bearing(0).tilt(0).build();

			Marker currentPosition = mMap_.addMarker(new MarkerOptions()
					.position(gps_.getLatCurrentLongVariable())
					.title("Hamburg"));

			Marker newAlienShip = mMap_
					.addMarker(new MarkerOptions()
							.position(gps_.getLatCurrentLongVariable())
							.title("AlienShip")
							// TODO specify which ship
							.snippet("Ship is landing!")
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.alien_ship_map_marker_small)));

			Toast.makeText(getApplicationContext(), "The Aliens are coming!",
					Toast.LENGTH_LONG).show();

			// Move the camera instantly to hamburg with a zoom of 15.
			mMap_.moveCamera(CameraUpdateFactory.newLatLngZoom(
					gps_.getLatCurrentLongVariable(), 15.0f));
			// Zoom in, animating the camera.
			mMap_.animateCamera(CameraUpdateFactory
					.newCameraPosition(mCameraPosition));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
/**
	/**
	 * This method will display the bulk of the screen as the camera
	 *
	private void displayCamera() {
		mTextureView = new TextureView(this);
		mTextureView.setSurfaceTextureListener(this);
		RelativeLayout.LayoutParams params;
		params = new RelativeLayout.LayoutParams(screenWidthPixels_,
				screenHeightPixels_);
		mRelativeLayout = new RelativeLayout(this);
		mRelativeLayout.addView(mTextureView, params);
		setContentView(mRelativeLayout);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewTexture(surface);
			mCamera.startPreview();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		if (null != mCamera) {
			mCamera.stopPreview();
			mCamera.release();
		}
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
	}
	// --------------------End of Section-----------------------//
*/
}