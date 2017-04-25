package com.example.sydney.pos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void admin(View view)
    {
        Context context = this;
        Intent intent = new Intent(context,LoginAdmin.class);
        startActivity(intent);

    }

    public void cashier(View view)
    {
        Context context = this;
        Intent intent = new Intent(context,LoginCashier.class);
        startActivity(intent);

    }
}