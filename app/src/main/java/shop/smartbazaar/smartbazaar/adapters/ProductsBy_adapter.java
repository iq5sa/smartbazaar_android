package shop.smartbazaar.smartbazaar.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import shop.smartbazaar.smartbazaar.ProductsModel;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class ProductsBy_adapter extends BaseAdapter {

    ArrayList<ProductsModel> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    FragmentManager fm;
    public ProductsBy_adapter(Context context, FragmentManager fm ,ArrayList<ProductsModel> arrayList){

        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fm = fm;

    }
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //image should be array list
        //options sound be array list


        View mView = layoutInflater.inflate(R.layout.single_product,null,false);
        ImageView product_image = mView.findViewById(R.id.product_image);
        TextView product_name = mView.findViewById(R.id.product_name);
        TextView product_price = mView.findViewById(R.id.product_price);
        final ProgressBar progressBar = mView.findViewById(R.id.progressBar);
        ToggleButton Toggle_favorite = mView.findViewById(R.id.Toggle_favorite);
        ToggleButton Toggle_cart = mView.findViewById(R.id.Toggle_cart);
        LinearLayout rate_linear = mView.findViewById(R.id.rate_linear);
        ImageView shareProduct = mView.findViewById(R.id.shareProduct);




        Picasso.with(context).load(arrayList.get(position).feature_image).resize(200,200).into(product_image, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });

        product_name.setText(arrayList.get(position).title);
        product_price.setText(arrayList.get(position).price + " $ ");
        final String id = arrayList.get(position).id;
        getRates(rate_linear,Integer.parseInt(arrayList.get(position).getRating()));

        final String title = arrayList.get(position).getTitle();
        final String store_name = arrayList.get(position).getStore_name();
        final String price = arrayList.get(position).getPrice();
        final String image = arrayList.get(position).getFeature_image();
        final String url = arrayList.get(position).getUrl();
        final String color = arrayList.get(position).getColor();
        final String option = arrayList.get(position).getOption();



        shareProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(Utiles.share("سمارت بازار" +"\n" +title + "\n"+ url,"smartbazaar"));
            }
        });
        Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    FavoritesTable.insert(id, title, price, image);
                    Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();

                } else {
                    FavoritesTable.deleteItemByPId(id);
                    Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();

                }
            }
        });
        Toggle_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    String p_option = "";


                    long insert = CartTable.insert(Integer.parseInt(id), title, store_name, price, image, color, p_option);

                    if (insert != -1) {


                        Toast.makeText(context, "تمت اضافة المنتج الى السلة", Toast.LENGTH_LONG).show();
                    }


                } else {

                    CartTable.deleteItemByPId(id);
                    Toast.makeText(context, "تمت الازالة من السلة", Toast.LENGTH_SHORT).show();

                }
            }
        });


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("title", arrayList.get(position).getTitle());
                bundle.putString("price", arrayList.get(position).getPrice());
                bundle.putString("image", arrayList.get(position).getFeature_image());
                bundle.putString("desc", arrayList.get(position).getDescription());
                bundle.putString("store_name", arrayList.get(position).getStore_name());

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });




        return mView;
    }

    private void getRates(LinearLayout linearLayout, int rate) {

        int read_rate = 5;

        if (rate <= 5) {
            for (int d = 0; d < read_rate; d++) {
                View view = layoutInflater.inflate(R.layout.single_rate, null, false);
                ImageView imageView = view.findViewById(R.id.star);
                if (d + 1 > rate) {
                    imageView.setColorFilter(ContextCompat.getColor(context, R.color.cgray), PorterDuff.Mode.SRC_IN);

                }
                linearLayout.addView(view);

            }


        }


    }
}
