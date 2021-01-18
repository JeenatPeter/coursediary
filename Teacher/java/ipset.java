package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ipset extends AppCompatActivity implements View.OnClickListener {

    EditText e1;
    Button b1;
    Intent i1;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipset);

        e1 = findViewById(R.id.ipaddress);
        b1 = findViewById(R.id.submitip);
        sp1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String ip = e1.getText().toString();
        if(ip.equals(""))
        {
            e1.setError("enter ip");
        }
        else {
            SharedPreferences.Editor ed1 = sp1.edit();
            ed1.putString("ip", ip);
            ed1.commit();

            i1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i1);
        }
    }
}