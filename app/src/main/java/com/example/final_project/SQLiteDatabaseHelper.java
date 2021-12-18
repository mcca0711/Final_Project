package com.example.final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.final_project.NasaModel;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Names
    private static final String DATABASE_NAME = "NasaDay";

    // Table Names
    private static final String TABLE_FAVOURITES = "favourites";
    private static final String TABLE_VIEWED = "viewed";

    // appData Table - column names
    public static final String KEY_ID = "id";
    public static final String DATE = "date";
    public static final String EXPLANATION = "explanation";
    public static final String HD_URL = "hd_url";
    public static final String MEDIA_TYPE = "media_type";
    public static final String SERVICE_VERSION = "service_version";
    public static final String TITLE = "title";
    public static final String URL = "url";



    /*  table create statement  */
    private static final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " + TABLE_FAVOURITES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ DATE + " TEXT,"
            + EXPLANATION + " TEXT," + HD_URL + " INTEGER," + MEDIA_TYPE + " INTEGER,"
            + SERVICE_VERSION + " TEXT,"+ TITLE + " INTEGER," + URL + " TEXT" +  ")";

    private static final String CREATE_TABLE_VIEWED = "CREATE TABLE " + TABLE_VIEWED
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ DATE + " TEXT,"
            + EXPLANATION + " TEXT," + HD_URL + " INTEGER," + MEDIA_TYPE + " INTEGER,"
            + SERVICE_VERSION + " TEXT,"+ TITLE + " INTEGER," + URL + " TEXT" +  ")";



    private Context context;
    private static final String TAG = "DatabaseHelper";

    public SQLiteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_FAVOURITE);
        db.execSQL(CREATE_TABLE_VIEWED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEWED);

        // create new tables
        onCreate(db);
    }

    //storing favourites data in SQLite Database
    public void addToFavourites(NasaModel model){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,model.getDate());
        contentValues.put(EXPLANATION,model.getExplanation());
        contentValues.put(HD_URL,model.getHd_url());
        contentValues.put(URL,model.getUrl());
        contentValues.put(MEDIA_TYPE,model.getMedia_type());
        contentValues.put(SERVICE_VERSION,model.getService_version());
        contentValues.put(TITLE,model.getTitle());
        database.insert(TABLE_FAVOURITES,null,contentValues);
    }

    //remove specific data from favourites by id
    public void removeFromFavourite(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        String[] arg = {id};
        database.delete(TABLE_FAVOURITES,KEY_ID+"=?",arg);
    }

    //get all favourites data from database
    public Cursor getAllFavourites(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM "+TABLE_FAVOURITES,null);
        return data;
    }

    //validating whether id is available in favourites table or not
    public boolean isFavourite(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor =  database.query(TABLE_FAVOURITES,new String[]{KEY_ID,DATE,EXPLANATION
                        ,HD_URL,MEDIA_TYPE,SERVICE_VERSION,TITLE,URL},KEY_ID + "=?"
                ,new String[]{id},null,null,null);

        if (cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    //storing searched data in SQLite Database
    public void addToViewed(NasaModel model){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,model.getDate());
        contentValues.put(EXPLANATION,model.getExplanation());
        contentValues.put(HD_URL,model.getHd_url());
        contentValues.put(URL,model.getUrl());
        contentValues.put(MEDIA_TYPE,model.getMedia_type());
        contentValues.put(SERVICE_VERSION,model.getService_version());
        contentValues.put(TITLE,model.getTitle());
        database.insert(TABLE_VIEWED,null,contentValues);
    }

    //get all searched data from database
    public Cursor getAllViewed(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM "+TABLE_VIEWED,null);
        return data;
    }

    //remove specific data from search table by id
    public void removeFromViewed(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        String[] arg = {id};
        database.delete(TABLE_VIEWED,KEY_ID+"=?",arg);
    }

    //delete all data from both tables
    public void deleteAllData(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ TABLE_FAVOURITES);
        database.execSQL("delete from "+ TABLE_VIEWED);
        database.execSQL("vacuum");
    }

}
