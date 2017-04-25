package com.example.sydney.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 * Created by sydney on 6/22/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "latest1.db";
    private static final int VERSION = 1;
    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS admin(id INTEGER,firstname TEXT,lastname TEXT,username TEXT UNIQUE,password TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS cashier(id INTEGER,firstname TEXT,lastname TEXT,username TEXT UNIQUE,password TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS products(itemname TEXT UNIQUE,itemprice REAL,itemquantity INTEGER,itemdepartment TEXT,itemcategory TEXT,itemsubcategory TEXT,itemcode INTEGER UNIQUE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS orders (itemname TEXT, itemprice REAL,itemcode INTEGER,orderquantity INTEGER,consecutivenumber INTEGER,invoicenumber INTEGER,transactionnumber INTEGER DEFAULT 1);");
        db.execSQL("CREATE TABLE IF NOT EXISTS cashierlog(date TEXT, time TEXT,firstname TEXT,lastname TEXT,username TEXT,transactionnumber INTEGER PRIMARY KEY AUTOINCREMENT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS sessions(time TEXT,date TEXT, username TEXT ); ");
        db.execSQL("CREATE TABLE IF NOT EXISTS receipts(invoicenumber INTEGER,transactionnumber INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS departments(department TEXT,category TEXT,subcategory TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
