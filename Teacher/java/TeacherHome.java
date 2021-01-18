package com.example.course_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherHome extends AppCompatActivity implements View.OnClickListener {


    Button b1,b2,b3,b4,b5,b6,b7;
    Intent i1,i2,i3,i4,i5,i6,i7,i8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        b1 = findViewById(R.id.hometh);
        b2 = findViewById(R.id.viewsubjects);
        b3 = findViewById(R.id.logout);
        b4 = findViewById(R.id.addsyllabus);
        b5 = findViewById(R.id.viewsyllabus);
        b6 = findViewById(R.id.addattendance);
        b7 = findViewById(R.id.viewattendance);
//        b8 = findViewById(R.id.logoutth);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
//        b8.setOnClickListener(this);

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
            i2=new Intent(getApplicationContext(),ViewSubjects.class);
            startActivity(i2);
        }

        if (v == b3)
        {
            i3=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i3);
        }

        if (v == b4)
        {
            i4=new Intent(getApplicationContext(),AddSyllabus.class);
            startActivity(i4);
        }

        if (v == b5)
        {
            i5=new Intent(getApplicationContext(),ViewSyllabus.class);
            startActivity(i5);
        }

        if (v == b6)
        {
            i6=new Intent(getApplicationContext(),AddAttendance.class);
            startActivity(i6);
        }

        if (v == b7)
        {
            i7=new Intent(getApplicationContext(),ViewAttendance.class);
            startActivity(i7);
        }

//        if (v == b8)
//        {
//            i8=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(i8);
//        }
    }
}