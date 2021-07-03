package sg.edu.np.s10194187_practical2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";
    public static final String TABLE_USER = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_FOLLOWED = "followed";

    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER
                +  "(" + COLUMN_NAME + " TEXT," + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FOLLOWED
                + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_DESCRIPTION, user.getDescription());
        values.put(COLUMN_FOLLOWED, user.getFollowed());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_DESCRIPTION, user.getDescription());
        values.put(COLUMN_FOLLOWED, user.getFollowed());
        SQLiteDatabase db = this.getWritableDatabase();


        db.update(TABLE_USER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public User findUser(String name) {
        String query = "SELECT * FROM " + TABLE_USER + " WHERE "
                + COLUMN_NAME
                + " = \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setId(Integer.parseInt(cursor.getString(2)));
            user.setName(cursor.getString(0));
            user.setDescription(cursor.getString(1));
            user.setFollowed(Boolean.parseBoolean(cursor.getString(3)));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    } //findUser

    public ArrayList<User> getUsers() {
        ArrayList<User> list = new ArrayList<User>();
        String query = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(Integer.parseInt(cursor.getString(2)));
            user.setName(cursor.getString(0));
            user.setDescription(cursor.getString(1));
            if(cursor.getString(3).equals("1")){
                user.setFollowed(true);
            } else {
                user.setFollowed(false);
            }


            list.add(user);
        }

        cursor.close();

        db.close();
        return list;
    } //findUser


}

