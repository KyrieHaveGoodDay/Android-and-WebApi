package com.example.oosam.tabbedexample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tab5Enter extends AppCompatActivity {






    //日期需要宣告的功能
    TextView tv;
    Calendar mCurrentDate;
    TextView Moeny;
    int day,month,year;
    Bitmap bmp;
    //下拉式選單需要宣告的功能
    //spinner
    Spinner in_class;
    private RequestQueue requestQueue;
    private ArrayList<Models> list2;
    private String[] Class_Name;


    private static final String TAG = "Tab5Enter";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView imv;
    private Button submit;
    private  boolean is_Update = false;
    private  int Update_ID = 0;
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab5_enter);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        submit = findViewById(R.id.Submit);
        Moeny = findViewById(R.id.Money);
        imv = (ImageView)findViewById(R.id.imageview);

        //日期
        tv = (TextView) findViewById(R.id.textView);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;
        tv.setText(year + "年" + month + "月" + day +"日");


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Tab5Enter.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                         monthOfYear = monthOfYear+1;
                         tv.setText(year+"年"+monthOfYear+"月"+dayOfMonth+"日");
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });
        SharedPreferences preferences  = getApplicationContext().getSharedPreferences("Build" , MODE_PRIVATE);
        int Build_ID = preferences.getInt("Build_ID",0);
        int Index = preferences.getInt("Index",0);

        if(Build_ID != 0)
        {
            Update_ID = Build_ID;
            GetRecord("api/Build/Inquire?Build_ID="+Build_ID,Index);
        }


        //下拉式選單
        in_class = (Spinner) findViewById(R.id.in_class);
        list2= new ArrayList<Models>();
        GetClass("api/Build/InquireClass");

        submit.setOnClickListener(btnlistener);






    }

    //拍照功能
    ArrayList<Bitmap> list = new ArrayList<Bitmap>();
    public void onGet(View v){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode==100){
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
            imv.setImageBitmap(bmp);
            list.add(bmp);

        }
        else {
            Toast.makeText(this,"沒有照片" , Toast.LENGTH_SHORT).show();
        }
    }
    //在右上角新增一個menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu3_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_expend) {
            //如果有多個項目，就要用switch case改寫
            Intent intent = new Intent();
            intent.setClass(Tab5Enter.this, Tab5_1.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //class

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
                                            list2.add(model);
                                            Class_Name[i+1] = model.Key;

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    in_class.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spinnertext,Class_Name));



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("leo",volleyError.getMessage());

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }



    //更新資料
    private View.OnClickListener btnlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(is_Update == true)
            {
                int Class_ID = list2.get(in_class.getSelectedItemPosition()-1).Value;
                int money = Integer.parseInt(Moeny.getText().toString());

                UpdateRecord("api/Build/Update",Class_ID,money,Update_ID);

                is_Update = false;
            }
            else
            {
                int ClassId = list2.get(in_class.getSelectedItemPosition()-1).Value;
                int Money = Integer.parseInt(Moeny.getText().toString());
                String ImgEncode = ImgToBase64(bmp);
                Log.d("Image",ImgEncode);
                Insertrecord("api/Build/Insertrecord",ClassId,Money,ImgEncode);
            }


        }
    };


    //圖片轉字串
    private String ImgToBase64(Bitmap img)
    {

        Bitmap bitmap = img;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


        return  encodedImage;

    }

    //新增資料
    private void  Insertrecord(String url ,final int ClassId ,final int Money, final String ImgEncode)
    {
        url = MainActivity.IP + url;



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response

                    Toast toast = Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_LONG);
                    toast.show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Build_Date", year + "-" + month + "-" + day);
                params.put("Class_ID",String.valueOf(ClassId));
                params.put("Build_Money",String.valueOf(Money));
                params.put("Img_Base", ImgEncode);

                return params;
            }

        };

        requestQueue.add(stringRequest);

    }

    //更新資料
    private void UpdateRecord(String url,final int Class_ID ,final int money,final int ID)
    {
        url = MainActivity.IP + url;



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                if (response.equals("1"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "變更資料成功", Toast.LENGTH_LONG);

                    toast.show();

                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "變更資料失敗", Toast.LENGTH_LONG);

                    toast.show();
                }

                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d("Error.Response", error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("Build_Class",String.valueOf(Class_ID));
                params.put("Build_Money", String.valueOf(money));
                params.put("Build_ID", String.valueOf(ID));

                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private Bitmap Base64toImg(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return  decodedByte;

    }

    public void GetRecord(String url,final int index)
    {
        url = MainActivity.IP + url;
        Log.d("leotest",url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        //成功要做的事情
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {


                                try {
                                    JSONObject data = response.getJSONObject(0);
                                   // Log.d("leotest",String.valueOf(data.getInt("Class_name")));
                                    imv.setImageBitmap(Base64toImg(data.getString("Image_text")));
                                    Moeny.setText(data.getString("Build_Money"));
                                    tv.setText(data.getString("Build_Date") );
                                    in_class.setSelection(data.getInt("Class_ID"));

                                    SharedPreferences preferences  = getApplicationContext().getSharedPreferences("Build" , MODE_PRIVATE);
                                    preferences.edit()
                                               .putInt("Build_ID",0)
                                               .putInt("Index",0)
                                               .commit();

                                    is_Update = true;


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("leo",volleyError.getMessage());

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }





    }
