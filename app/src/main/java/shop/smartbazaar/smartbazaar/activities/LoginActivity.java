package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {


    Button btn_login;
    EditText edt_mobile,edt_password;
    Context context;
    ProgressBar progressBar;
    TextView forget_password;

    FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.login);

        edt_mobile = findViewById(R.id.mobile);
        edt_password = findViewById(R.id.password);
        progressBar = findViewById(R.id.spin_kit);

        fm = getSupportFragmentManager();


        this.context = this;

        forget_password = findViewById(R.id.forget_password);


        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(intent);


            }
        });


    }

    public void Login(View view) {

        this.progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);



                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println(jsonObject.getString("status"));

                    if (jsonObject.getBoolean("status")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");

                        Toast.makeText(context, "تم تسجيل الدخول بنجاح", Toast.LENGTH_LONG).show();

                        new User(context).saveUserData(jsonObject1.getString("id"),jsonObject1.getString("name"),jsonObject1.getString("email")
                                ,jsonObject1.getString("mobile"),jsonObject.getString("access_token"),jsonObject1.getString("area"),jsonObject1.getString("gender"));
//

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);

                    }else{

                        Toast.makeText(context, "رقم الهاتف او كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                    }
//

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(context, "رقم الهاتف او كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();



                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





            }
        }){
            @Override
            protected Map<String, String> getParams()  {

                final String mobile = edt_mobile.getText().toString();
                final String password = edt_password.getText().toString();
                Map<String,String> params = new HashMap<>();

                params.put("mobile",mobile);
                params.put("password",password);

                return params;

            }
        };

        requestQueue.add(stringRequest);

    }


}
