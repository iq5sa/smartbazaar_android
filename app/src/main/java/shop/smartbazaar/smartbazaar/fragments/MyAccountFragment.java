package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.MainActivity;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class MyAccountFragment extends Fragment {


    Context context;
    FragmentManager fm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.context = getContext();

        
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View custom_view = inflater.inflate(R.layout.myaccountlayout,container,false);
        fm = getFragmentManager();

        TextView name = custom_view.findViewById(R.id.name);
        TextView phone = custom_view.findViewById(R.id.phone);
        TextView email = custom_view.findViewById(R.id.email);
        TextView gender = custom_view.findViewById(R.id.gender);
        TextView area = custom_view.findViewById(R.id.area);
        Button btn_logout = custom_view.findViewById(R.id.btn_logout);



        name.setText(new User(context).getName());
        phone.setText(new User(context).getMobile());
        email.setText(new User(context).getEmail());
        area.setText(new User(context).getArea());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new User(context).logout();

                getActivity().finish();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);

                Utiles.ReplaceFragment(fm,new HomeFragment(),null);
                Toast.makeText(context, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();
            }
        });


        return custom_view;


    }
}
