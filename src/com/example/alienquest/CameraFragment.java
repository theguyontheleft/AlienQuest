package com.example.alienquest;

import java.io.IOException;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

@SuppressLint("NewApi")
public class CameraFragment extends Fragment implements SurfaceTextureListener
{

    private RelativeLayout cameraLayout;
    private ListView objectives;
    private ArrayAdapter<String> itemAdapter;
    private ArrayList<String> itemList;
    private Camera mCamera;
    private TextureView mTextureView;

 // this is the first callback method that is invoked.
    public void onCreate(Bundle state) {
        super.onCreate(state);
        itemList = new ArrayList<String>();
        itemAdapter = new ArrayAdapter<String>(this.getActivity(),
            android.R.layout.simple_list_item_1, itemList);
        itemList.add(0 + " , " + 0);
    }

    // Initializing the views and listeners
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // linking together the xml and the view object
        // Note that each fragment has its own layout file
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.
                camera_fragment, container, false);
        objectives = (ListView) view.findViewById(R.id.listView1);
        objectives.setAdapter(itemAdapter);
        mTextureView = new TextureView(this.getActivity());
        mTextureView.setSurfaceTextureListener(this);
        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams(view.getHeight(),
                view.getWidth());
        cameraLayout = (RelativeLayout)view.findViewById(R.id.cameraLayout);
        view.addView(mTextureView);
        objectives.bringToFront();
        return view;
    }

    /**
     * this method populates the listview with the location of the alien ships
     */
    private void populateObjectives()
    {
        int numShips = ((GameActivity)this.getActivity()).setUp.getmNumberOfAlienShips();
    }

    /**
     * this method removes a complete objective from the listview, or adds
     * if such a scenario is supported
     */
    private void updateObjectives()
    {
        //TODO: populate objectives listview
        //TODO: retrieve location as well?
    }

    public void updateLocation(double latitude, double longitude)
    {
        itemList.set(0, latitude + " , " + longitude);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        mCamera = Camera.open();
        try
        {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
            int height) {
        // Does nothing
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
        // Does Nothing
    }

}
