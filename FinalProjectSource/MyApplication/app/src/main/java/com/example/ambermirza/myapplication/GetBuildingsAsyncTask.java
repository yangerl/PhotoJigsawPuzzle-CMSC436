package com.example.ambermirza.myapplication;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Eggroll Eric on 12/4/2016.
 */

public class GetBuildingsAsyncTask extends AsyncTask<String, Void, List<Building>> {
    private WeakReference<MapsActivity> mParent;
    AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
    public GoogleMap mMap;

    public GetBuildingsAsyncTask(MapsActivity parent) {
        super();
        this.mParent = new WeakReference<MapsActivity>(parent);
    }

    @Override
    protected List<Building> doInBackground(String... strings) {

        HttpGet request = new HttpGet(strings[0]);
        JSONResponseHandler responseHandler = new JSONResponseHandler();


        try {
            return mClient.execute(request, responseHandler);

        } catch (ClientProtocolException e) {
            Log.i("GetBuildingAsyncTask", "ClientProtocolException");
        } catch (IOException e) {
            Log.i("GetBuildingAsyncTask", "IOException");
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Building> result) {
        mMap = mParent.get().getMap();
        if (null != mMap) {

            // Add a marker for every earthquake

            for (Building b : result) {
                mParent.get().addPlace(b);
                Float color;
                if (b.completed) {
                    color = BitmapDescriptorFactory.HUE_GREEN;
                } else {
                    color = BitmapDescriptorFactory.HUE_RED;
                }

                // Add a new marker for this earthquake
                Marker marker = mMap.addMarker(new MarkerOptions()

                        // Set the Marker's position
                        .position(new LatLng(b.getLat(), b.getLng()))

                        // Set the title of the Marker's information window, not sure what we want
                        // for this part. I'm just setting it to the name for now
                        .title(String.valueOf(b.getName()))

                        // Set the color for the Marker to red
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(color)));

                mParent.get().markerList.add(marker);

            }


        }

        if (null != mClient) {
            mClient.close();
        }


    }
}
