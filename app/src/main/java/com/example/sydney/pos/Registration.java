package com.example.sydney.pos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by sydney on 6/23/2016.
 */
public class Registration extends Activity
{
    public EditText username;
    public EditText password;
    public EditText firstname;
    public EditText lastname;
    DatabaseHelper helper;
    Bundle bundle;
    int sessions;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        Intent intent = getIntent();
        sessions = intent.getIntExtra("session", 0);
        helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();






    }

    public void register(View view)
    {

        if(sessions==1 && sessions!=0) {
            username = (EditText) findViewById(R.id.username);
            String usernameString = username.getText().toString().trim();
            password = (EditText) findViewById(R.id.password);
            String passwordString = password.getText().toString().trim();
            firstname = (EditText) findViewById(R.id.firstName);
            String firstnameString = firstname.getText().toString().trim();
            lastname = (EditText) findViewById(R.id.lastName);
            String lastnameString = lastname.getText().toString().trim();
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();



            if(username.getText().toString().trim().equals("") || password.getText().toString().trim().equals("") || firstname.getText().toString().trim().equals("") ||
            lastname.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(), "INPUT ALL FIELDS", Toast.LENGTH_LONG).show();

            }

            else

            {


                cv.put("username", usernameString);
                cv.put("password", passwordString);
                cv.put("firstname", firstnameString);
                cv.put("lastname", lastnameString);
                cv.put("id", 1);
                db.insert("admin", null, cv);
                db.close();
                usernameString = null;
                password = null;
                firstname = null;
                lastname = null;
                Toast.makeText(getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_LONG).show();
                finish();
            }

        }

       else if(sessions==2 && sessions!=0) {
            username = (EditText) findViewById(R.id.username);
            String usernameString = username.getText().toString().trim();
            password = (EditText) findViewById(R.id.password);
            String passwordString = password.getText().toString().trim();
            firstname = (EditText) findViewById(R.id.firstName);
            String firstnameString = firstname.getText().toString().trim();
            lastname = (EditText) findViewById(R.id.lastName);
            String lastnameString = lastname.getText().toString().trim();
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();




            if(username.getText().toString().trim().equals("") || password.getText().toString().trim().equals("") || firstname.getText().toString().trim().equals("") ||
                    lastname.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(), "INPUT ALL FIELDS", Toast.LENGTH_LONG).show();

            }

            else

            {
                cv.put("username", usernameString);
                cv.put("password", passwordString);
                cv.put("firstname", firstnameString);
                cv.put("lastname", lastnameString);
                cv.put("id", 2);
                db.insert("cashier", null, cv);
                db.close();
                usernameString = null;
                password = null;
                firstname = null;
                lastname = null;

                Toast.makeText(getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void exit(View view)
    {
        finish();
    }


}
