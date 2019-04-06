package shop.smartbazaar.smartbazaar.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import shop.smartbazaar.smartbazaar.Favorites_model;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.activities.RegisterOrLoginActivity;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class Favorites_adapter extends RecyclerView.Adapter<Favorites_adapter.MyViewHolder> {


    public ArrayList<Favorites_model> arrayList;
    public Context context;
    FragmentManager fm;

    public Favorites_adapter(ArrayList<Favorites_model> arrayList, Context context,FragmentManager fm) {

        this.arrayList = arrayList;
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_favorite_item,viewGroup,false);


        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.product_name.setText(arrayList.get(i).title);
        myViewHolder.price.setText(arrayList.get(i).price);
        Picasso.with(context).load(arrayList.get(i).image).into(myViewHolder.product_image);
        myViewHolder.itemView.setTag(arrayList.get(i).id);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name,price;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();

                    bundle.putString("id", v.getTag().toString());
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            });
        }

    }




}
