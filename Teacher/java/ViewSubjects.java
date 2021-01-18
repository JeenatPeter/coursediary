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

public class ViewSubjects extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2;
    Intent i1,i2;
    ListView l1;
    SharedPreferences sp;
    String ip="",url="";
    public static ArrayList<String> sem,code,cname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subjects);

        b1 = findViewById(R.id.homevs);
//        b2 = findViewById(R.id.logoutvs);
        l1=findViewById(R.id.viewsubjectlv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");


        url ="http://"+ip+":5000/viewsubject";

        sem=new ArrayList<>();
        code=new ArrayList<>();
        cname=new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ViewSubjects.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.

                try {
                    JSONArray jo = new JSONArray(response);
                    for (int i=0;i<jo.length();i++){
                        JSONObject job=jo.getJSONObject(i);

                        sem.add(job.getString("Sem"));
                        code.add(job.getString("Course_Code"));
                        cname.add(job.getString("Course_Name"));


                    }

                    l1.setAdapter(new customview3(ViewSubjects.this,sem,code,cname));

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewSubjects.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("teacher_id",sp.getString("lid",""));






                return params;
            }
        };


// Add the request to the RequestQueue.
        queue.add(stringRequest);



        b1.setOnClickListener(this);
//        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == b1)
        {
            i1=new Intent(getApplicationContext(),TeacherHome.class);
            startActivity(i1);
        }

//        if (v == b2)
////        {
////            i2=new Intent(getApplicationContext(),MainActivity.class);
////            startActivity(i2);
////        }
    }
}