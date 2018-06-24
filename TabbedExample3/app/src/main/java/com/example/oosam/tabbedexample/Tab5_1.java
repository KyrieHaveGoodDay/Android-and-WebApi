package com.example.oosam.tabbedexample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Tab5_1 extends AppCompatActivity {
    //日期需要宣告的功能
    TextView tv;
    Calendar mCurrentDate;
    int day, month, year;

    //下拉式選單需要宣告的功能
    Spinner out_class;
    private RequestQueue requestQueue;
    private ArrayList<Models> list2;
    private String[] Class_Name;


    private static final String TAG = "Tab5Enter";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @TargetApi(Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab5_1);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        //日期
        tv = (TextView) findViewById(R.id.textView);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month + 1;
        tv.setText(year + "年" + month + "月" + day + "日");


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Tab5_1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tv.setText(year + "年" + monthOfYear + "月" + dayOfMonth + "日");
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

//        下拉式選單
        out_class = findViewById(R.id.out_class);
        list2= new ArrayList<Models>();
        GetClass("api/Build/InquireClass");




    }


    //拍照功能
    public void onGet(View v) {
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            ImageView imv = (ImageView) findViewById(R.id.imageview);
            imv.setImageBitmap(bmp);
        } else {
            Toast.makeText(this, "沒有照片", Toast.LENGTH_SHORT).show();
        }
    }

    //在右上角新增一個menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_income) {
            //如果有多個項目，就要用switch case改寫
            Intent intent = new Intent();
            intent.setClass(Tab5_1.this, Tab5Enter.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //class

    private void GetClass(String url) {
        url = MainActivity.IP + url;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        //成功要做的事情
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                Log.d("leoClass", String.valueOf(response.length()));
                                Class_Name = new String[response.length() + 1];
                                Class_Name[0] = "請選擇欄位（若不選擇留空）";
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        Models model = new Models();

                                        JSONObject object = response.getJSONObject(i);
                                        model.Key = object.getString("Class_name");
                                        model.Value = object.getInt("Class_ID");
                                        list2.add(model);
                                        Class_Name[i + 1] = model.Key;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                out_class.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinnertext, Class_Name));


                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("leo", volleyError.getMessage());

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
