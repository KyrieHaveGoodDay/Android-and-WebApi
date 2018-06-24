package com.example.oosam.tabbedexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;

public class AllRecordAdapter extends BaseAdapter{

    private LayoutInflater myInflater;
    private  JSONArray dataArray;
    private  Context context;
    public AllRecordAdapter(JSONArray dataArray , Context context)
    {
        this.myInflater = LayoutInflater.from(context);
        this.dataArray = dataArray;
        this.context = context;

    }

    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = myInflater.inflate(R.layout.allrecord,null);
        TextView Date = view.findViewById(R.id.Date);
        TextView ClasName = view.findViewById(R.id.ClassName);
        TextView Money = view.findViewById(R.id.Money);
        ImageView img = view.findViewById(R.id.Record_Img);

        try {
            Date.setText(dataArray.getJSONObject(i).getString("Build_Date"));
            ClasName.setText(dataArray.getJSONObject(i).getString("Class_name"));
            Money.setText(dataArray.getJSONObject(i).getString("Build_Money"));
            img.setImageBitmap(Base64toImg(dataArray.getJSONObject(i).getString("Image_text")));
            Log.d("leo",dataArray.getJSONObject(i).getString("Build_Date"));
            Log.d("leo","Hello World");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private Bitmap Base64toImg(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return  decodedByte;

    }
}
