package com.example.w0274203.assign4_movie;

/**
 * Created by Kristina on 2015-12-05.
 */
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

public class DBAdapter {
    //Constants for Keys
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_RATING = "rating";
    public static final String KEY_DESCRIPTION ="description";
    public static final String KEY_THUMBNAILID = "thumbnailId";
    public static final String KEY_LARGEIMG = "largeImg";

    //DBAdapter
    public static final String TAG = "DBAdapter";

    //Constants for database keys
    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "movies";
    private static final int DATABASE_VERSION = 16;

    //Creates the table movies
    private static final String DATABASE_CREATE =
            "create table movies(_id integer primary key autoincrement, title text not null, rating text not null, description text not null, thumbnailId text not null, largeImg text not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }//end DBAdapter

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        //Creates database
        public void onCreate(SQLiteDatabase db)
        {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        //Upgrades database
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS movies");
            onCreate(db);
        }//end method onUpgrade
    }//end DatabaseHelper class

    //open the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //insert a movie into the database
    public long insertMovie(String title,String rating, String description, String thumbnailId, String largeImg)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_RATING, rating);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_THUMBNAILID, thumbnailId);
        initialValues.put(KEY_LARGEIMG, largeImg);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a particular movie
    public boolean deleteMovie(long rowId)
    {
        return db.delete(DATABASE_TABLE,KEY_ROWID + "=" + rowId,null) >0;
    }//end deleteMovie

    //retrieve all the movies
    public Cursor getAllMovies()
    {
        return db.query(DATABASE_TABLE,new String[]{KEY_ROWID,KEY_TITLE, KEY_RATING,
                KEY_DESCRIPTION, KEY_THUMBNAILID, KEY_LARGEIMG},null,null,null,null,null);
    }//end getAllMovies

    //retrieve a single movie
    public Cursor getMovie(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_TITLE, KEY_RATING, KEY_DESCRIPTION, KEY_THUMBNAILID, KEY_LARGEIMG},KEY_ROWID + "=" + rowId,null,null,null,null,null);
        if(mCursor != null)
            mCursor.moveToFirst();
        return mCursor;
    }//end getMovie

    //updates a movie rating
    public boolean updateMovieRating(long rowId,String rating)
    {
        ContentValues cval = new ContentValues();
        cval.put(KEY_RATING, rating);
        return db.update(DATABASE_TABLE, cval, KEY_ROWID + "=" + rowId,null) >0; //Updates the movie rating for the movie id passed in
    }//end updateMovieRating

}//end class DBAdapter
