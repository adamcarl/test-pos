package com.example.sydney.pos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sydney on 6/22/2016.
 */
public class LoginAdmin extends Activity {
    EditText username;
    EditText password;
    DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginadmin);
        helper = new DatabaseHelper(this);

    }

    public void login(View view)
    {
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String[] projectsions = {"id","username","password"};
        int idadmin = 1;
        String[] account = {Integer.toString(idadmin),usernameString,passwordString};
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("admin",projectsions,"id = ? AND username = ? AND password = ?",account,null,null,null);
        cursor.moveToFirst();



        int number = cursor.getCount();

        if(number>=1) {
            db.close();
            Intent intent = new Intent(this,AdminUI.class);
            intent.putExtra("firstname",""+usernameString+"");
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "incorrect", Toast.LENGTH_LONG).show();
            db.close();

        }



    }
    public void register(View view)
    {
        Intent intent = new Intent(this,Registration.class);
        intent.putExtra( "session", 1);
        startActivity(intent);


    }



}
