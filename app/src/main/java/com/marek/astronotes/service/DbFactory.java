package com.marek.astronotes.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.entity.MessierTrophy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marek on 11/21/2015.
 */
public class DbFactory {

    private static  Map<String, DbManager> store = new HashMap<>();
    private static DbFactory dbFactory = null;

    public static DbFactory getInstance() {
        if(dbFactory == null)
            dbFactory = new DbFactory();
        return dbFactory;
    }

    private DbFactory() {
    }

    public static DbManager get(Context context, String dbName) {
        synchronized (store) {
            DbManager result = store.get(dbName);
            if (result == null) {
                result = new DbManager(context, dbName);
                store.put(dbName, result);
            }
            return result;
        }
    }

    public static class DbManager {
        private static final String DATABASE_CREATE = "" +
                "CREATE TABLE MessiersObjects (" +
                "MessierNumber INTEGER PRIMARY KEY, " +
                "NgcNumber TEXT, " +
                "PictureUrl TEXT, " +
                "Type TEXT," +
                "Constellation TEXT, " +
                "ApparentMagnitude REAL, " +
                "Note TEXT" +
                ")";

        private static final String MESSIER_NUMBER_KEY = "MessierNumber";
        private static final String NGC_NUMBER_KEY = "NgcNumber";
        private static final String PICTURE_URL_KEY = "PictureUrl";
        private static final String TYPE_KEY = "Type";
        private static final String CONSTELLATION_KEY = "Constellation";
        private static final String APPARENT_MAGNITUDE_KEY = "ApparentMagnitude";
        private static final String NOTE_KEY = "Note";
        private static final String TABLE_NAME = "MessiersObjects";

        private static final int MESSIER_NUMBER_POS = 0;
        private static final int NGC_NUMBER_POS = 1;
        private static final int PICTURE_URL_POS = 2;
        private static final int TYPE_POS = 3;
        private static final int CONSTELLATION_POS = 4;
        private static final int APPARENT_MAGNITUDE_POS = 5;
        private static final int NOTE_POS = 6;

        private static final int DATABASE_VERSION = 1;

        private SQLiteDatabase db;
        private DatabaseHelper databaseHelper;
        private String dbName;

        private DbManager(Context context, String dbName) {
            databaseHelper = new DatabaseHelper(context,
                    DATABASE_CREATE, dbName, DATABASE_VERSION);
            this.dbName = dbName;
        }

        public void open() throws SQLException{
            db = databaseHelper.getReadableDatabase();
        }

        public void close() {
            databaseHelper.close();
        }

        public void storeMessier(MessierObject messierObject) throws SQLException{
            ContentValues values = new ContentValues();

            messierObject.setNote("");
            values.put(MESSIER_NUMBER_KEY, messierObject.getMessierNumber());
            values.put(NGC_NUMBER_KEY, messierObject.getNgcNumber());
            values.put(PICTURE_URL_KEY, messierObject.getPictureUrl());
            values.put(TYPE_KEY, messierObject.getType());
            values.put(CONSTELLATION_KEY, messierObject.getConstellation());
            values.put(APPARENT_MAGNITUDE_KEY, messierObject.getApparentMagnitude());
            values.put(NOTE_KEY, messierObject.getNote());

            open();
            db.insert(TABLE_NAME, null, values);
            //close();
        }

        public MessierObject getMessier(int messierNumber) throws SQLException{
            open();
            Cursor c = db.query(
                    TABLE_NAME,
                    null,
                    MESSIER_NUMBER_KEY + "=" + messierNumber,
                    null,
                    null,
                    null,
                    null);

            MessierObject messierObject = null;

            if(c.moveToNext()) {
                messierObject = new MessierObject();
                //TODO: messier object has to be filled
            }
            //close();
            return messierObject;
        }

        public List<MessierObject> getAllMessiers() throws  SQLException{
            open();
            Cursor c = db.query(TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    MESSIER_NUMBER_KEY + " ASC");

            List<MessierObject> messierObjects = new ArrayList<>();

            while(c.moveToNext()) {
                MessierObject messierObject = new MessierObject();

                messierObject.setMessierNumber(c.getInt(MESSIER_NUMBER_POS));
                messierObject.setNgcNumber(c.getString(NGC_NUMBER_POS));
                messierObject.setPictureUrl(c.getString(PICTURE_URL_POS));
                messierObject.setType(c.getString(TYPE_POS));
                messierObject.setConstellation(c.getString(CONSTELLATION_POS));
                messierObject.setApparentMagnitude(c.getDouble(APPARENT_MAGNITUDE_POS));
                messierObject.setNote(c.getString(NOTE_POS));

                messierObjects.add(messierObject);
            }
            //close();
            return messierObjects;
        }

        public void deleteMessier(MessierObject messierObject) throws SQLException{
            String[] selectArgs = { String.valueOf(messierObject.getMessierNumber())};
            String select = MESSIER_NUMBER_KEY + " = ?";

            open();
            db.delete(
                    TABLE_NAME,
                    select,
                    selectArgs);

            //close();
        }

        public void editNote(MessierObject messierObject, String note) throws SQLException {
            ContentValues values = new ContentValues();
            String[] selectArgs = { String.valueOf(messierObject.getMessierNumber())};
            String select = MESSIER_NUMBER_KEY + " = ?";

            values.put(NOTE_KEY, note);

            open();
            db.update(
                    TABLE_NAME,
                    values,
                    select,
                    selectArgs);
            //close();
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        String dbCommand;

        DatabaseHelper(Context context, String dbCommand, String dbName, int dbVersion) {
            super(context, dbName, null, dbVersion);
            this.dbCommand = dbCommand;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(dbCommand);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(dbCommand);
        }
    }
}
