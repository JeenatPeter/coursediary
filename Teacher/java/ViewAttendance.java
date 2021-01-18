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

public class ViewAttendance extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    Button b1,b2;
    Intent i1,i2;
    Spinner s1;
    String mothid="",url="",ip;
    ArrayList<String>sname,regno,att;
    SharedPreferences sp;
    ListView lv;
    String month[]={"Select","Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        b1 = findViewById(R.id.homeva);
//        b2 = findViewById(R.id.logoutva);
        s1=findViewById(R.id.spinner2);
        lv=findViewById(R.id.viewattenance);
        ArrayAdapter<String>ad=new ArrayAdapter<>(ViewAttendance.this,android.R.layout.simple_list_item_1,month);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i1=new Intent(getApplicationContext(),TeacherHome.class);
                startActivity(i1);
            }
        });


        url ="http://"+ip+":5000/viewattendance";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==s1) {

            String m = month[i];
            if(m.equals("Jan"))
            {
               mothid="1";
            }
            else if(m.equals("Feb"))
            {
                mothid="2";
            }
            else if(m.equals("March"))
            {
                mothid="3";
            }
            else if(m.equals("April"))
            {
                mothid="4";
            }
            else if(m.equals("May"))
            {
                mothid="5";
            }
            else if(m.equals("June"))
            {
                mothid="6";
            }
            else if(m.equals("July"))
            {
                mothid="7";
            }
            else if(m.equals("Aug"))
            {
                mothid="8";
            }
            else if(m.equals("Sept"))
            {
                mothid="9";
            }
            else if(m.equals("Oct"))
            {
                mothid="10";
            }
            else if(m.equals("Nov"))
            {
                mothid="11";
            }
            else if(m.equals("Dec"))
            {
                mothid="12";
            }
            sname = new ArrayList<>();
            regno = new ArrayList<>();
            att = new ArrayList<>();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(ViewAttendance.this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the response string.

                    try {
                        JSONArray jo = new JSONArray(response);
                        for (int i = 0; i < jo.length(); i++) {
                            JSONObject job = jo.getJSONObject(i);

                            sname.add(job.getString("first_name")+job.getString("last_name"));
                            regno.add(job.getString("Register_number"));
                            att.add(job.getString("attendance"));


                        }
                        lv.setAdapter(new customview3(ViewAttendance.this,sname,regno,att));

                    } catch (Exception e) {
                        Log.d("=========", e.toString());
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(ViewAttendance.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("month", mothid);


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
}