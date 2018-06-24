package com.example.oosam.tabbedexample;

/**
 * Created by jay on 2018/1/10.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Tab1Contacts extends Fragment {
    ListView listView;
    TextView income;
    TextView expend;
    TextView total;
    private RequestQueue requestQueue;
    JSONArray data;
    ImageView img_restore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contacts, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        listView = rootView.findViewById(R.id.ListView);
        income = rootView.findViewById(R.id.income);
        expend = rootView.findViewById(R.id.expend);
        total = rootView.findViewById(R.id.total);
        listView.setOnItemClickListener(listener);
        img_restore = rootView.findViewById(R.id.restore);
        img_restore.setOnClickListener(restore);
        PutMoney("api/Build/GetMoney");
        GetAllRecord("api/Build/Getrecord");
        return rootView;
    }

    //顯示記帳紀錄
    public void GetAllRecord(String url)
    {

        url = MainActivity.IP + url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        //成功要做的事情
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                             data = response;
                             listView.setAdapter(new AllRecordAdapter(response,getContext()));

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("leo",volleyError.getMessage());

                    }
                });
        requestQueue.add(jsonArrayRequest);

    }

    //收入 支出 餘額
    public void PutMoney(String url)
    {
        url = MainActivity.IP + url;
        final   SharedPreferences preferences  = getContext().getSharedPreferences("Build" , MODE_PRIVATE);
        income.setText(income.getText() + preferences.getString("Money",null));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                expend.setText(expend.getText() + response);

                int Total = Integer.parseInt(preferences.getString("Money",null)) - Integer.parseInt(response);

                if(Total <0)
                {
                    total.setText("超支：" + String.valueOf(Total*-1));
                    total.setTextColor(Color.RED);
                }
                else
                {
                    total.setText("剩餘：" + String.valueOf(Total));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", error.getMessage());
            }
        });

        requestQueue.add(stringRequest);

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                int Build_ID =   data.getJSONObject(i).getInt("Build_ID");
                Log.d("leoBuild_ID",String.valueOf(Build_ID));
                SharedPreferences preferences  = getContext().getSharedPreferences("Build" , MODE_PRIVATE);
                preferences.edit()
                        .putInt("Build_ID",Build_ID)
                        .putInt("Index",i)
                        .commit();
                Intent intent = new Intent();
                intent.setClass(getContext() , Tab5Enter.class);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener restore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GetAllRecord("api/Build/Getrecord");
        }
    };

}






