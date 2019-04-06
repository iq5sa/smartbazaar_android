package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.CartActivity;
import shop.smartbazaar.smartbazaar.activities.FavoriteActivity;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class ToolBarIcons extends Fragment {

    Context context;
    RelativeLayout cartCountAria, favoriteCountAria, shareAria;

    TextView tv_cartCount, tv_favoriteCount;


    String p_title,p_url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        Bundle bundle = getArguments();
        this.p_title = bundle.getString("title");
        this.p_url = bundle.getString("url");



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tool_bar_layout, container, false);
        cartCountAria = view.findViewById(R.id.cartCountAria);
        favoriteCountAria = view.findViewById(R.id.favoriteCountAria);
        shareAria = view.findViewById(R.id.shareAria);
        tv_cartCount = view.findViewById(R.id.tv_cart_count);
        tv_favoriteCount = view.findViewById(R.id.tv_favorite_count);

        tv_cartCount.setText(String.valueOf(CartTable.getAllItems().getCount()));
        tv_favoriteCount.setText(String.valueOf(FavoritesTable.getAllItems().getCount()));


        cartCountAria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCart = new Intent(context, CartActivity.class);
                startActivity(goToCart);
            }
        });

        favoriteCountAria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCart = new Intent(context, FavoriteActivity.class);
                startActivity(goToCart);
            }
        });

        shareAria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(Utiles.share("سمارت بازار" + "\n" + p_title + "\n" + p_url, "smartbazaar"));
            }
        });
        return view;
    }
}
