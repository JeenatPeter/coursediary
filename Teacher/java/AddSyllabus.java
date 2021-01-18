package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSyllabus extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button b1,b2,b3,b4;
    Intent i1,i3,i4;
    Spinner s1,s2;
    TextView t1;
    String semlist[]={"Select","1","2","3","4","5","6"};
    SharedPreferences sp;
    String ip,url="",sem;
    private static final int GALLERY_CODE = 201;
    private Uri mImageCaptureUri;
    private File outPutFile = null;
    String fpth = "",subject="",ex;

    public static ArrayList<String> cid,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_syllabus);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");


        url ="http://"+ip+":5000/Subjects";


        b1 = findViewById(R.id.homeas);
        b2 = findViewById(R.id.browseas);
        b3 = findViewById(R.id.submitas);
//        b4 = findViewById(R.id.logoutas);
        s1 = findViewById(R.id.spinnersem);
        s2 = findViewById(R.id.spinneras);
        t1 = findViewById(R.id.textView20);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
//        b4.setOnClickListener(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode
                        .ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }
        ArrayAdapter<String>ad=new ArrayAdapter<>(AddSyllabus.this,android.R.layout.simple_list_item_1,semlist);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);
        s2.setOnItemSelectedListener(this);

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
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), GALLERY_CODE);
            } catch (android.content.ActivityNotFoundException ex) { // Potentially direct the user to the Market with a Dialog
                Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == b3)
        { if(sem.equals("Select"))
        {
            Toast.makeText(getApplicationContext(), "Select sem", Toast.LENGTH_SHORT).show();
        }
        else if(subject.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Select course", Toast.LENGTH_SHORT).show();
        }
        else if(fpth.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Select file", Toast.LENGTH_SHORT).show();
        }

        else {
            ex = fpth.substring(fpth.lastIndexOf(".") + 1);
            if(ex.equals("txt")) {
                String resp = uploadFile(fpth);
                try {
                    JSONObject json = new JSONObject(resp);
                    String res = json.getString("task");
                    if (res.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), TeacherHome.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "pls select text file", Toast.LENGTH_LONG).show();
            }
        }
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

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(AddSyllabus.this);

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


                        }
                        ArrayAdapter<String> ad = new ArrayAdapter<>(AddSyllabus.this, android.R.layout.simple_list_item_1, code);
                        s2.setAdapter(ad);

                    } catch (Exception e) {
                        Log.d("=========", e.toString());
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(AddSyllabus.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        if(adapterView==s2)
        {
            subject=cid.get(i);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data)
        {
            try {
                mImageCaptureUri = data.getData();
                Uri selectedImage = data.getData();
                fpth=FileUtils.getPath(this,selectedImage);
                t1.setText(fpth);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
    private String getRealPathFromURI(Uri contentURI) {
        String path;
        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null)
            path=contentURI.getPath();

        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

            path=cursor.getString(idx);

        }
        if(cursor!=null)
            cursor.close();
        return path;
    }
    public String uploadFile(String sourceFileUri) {
        url = "http://" + sp.getString("ip", "") + ":5000/uploadsyllabus";
        Log.d("=======", sourceFileUri);
        String fileName = sourceFileUri;

        FileUpload fp = new FileUpload(fileName);
        Map mp = new HashMap<String, String>();
	    mp.put("subject_id",subject);
        mp.put("teacher_id", sp.getString("lid",""));

        String res = fp.multipartRequest(url, mp, fileName, "files", "application/octet-stream");
        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_LONG).show();
        return res;
    }
}