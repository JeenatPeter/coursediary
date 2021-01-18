package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

public class ViewSyllabus extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Button b1,b4;
    Intent i1,i2,i3,i4;
    ListView l1;
    Spinner s1;
    String semlist[]={"Select","1","2","3","4","5","6"};
    SharedPreferences sp;
    String ip,url="",sem;
    public static ArrayList<String> cid,code,cname,syllabus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_syllabus);

        b1 = findViewById(R.id.homevsy);
//        b4 = findViewById(R.id.logoutvsy);
        s1=findViewById(R.id.spinner);
        l1=findViewById(R.id.viewsyllabuslv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");


        url ="http://"+ip+":5000/viewsyllabus";

        b1.setOnClickListener(this);
//        b4.setOnClickListener(this);
        ArrayAdapter<String>ad=new ArrayAdapter<>(ViewSyllabus.this,android.R.layout.simple_list_item_1,semlist);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);
        l1.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == b1)
        {
            i1=new Intent(getApplicationContext(),TeacherHome.class);
            startActivity(i1);
        }


//        if (v == b4)
//        {
//            i4=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(i4);
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==s1) {
            sem = semlist[i];
            cid = new ArrayList<>();
            code = new ArrayList<>();
            cname = new ArrayList<>();
            syllabus = new ArrayList<>();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(ViewSyllabus.this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.

                    try {
                        JSONArray jo = new JSONArray(response);
                        for (int i = 0; i < jo.length(); i++) {
                            JSONObject job = jo.getJSONObject(i);

                            cid.add(job.getString("Subject_id"));
                            code.add(job.getString("Course_Code"));
                            cname.add(job.getString("Course_Name"));
                            syllabus.add(job.getString("Syllabus"));


                        }
                        l1.setAdapter(new customview3(ViewSyllabus.this,code,cname,syllabus));

                    } catch (Exception e) {
                        Log.d("=========", e.toString());
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ViewSyllabus.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("sem", sem);


                    return params;
                }
            };


// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent ik=new Intent(getApplicationContext(),CoursePlan.class);
        ik.putExtra("syllabus",syllabus.get(i));
        startActivity(ik);
    }
}