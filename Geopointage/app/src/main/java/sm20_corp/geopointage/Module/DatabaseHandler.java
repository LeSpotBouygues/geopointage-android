package sm20_corp.geopointage.Module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import sm20_corp.geopointage.Model.User;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables

    private static DatabaseHandler mInstance = null;
    private Context mContext;

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Geapointage";

    private static final String KEY_ID = "rowId";

    // Tables names

    private static final String TABLE_USER = "user";
    private static final String TABLE_CHEF = "chef";


    // user Table Columns names
    private static final String KEY_USER_LASTNAME = "lastName";
    private static final String KEY_USER_FIRSTNAME = "firtName";
    private static final String KEY_USER_ID = "id";

    // user Table Columns names
    private static final String KEY_CHEF_LASTNAME = "lastName";
    private static final String KEY_CHEF_FIRSTNAME = "firtName";
    private static final String KEY_CHEF_ID = "id";

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
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_USER_FIRSTNAME + " text,"
                + KEY_USER_ID + " text,"
                + KEY_USER_LASTNAME + " text" + ");";

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_CHEF_TABLE = "CREATE TABLE " + TABLE_CHEF + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_CHEF_FIRSTNAME + " text,"
                + KEY_CHEF_ID + " text,"
                + KEY_CHEF_LASTNAME + " text" + ");";

        db.execSQL(CREATE_CHEF_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHEF);
        // Create tables again
        onCreate(db);
    }

    //add product in db from string
    public String addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_USER);


        values.put(KEY_ID, cnt);
        values.put(KEY_USER_LASTNAME, user.getLastName());
        values.put(KEY_USER_FIRSTNAME, user.getFirstName());
        values.put(KEY_USER_ID, user.getId());

        long idDb = db.insert(TABLE_USER, null, values);
        System.out.println(cnt + " (" + idDb + ")lastName = " + user.getLastName() + "\nfirstName = " + user.getFirstName() + "\n id = " + user.getId());

        db.close(); // Closing database connection
        return (Long.toString(cnt));
    }

    public User getUser(String lastName, String firstName, String id, int search) {
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
            System.out.println("1 user found");
            return user;
        }
        return (null);
    }

    public ArrayList<User> getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;


        //  Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID, KEY_USER_PASSWORD_ID, KEY_USER_EMAIL_ID, KEY_USER_ID}, null, null, null, null, null);

        cursor = db.query(TABLE_USER,
                new String[]{KEY_ID,
                        KEY_USER_LASTNAME,
                        KEY_USER_FIRSTNAME,
                        KEY_USER_ID},
                null, null, null, null,
                null);

        User user = null;
        ArrayList<User> arrayListUser = new ArrayList<User>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            System.out.println("i = " + i);
            System.out.println("iddb = " + cursor.getString(0));
            System.out.println("lastname = " + cursor.getString(1));
            System.out.println("firstname = " + cursor.getString(2));
            System.out.println("id = " + cursor.getString(3));
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            arrayListUser.add(user);
            i++;

            while (cursor.moveToNext()) {
                System.out.println("i = " + i);
                System.out.println("iddb = " + cursor.getString(0));
                System.out.println("lastname = " + cursor.getString(1));
                System.out.println("firstname = " + cursor.getString(2));
                System.out.println("id = " + cursor.getString(3));
                user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                arrayListUser.add(user);
                i++;
            }
            cursor.close();
            return arrayListUser;
        }
        System.out.println("aucun user trouvé");
        return (null);
    }


    //create CHEF in db from string
    public int createChef() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_CHEF);
        if (cnt != 0) {
            return (-1);
        }
        values.put(KEY_ID, cnt);
        values.put(KEY_CHEF_LASTNAME, "-1");
        values.put(KEY_CHEF_FIRSTNAME, "-1");
        values.put(KEY_CHEF_ID, "-1");

        long id = db.insert(TABLE_CHEF, null, values);
        db.close(); // Closing database connection
        return (1);
    }

    //update LAST in db
    public int updateChef(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor2 = db.query(TABLE_CHEF, new String[]{KEY_ID,
                        KEY_CHEF_LASTNAME,
                        KEY_CHEF_FIRSTNAME,
                        KEY_CHEF_ID
                }, KEY_ID + "=?",
                new String[]{"0"}, null, null, null,
                null);
        if (cursor2 != null && cursor2.moveToFirst()) {
            cursor2.moveToFirst();

            values.put(KEY_CHEF_LASTNAME, user.getLastName());
            values.put(KEY_CHEF_FIRSTNAME, user.getFirstName());
            values.put(KEY_CHEF_ID, user.getId());

            // updating row
            return db.update(TABLE_CHEF, values, KEY_ID + " = ?", new String[]{"0"});
        }
        return (-1);
    }


    public User getChef() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(TABLE_CHEF,
                new String[]{KEY_ID,
                        KEY_CHEF_LASTNAME,
                        KEY_CHEF_FIRSTNAME,
                        KEY_CHEF_ID}, KEY_ID + "=?",
                new String[]{"0"}, null, null, null,
                null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            // return contact
            return user;
        }
        return (null);
    }
}
