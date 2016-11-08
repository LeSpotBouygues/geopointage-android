package sm20_corp.geopointage.Module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sm20_corp.geopointage.Model.User;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class DatabaseHandler extends SQLiteOpenHelper {

   // All Static variables

    private static DatabaseHandler mInstance = null;
    private Context mContext;

    private static final int DATABASE_VERSION = 0;

    private static final String DATABASE_NAME = "Geapointage";

    private static final String KEY_ID = "rowId";

    // Tables names

    private static final String TABLE_USER = "user";

    // user Table Columns names
    private static final String KEY_USER_LASTNAME = "lastName";
    private static final String KEY_USER_FIRSTNAME = "firtName";
    private static final String KEY_USER_ID = "id";

    public static DatabaseHandler getInstance(Context context) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONCAT_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_USER_FIRSTNAME + " text,"
                + KEY_USER_ID + " text,"
                + KEY_USER_LASTNAME + " text" + ");";

        db.execSQL(CREATE_CONCAT_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }

    //add product in db from string
    public String addUser(String lastName, String firstName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_USER);

        values.put(KEY_ID, cnt);
        values.put(KEY_USER_LASTNAME, lastName);
        values.put(KEY_USER_FIRSTNAME, firstName);
        values.put(KEY_USER_ID, id);

        long idDb = db.insert(TABLE_USER, null, values);
        System.out.println(cnt + " (" + idDb + ")lastName = " + lastName + "\nfirstName = " + firstName + "\n id = " + id);

        db.close(); // Closing database connection
        return (Long.toString(cnt));
    }

    public User getProductNotSyncronyse(String lastName, String firstName, String id, int search) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        switch (search) {

            case 0:
                cursor = db.query(TABLE_USER,
                        new String[]{KEY_ID,
                                KEY_USER_LASTNAME,
                                KEY_USER_FIRSTNAME,
                                KEY_USER_ID}, KEY_ID + "=?",
                        new String[]{id}, null, null, null,
                        null);
                break;
        }
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {


            System.out.println("iddb = " + cursor.getString(0));
            System.out.println("lastname = " + cursor.getString(1));
            System.out.println("firstname = " + cursor.getString(2));
            System.out.println("id = " + cursor.getString(3));
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            // return contact
            System.out.println("prodcut found");
            return user;
        }
        return (null);
    }





}
