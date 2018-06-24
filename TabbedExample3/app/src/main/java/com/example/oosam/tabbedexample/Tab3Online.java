package com.example.oosam.tabbedexample;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by jay on 2018/1/10.
 */


class Models {
    String Key;
    int Value;
}

public class Tab3Online extends Fragment {

    //日期
    TextView start;
    TextView end;
    Calendar mCurrentDate;
    int day,month,year;

    //spinner
    Spinner class_spinner;
    Button Enter;
    private RequestQueue requestQueue;
    private ArrayList<Models> list;
    private String[] Class_Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3online, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        // spinner
        class_spinner = rootView.findViewById(R.id.class_spinner);
        list = new ArrayList<Models>();
        GetClass("api/Build/InquireClass");

        // Button
        Enter = rootView.findViewById(R.id.Enter);
        Enter.setOnClickListener(EnterListener);

        //日期
        start = rootView.findViewById(R.id.date_start);
        end = rootView.findViewById(R.id.date_end);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;
        start.setText(year + "-" + month + "-" + day );
        end.setText(year + "-" + month + "-" + day);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        start.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        end.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        return rootView;


    }



    public void GoAnalysis(View v)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity() , Analysis.class);
        startActivity(intent);

    }


    private  void GetClass(String url)
    {
        url = MainActivity.IP + url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        //成功要做的事情
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                Log.d("leoClass",String.valueOf(response.length()));
                                Class_Name = new String[response.length()+1];
                                Class_Name[0] = "請選擇欄位（若不選擇留空）";
                                for(int  i=0;i<response.length();i++)
                                {
                                    try {
                                        Models model = new Models();

                                        JSONObject object = response.getJSONObject(i);
                                        model.Key = object.getString("Class_name");
                                        model.Value = object.getInt("Class_ID");
                                        list.add(model);
                                        Class_Name[i+1] = model.Key;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                class_spinner.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,Class_Name));



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("leo",volleyError.getMessage());

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }


    private View.OnClickListener EnterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("leoClass","第" + class_spinner.getSelectedItemPosition() + "筆");

            // 傳送參數，將Bundle夾帶在Intent傳送
            Bundle bundle = new Bundle();
            if(class_spinner.getSelectedItemPosition() != 0)
            {
                bundle.putInt("Class_ID",list.get(class_spinner.getSelectedItemPosition()-1).Value);
            }
            else
            {
                bundle.putInt("Class_ID",0);

            }
            bundle.putString("Start",start.getText().toString() );//傳遞Double
            bundle.putString("End",end.getText().toString());//傳遞String
            Intent intent = new Intent();
            intent.setClass(getContext() , Analysis.class);

            //將Bundle物件傳給intent
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


}
