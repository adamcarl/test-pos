package com.example.sydney.pos;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sydney on 6/29/2016.
 */
public class AddItem extends Activity implements AdapterView.OnItemSelectedListener {
    EditText addItemName;
    EditText addItemPrice;
    EditText addItemDepartment;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;
    ArrayAdapter adapter3;
    Spinner spin;
    Spinner addItemCategory;
    Spinner addItemSubCategory;
    EditText addItemCode;
    EditText addItemQuantity;
    EditText editItemName;
    EditText editItemPrice;
    EditText editItemDepartment;
    EditText editItemCategory;
    EditText editItemSubCategory;
    EditText editItemCode;
    EditText editItemQuantity;
    EditText deleteItemCode;
    String itemdepartment;
    String itemcategory;
    String itemsubcategory;
    ArrayList<String> addDepartmentList = new ArrayList<String>();
    ArrayList<String> addCategoryList = new ArrayList<String>();
    ArrayList<String> addSubCategoryList = new ArrayList<String>();
    DatabaseHelper helper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additem);
        helper = new DatabaseHelper(this);
        addItemName = (EditText) findViewById(R.id.addItemName);
        addItemPrice = (EditText) findViewById(R.id.addItemPrice);
        addItemCode = (EditText) findViewById(R.id.addItemCode);
        addItemQuantity = (EditText) findViewById(R.id.addItemQuantity);
        editItemName = (EditText) findViewById(R.id.editItemName);
        editItemPrice = (EditText) findViewById(R.id.editItemPrice);
        editItemDepartment = (EditText) findViewById(R.id.editItemDepartment);
        editItemCategory = (EditText) findViewById(R.id.editItemCategory);
        editItemSubCategory = (EditText) findViewById(R.id.editItemSubCategory);
        editItemCode = (EditText) findViewById(R.id.editItemCode);
        editItemQuantity = (EditText) findViewById(R.id.editItemQuantity);
        deleteItemCode = (EditText)findViewById(R.id.deleteItemCode);
        addDepartmentList.add("Departments");
        addCategoryList.add("Category");
        addSubCategoryList.add("Subcategory");
        spin = (Spinner) findViewById(R.id.addItemDepartment);
        addItemCategory = (Spinner) findViewById(R.id.addItemCategory);
        addItemSubCategory = (Spinner) findViewById(R.id.addItemSubCategory);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addDepartmentList);
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addCategoryList);
        adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addSubCategoryList);
        spin.setAdapter(adapter);
        addItemCategory.setAdapter(adapter2);
        addItemSubCategory.setAdapter(adapter3);
        spin.setOnItemSelectedListener(this);
        addItemCategory.setOnItemSelectedListener(this);
        addItemSubCategory.setOnItemSelectedListener(this);
        SQLiteDatabase dbReader = helper.getReadableDatabase();
        Cursor cursor = dbReader.rawQuery("select DISTINCT department,category,subcategory FROM departments", null);


        if (cursor.moveToFirst()) {
            do {
                String temp1 = cursor.getString(cursor.getColumnIndex("department"));
                addDepartmentList.add(temp1);
                String temp2 = cursor.getString(cursor.getColumnIndex("category"));
                addCategoryList.add(temp2);
                String temp3 = cursor.getString(cursor.getColumnIndex("subcategory"));
                addSubCategoryList.add(temp3);
            } while (cursor.moveToNext());
        }


    }

    public void addItem(View view) {
        if (addItemName.getText().toString().trim().equals("") || addItemPrice.getText().toString().trim().equals("") || addItemCode.getText().toString().trim().equals("") || addItemQuantity.getText().toString().trim().equals("")) {
            String itemname = addItemName.getText().toString();
            String itemprice = addItemPrice.getText().toString();
            String itemcode = addItemCode.getText().toString();
            String itemquantity = addItemQuantity.getText().toString();

            try {

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("itemname", "" + itemname + "");
                cv.put("itemprice", itemprice);
                cv.put("itemquantity", itemquantity);
                cv.put("itemdepartment", "" + itemdepartment + "");
                cv.put("itemcategory", "" + itemcategory + "");
                cv.put("itemsubcategory", "" + itemsubcategory + "");
                cv.put("itemcode", "" + itemcode + "");
                long num = db.insert("products", null, cv);
                if (num != -1) {
                    Toast.makeText(getApplicationContext(), "New Row Added", Toast.LENGTH_SHORT).show();
                    addItemName.getText().clear();
                    addItemPrice.getText().clear();
                    addItemCode.getText().clear();
                    addItemQuantity.getText().clear();

                } else
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {


            }
        } else
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        helper.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.addItemDepartment) {
            for (int x = 0; x < adapter.getCount(); x++) {
                if (position == spin.getItemIdAtPosition(0)) {

                } else if (position == spin.getItemIdAtPosition(x)) {
                    itemdepartment = addDepartmentList.get(x);

                }
            }
        } else if (spinner.getId() == R.id.addItemCategory) {
            for (int x = 0; x < adapter2.getCount(); x++) {
                if (position == addItemCategory.getItemIdAtPosition(0)) {

                } else if (position == addItemCategory.getItemIdAtPosition(x)) {
                    itemcategory = addCategoryList.get(x);

                }
            }
        } else if (spinner.getId() == R.id.addItemSubCategory) {
            for (int x = 0; x < adapter3.getCount(); x++) {
                if (position == addItemSubCategory.getItemIdAtPosition(0)) {

                } else if (position == addItemSubCategory.getItemIdAtPosition(x)) {
                    itemsubcategory = addSubCategoryList.get(x);

                }
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void editItemSearch(View view) {
        try {
            SQLiteDatabase dbReader = helper.getReadableDatabase();
            String code = editItemCode.getText().toString();
            String[] array = {code};
            Cursor cursor = dbReader.rawQuery("SELECT itemname,itemprice,itemquantity,itemdepartment,itemcategory,itemsubcategory FROM products WHERE itemcode = ? ", array);
            cursor.moveToFirst();
            editItemName.setText(cursor.getString(cursor.getColumnIndex("itemname")));
            editItemPrice.setText(cursor.getString(cursor.getColumnIndex("itemprice")));
            editItemDepartment.setText(cursor.getString(cursor.getColumnIndex("itemdepartment")));
            editItemCategory.setText(cursor.getString(cursor.getColumnIndex("itemcategory")));
            editItemSubCategory.setText(cursor.getString(cursor.getColumnIndex("itemsubcategory")));
            editItemQuantity.setText(cursor.getString(cursor.getColumnIndex("itemquantity")));
            dbReader.close();
        } catch (Exception e) {
            Toast.makeText(this, "Please input correct item code", Toast.LENGTH_LONG).show();
        }
    }

    public void editItem(View view) {
        try {

            String editItemNameString = editItemName.getText().toString();
            String editItemPriceString = editItemPrice.getText().toString();
            String editItemDepartmentString = editItemDepartment.getText().toString();
            String editItemCategoryString = editItemCategory.getText().toString();
            String editItemSUbCategoryString = editItemSubCategory.getText().toString();
            String editItemCodeString = editItemCode.getText().toString();
            String editItemQuantityString = editItemQuantity.getText().toString();
            SQLiteDatabase dbWriter = helper.getWritableDatabase();
            dbWriter.execSQL("UPDATE products set itemname = '" + editItemNameString + "',itemprice = " + editItemPriceString + ", itemquantity = " + editItemQuantityString + ",itemdepartment = '" + editItemDepartmentString + "',itemcategory = '" + editItemCategoryString + "',itemsubcategory = '" + editItemSUbCategoryString + "' WHERE itemcode =" + editItemCodeString + "");
            dbWriter.close();
        } catch (Exception e) {
            Toast.makeText(this, "Not Changed", Toast.LENGTH_LONG).show();
        }


    }

    public void deleteSubmit(View view)
    {
        SQLiteDatabase dbWriter = helper.getWritableDatabase();
        String code = deleteItemCode.getText().toString();
        int codenumber = Integer.parseInt(code);
        dbWriter.execSQL("DELETE FROM products where itemcode = "+codenumber+"");
        dbWriter.close();   

    }

}
