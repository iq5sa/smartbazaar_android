package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class RegisterActivity extends AppCompatActivity {


    Spinner sp_city, sp_sex;
    String phone;
    String email;
    String password;
    String confirm_password;
    String name;
    String area;
    int gender;
    EditText edt_phone, edt_email, edt_password, edt_confirm_password, edt_name;
    ProgressBar progressBar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = getApplicationContext();


        sp_city = findViewById(R.id.sp_city);
        sp_sex = findViewById(R.id.sp_sex);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        edt_name = findViewById(R.id.edt_name);
        progressBar = findViewById(R.id.spin_kit);


        ArrayAdapter citiesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Utiles.cities);

        sp_city.setAdapter(citiesAdapter);

        String[] genders = {"ذكر", "انثى"};
        ArrayAdapter gendersAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, genders);
        sp_sex.setAdapter(gendersAdapter);


    }

    public void register_user(View view) {

        //validate phone
        int phoneLength = edt_phone.getText().toString().length();

        //check length
        if (phoneLength > 11 || phoneLength < 11) {

            edt_phone.setError("رقم الهاتف غير صحيح");
        } else {

            //check structure number

            char phonePart1 = edt_phone.getText().charAt(0);
            char phonePart2 = edt_phone.getText().charAt(1);


            if (phonePart1 == '0' && phonePart2 == '7') {

                //validate name
                if (edt_name.getText().toString().equals("")) {
                    edt_name.setError("الاسم الكامل مطلوب");


                } else {
                    //check text arabic
                    if (!Utiles.isProbablyArabic(edt_name.getText().toString())) {
                        edt_name.setError("الاسم يجب ان يكون باللغة العربية");
                    } else {

                        //validate password
                        if (edt_password.getText().toString().length() < 8) {
                            edt_password.setError("كلمة المرور يجب ان تكون اكبر من ٨ احرف او ارقام");
                        } else {


                            if (edt_password.getText().toString().equals(edt_confirm_password.getText().toString())) {

                                //password correct

                                //make post

                                name = edt_name.getText().toString();
                                email = edt_email.getText().toString();
                                password = edt_password.getText().toString();
                                confirm_password = edt_confirm_password.getText().toString();
                                phone = edt_phone.getText().toString();
                                gender = sp_sex.getSelectedItemPosition() + 1;
                                area = sp_city.getSelectedItem().toString();


                                if (!email.equals("")) {
                                    if (!Utiles.isValidEmailAddress(edt_email.getText().toString())) {
                                        edt_email.setError("البريد الاكتروني غير صحيح");

                                    } else {

                                        progressBar.setVisibility(View.VISIBLE);
                                        sendRegisterPost(name,email,password,confirm_password,phone,String.valueOf(gender),area,"");
                                    }
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    sendRegisterPost(name,email,password,confirm_password,phone,String.valueOf(gender),area,"");
                                }


                            } else {
                                edt_confirm_password.setError("كلمة السر غير متطابقتين");
                            }
                        }


                    }
                }
            } else {
                edt_phone.setError("رقم الهاتف غير صحيح");

            }


        }


    }


    private void sendRegisterPost(final String name, final String email, final String password, final String password_confirmation,
                                  final String mobile, final String gender, final String area, final String firebase_token) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Api.register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e("sajjadiq", response);

                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(context, "تم انشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_data");
                        int id = jsonObject1.getInt("id");
                        String name = jsonObject1.getString("name");
                        String email = jsonObject1.getString("email");
                        String mobile = jsonObject1.getString("mobile");
                        String gender = jsonObject1.getString("gender");
                        String area = jsonObject1.getString("area");
                        String token = jsonObject.getString("access_token");

                        new User(context).saveUserData(String.valueOf(id), name, email, mobile, token, area, gender);

                        Intent intent = new Intent(RegisterActivity.this, Vercode.class);
                        startActivity(intent);

                    } else {
                        String message = jsonObject.getString("message");

                        if (message.equals("The email has already been taken.")) {
                            Toast.makeText(RegisterActivity.this, "البريد الالكتروني مستخدم", Toast.LENGTH_SHORT).show();
                            edt_email.setError("البريد الالكتروني مستخدم");
                        } else if (message.equals("The mobile has already been taken.")) {
                            Toast.makeText(RegisterActivity.this, "رقم الهاتف مستخدم", Toast.LENGTH_SHORT).show();
                            edt_phone.setError("رقم الهاتف مستخدم");

                        }
                    }

                } catch (JSONException e) {
                    e.getStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("name", name);
                if (!email.equals("")) {
                    hashMap.put("email", email);

                }
                hashMap.put("password", password);
                hashMap.put("password_confirmation", password_confirmation);
                hashMap.put("mobile", mobile);
                hashMap.put("gender", gender);
                hashMap.put("area", area);
                hashMap.put("firebase_token", firebase_token);


                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
