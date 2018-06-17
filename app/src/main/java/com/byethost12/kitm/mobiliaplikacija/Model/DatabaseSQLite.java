package com.byethost12.kitm.mobiliaplikacija.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mokytojas on 2018-02-07.
 */

public class DatabaseSQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION   = 1;
    private static final String DATABASE_NAME   = "db";

    private static final String TABLE_USERS     = "users";
    private static final String USER_ID         = "id";
    private static final String USER_LEVEL      = "userlevel";
    private static final String USER_NAME       = "name";
    private static final String USER_PASSWORD   = "password";
    private static final String USER_EMAIL      = "email";

    private static final String TABLE_CHILDREN      = "children";
    private static final String CHILD_ID            = "id";
    private static final String CHILD_NAME          = "name";
    private static final String CHILD_SURNAME       = "surname";
    private static final String CHILD_PERSON_ID     = "personId";
    private static final String CHILD_EDUCATION     = "education";
    private static final String CHILD_ACTIVITIES    = "activities";
    private static final String CHILD_GROUP         = "grupe";
    //private static final String POKEMON_WEIGHT      = "weight"; //
    //private static final String POKEMON_HEIGHT      = "height"; //

    public DatabaseSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY,"
                + USER_LEVEL + " TEXT,"
                + USER_NAME + " TEXT,"
                + USER_PASSWORD + " TEXT,"
                + USER_EMAIL + ")";

        String CREATE_CHILDREN_TABLE = "CREATE TABLE " + TABLE_CHILDREN + "("
                + CHILD_ID + " INTEGER PRIMARY KEY,"
                + CHILD_NAME + " TEXT,"
                + CHILD_SURNAME + " TEXT,"
                + CHILD_PERSON_ID + " TEXT,"
                + CHILD_EDUCATION + " TEXT,"
                + CHILD_ACTIVITIES + " TEXT,"
                + CHILD_GROUP + " TEXT)";
                //+ POKEMON_WEIGHT + " REAL,"
                //+ POKEMON_HEIGHT + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CHILDREN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_LEVEL,      user.getUserlevel());
        values.put(USER_NAME,       user.getUsernameForRegister());
        values.put(USER_PASSWORD,   user.getPasswordForRegister());
        values.put(USER_EMAIL,      user.getEmailForRegister());

        // Inserting Row
        db.insert(TABLE_USERS, null, values);

        // Closing database connection
        db.close();
    }

    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{
                        USER_ID,
                        USER_LEVEL,
                        USER_NAME,
                        USER_PASSWORD,
                        USER_EMAIL
                },
                USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );

        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUserlevel(cursor.getString(1));
                user.setUsernameForRegister(cursor.getString(2));
                user.setPasswordForRegister(cursor.getString(3));
                user.setEmailForRegister(cursor.getString(4));

                // adding user to list
                users.add(user);
            } while (cursor.moveToNext());
        }

        // return users list
        return users;
    }

    public void addChild(Child child) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CHILD_NAME,        child.getName());
        values.put(CHILD_SURNAME,     child.getSurname());
        values.put(CHILD_PERSON_ID,   child.getPersonId());
        values.put(CHILD_EDUCATION,   child.getEducation());
        values.put(CHILD_ACTIVITIES,  child.getActivities());
        values.put(CHILD_GROUP,       child.getGroup());
        //values.put(POKEMON_WEIGHT,    child.getWeight());
        //values.put(POKEMON_HEIGHT,    child.getHeight());

        // Inserting Row
        db.insert(TABLE_CHILDREN, null, values);

        // Closing database connection
        db.close();
    }

    public List<Child> getAllChildren() {
        List<Child> vaikai = new ArrayList<Child>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHILDREN;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Child child = new Child();

                child.setId(Integer.parseInt(cursor.getString(0)));
                child.setName(cursor.getString(1));
                child.setSurname(cursor.getString(2));
                child.setPersonId(cursor.getString(3));
                child.setEducation(cursor.getString(4));
                child.setActivities(cursor.getString(5));
                child.setGroup(cursor.getString(6));
               // child.setWeight(cursor.getDouble(7));
               // child.setHeight(cursor.getDouble(8));

                // adding child to list
                vaikai.add(child);
            } while (cursor.moveToNext());
        }

        // return childrenSQLite list
        return vaikai;
    }

    public List<Child> getChildByName(String name) {
        List<Child> vaikai = new ArrayList<Child>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHILDREN + " WHERE name LIKE '%"+name+"%' OR surname LIKE '%"+name+"%'"  , null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Child child = new Child();

                child.setId(Integer.parseInt(cursor.getString(0)));
                child.setName(cursor.getString(1));
                child.setSurname(cursor.getString(2));
                child.setPersonId(cursor.getString(3));
                child.setEducation(cursor.getString(4));
                child.setActivities(cursor.getString(5));
                child.setGroup(cursor.getString(6));
                //child.setWeight(cursor.getDouble(7));
                //child.setHeight(cursor.getDouble(8));

                // adding user to list
                vaikai.add(child);
            } while (cursor.moveToNext());
        }

        // return childrenSQLite list
        return vaikai;

    }

    public Child getChild(int id) {
        Child child = new Child();

        List<Child> vaikai = new ArrayList<Child>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHILDREN + " WHERE id = " + id + "", null);
        if (cursor.moveToFirst()) {
            do {
                child.setId(Integer.parseInt(cursor.getString(0)));
                child.setName(cursor.getString(1));
                child.setSurname(cursor.getString(2));
                child.setPersonId(cursor.getString(3));
                child.setEducation(cursor.getString(4));
                child.setActivities(cursor.getString(5));
                child.setGroup(cursor.getString(6));
               // child.setWeight(cursor.getDouble(7));
               // child.setHeight(cursor.getDouble(8));

                vaikai.add(child);
            } while (cursor.moveToNext());
        }
        return vaikai.get(0);
    }


    public boolean isValidUser(String username, String password){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE "
                        + USER_NAME + "='" + username + "'AND " +
                        USER_PASSWORD + "='" + password + "'" , null);
        if (c.getCount() > 0)
            return true;
        return false;
    }

    public void updateChild(Child child){
        ContentValues cv = new ContentValues();
        cv.put(CHILD_NAME,child.getName());
        cv.put(CHILD_SURNAME,child.getSurname());
        cv.put(CHILD_PERSON_ID,child.getPersonId());
        cv.put(CHILD_EDUCATION,child.getEducation());
        cv.put(CHILD_ACTIVITIES,child.getActivities());
        cv.put(CHILD_GROUP,child.getGroup());
        //cv.put(POKEMON_WEIGHT,child.getWeight());
        //cv.put(POKEMON_HEIGHT,child.getHeight());

        getReadableDatabase().update(TABLE_CHILDREN, cv, " id = "+child.getId(), null);
    }

    public void deleteChild(int id) {

        getReadableDatabase().delete(TABLE_CHILDREN, CHILD_ID + "=?", new String[] {String.valueOf(id)});

        //Closing database connection;
        getReadableDatabase().close();
    }
}
