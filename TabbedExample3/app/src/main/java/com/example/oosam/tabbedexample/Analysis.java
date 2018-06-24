package com.example.oosam.tabbedexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Analysis extends AppCompatActivity {
    ListView list;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        list = findViewById(R.id.analysis_view);
        GetAllRecord("api/Build/SearchRecord");


    }

    private void GetAllRecord(String url) {
        url = MainActivity.IP + url;

        Bundle bundle = getIntent().getExtras();
       final String Start = bundle.getString("Start");
       final String End = bundle.getString("End");
       final  int Class_Id = bundle.getInt("Class_ID");

        Map<String, String> params = new HashMap<String, String>();

        if (Class_Id!=0)
        {
            params.put("Class_ID", String.valueOf(Class_Id) );
        }
        params.put("Start", Start);
        params.put("End", End);
        JSONObject object = new JSONObject(params);



        Volley.newRequestQueue(getApplicationContext())
                .add(new JsonRequest<JSONArray>(Request.Method.POST,
                             url,
                            object.toString(),
                             new Response.Listener<JSONArray>() {
                                 @Override
                                 public void onResponse(JSONArray jsonArray) {
                                    Log.d("Response1",String.valueOf(jsonArray.length()));
                                    list.setAdapter(new AllanalysisAdapter(jsonArray,getApplicationContext()));
                                 }
                             }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError volleyError) {

                         }
                     })

                     {

                         @Override
                         protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {


                             try {
                                 String jsonString = new String(networkResponse.data,
                                         HttpHeaderParser
                                                 .parseCharset(networkResponse.headers));
                                 return Response.success(new JSONArray(jsonString),
                                         HttpHeaderParser
                                                 .parseCacheHeaders(networkResponse));
                             } catch (UnsupportedEncodingException e) {
                                 return Response.error(new ParseError(e));
                             } catch (JSONException je) {
                                 return Response.error(new ParseError(je));
                             }

                         }
                     }
                );

    }



}
