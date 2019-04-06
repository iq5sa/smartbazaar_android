package shop.smartbazaar.smartbazaar.public_classes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;


public class StoreProducts extends AsyncTask<String, String, String> {

    Context context;
    RequestQueue requestQueue;
    String apiUrl;
    LayoutInflater layoutInflater;
    LinearLayout parent;
    int method;
    HashMap<String,String> hashMap;
    BottomNavigationView bottomNavigationView;

    public StoreProducts(Context context, String apiUrl,int method,HashMap<String,String> hashMap, LinearLayout parent,BottomNavigationView bottomNavigationView) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        this.apiUrl = apiUrl;
        this.layoutInflater = LayoutInflater.from(context);

        this.parent = parent;
        if (bottomNavigationView !=null){
            this.bottomNavigationView = bottomNavigationView;
        }
        if (method !=0){
            this.method = method;
            if (hashMap !=null){
                this.hashMap = hashMap;
            }
        }else {
            method = StringRequest.Method.GET;
        }

    }

    @Override
    protected String doInBackground(String... strings) {

        StringRequest stringRequest = new StringRequest(method, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.get("status").equals(true)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            View mView = layoutInflater.inflate(R.layout.single_product, null, false);
                            ImageView product_image = mView.findViewById(R.id.product_image);
                            ImageView shareProduct = mView.findViewById(R.id.shareProduct);
                            TextView product_name = mView.findViewById(R.id.product_name);
                            TextView product_price = mView.findViewById(R.id.product_price);
                            TextView tv_discount = mView.findViewById(R.id.tv_discount);
                            ToggleButton Toggle_favorite = mView.findViewById(R.id.Toggle_favorite);
                            ToggleButton Toggle_cart = mView.findViewById(R.id.Toggle_cart);
                            LinearLayout rate_linear = mView.findViewById(R.id.rate_linear);


                            int product_rate = Integer.parseInt(jsonObject1.getString("rating"));

                            getRates(rate_linear, product_rate);

                            final ProgressBar progressBar = mView.findViewById(R.id.progressBar);
                            final String id = jsonObject1.getString("id");
                            final String title = jsonObject1.getString("title");
                            final String price = jsonObject1.getString("price");
                            final String image = jsonObject1.getString("feature_image");
                            final String desc = jsonObject1.getString("description");
                            final String store_name = jsonObject1.getString("store_name");
                            final String url = jsonObject1.getString("url");
                            final String discount_price = jsonObject1.getString("discount");


                            JSONArray options_array = jsonObject1.getJSONArray("options");
                            JSONObject firstJsonOption = options_array.getJSONObject(0);
                            final String color = firstJsonOption.getString("color");


                            final JSONArray options = firstJsonOption.getJSONArray("options");


                            if (!discount_price.equals("null")){
                                tv_discount.setVisibility(View.VISIBLE);
                                tv_discount.setText(price + " $");
                                product_price.setText(discount_price + " $");
                                tv_discount.setPaintFlags(tv_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }else {
                                product_price.setText(price + " $ ");
                            }
                            product_name.setText(title);


                            //check the product in the database or not
                            Cursor itemInDb = FavoritesTable.getItemByPId(id);
                            if (itemInDb.getCount() == 1) {
                                Toggle_favorite.setChecked(true);
                            }

                            Cursor CartitemInDb = CartTable.getItemByPId(id);
                            if (CartitemInDb.getCount() == 1) {
                                Toggle_cart.setChecked(true);
                            }

                            shareProduct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(Utiles.share("سمارت بازار" +"\n"+ title + "\n"+ url,"smartbazaar"));
                                }
                            });
                            Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        FavoritesTable.insert(id, title, price, image);
                                        Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();
                                        if (bottomNavigationView !=null){
                                            BottomMenuHelper.showBadge(context, bottomNavigationView, R.id.favorite_tab, String.valueOf(FavoritesTable.getAllItems().getCount()));

                                        }

                                    } else {
                                        FavoritesTable.deleteItemByPId(id);
                                        Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();
                                        if (bottomNavigationView !=null){
                                            BottomMenuHelper.showBadge(context, bottomNavigationView, R.id.favorite_tab, String.valueOf(FavoritesTable.getAllItems().getCount()));

                                        }

                                    }
                                }
                            });

                            Picasso.with(context).load(image).resize(200, 200).into(product_image, new Callback() {
                                @Override
                                public void onSuccess() {

                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {

                                }
                            });

                            Toggle_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        String p_option = "";
                                        if (options.length() != 0) {
                                            try {
                                                p_option = options.getString(0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        long insert = CartTable.insert(Integer.parseInt(id), title, store_name, price, image, color, p_option);

                                        if (insert != -1) {


                                            Toast.makeText(context, "تمت اضافة المنتج الى السلة", Toast.LENGTH_LONG).show();
                                            if (bottomNavigationView !=null){
                                                BottomMenuHelper.showBadge(context, bottomNavigationView, R.id.cart_tab, String.valueOf(FavoritesTable.getAllItems().getCount()));

                                            }
                                        }


                                    } else {

                                        CartTable.deleteItemByPId(id);
                                        Toast.makeText(context, "تمت الازالة من السلة", Toast.LENGTH_SHORT).show();
                                        if (bottomNavigationView !=null){
                                            BottomMenuHelper.showBadge(context, bottomNavigationView, R.id.cart_tab, String.valueOf(FavoritesTable.getAllItems().getCount()));

                                        }

                                    }
                                }
                            });
                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("title", title);
                                    bundle.putString("price", price);
                                    bundle.putString("image", image);
                                    bundle.putString("desc", desc);
                                    bundle.putString("store_name", store_name);

                                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            });


                            parent.addView(mView);


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

//                HashMap<String,String> hashMap  = new HashMap<>();
//                hashMap.put("type","3");
                return hashMap;

            }
        };

        requestQueue.add(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


    public void getRates(LinearLayout linearLayout, int rate) {

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




