package edu.wwu.csci412.mapapp.mapapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public Database(Context c) {
        super(c, "ContactDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "create table contacts(id integer primary key autoincrement, name text, phone text)";
        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("drop table if exists contacts");
        onCreate(db);
    }

    public void insert(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "insert into contacts values ( null, '" + c.getName() + "', '" + c.getPhone() + "' )";
        db.execSQL(q);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "delete from contacts where id = " + id;
        db.execSQL(q);
        db.close();
    }

    public void update(int id, String n, String p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "update contacts set name = '" + n +
                "', phone = '" + p + "' where id = " + id;
        db.execSQL(q);
        db.close();
    }

    public Contact select(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "select * from contacts where id = " + id;
        Cursor c = db.rawQuery(q, null);
        Contact r = null;
        if (c.moveToFirst()) r = new Contact(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2));
        db.close();
        return r;
    }

    public ArrayList<Contact> selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "select * from contacts";
        Cursor c = db.rawQuery(q, null);
        ArrayList<Contact> list = new ArrayList<Contact>();
        while (c.moveToNext()) {
            list.add(new Contact(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2)));
        }
        db.close();
        return list;
    }
}
