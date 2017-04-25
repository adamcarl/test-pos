package com.example.sydney.pos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sydney on 7/12/2016.
 */
public class Departments extends Activity {
    EditText department;
    EditText category;
    EditText subcategory;
    DatabaseHelper helper;
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.departments);
        helper = new DatabaseHelper(this);
        department = (EditText) findViewById(R.id.department);
        category = (EditText) findViewById(R.id.category);
        subcategory = (EditText) findViewById(R.id.subcategory);

    }

    public void submit(View view) {
        String departmentString = department.getText().toString();
        String categoryString = category.getText().toString();
        String subcategoryString = subcategory.getText().toString();
        try {
            SQLiteDatabase dbWriter = helper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("department", departmentString);
            cv.put("category", categoryString);
            cv.put("subcategory",subcategoryString);
            dbWriter.insert("departments", null, cv);
            Toast.makeText(Departments.this, "Successful", Toast.LENGTH_SHORT).show();
            department.getText().clear();
            category.getText().clear();

        } catch (Exception e)
        {
            Toast.makeText(Departments.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}