package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.support.v4.app.FragmentManager;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;


import shop.smartbazaar.smartbazaar.ProductsModel;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.adapters.ProductsBy_adapter;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.public_classes.ExpandableHeightGridView;


public class ProductsArchive extends AppCompatActivity {

    ExpandableHeightGridView expandableHeightGridView;
    Context context;
    ExpandableHeightGridView gridView;
    ArrayList<ProductsModel> productsModel;
    FragmentManager fm;
    String type;


    ScrollView scrollView;
    int scrollYPos = 0;
    int total_pages = 0;
    int current_page = 1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_archive);
        this.context = getApplicationContext();
        productsModel = new ArrayList<>();
        fm = getSupportFragmentManager();
        scrollView = findViewById(R.id.scrollView);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(toolbar);



        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type");

            if (type.equals("1")){
                getSupportActionBar().setTitle("احدث المنتجات");
            }else if (type.equals("2")){
                getSupportActionBar().setTitle("احدث الخصومات");
            }else if (type.equals("3")){
                getSupportActionBar().setTitle("منتجات اكثر زيارة");

            }
        }

        expandableHeightGridView = new ExpandableHeightGridView(context);

        gridView = findViewById(R.id.gridView);
        gridView.setExpanded(true);

        Display display = getWindowManager().getDefaultDisplay();

        if (display != null) {

            Point point = new Point();

            display.getSize(point);

            int width = point.x;

            gridView.setColumnWidth(width / 2 - 15);


        }


        getProducts(type, String.valueOf(current_page));
        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {


                            current_page = current_page + 1;

                            getProducts(type, String.valueOf(current_page));


                        }
                    }
                });


    }


    private void getProducts(final String type, String page) {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url;
        if (type.equals("1")) {
            url = Api.lastProducts + "?page=" + page;
        } else if (type.equals("2")) {
            url = Api.getLastProductsDiscounts + "?page=" + page;

        } else {
            url = Api.productsWeOffer + "?page=" + page;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.get("status").equals(true)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int product = 0; product < jsonArray.length(); product++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(product);
                            JSONArray image = jsonObject1.getJSONArray("images");
                            JSONArray options = jsonObject1.getJSONArray("options");
//
                            String option = "";
                            String color = "";


                            productsModel.add(new ProductsModel(
                                    jsonObject1.getString("id"),
                                    jsonObject1.getString("title"),
                                    jsonObject1.getString("description"),
                                    jsonObject1.getString("price"),
                                    jsonObject1.getString("discount"),
                                    jsonObject1.getString("classification_id"),
                                    jsonObject1.getString("category_id"),
                                    jsonObject1.getString("sub_category_id"),
                                    jsonObject1.getString("rating"),
                                    jsonObject1.getString("feature_image"),
                                    jsonObject1.getString("views"),
                                    jsonObject1.getString("certified"),
                                    jsonObject1.getString("code"),
                                    jsonObject1.getString("discount_end_date"),
                                    jsonObject1.getString("video"),
                                    jsonObject1.getString("pay_with"),
                                    jsonObject1.getString("brand_id"),
                                    jsonObject1.getString("brand_name"),
                                    jsonObject1.getString("shipment"),
                                    option,
                                    color,
                                    jsonObject1.getString("url"),
                                    jsonObject1.getString("store_name")

                            ));
                        }


                        gridView.setAdapter(new ProductsBy_adapter(context, fm, productsModel));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        queue.add(stringRequest);


    }
}
