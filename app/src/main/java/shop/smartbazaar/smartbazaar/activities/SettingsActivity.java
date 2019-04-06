package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.public_classes.SelectLangDialog;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class SettingsActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView selectLangTv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        initViews();

        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (selectLangTv != null) {
            selectLangTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SelectLangDialog selectLangDialog = new SelectLangDialog();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    selectLangDialog.setEnterTransition(fragmentTransaction);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            selectLangDialog.show(getSupportFragmentManager(),"");
                        }
                    },200);

                }
            });
        }

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        selectLangTv = findViewById(R.id.selectLangTv);




    }
}
