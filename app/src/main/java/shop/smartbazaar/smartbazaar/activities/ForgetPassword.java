package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import shop.smartbazaar.smartbazaar.R;


public class ForgetPassword extends AppCompatActivity {

    Context context;
    EditText edt_mobile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forget_password_layout);

        edt_mobile = findViewById(R.id.mobile);
        context = this;



    }


    public void submit(View view) {

        //validate phone
        int phoneLength = edt_mobile.getText().toString().length();

        //check length
        if (phoneLength > 11 || phoneLength < 11) {

            edt_mobile.setError("رقم الهاتف غير صحيح");
        } else {

            //check structure number

            char phonePart1 = edt_mobile.getText().charAt(0);
            char phonePart2 = edt_mobile.getText().charAt(1);


            if (phonePart1 == '0' && phonePart2 == '7') {

                Toast.makeText(context, "تم الارسال بنجاح", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,Vercode.class);
                startActivity(intent);


            }
        }
    }
}
