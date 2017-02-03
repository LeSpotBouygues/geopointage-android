package sm20_corp.geopointage.Module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import sm20_corp.geopointage.Model.Tach;
import sm20_corp.geopointage.Model.TimeStamp;
import sm20_corp.geopointage.Model.User;

/**
 * Created by gun on 02/11/2016.
 * Geopointage
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables

    private static DatabaseHandler mInstance = null;
    private Context mContext;

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "Geapointage";

    private static final String KEY_ID = "rowId";

    // Tables names

    private static final String TABLE_USER = "user";
    private static final String TABLE_CHEF = "chef";
    private static final String TABLE_RECENT = "recent";
    private static final String TABLE_TIME = "time";
    private static final String TABLE_CURENT_TACH = "curentTach";
    private static final String TABLE_TACH = "tach";


    // user Table Columns names
    private static final String KEY_USER_LASTNAME = "lastName";
    private static final String KEY_USER_FIRSTNAME = "firtName";
    private static final String KEY_USER_ID = "id";

    // chef Table Columns names
    private static final String KEY_CHEF_LASTNAME = "lastName";
    private static final String KEY_CHEF_FIRSTNAME = "firtName";
    private static final String KEY_CHEF_ID = "id";

    // recent Table Columns names
    private static final String KEY_RECENT_LASTNAME = "lastName";
    private static final String KEY_RECENT_FIRSTNAME = "firtName";
    private static final String KEY_RECENT_ID = "id";
    private static final String KEY_RECENT_PLACE = "place";

    // time Table Columns names
    private static final String KEY_TIME_STATUS = "status";
    private static final String KEY_TIME_TIMESTAMP = "timestamp";


    // curentTach Table Columns names
    private static final String KEY_CURENT_TACH_IOTP = "status";
    private static final String KEY_CURENT_TACH_ADDRESS = "address";
    private static final String KEY_CURENT_TACH_IDCOLLABORATEUR = "Idcollaborateur";
    private static final String KEY_CURENT_TACH_TIME = "timestamp";

    // curentTach Table Columns names
    private static final String KEY_TACH_IOTP = "status";
    private static final String KEY_TACH_ADDRESS = "address";
    private static final String KEY_TACH_IDCOLLABORATEUR = "Idcollaborateur";
    private static final String KEY_TACH_TIME = "timestamp";

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

        String CREATE_RECENT_TABLE = "CREATE TABLE " + TABLE_RECENT + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_RECENT_FIRSTNAME + " text,"
                + KEY_RECENT_ID + " text,"
                + KEY_RECENT_PLACE + " text,"
                + KEY_RECENT_LASTNAME + " text" + ");";
        db.execSQL(CREATE_RECENT_TABLE);

        String CREATE_TIME_TABLE = "CREATE TABLE " + TABLE_TIME + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_TIME_STATUS + " text,"
                + KEY_TIME_TIMESTAMP + " text" + ");";
        db.execSQL(CREATE_TIME_TABLE);

        String CREATE_CURENT_TACH = "CREATE TABLE " + TABLE_CURENT_TACH + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_CURENT_TACH_ADDRESS + " text,"
                + KEY_CURENT_TACH_IOTP + " text,"
                + KEY_CURENT_TACH_IDCOLLABORATEUR + " text,"
                + KEY_CURENT_TACH_TIME + " text" + ");";
        db.execSQL(CREATE_CURENT_TACH);

        String CREATE_TACH = "CREATE TABLE " + TABLE_TACH + "("
                + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_TACH_ADDRESS + " text,"
                + KEY_TACH_IOTP + " text,"
                + KEY_TACH_IDCOLLABORATEUR + " text,"
                + KEY_TACH_TIME + " text" + ");";
        db.execSQL(CREATE_TACH);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHEF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURENT_TACH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TACH);
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
       // System.out.println("user add " + cnt + " (" + idDb + ")lastName = " + user.getLastName() + "\nfirstName = " + user.getFirstName() + "\n id = " + user.getId());

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
                                KEY_USER_ID}, KEY_USER_ID + "=?",
                        new String[]{id}, null, null, null,
                        null);
                break;
        }
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            /*System.out.println("iddb = " + cursor.getString(0));
            System.out.println("lastname = " + cursor.getString(1));
            System.out.println("firstname = " + cursor.getString(2));
            System.out.println("id = " + cursor.getString(3));*/
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
        final ArrayList<User> arrayListUser = new ArrayList<User>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            /*System.out.println("i = " + i);
            System.out.println("iddb = " + cursor.getString(0));
            System.out.println("lastname = " + cursor.getString(1));
            System.out.println("firstname = " + cursor.getString(2));
            System.out.println("id = " + cursor.getString(3));*/
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            arrayListUser.add(user);
            i++;

            while (cursor.moveToNext()) {
               /* System.out.println("i = " + i);
                System.out.println("iddb = " + cursor.getString(0));
                System.out.println("lastname = " + cursor.getString(1));
                System.out.println("firstname = " + cursor.getString(2));
                System.out.println("id = " + cursor.getString(3));*/
                user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                arrayListUser.add(user);
                i++;
            }
            cursor.close();
            if (arrayListUser.size() > 0) {
                Collections.sort(arrayListUser, new Comparator<User>() {
                    @Override
                    public int compare(User p1, User p2) {
                        int res = p1.getLastName().compareToIgnoreCase(p2.getLastName());
                        if (res != 0)
                            return res;
                        return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
                    }
                });
            }
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

    public boolean removeChef() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_CHEF, KEY_ID + "=?", new String[]{"0"}) > 0;
    }

    //update CHEF in db
    public long updateChef(User user) {
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
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_CHEF);
        if (cnt != 0) {
            System.out.println("update chef faile");
            return (-1);
        }
        values.put(KEY_ID, cnt);
        values.put(KEY_CHEF_LASTNAME, user.getLastName());
        values.put(KEY_CHEF_FIRSTNAME, user.getFirstName());
        values.put(KEY_CHEF_ID, user.getId());

        long id = db.insert(TABLE_CHEF, null, values);
        db.close(); // Closing database connection
        return (id);
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
            //System.out.println("db = " + user.toString());
            return user;
        }
        return (null);
    }

    public String addRecent(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        User tmpUser = getRecent("", "", user.getId(), 0);
        if (tmpUser != null)
            return "";
        ArrayList<User> arrayList = getAllRecent();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_RECENT);

        if ((arrayList != null && arrayList.size() < 5) || cnt == 0) {
            values.put(KEY_ID, cnt);
            values.put(KEY_RECENT_LASTNAME, user.getLastName());
            values.put(KEY_RECENT_FIRSTNAME, user.getFirstName());
            values.put(KEY_RECENT_ID, user.getId());
            values.put(KEY_RECENT_PLACE, cnt);
            long idDb = db.insert(TABLE_RECENT, null, values);
            System.out.println("add recent <5");
            //System.out.println(user.toString());
            db.close(); // Closing database connection
            return (Long.toString(cnt));
        } else {
            arrayList = getAllRecent();
            System.out.println("befor arraylist size = " + arrayList.size());
            db.delete(TABLE_RECENT, KEY_RECENT_PLACE + "=?", new String[]{"0"});
            arrayList = getAllRecent();
            System.out.println("affter arraylist size = " + arrayList.size());
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println("etat update = " + updateRecent(arrayList.get(i)));
            }
            System.out.println("after update" + arrayList.toString());
            db = this.getWritableDatabase();
            values = new ContentValues();
            cnt = DatabaseUtils.queryNumEntries(db, TABLE_RECENT);
            // values.put(KEY_ID, cnt);
            values.put(KEY_RECENT_LASTNAME, user.getLastName());
            values.put(KEY_RECENT_FIRSTNAME, user.getFirstName());
            values.put(KEY_RECENT_ID, user.getId());
            values.put(KEY_RECENT_PLACE, 4);
            long idDb = db.insert(TABLE_RECENT, null, values);
            System.out.println("add recent after dellete <5");
            arrayList = getAllRecent();
            db.close(); // Closing database connection
            return (Long.toString(cnt));
        }
    }

    //update LAST in db
    public long updateRecent(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.query(TABLE_RECENT, new String[]{KEY_ID,
                        KEY_RECENT_LASTNAME,
                        KEY_RECENT_FIRSTNAME,
                        KEY_RECENT_ID,
                        KEY_RECENT_PLACE}, KEY_RECENT_ID + "=?",
                new String[]{user.getId()}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();

            values.put(KEY_RECENT_LASTNAME, user.getLastName());
            values.put(KEY_RECENT_FIRSTNAME, user.getFirstName());
            values.put(KEY_RECENT_ID, user.getId());
            values.put(KEY_RECENT_PLACE, Integer.toString(user.getPlace() - 1));
            // updating row
            System.out.println("update ok");
            System.out.println("value = " + values.toString());
            return db.update(TABLE_RECENT, values, KEY_RECENT_ID + "=?", new String[]{String.valueOf(user.getId())});
        }
        db.close(); // Closing database connection
        System.out.println("update faile");
        return (-1);
    }

    public ArrayList<User> getAllRecent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        System.out.println("get all recent");

        //  Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID, KEY_USER_PASSWORD_ID, KEY_USER_EMAIL_ID, KEY_USER_ID}, null, null, null, null, null);

        cursor = db.query(TABLE_RECENT,
                new String[]{KEY_ID,
                        KEY_RECENT_LASTNAME,
                        KEY_RECENT_FIRSTNAME,
                        KEY_RECENT_ID,
                        KEY_RECENT_PLACE},
                null, null, null, null,
                null);

        User user = null;
        final ArrayList<User> arrayListUser = new ArrayList<User>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            arrayListUser.add(user);
            i++;
            while (cursor.moveToNext()) {
                user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                arrayListUser.add(user);
                i++;
            }
            cursor.close();
            if (arrayListUser.size() > 0) {
                Collections.sort(arrayListUser, new Comparator<User>() {
                    @Override
                    public int compare(User p1, User p2) {
                        int res = p1.getLastName().compareToIgnoreCase(p2.getLastName());
                        if (res != 0)
                            return res;
                        return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
                    }
                });
            }
            //System.out.println("getAllrecent  = " + arrayListUser.size() + "  " + arrayListUser.toString());
            return arrayListUser;
        }
        System.out.println("aucun getAllrecent trouvé");
        return (null);
    }

    public User getRecent(String lastName, String firstName, String id, int search) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        switch (search) {
            case 0:
                cursor = db.query(TABLE_RECENT,
                        new String[]{KEY_ID,
                                KEY_RECENT_LASTNAME,
                                KEY_RECENT_FIRSTNAME,
                                KEY_RECENT_ID,
                                KEY_RECENT_PLACE}, KEY_RECENT_ID + "=?",
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
            System.out.println("place = " + cursor.getString(4));
            user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            cursor.close();
            System.out.println("1 recent found");
            return user;
        }
        System.out.println("aucun recent trouvé");
        return (null);
    }


    //add time in db from string
    public String addTime(TimeStamp timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_TIME);
        values.put(KEY_ID, cnt);
        values.put(KEY_TIME_STATUS, timeStamp.getStatus());
        values.put(KEY_TIME_TIMESTAMP, timeStamp.getTimeStamp());
        long idDb = db.insert(TABLE_TIME, null, values);
        System.out.println("time add " + cnt + " idDb" + idDb);
        //System.out.println(timeStamp.toString());
        db.close(); // Closing database connection
        return (Long.toString(cnt));
    }

    public ArrayList<TimeStamp> getAllTimeStamp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.query(TABLE_TIME,
                new String[]{KEY_ID,
                        KEY_TIME_STATUS,
                        KEY_TIME_TIMESTAMP},
                null, null, null, null,
                null);

        TimeStamp timeStamp = null;
        final ArrayList<TimeStamp> arrayListTimeStamp = new ArrayList<TimeStamp>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            timeStamp = new TimeStamp(cursor.getString(1), Long.valueOf(cursor.getString(2)));
            arrayListTimeStamp.add(timeStamp);
            i++;
            while (cursor.moveToNext()) {
                timeStamp = new TimeStamp(cursor.getString(1), Long.valueOf(cursor.getString(2)));
                arrayListTimeStamp.add(timeStamp);
                i++;
            }
            cursor.close();
            //System.out.println("getallTimeStamp" + arrayListTimeStamp.toString());
            return arrayListTimeStamp;
        }
        System.out.println("aucun getAlltimeStamp trouvé");
        return (null);
    }

    public boolean removeTimeStamp() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_TIME, null, null) > 0;
    }

    //add time in db from string
    public String addCurentTach(Tach tach) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_CURENT_TACH);
        values.put(KEY_ID, cnt);
        values.put(KEY_CURENT_TACH_ADDRESS, tach.getAddress());
        values.put(KEY_CURENT_TACH_IOTP, tach.getIotp());
        values.put(KEY_CURENT_TACH_IDCOLLABORATEUR, tach.getIdCollaborateur());
        values.put(KEY_CURENT_TACH_TIME, tach.getTimeStamp());

        long idDb = db.insert(TABLE_CURENT_TACH, null, values);
        System.out.println("curentTach add " + cnt + " (" + idDb);
        System.out.println(tach.toString());
        db.close(); // Closing database connection
        return (Long.toString(cnt));
    }

    public Tach getCurentTach() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(TABLE_CURENT_TACH,
                new String[]{KEY_ID,
                        KEY_CURENT_TACH_ADDRESS,
                        KEY_CURENT_TACH_IOTP,
                        KEY_CURENT_TACH_IDCOLLABORATEUR,
                        KEY_CURENT_TACH_TIME}, KEY_ID + "=?",
                new String[]{"0"}, null, null, null,
                null);

        Tach tach = null;
        if (cursor != null && cursor.moveToFirst()) {
            System.out.println("iddb = " + cursor.getString(0));
            tach = new Tach(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            System.out.println(tach.toString());
            cursor.close();
            System.out.println("1 CurentTach found");
            return tach;
        }
        System.out.println("aucun curentTach trouvé");
        return (null);
    }


    public ArrayList<Tach> getAllCurentTach() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.query(TABLE_CURENT_TACH,
                new String[]{KEY_ID,
                        KEY_CURENT_TACH_ADDRESS,
                        KEY_CURENT_TACH_IOTP,
                        KEY_CURENT_TACH_IDCOLLABORATEUR,
                        KEY_CURENT_TACH_TIME},
                null, null, null, null,
                null);

        Tach tach = null;
        final ArrayList<Tach> arrayListCurentTach = new ArrayList<Tach>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            tach = new Tach(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            arrayListCurentTach.add(tach);
            i++;

            while (cursor.moveToNext()) {
                tach = new Tach(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                arrayListCurentTach.add(tach);
                i++;
            }
            cursor.close();
            System.out.println("getallCurent = " + arrayListCurentTach.toString());
            return arrayListCurentTach;
        }
        System.out.println("aucun curentTache trouvé");
        return (null);
    }

    public boolean removeCuentTach() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_CURENT_TACH, null, null) > 0;
    }

    //add time in db from string
    public String addTach(Tach tach) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_TACH);
        values.put(KEY_ID, cnt);
        values.put(KEY_TACH_ADDRESS, tach.getAddress());
        values.put(KEY_TACH_IOTP, tach.getIotp());
        values.put(KEY_TACH_IDCOLLABORATEUR, tach.getIdCollaborateur());
        values.put(KEY_TACH_TIME, tach.getTimeStamp());

        long idDb = db.insert(TABLE_TACH, null, values);
        System.out.println("tach add " + cnt + " (" + idDb);
        System.out.println(tach.toString());
        db.close(); // Closing database connection
        return (Long.toString(cnt));
    }

    public ArrayList<Tach> getAllTach() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.query(TABLE_TACH,
                new String[]{KEY_ID,
                        KEY_TACH_ADDRESS,
                        KEY_TACH_IOTP,
                        KEY_TACH_IDCOLLABORATEUR,
                        KEY_TACH_TIME},
                null, null, null, null,
                null);

        Tach tach = null;
        final ArrayList<Tach> arrayListTach = new ArrayList<Tach>();
        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            tach = new Tach(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            arrayListTach.add(tach);
            i++;

            while (cursor.moveToNext()) {
                tach = new Tach(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                arrayListTach.add(tach);
                i++;
            }
            cursor.close();
            System.out.println("getallTach = " + arrayListTach.toString());
            return arrayListTach;
        }
        System.out.println("aucun allTache trouvé");
        return (null);
    }

    public boolean removeTach() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_TACH, null, null) > 0;
    }
}
