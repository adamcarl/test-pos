package com.example.sydney.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sydney on 6/28/2016.
 */
public class AdminUI extends Activity implements AdapterView.OnItemSelectedListener {
     String array_spinner[] = {"Transactions","Add/Edite/Delete Items","Add Departments","Generate report"};
     DatabaseHelper helper;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminui);
        Spinner spinner = (Spinner) findViewById(R.id.spin);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,array_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        String firstname = intent.getStringExtra("firstname");
        TextView view = (TextView) findViewById(R.id.text3);
        view.setText("Great Day "+firstname);
        TextClock textClock = (TextClock) findViewById(R.id.textClock);
        TextView viewDate = (TextView) findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy");
        String dateformatted = dateformat.format(c.getTime());
        viewDate.setText(dateformatted);
        helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+firstname+"') ");
        db.close();



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position==1)
        {
            Intent intent = new Intent(this,AddItem.class);
            startActivity(intent);
        }

        else if(position==2)
        {
            Intent intent = new Intent(this,Departments.class);
            startActivity(intent);
        }

        else if(position==3)
        {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
