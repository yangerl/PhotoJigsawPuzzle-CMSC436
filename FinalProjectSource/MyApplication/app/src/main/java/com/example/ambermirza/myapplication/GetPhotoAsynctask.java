package com.example.ambermirza.myapplication;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.places.Place;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eggroll Eric on 12/3/2016.
 */

public class GetPhotoAsynctask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<PlaceDescription> mParent;
    private HttpURLConnection mHttpUrl;

    public GetPhotoAsynctask(PlaceDescription parent) {
        super();
        this.mParent = new WeakReference<PlaceDescription>(parent);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        InputStream in = null;
        String placePic = strings[0];
        if (placePic.length() < 32) {
            placePic = "https://photojigsawpuzzledjango.appspot.com/static/img/"
                    + placePic;
            Log.i("GETPHOTO", placePic.toString());
        }

        try {
            URL url = new URL(placePic);
            mHttpUrl = (HttpURLConnection) url.openConnection();
            in = mHttpUrl.getInputStream();
            Log.i("GETPHOTO", in.toString());
            return BitmapFactory.decodeStream(in);

        } catch (MalformedURLException e) {
            Log.e("DEBUG", e.toString());
        } catch (IOException e) {
            Log.e("DEBUG", e.toString());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHttpUrl.disconnect();
        }

        return BitmapFactory.decodeResource(mParent.get().getResources(),
                R.drawable.stub);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null && mParent.get() != null){
            mParent.get().addPlacePic(result);
        }
    }
}
