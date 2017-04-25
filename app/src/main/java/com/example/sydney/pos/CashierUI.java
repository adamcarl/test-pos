package com.example.sydney.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import hdx.HdxUtil;

/**
 * Created by sydney on 6/23/2016.
 */
public class CashierUI extends Activity
{
    int counter;
    int temp = 0;
    int temp2 = 1;
    double vattable;
    double vat;
    double vattable2;
    double vat2;
    double cash;
    double totalPrice;
    int dialogVar;
    Cursor cursor;
    double change;
     DatabaseHelper helper;
    ContentValues cv;
    int invoicenumber;
    int transactionnumber;
    TextView selection;
    ArrayList<String> items;
    TextView searchText;
    int codenumber;
    String itemnameCol;
    double itempriceCol;
    int itemquantityCol;
    String itemdepartmentCol;
    String itemcategoryCol;
    int quantityCount = 0;
    String itemsubcategoryCol;
    ArrayList<Integer>itemQuantityList = new ArrayList<Integer>();
    ArrayList<Double>itemPriceList = new ArrayList<Double>();
    ArrayList<String>itemNameList = new ArrayList<String>();
    ArrayList<Integer>itemCodeList  = new ArrayList<Integer>();
    int itemcodeCol;
    String firstname;
    SerialPrinter mSerialPrinter = SerialPrinter.GetSerialPrinter();
    PowerManager.WakeLock lock;
    TableLayout layout;
    ArrayList<String> products = new ArrayList<String>();
    SQLiteDatabase dbReader;
    SQLiteDatabase dbWriter;
    protected void onCreate(Bundle savedInstanceState)
    {
         helper = new DatabaseHelper(this);
         dbReader = helper.getReadableDatabase();
         dbWriter = helper.getWritableDatabase();
         cv = new ContentValues();

        try {
            mSerialPrinter.OpenPrinter(new SerialParam(9600, "/dev/ttyS3", 0), new SerialDataHandler());
            HdxUtil.SetPrinterPower(1);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashierui);
        Intent intent = getIntent();
        firstname = intent.getStringExtra("firstname");
        TextView viewSession = (TextView) findViewById(R.id.text);
        searchText =(TextView) findViewById(R.id.searchText);
        layout = (TableLayout) findViewById(R.id.table);
        viewSession.setText("Great Day "+firstname);
        TextClock textClock = (TextClock) findViewById(R.id.textClock);
        TextView viewDate = (TextView) findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd.yyyy");
        String dateformatted = dateformat.format(c.getTime());
        viewDate.setText(dateformatted);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(new Date());

        dbWriter.execSQL("INSERT INTO sessions(time,date,username) VALUES(time('now'),date('now'),'"+firstname+"') ");



        items = new ArrayList<String>();
        items.add("Name");
        items.add("Quantity");
        items.add("Price");
        products.add("     ABZTRAK INC CONVENIENCE STORE");
        products.add("2nd Floor, #670,");
        products.add("Sgt. Bumatay St, Mandaluyong");
        products.add("NCR, Philippines");
        products.add("             Cash Invoice");
        products.add("Date   :"+dateformatted+" "+currentTime+"");
        products.add("--------------------------------------");
        products.add("Name          Quantity           Price");
        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new ArrayAdapter<String>(this,R.layout.cell,items));

    }

    public void search(View view)
    {

        int code = Integer.parseInt(searchText.getText().toString());
        final String[] itemcode = {Integer.toString(code)};
        String[] projections = {"itemcode","itemname","itemquantity","itemprice"};
        cursor = dbReader.query("products",projections,"itemcode = ?",itemcode,null,null,null);
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if(rows>=1) {

            // 1. Instantiate an AlertDialog.Builder with its constructor
            final EditText dialogText = new EditText(this);
            dialogText.setInputType(InputType.TYPE_CLASS_NUMBER);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Quantity");
            builder.setView(dialogText);
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {

                    dialogVar = Integer.parseInt(dialogText.getText().toString());
                    itempriceCol = cursor.getInt(cursor.getColumnIndex("itemprice"));
                    itemnameCol = cursor.getString(cursor.getColumnIndex("itemname"));
                    itemcodeCol = cursor.getInt(cursor.getColumnIndex("itemcode"));
                    itemQuantityList.add(dialogVar);
                    itemPriceList.add( itempriceCol);
                    itemNameList.add(itemnameCol);
                    itemCodeList.add(itemcodeCol);
                    itempriceCol = dialogVar * itempriceCol;
                    ArrayList<Double>total = new ArrayList<Double>();
                    total.add(itempriceCol);
                    items.add("" + itemnameCol + "");
                    items.add(""+dialogVar+"");
                    items.add("" + itempriceCol + "");
                    cursor.close();
                    layout.invalidate();
                    layout.requestLayout();
                    for(int x =0;x<total.size();x++) {
                        totalPrice = totalPrice += total.get(x);
                        quantityCount++;
                    }

                    DecimalFormat format = new DecimalFormat("#.##");
                    vattable = totalPrice / 1.12;
                    vat = vattable * 0.12;
                    vattable2 =  Double.valueOf(format.format(vattable));
                    vat2 = Double.valueOf(format.format(vat));

                    products.add(""+itemnameCol+"\t\t"+dialogVar+"\t \t \t"+itempriceCol+"");

                }
            });
            Dialog dialog = builder.create();
            dialog.show();

        }
        else
            Toast.makeText(this, "Product cant be found",
                    Toast.LENGTH_LONG).show();

    }

    public void print(View view)
    {

        cursor = dbReader.rawQuery("SELECT MAX(transactionnumber) AS maxix from receipts", null);
        cursor.moveToFirst();
        temp = cursor.getInt(cursor.getColumnIndex("maxix"));

        if(temp<1)
        {

            for(int x=0;x<itemPriceList.size();x++)
            {
                cv.put("itemname",""+itemNameList.get(x)+"");
                cv.put("itemprice", "" + itemPriceList.get(x) + "");
                cv.put("itemcode", "" + itemCodeList.get(x)+ "");
                cv.put("orderquantity", "" +itemQuantityList.get(x) + "");
                temp2 = 1;
                dbWriter.insert("orders", null, cv);
            }
            dbWriter.execSQL("INSERT INTO receipts(transactionnumber) values(1)");
            itemQuantityList.clear();
            itemPriceList.clear();
            itemNameList.clear();
            itemCodeList.clear();
            cursor.close();

            Toast.makeText(this,"aw",Toast.LENGTH_LONG).show();
        }
        else
        {
                dbWriter.execSQL("UPDATE receipts set transactionnumber = transactionnumber + 1");
                cursor = dbReader.rawQuery("SELECT MAX(transactionnumber) AS maxix from receipts", null);
                cursor.moveToFirst();
                temp2 = cursor.getInt(cursor.getColumnIndex("maxix"));


            for(int x=0;x<itemPriceList.size();x++)
            {
                cv.put("itemname",""+itemNameList.get(x)+"");
                cv.put("itemprice", "" + itemPriceList.get(x) + "");
                cv.put("itemcode", "" + itemCodeList.get(x)+ "");
                cv.put("orderquantity", "" +itemQuantityList.get(x) + "");
                cv.put("transactionnumber",temp2);
                dbWriter.insert("orders", null, cv);
            }

            dbWriter.execSQL("INSERT INTO receipts(transactionnumber) values("+temp2+") ");
            dbWriter.execSQL("INSERT INTO receipts(invoicenumber) values("+temp2+")");
            itemQuantityList.clear();
            itemPriceList.clear();
            itemNameList.clear();
            itemCodeList.clear();


        }

        try {
            products.add("--------------------------------------");
            products.add("Invoice Number "+temp2+"");
            products.add(quantityCount+" item(s)"+"\t\tSubtotal\t"+totalPrice+"");
            products.add("\t\tCash\t\t"+cash+"");
            products.add("\t\tVattable"+""+"\t\t" +vattable2);
            products.add("\t\tVat"+""+"\t\t " +vat2);
            products.add("\t\tTotal"+" \t\t"+totalPrice+"");
            mSerialPrinter.sydneyDotMatrix7by7();
            mSerialPrinter.printString(products);
            mSerialPrinter.walkPaper(50);
            mSerialPrinter.sendLineFeed();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally {
            itemQuantityList.clear();
            itemPriceList.clear();
            itemNameList.clear();
            itemCodeList.clear();
            finish();

        }

    }



    private void sleep(int ms) {

        try
        {
            java.lang.Thread.sleep(ms);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class SerialDataHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
                    SerialPortOperaion.SerialReadData data = (SerialPortOperaion.SerialReadData)msg.obj;
                    StringBuilder sb=new StringBuilder();
                    for(int x=0;x<data.size;x++)
                        sb.append(String.format("%02x", data.data[x]));

            }
        }
    }
}
