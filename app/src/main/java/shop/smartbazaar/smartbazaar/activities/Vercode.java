package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class Vercode extends AppCompatActivity {

    EditText edt_code;
    TextView tv_notNow;
    Button btn_check;
    Context context;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercode);

        edt_code = findViewById(R.id.edt_code);
        tv_notNow = findViewById(R.id.tv_notNow);
        btn_check = findViewById(R.id.btn_check);
        progressBar = findViewById(R.id.spin_kit);
        this.context = getApplicationContext();



        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new User(context).isLogin()){

                    String user_id = new User(context).getId();

                    check_code(edt_code.getText().toString(),user_id);

                }


            }
        });


        tv_notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vercode.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }


    private void check_code(final String code,final String user_id){


        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Api.check_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("status")){

                        Toast.makeText(Vercode.this, "تم التحقق بنجاح", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Vercode.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Vercode.this, "الرقم الذي ادخلتةُ غير صحيح", Toast.LENGTH_SHORT).show();

                    }

                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",user_id);
                hashMap.put("verification_code",code);

                return hashMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}
