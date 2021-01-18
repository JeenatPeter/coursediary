package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoursePlan extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2;
    Intent i1,i2;
    ListView l1;
    SharedPreferences sp;
    String ip,url="",sem;
    public static ArrayList<String> Topics,Working_hours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_plan);

        b1=findViewById(R.id.homecp);
        b2 = findViewById(R.id.logoutcp);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        l1=findViewById(R.id.list1);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");


        url ="http://"+ip+":5000/courseplan";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(CoursePlan.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Topics = new ArrayList<>();
                Working_hours = new ArrayList<>();
                try {
                    JSONArray jo = new JSONArray(response);
                    for (int i = 0; i < jo.length(); i++) {
                        JSONObject job = jo.getJSONObject(i);

                        Topics.add(job.getString("Topics"));

                        Working_hours.add(job.getString("Hours"));

                    }
                    l1.setAdapter(new Custom2(CoursePlan.this,Topics,Working_hours));

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CoursePlan.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("syllabus", getIntent().getStringExtra("syllabus"));


                return params;
            }
        };


// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {

        if (v == b1)
        {
            i1=new Intent(getApplicationContext(),TeacherHome.class);
            startActivity(i1);
        }

        if (v == b2)
        {
            i2=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i2);
        }
    }
}