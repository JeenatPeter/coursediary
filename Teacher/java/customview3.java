package com.example.course_diary;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class customview3 extends BaseAdapter {
    private android.content.Context Context;
    ArrayList<String> b;
    ArrayList<String> c,d;



    public customview3(android.content.Context applicationContext, ArrayList<String> b, ArrayList<String> c, ArrayList<String> d) {
        this.Context=applicationContext;
        this.b=b;
        this.c=c;
        this.d=d;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return c.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflator=(LayoutInflater)Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(convertView==null)
        {
            gridView=new View(Context);
            gridView=inflator.inflate(R.layout.activity_customview3, null);

        }
        else
        {
            gridView=(View)convertView;

        }


        TextView t=(TextView) gridView.findViewById(R.id.textView4);
        TextView s=(TextView) gridView.findViewById(R.id.textView5);
        TextView sch=(TextView) gridView.findViewById(R.id.textView6);

        t.setTextColor(Color.BLACK);
        s.setTextColor(Color.BLACK);
        sch.setTextColor(Color.BLACK);



        t.setText(b.get(position));
        s.setText(c.get( position ) );
        sch.setText(d.get(position));








        //Picasso.with(Context)
            //    .load(urll)
            //  .transform(new Circulartransform())
            // .error(R.drawable.a)
            //  .into(img);





        return gridView;

    }



}
