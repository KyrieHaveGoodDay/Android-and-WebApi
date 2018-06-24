package com.example.oosam.tabbedexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jay on 2018/1/12.
 */

public class Tab4Analysis extends Fragment {

    //設定金額上限宣告
    private SharedPreferences settings;
    private final String data = "DATA";
    private final String et_money = "money";
    EditText setMoney;
    Button btmoney;


    //新增類別宣告
    private RequestQueue requestQueue;
    Button class_set;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab4analysis, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        Init();
        return rootView;
    }

    // 初始化所有元件
    private  void Init() {
        class_set = rootView.findViewById(R.id.set_class);
        class_set.setOnClickListener(clickListener);
        setMoney = rootView.findViewById(R.id.money);
        btmoney =rootView.findViewById(R.id.set_money);
        btmoney.setOnClickListener(MoneyListener);

    }


    private void InsertClass(String url) {
        url = MainActivity.IP + url;
        final TextView textView = rootView.findViewById(R.id.Class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                if (response.equals("1"))
                {
                    Toast toast = Toast.makeText(getContext(),
                            "新增類別成功", Toast.LENGTH_LONG);

                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(getContext(),
                            "新增類別失敗", Toast.LENGTH_LONG);

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
                params.put("Class_name", textView.getText().toString());

                return params;
            }

        };

       requestQueue.add(stringRequest);
    }


    // 新增類別
     private View.OnClickListener clickListener = new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             InsertClass("api/Build/InsertClass");

         }
     };

    // 設定收入
    private View.OnClickListener MoneyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Money =  setMoney.getText().toString();
            if ( !Money.equals(""))
            {
                SharedPreferences preferences  = getContext().getSharedPreferences("Build" , MODE_PRIVATE);
                preferences.edit()
                        .putString("Money",Money)
                        .commit();


                Toast toast = Toast.makeText(getContext(),
                        "新增金額成功", Toast.LENGTH_LONG);

                toast.show();

            }
            else
            {
                Toast toast = Toast.makeText(getContext(),
                        "欄位不可空白", Toast.LENGTH_LONG);

                toast.show();
            }




        }
    };


}
