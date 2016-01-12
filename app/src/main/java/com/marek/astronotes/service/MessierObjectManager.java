package com.marek.astronotes.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.marek.astronotes.entity.MessierObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek on 11/22/2015.
 */
public class MessierObjectManager {

    private final String SERVER_URL = "http://www.stud.fit.vutbr.cz/~xbieli05/messierResponse.php";
    private static List<MessierObject> currentMessierObjects;
    private static List<MessierObject> myTrophies;
    private final NetworkManager networkManager;
    private final DbFactory dbFactory;
    private static MessierObjectManager messierObjectManager = null;
    private static Context context;
    private final String MY_TROPHIES_DB = "MyTrophiesDb";

    public static List<MessierObject> getMyTrophies() {
        return myTrophies;
    }

    public static void setMyTrophies(List<MessierObject> myTrophiesP) {
        myTrophies = myTrophiesP;
    }

    public static List<MessierObject> getCurrentMessierObjects() {
        return currentMessierObjects;
    }

    public static void setCurrentMessierObjects(List<MessierObject> currentMessierObjectsP){
        currentMessierObjects = currentMessierObjectsP;
    }

    public static MessierObjectManager getInstance(Context contextP) {
        context = contextP;
        if(messierObjectManager == null)
            messierObjectManager = new MessierObjectManager();
        return messierObjectManager;
    }

    private MessierObjectManager() {
        setCurrentMessierObjects(new ArrayList<MessierObject>());
        networkManager = NetworkManager.getInstance();
        dbFactory = DbFactory.getInstance();
    }

    public void refreshCurrentMessierObjects() throws IOException, JSONException{
        JSONArray currentMessierObjects;
        List<MessierObject> currentMessierObjectsList = new ArrayList<MessierObject>();

        NetworkManager networkManager = NetworkManager.getInstance();
        currentMessierObjects = networkManager.downloadJSON(SERVER_URL);

        for(int i = 0; i < currentMessierObjects.length(); i++) {
            currentMessierObjectsList.
                    add(i, createMessierObject(currentMessierObjects.getJSONObject(i)));
        }

        setCurrentMessierObjects(currentMessierObjectsList);
    }


    private MessierObject createMessierObject(JSONObject jsonMessier) throws JSONException{
        MessierObject messierObject = new MessierObject();

            messierObject.setMessierNumber(jsonMessier.getInt("messierNumber"));
            messierObject.setNgcNumber(jsonMessier.getString("ngcNumber"));
            messierObject.setPictureUrl(jsonMessier.getString("pictureUrl"));
            messierObject.setType(jsonMessier.getString("type"));
            messierObject.setConstellation(jsonMessier.getString("constellation"));
            messierObject.setApparentMagnitude(jsonMessier.getDouble("apparentMagnitude"));

        return messierObject;
    }

    public List<String> getMessiersStringArray() {
        List<MessierObject> messiers;
        List<String> messiersString = new ArrayList<>();

        messiers = getCurrentMessierObjects();
        for (MessierObject messierObject :
                messiers) {
            messiersString.add("M " + String.valueOf(messierObject.getMessierNumber()));
        }

        return messiersString;
    }


    public int getNumberOfCurrentObjects() {
        return getCurrentMessierObjects().size();
    }

    public MessierObject getCurrentObject(int position) {
        return getCurrentMessierObjects().get(position);
    }

    public Bitmap getMessierImage(MessierObject messierObject) throws IOException{
        return networkManager.getBitmap(messierObject.getPictureUrl());
    }

    public void refreshMyTrophies() throws SQLException{
        DbFactory.DbManager dbManager = dbFactory.get(context, MY_TROPHIES_DB);
        setMyTrophies(dbManager.getAllMessiers());
    }

    public void addToMyTrophies(MessierObject messierObject) throws SQLException{
        DbFactory.DbManager dbManager = dbFactory.get(context, MY_TROPHIES_DB);
        dbManager.storeMessier(messierObject);
    }

    public boolean isInMyTrophies(MessierObject messierObject) throws  SQLException{
        DbFactory.DbManager dbManager = dbFactory.get(context, MY_TROPHIES_DB);

        return dbManager.getMessier(messierObject.getMessierNumber()) != null;

    }

    public void deleteTrophy(MessierObject messierObject) throws SQLException{
        DbFactory.DbManager dbManager = dbFactory.get(context, MY_TROPHIES_DB);

        dbManager.deleteMessier(messierObject);
        refreshMyTrophies();
    }

    public List<String> getMyTrophiesStringArray() {
        List<MessierObject> messiers;
        List<String> messiersString = new ArrayList<>();

        messiers = getMyTrophies();
        for (MessierObject messierObject :
                messiers) {
            messiersString.add("M " + String.valueOf(messierObject.getMessierNumber()));
        }

        return messiersString;
    }

    public int getNumberOfMyTrophies() {
        return getMyTrophies().size();
    }

    public void editMyTrophy(MessierObject messierObject, String note) throws SQLException {
        DbFactory.DbManager dbManager = dbFactory.get(context, MY_TROPHIES_DB);

        dbManager.editNote(messierObject, note);
        refreshMyTrophies();
    }

    public MessierObject getMyTrophy(int position) {
        return getMyTrophies().get(position);
    }
}
