package com.example.ambermirza.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import java.io.ByteArrayOutputStream;

public class PlaceDescription extends AppCompatActivity {
    static final int START_PUZZLE_REQUEST = 1337;
    Intent fromMapActivity;
    Bitmap mPlacePic;
    String mPlaceName;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Double mLat, buidlingLat;
    Double mLng, buidlingLng;
    TextView mCompleted;
    private LocationManager mLocationmanager;
    static final String TAG = "PLACE_DESCRIPTION";
    final int REFRESH_TIME = 360;
    final float REFRESH_DIST = 100.0f;
    static final String postURL =
            "https://photojigsawpuzzledjango.appspot.com/puzzle/postPuzzleSolved/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        mPlacePic = BitmapFactory.decodeResource(getResources(), R.drawable.stub);
        mLocationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fromMapActivity = getIntent();
        TextView placeName = (TextView) findViewById(R.id.place_name);
        mPlaceName = fromMapActivity.getStringExtra("placeName");
        placeName.setText(mPlaceName);

        mCompleted = (TextView) findViewById(R.id.place_complete);
        if (fromMapActivity.getBooleanExtra("completed", false)) {
            mCompleted.setText(R.string.completed);
        } else {
            mCompleted.setText(R.string.incomplete);
        }
        mLat = fromMapActivity.getDoubleExtra("mLat",0);
        mLng = fromMapActivity.getDoubleExtra("mLng",0);
        buidlingLat = fromMapActivity.getDoubleExtra("lat", 0);
        buidlingLng = fromMapActivity.getDoubleExtra("lng", 0);

        Log.i(TAG, "loaded all info from intent");
        GetPhotoAsynctask task = new GetPhotoAsynctask(this);
        task.execute(fromMapActivity.getStringExtra("placePic"));

        Button playButton = (Button) findViewById(R.id.play_puzzle);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "begin distance calculations");
                final int R = 6371; // Radius of the earth
                Double latDistance = Math.toRadians(mLat - buidlingLat);
                Double lonDistance = Math.toRadians(mLng - buidlingLng);
                Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(buidlingLat)) * Math.cos(Math.toRadians(mLat))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distance = R * c * 1000; // convert to meters
                distance = Math.pow(distance, 2);
                Log.i(TAG, "finished distance calculations");



                if (Math.sqrt(distance) > 300) {
                    Toast.makeText(PlaceDescription.this,
                            "You are " + (long) Math.sqrt(distance) + " meters from the building.",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(PlaceDescription.this,
                            "You need to get closer to start this puzzle!",
                            Toast.LENGTH_LONG).show();
                } else {

                    Intent startPuzzle = new Intent(PlaceDescription.this, PuzzleActivity.class);
                    //Convert to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mPlacePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    startPuzzle.putExtra("picture", byteArray);
                    // startPuzzle.putExtra("picture", mPlacePic);
                    startPuzzle.putExtra("name", mPlaceName);

                    startActivityForResult(startPuzzle, START_PUZZLE_REQUEST);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == START_PUZZLE_REQUEST) {

            // Todo -- manage the result sent by Daniel's puzzle

            // Todo -- change completed to whatever key Daniel used
            boolean completed = data.getBooleanExtra("solved", false);
            if (completed) {
                POSTAsyncTask postCompleted = new POSTAsyncTask();
                postCompleted.execute(postURL, data.getStringExtra("name"));
                mCompleted.setText(R.string.completed);
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("completed", completed);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            // Victory Screen??
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    public void addPlacePic(Bitmap placePic) {
        ImageView placeImage = (ImageView) findViewById(R.id.place_image);
        placeImage.setImageBitmap(placePic);
        mPlacePic = placePic;
    }
}
