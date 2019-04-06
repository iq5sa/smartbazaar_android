package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import shop.smartbazaar.smartbazaar.Cities_model;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.adapters.Cities_adapter;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.user.User;

public class MyOrdersFragment extends Fragment {

    Context context;

    LinearLayout order_linear;

    ActionBar actionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View custom_view = inflater.inflate(R.layout.myorderlayout,container,false);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("طلباتي");


        order_linear = custom_view.findViewById(R.id.order_linear);



        getOrders(order_linear);
        return custom_view;
    }



    private void getOrders(final LinearLayout linear){


            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.GET, Api.getOrders, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        if (status){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            Log.v("tag",jsonObject.toString());

                            for (int order = 0 ; order  < jsonArray.length();order++){

                                JSONObject jsonObject1 = jsonArray.getJSONObject(order);

                                String p_id = jsonObject1.getString("product_id");
                                String title = jsonObject1.getString("title");
                                String image = jsonObject1.getString("feature_image");

                                String qtn = jsonObject1.getString("quantity");

                                String price = jsonObject1.getString("price");
                                String option = jsonObject1.getString("option");



                                View orderView = getLayoutInflater().inflate(R.layout.single_order_item,null,false);

                                TextView tv_qtn = orderView.findViewById(R.id.qtn_value);
                                TextView totalPrice = orderView.findViewById(R.id.totalPrice);
                                TextView product_name = orderView.findViewById(R.id.product_name);
                                ImageView orderImage = orderView.findViewById(R.id.product_image);
                                final ProgressBar progressBar = orderView.findViewById(R.id.progressBar);

                                Picasso.with(context).load(image).into(orderImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });

                                tv_qtn.setText(qtn);
                                totalPrice.setText(price);
                                product_name.setText(title);


                                linear.addView(orderView);



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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();

                    params.put("Authorization", "Bearer " + new User(context).getAccess_toekn());

                    return params;
                }
            };

            requestQueue.add(request);

    }
}
