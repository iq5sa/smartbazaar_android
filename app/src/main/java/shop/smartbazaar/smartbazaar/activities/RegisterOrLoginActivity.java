package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import shop.smartbazaar.smartbazaar.R;

public class RegisterOrLoginActivity extends AppCompatActivity {

    Button btn_login,btn_register;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);
        context = this;


        btn_login = findViewById(R.id.login);
        btn_register = findViewById(R.id.register);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);


            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);


            }
        });


    }
}
