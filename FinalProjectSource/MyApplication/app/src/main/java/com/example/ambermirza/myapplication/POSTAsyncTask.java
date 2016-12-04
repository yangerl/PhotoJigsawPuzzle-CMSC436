package com.example.ambermirza.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eggroll Eric on 12/2/2016.
 */

public class POSTAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String result = postData(strings[0], strings[1]);
        Log.i("POSTAsyncTask", result);
        return result;
    }

    public String postData(String url, String building) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", building));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseStr = EntityUtils.toString(response.getEntity());
            return responseStr;

        } catch (ClientProtocolException e) {
            return "ClientProtocolException";
        } catch (IOException e) {
            return "IOException";
        }
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


    }


}
