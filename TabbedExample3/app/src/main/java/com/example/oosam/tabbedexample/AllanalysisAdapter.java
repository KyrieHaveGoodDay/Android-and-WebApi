package com.example.oosam.tabbedexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class AllanalysisAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private JSONArray dataArray;
    public AllanalysisAdapter(JSONArray dataArray , Context context)
    {
        this.myInflater = android.view.LayoutInflater.from(context);
        this.dataArray = dataArray;

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

        view = myInflater.inflate(R.layout.allrecord , null);
        TextView Date = view.findViewById(R.id.Date);
        TextView ClasName = view.findViewById(R.id.ClassName);
        TextView Money = view.findViewById(R.id.Money);

        try {
            Date.setText(dataArray.getJSONObject(i).getString("Build_Date"));
            ClasName.setText(dataArray.getJSONObject(i).getString("Class_name"));
            Money.setText(dataArray.getJSONObject(i).getString("Build_Money"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}
