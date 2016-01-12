package com.marek.astronotes.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marek on 11/22/2015.
 */
public class NetworkManager {
    private static final int SECOND = 1000;
    private static final int READ_TIMEOUT = 5 * SECOND;
    private static final int CONNECT_TIMEOUT = 5 * SECOND;
    private static final String GET_METHOD = "GET";

    private static NetworkManager networkManager = null;

    public static NetworkManager getInstance() {
        if(networkManager == null)
            networkManager = new NetworkManager();
        return networkManager;
    }

    private NetworkManager() {
    }

    public JSONArray downloadJSON(String urlS) throws IOException, JSONException{
        InputStream is = null;
        String jsonString;
        try {
            is = getInput(urlS);
            jsonString = isToString(is);
        } finally {
            if (is != null)
                is.close();
        }
        return new JSONArray(jsonString);
    }

    private String isToString(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = r.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    private InputStream getInput(String urlS) throws IOException {
            URL url = new URL(urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setRequestMethod(GET_METHOD);
            connection.setDoInput(true);
            connection.connect();
            return connection.getInputStream();
    }

    public Bitmap getBitmap(String urlS) throws IOException {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getInput(urlS);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
        }
        finally {
            if (is != null)
                is.close();
        }
        return bitmap;
    }
}
