package com.example.course_diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText e1,e2;
    Button b1;
    Intent i1;
    SharedPreferences sp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = findViewById(R.id.uname);
        e2 = findViewById(R.id.password);
        b1 = findViewById(R.id.loginbutton);
        sp1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        final String username=e1.getText().toString();
        final String password=e2.getText().toString();
        if(username.equals(""))
        {
            e1.setError("enter username");
        }
        else if(password.equals(""))
        {
            e2.setError("enter password");
        }
        else
        {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="http://"+sp1.getString("ip","")+":5000/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
               try {
                    JSONObject json=new JSONObject(response);
                    String res=json.getString("task");
                    if(res.equalsIgnoreCase("error"))
                    {
                        Toast.makeText(getApplicationContext(),"invalid user",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SharedPreferences.Editor ed1 = sp1.edit();
                        ed1.putString("lid", res);
                        ed1.commit();
                        startActivity(new Intent(getApplicationContext(),TeacherHome.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("uname", username);
                params.put("password", password);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }}

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder ald=new AlertDialog.Builder(MainActivity.this);
        ald.setTitle("Do you want to Exit")
                .setPositiveButton(" YES ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent in=new Intent(Intent.ACTION_MAIN);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.addCategory(Intent.CATEGORY_HOME);
                        startActivity(in);
                    }
                })
                .setNegativeButton(" NO ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog al=ald.create();
        al.show();

    }

}