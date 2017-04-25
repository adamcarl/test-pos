package com.example.sydney.pos;

import android.app.Activity;
import android.content.Context;
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
public class LoginCashier extends Activity
{

    EditText username;
    EditText password;
    DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincashier);
        helper = new DatabaseHelper(this);

    }

    public void login(View view) {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String[] projectsions = {"id", "username", "password"};
        int idcashier = 2;
        String[] account = {Integer.toString(idcashier), usernameString, passwordString};
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("cashier", projectsions, "id = ? AND username = ? AND password = ?", account, null, null, null);
        int idIndex = cursor.getColumnIndex("id");
        int usernameIndex = cursor.getColumnIndex("username");
        int passwordIndex = cursor.getColumnIndex("password");
        cursor.moveToFirst();
        int number = cursor.getCount();

        if (number >= 1)
        {
            db.close();
            Context context = this;
            Intent intent = new Intent(context,CashierUI.class);
            intent.putExtra("firstname",""+usernameString+"");
            startActivity(intent);

        }
        else {
            Toast.makeText(getApplicationContext(), "INCORRECT", Toast.LENGTH_LONG).show();
            db.close();
        }

    }



    public void register(View view)
    {
        Intent intent = new Intent(this,Registration.class);
        intent.putExtra("session",2);
        startActivity(intent);
    }
}
