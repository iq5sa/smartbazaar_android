package shop.smartbazaar.smartbazaar.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import shop.smartbazaar.smartbazaar.ProductsModel;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.adapters.ProductsBy_adapter;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.public_classes.ExpandableHeightGridView;
import shop.smartbazaar.smartbazaar.public_classes.ObservableScrollView;
import shop.smartbazaar.smartbazaar.public_classes.ScrollViewListener;

public class ProductInActivity extends AppCompatActivity implements ScrollViewListener {


    Context context;
    ArrayList<ProductsModel> productsArray;
    ExpandableHeightGridView gridView;

    ExpandableHeightGridView expandableHeightGridView;

    LinearLayout categories_parent;

    FragmentManager fm;
    String classification_id;
    String type;

    Bundle bundle;
    Toolbar toolbar;
    ObservableScrollView scrollView;
    private int visibleThreshold = 2;
    private  int mLastFirstVisibleItem;
    private int currentPage = 1;
    private int previousTotal = 0;
    private int filtter = 1;
    private boolean loading = true;
    ProgressBar progressBar;
    ImageView iv_filter;

    ProductsBy_adapter productsBy_adapter;
    private boolean isHttpOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in);
        this.context = this;



        initViews();


        bundle = getIntent().getExtras();

        scrollView.setScrollViewListener(this);

        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        setSupportActionBar(toolbar);

        expandableHeightGridView = new ExpandableHeightGridView(context);
        fm = getSupportFragmentManager();




        String title = bundle.getString("title");

        getSupportActionBar().setTitle(title);


        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog alertDialog = showDialog(context);

                alertDialog.show();
            }
        });


        //init products in
        productsArray = new ArrayList<>();
        productsBy_adapter = new ProductsBy_adapter(context, fm, productsArray);


        gridView.setExpanded(true);

        Display display = getWindowManager().getDefaultDisplay();

        if (display != null) {

            Point point = new Point();

            display.getSize(point);

            int width = point.x;

            gridView.setColumnWidth(width / 2 - 15);


        }

        if (bundle != null) {

            //get products with classification id
            classification_id = bundle.getString("id");
            type = bundle.getString("type");


            getProducts(classification_id, type,String.valueOf(currentPage));




            if (type.equals("1")) {
                //classification
                getCategories(classification_id, categories_parent);

            } else if (type.equals("2")) {
                //category
                getSubCategories(classification_id, categories_parent);
            } else if (type.equals("3")) {
                //sub category
                getSubCategories(classification_id, categories_parent);

            } else if (type.equals("4")) {
                //brands
                setBrands(categories_parent);
            }

        }


    }


    private void initViews(){
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        categories_parent = findViewById(R.id.categories);
        gridView = findViewById(R.id.gridView);
        iv_filter = findViewById(R.id.iv_filter);

    }




    private Dialog showDialog(final Context context){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("عرض حسب ..");
        Resources resources = getResources();

        String[] filters_name = resources.getStringArray(R.array.filter);

        alert.setItems(filters_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                productsArray.clear();
                currentPage = 1;
                filtter = which + 1;
                getProducts(classification_id, type,String.valueOf(currentPage));
            }
        });


       return alert.create();


    }



    private void getProducts(final String id, final String type, final String page) {


        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.productsBy, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.get("status").equals(true)) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int product = 0; product < jsonArray.length(); product++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(product);
                            JSONArray  image = jsonObject1.getJSONArray("images");
                            JSONArray  options = jsonObject1.getJSONArray("options");
                            JSONObject options_array = options.getJSONObject(0);

                            //TODO:: getting options

                            String option = "";
                            String color = "";
                            if (options_array.length() !=0){

                                try{
                                    option = options_array.getJSONArray("options").getString(0);
                                }catch (JSONException e){
                                    e.getStackTrace();
                                    option = "";
                                }
                                color = options_array.getString("color");
                            }





                            productsArray.add(new ProductsModel(
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


                        if (productsArray.size() <=10){
                            gridView.setAdapter(productsBy_adapter);
                        }else {
                            productsBy_adapter.notifyDataSetChanged();
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
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("type", type);
                params.put("page", String.valueOf(page));
                params.put("filter", String.valueOf(filtter));

                return params;

            }
        };

        queue.add(stringRequest);


    }
    private void getCategories(final String classification_id, final LinearLayout parent) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.categoriesInClassifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int category = 0; category < jsonArray.length(); category++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(category);
                            final String category_id = jsonObject1.getString("id");
                            final String category_name = jsonObject1.getString("name");
                            String category_order = jsonObject1.getString("order");

                            View category_view = getLayoutInflater().inflate(R.layout.single_classification, null, false);
                            TextView tv_category_name = category_view.findViewById(R.id.name);

                            tv_category_name.setText(category_name);

                            category_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", category_id);
                                    bundle.putString("title", category_name);
                                    bundle.putString("type", "2");

                                    Intent intent = new Intent(context, ProductInActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            parent.addView(category_view);


                        }

                    }
                } catch (JSONException e) {

                    e.getStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("classification_id", classification_id);


                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
    private void getSubCategories(final String category_id, final LinearLayout parent) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getSubCategories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int category = 0; category < jsonArray.length(); category++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(category);
                            final String Subcategory_id = jsonObject1.getString("id");
                            final String category_name = jsonObject1.getString("name");

                            View category_view = getLayoutInflater().inflate(R.layout.single_classification, null, false);
                            TextView tv_category_name = category_view.findViewById(R.id.name);

                            tv_category_name.setText(category_name);

                            category_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", Subcategory_id);
                                    bundle.putString("title", category_name);
                                    bundle.putString("type", "3");

                                    Intent intent = new Intent(context, ProductInActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            parent.addView(category_view);


                        }

                    }
                } catch (JSONException e) {

                    e.getStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<>();
                map.put("category_id", classification_id);


                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
    public  void setBrands(final LinearLayout parent) {

        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.brands, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.get("status").equals(true)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length() / 2; i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            View mView = layoutInflater.inflate(R.layout.single_brand, null, false);
                            ImageView brand_image = mView.findViewById(R.id.brand_image);

                            Picasso.with(context).load(jsonObject1.getString("image")).resize(150, 130).into(brand_image);

                            final String id = jsonObject1.getString("id");
                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("type", "4");

                                    Intent intent = new Intent(context, ProductInActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
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
        });

        queue.add(stringRequest);


    }

    @Override
    public void onScrollEnded(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        currentPage++;

        if (isHttpOpen){
            getProducts(classification_id, type,String.valueOf(currentPage));
            isHttpOpen = false;
        }else {

            //updating is http open
            Timer timer = new Timer();
            class OpenHttp extends TimerTask {

                @Override
                public void run() {

                    isHttpOpen = true;
                }
            }
            timer.schedule(new OpenHttp(),0,2000);

        }



    }



}
