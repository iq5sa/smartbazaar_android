package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.ProductsModel;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.activities.ProductInActivity;
import shop.smartbazaar.smartbazaar.activities.ProductsArchive;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.public_classes.StoreProducts;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class HomeFragment extends Fragment {

    private SliderLayout mDemoSlider;
    private Context context;
    private LinearLayout firstCategory_row, secondCategory_row, brands_layout,
            lastProductsParent, lastProductsParent2,most_visited,weOffer;
    private LayoutInflater layoutInflater;
    private FragmentManager fm;
    private HorizontalScrollView categories_scroll;
    private ActionBar actionBar;
    private ImageView actionBarLogo;
    private EditText edt_search;
    ScrollView scrollView;
    LinearLayout search_result_linear;
    int scrollYPos = 0;
    int total_pages = 0;
    int current_page = 1;
    SwipeRefreshLayout swipeRefresh;

    LinearLayout homeViewGroup;

    TextView tv_showMore_one,tv_lastDiscount,tv_mostVisited;
    AppCompatActivity activity;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();



        fm = getFragmentManager();


        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        activity = ((AppCompatActivity) getActivity());


        actionBarLogo = getActivity().findViewById(R.id.actionBarLogo);
        bottomNavigationView = activity.findViewById(R.id.bottomBar);


        if (context != null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }







    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View custom_view = inflater.inflate(R.layout.homelayout, container, false);
        mDemoSlider = custom_view.findViewById(R.id.slider);
        firstCategory_row = custom_view.findViewById(R.id.firstCategory_row);
        secondCategory_row = custom_view.findViewById(R.id.secondCategory_row);
        brands_layout = custom_view.findViewById(R.id.brands_layout);
        lastProductsParent = custom_view.findViewById(R.id.lastProductsParent);
        lastProductsParent2 = custom_view.findViewById(R.id.lastProductsParent2);
        categories_scroll = custom_view.findViewById(R.id.categories_scroll);
        most_visited = custom_view.findViewById(R.id.most_visited);
        edt_search = custom_view.findViewById(R.id.edt_search);
        scrollView = custom_view.findViewById(R.id.scrollView);
        search_result_linear = custom_view.findViewById(R.id.search_result_linear);
        swipeRefresh = custom_view.findViewById(R.id.swipeRefresh);
        homeViewGroup = custom_view.findViewById(R.id.homeViewGroup);
        tv_showMore_one = custom_view.findViewById(R.id.tv_showMore_one);
        tv_lastDiscount = custom_view.findViewById(R.id.tv_lastDiscount);
        tv_mostVisited = custom_view.findViewById(R.id.tv_mostVisited);
        weOffer = custom_view.findViewById(R.id.weOffer);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO:: splash

                try {
                    activity.findViewById(R.id.progressBarArea).setVisibility(View.INVISIBLE);

                }catch (Exception e){
                    e.getStackTrace();
                }


            }
        },7000);

         HashMap<String,String> hashMap = new HashMap<>();
         hashMap.put("type","3");

         new StoreProducts(context,Api.lastProducts,0,null,lastProductsParent,bottomNavigationView).execute();
         new StoreProducts(context,Api.getLastProductsDiscounts,0,null,lastProductsParent2,bottomNavigationView).execute();
         new StoreProducts(context,Api.getProductWithType,StringRequest.Method.POST,hashMap,most_visited,bottomNavigationView).execute();
         new StoreProducts(context,Api.productsWeOffer,0,null,weOffer,bottomNavigationView).execute();



        homeViewGroup.setClickable(false);
        if (actionBarLogo != null) {

            actionBarLogo.setVisibility(View.VISIBLE);

        }

        tv_showMore_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductsArchive.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        tv_lastDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsArchive.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","2");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tv_mostVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsArchive.class);
                Bundle bundle = new Bundle();
                bundle.putString("type","3");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new getData(firstCategory_row, secondCategory_row, brands_layout).execute();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getData(firstCategory_row, secondCategory_row, brands_layout).execute();

            }
        });

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomIndicator((PagerIndicator) custom_view.findViewById(R.id.custom_indicator));

        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utiles.ReplaceFragment(fm,new SearchFragment(),null);
            }
        });


        return custom_view;


    }


    @Override
    public void onPause() {
        super.onPause();
        actionBarLogo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (actionBarLogo !=null){
            actionBarLogo.setVisibility(View.VISIBLE);

        }
        if (actionBar !=null){
            actionBar.setTitle("");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actionBarLogo != null) {
            actionBarLogo.setVisibility(View.INVISIBLE);

        }
    }


    public void setmDemoSlider() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Api.getSlider, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.getBoolean("status");

                    if (status) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            int id = jsonObject1.getInt("id");
                            String title = jsonObject1.getString("title");
                            String description = jsonObject1.getString("description");
                            String link = jsonObject1.getString("link");
                            String image = jsonObject1.getString("image");
                            String classification_id = jsonObject1.getString("classification_id");
                            String category_id = jsonObject1.getString("category_id");
                            String sub_category_id = jsonObject1.getString("sub_category_id");
                            final String brand_id = jsonObject1.getString("brand_id");
                            TextSliderView textSliderView = new TextSliderView(context);
                            textSliderView.bundle(new Bundle());
                            Bundle bundle = textSliderView.getBundle();



                            if (brand_id.equals("null")){
                                //check sub categories
                                if (sub_category_id.equals("null")){
                                    if (category_id.equals("null")){
                                        if (classification_id.equals("null")){


                                            bundle.putString("type",null);
                                            bundle.putString("id",null);
                                            bundle.putString("link",link);

                                        }else {
                                            bundle.putString("type","1");
                                            bundle.putString("id",classification_id);
                                        }
                                    }else {
                                        bundle.putString("type","2");
                                        bundle.putString("id",category_id);
                                    }
                                }else {
                                    bundle.putString("type","3");
                                    bundle.putString("id",sub_category_id);

                                }
                            }else {

                                bundle.putString("type","4");
                                bundle.putString("id",brand_id);
                            }

                            textSliderView
                                    .image(image)
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {

                                            Bundle bundle = slider.getBundle();

                                            if (bundle.getString("type") == null && bundle.getString("id") == null){
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(bundle.getString("link")));
                                                startActivity(intent);

                                            }else {
                                                Intent intent = new Intent(context,ProductInActivity.class);
                                                intent.putExtras(bundle);
                                                startActivity(intent);

                                            }


                                        }
                                    });
                            mDemoSlider.addSlider(textSliderView);


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


        requestQueue.add(stringRequest);
    }

    public void setCategoriesToFirstRow(final LinearLayout parent, final LinearLayout parent2) {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.classifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.get("status").equals(true)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length() / 2; i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            View mView = layoutInflater.inflate(R.layout.single_category, null, false);
                            ImageView iv_category_image = mView.findViewById(R.id.category_image);
                            TextView tv_category_name = mView.findViewById(R.id.category_name);

                            Picasso.with(getContext()).load(jsonObject1.getString("image")).resize(150, 100).into(iv_category_image);
                            tv_category_name.setText(jsonObject1.getString("name"));

                            final String id = jsonObject1.getString("id");
                            final String category_name = jsonObject1.getString("name");


                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("title", category_name);
                                    bundle.putString("type", "1");
                                    Intent intent = new Intent(context, ProductInActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);


//                                    Utiles.ReplaceFragment(fm, new ProductsBy(), bundle);
                                }
                            });


                            parent.addView(mView);


                        }


                        for (int i = jsonArray.length() / 2; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            View mView = layoutInflater.inflate(R.layout.single_category, null, false);
                            ImageView category_image = mView.findViewById(R.id.category_image);
                            TextView category_name = mView.findViewById(R.id.category_name);

                            category_name.setText(jsonObject1.getString("name"));
                            Picasso.with(getContext()).load(jsonObject1.getString("image")).resize(150, 100).into(category_image);
                            mView.setTag(jsonObject1.getString("id"));

                            final String category_name2 = jsonObject1.getString("name");


                            final String id = jsonObject1.getString("id");


                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("title", category_name2);
                                    bundle.putString("type", "1");
                                    Intent intent = new Intent(context, ProductInActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
//                                    Utiles.ReplaceFragment(fm, new ProductsBy(), bundle);
                                }
                            });
                            parent2.addView(mView);


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

    public void setBrands(final LinearLayout parent) {



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

                            Picasso.with(getContext()).load(jsonObject1.getString("image")).resize(150, 130).into(brand_image);

                            final String id = jsonObject1.getString("id");
                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("type", "4");

                                    Intent intent = new Intent(context,ProductInActivity.class);
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



    public void setLastProducts(final LinearLayout parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.lastProducts, new Response.Listener<String>() {
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
                            TextView product_name = mView.findViewById(R.id.product_name);
                            TextView product_price = mView.findViewById(R.id.product_price);
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



                            JSONArray options_array = jsonObject1.getJSONArray("options");
                            JSONObject firstJsonOption = options_array.getJSONObject(0);
                            final String color = firstJsonOption.getString("color");


                            final JSONArray options = firstJsonOption.getJSONArray("options");


                            product_name.setText(title);
                            product_price.setText(price + " $ ");

                            Cursor itemInDb = FavoritesTable.getItemByPId(id);
                            if (itemInDb.getCount() ==1){
                                Toggle_favorite.setChecked(true);
                            }

                            Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked){
                                        FavoritesTable.insert(id, title, price, image);
                                        Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();

                                    }else {
                                        FavoritesTable.deleteItemByPId(id);
                                        Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            Picasso.with(getContext()).load(image).resize(200, 200).into(product_image, new Callback() {
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

                                    if (isChecked){
                                        String p_option = "";
                                        if (options.length() !=0){
                                            try {
                                                p_option = options.getString(0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        long insert = CartTable.insert(Integer.parseInt(id), title, store_name, price, image,color , p_option);

                                        if (insert != -1) {


                                            Toast.makeText(context, "تمت اضافة المنتج الى السلة", Toast.LENGTH_LONG).show();
                                        }


                                    }else {

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
                                    bundle.putString("title", title);
                                    bundle.putString("price", price);
                                    bundle.putString("image", image);
                                    bundle.putString("desc", desc);
                                    bundle.putString("store_name", store_name);

                                   Intent intent = new Intent(context, ProductDetailsActivity.class);
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

    public void setMost_visited(final LinearLayout parent) {

        final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.getProductWithType, new Response.Listener<String>() {
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
                            TextView product_name = mView.findViewById(R.id.product_name);
                            TextView product_price = mView.findViewById(R.id.product_price);
                            ToggleButton Toggle_favorite = mView.findViewById(R.id.Toggle_favorite);
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

                            product_name.setText(title);
                            product_price.setText(price + " $ ");

                            Cursor itemInDb = FavoritesTable.getItemByPId(id);
                            if (itemInDb.getCount() ==1){
                                Toggle_favorite.setChecked(true);
                            }

                            Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked){
                                        FavoritesTable.insert(id, title, price, image);
                                        Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();

                                    }else {
                                        FavoritesTable.deleteItemByPId(id);
                                        Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                            Picasso.with(getContext()).load(image).resize(200, 200).into(product_image, new Callback() {
                                @Override
                                public void onSuccess() {

                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {

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
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("type","3");

                return hashMap;
            }
        };

        queue.add(stringRequest);


    }

    public void setDiscountProducts(final LinearLayout parent) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, Api.getLastProductsDiscounts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        try {

                        } catch (Exception e) {
                            e.getStackTrace();
                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            View mView = layoutInflater.inflate(R.layout.single_product, null, false);
                            ImageView product_image = mView.findViewById(R.id.product_image);
                            TextView product_name = mView.findViewById(R.id.product_name);
                            TextView product_price = mView.findViewById(R.id.product_price);
                            TextView tv_discount = mView.findViewById(R.id.tv_discount);
                            ToggleButton Toggle_favorite = mView.findViewById(R.id.Toggle_favorite);

                            tv_discount.setVisibility(View.VISIBLE);
                            tv_discount.setPaintFlags(tv_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            tv_discount.setText(jsonObject1.getString("discount") + " $");

                            final ProgressBar progressBar = mView.findViewById(R.id.progressBar);
                            final String id = jsonObject1.getString("id");
                            final String p_title = jsonObject1.getString("title");
                            final String p_price = jsonObject1.getString("price");
                            final String p_image = jsonObject1.getString("feature_image");
                            final String p_desc = jsonObject1.getString("description");
                            final String p_store_name = jsonObject1.getString("store_name");

                            product_name.setText(jsonObject1.getString("title"));
                            product_price.setText(jsonObject1.getString("price") + " $ ");

                            Cursor itemInDb = FavoritesTable.getItemByPId(id);
                            if (itemInDb.getCount() == 1) {
                                Toggle_favorite.setChecked(true);
                            }
                            Picasso.with(getContext()).load(jsonObject1.getString("feature_image")).into(product_image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {

                                }
                            });



                            Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                    if (isChecked){
                                        FavoritesTable.insert(id, p_title, p_price, p_image);
                                        Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();

                                    }else {
                                        FavoritesTable.deleteItemByPId(id);
                                        Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    bundle.putString("title", p_title);
                                    bundle.putString("price", p_price);
                                    bundle.putString("image", p_image);
                                    bundle.putString("desc", p_desc);
                                    bundle.putString("store_name", p_store_name);

                                    Intent intent = new Intent(context, ProductDetailsActivity.class);
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

        requestQueue.add(stringRequest);

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


    private class getData extends AsyncTask<String, String, String> {

        LinearLayout firstCategory_row, secondCategory_row, brands_layout;

        public getData(final LinearLayout firstCategory_row, final LinearLayout secondCategory_row, final LinearLayout brands_layout) {

            this.firstCategory_row = firstCategory_row;
            this.secondCategory_row = secondCategory_row;
            this.brands_layout = brands_layout;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            setCategoriesToFirstRow(firstCategory_row, secondCategory_row);
            setBrands(brands_layout);
            setmDemoSlider();


            return "ok";
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            swipeRefresh.setRefreshing(false);

        }
    }


}
