package com.example.ambermirza.myapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONResponseHandler implements
        ResponseHandler<List<Building>> {
    @Override
    public List<Building> handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {
        List<Building> result = new ArrayList<Building>();
        String JSONResponse = new BasicResponseHandler()
                .handleResponse(response);
        try {
            JSONObject object = (JSONObject) new JSONTokener(JSONResponse)
                    .nextValue();
            JSONArray buildings = object.getJSONArray("buildings");
            for (int i = 0; i < buildings.length(); i++) {
                JSONObject tmp = (JSONObject) buildings.get(i);
                result.add(new Building(
                        tmp.getDouble("lat"),
                        tmp.getDouble("lng"),
                        tmp.getString("pic"),
                        tmp.getBoolean("completed"),
                        tmp.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}