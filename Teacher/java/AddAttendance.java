package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
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
import android.widget.EditText;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAttendance extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button b1,b2,b3;
    Intent i1,i2,i3,i4;
    Spinner s1,s2;
    TextView t1;
    String semlist[]={"Select","1","2","3","4","5","6"};
    SharedPreferences sp;
    String ip,url;
    private Uri mImageCaptureUri;
    private File outPutFile = null;
    String fpth = "";
    String  fname = "",sem,subject;
    public static ArrayList<String> cid,code;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ip","");
        fname = new SimpleDateFormat("yyyyMMhhddmmss").format(new Date()) + ".jpg";
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), fname);







        url ="http://"+ip+":5000/Subjects";

        b1 = findViewById(R.id.homeaa);
        b2 = findViewById(R.id.browseaa);
        b3 = findViewById(R.id.submitaa);
        s1 = findViewById(R.id.spinneraa);
        s2 = findViewById(R.id.spinercou);
        t1 = findViewById(R.id.textView14);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
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
        ArrayAdapter<String> ad=new ArrayAdapter<>(AddAttendance.this,android.R.layout.simple_list_item_1,semlist);
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
        selectImageOption();
        }

        if (v == b3)
        {
            if(sem.equals("Select"))
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
                String resp = uploadFile(fpth);
                try {
                    JSONObject json = new JSONObject(resp);
                    String res = json.getString("task");
                    if (res.equalsIgnoreCase("invalid")) {
                        Toast.makeText(getApplicationContext(), "already added", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), TeacherHome.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==s1) {
            sem = semlist[i];
            cid = new ArrayList<>();
            code = new ArrayList<>();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(AddAttendance.this);

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
                        ArrayAdapter<String> ad = new ArrayAdapter<>(AddAttendance.this, android.R.layout.simple_list_item_1, code);
                        s2.setAdapter(ad);

                    } catch (Exception e) {
                        Log.d("=========", e.toString());
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(AddAttendance.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void selectImageOption()
    {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddAttendance.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), fname);
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
                else if (items[item].equals("Choose from Gallery"))
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
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            try
            {
                try
                {
                    System.out.println("Camera Image URI : "+mImageCaptureUri);
//			   CropingIMG();


                    String pt=Rotation.resaveBitmap(outPutFile.getAbsolutePath());
                    fpth=outPutFile.getPath();//File imgFile=new File(data.);
                    //Toast.makeText(getApplicationContext(), "path"+fpth, Toast.LENGTH_SHORT).show();
                   t1.setText(fpth);


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else   if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data)
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
        else if (requestCode == CROPING_CODE) {

            try {
                if(outPutFile.exists()){
                    Bitmap photo = decodeFile(outPutFile);
//                    v.setImageBitmap(photo);

                    fpth=outPutFile.getPath();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void CropingIMG() {
        @SuppressWarnings("rawtypes")
        final ArrayList<Object> cropOptions = new ArrayList<Object>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 240);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 0.5);
            intent.putExtra("scale", true);

            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1)
            {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);
                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROPING_CODE);
            }
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String path;
        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null)
            path=contentURI.getPath();

        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path=cursor.getString(idx);

        }
        if(cursor!=null)
            cursor.close();
        return path;
    }
    public String uploadFile(String sourceFileUri) {
        url = "http://" + sp.getString("ip", "") + ":5000/markattendance ";
        Log.d("=======", sourceFileUri);
        String fileName = sourceFileUri;
        String fln = fpth.substring(fpth.lastIndexOf("/") + 1);
        FileUpload fp = new FileUpload(fileName);
        Map mp = new HashMap<String, String>();
        mp.put("subj",subject);

        String res = fp.multipartRequest(url, mp, fileName, "files", "application/octet-stream");
        return res;
    }
}