package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.Favorites_model;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.fragments.ToolBarIcons;
import shop.smartbazaar.smartbazaar.public_classes.StoreProducts;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class ProductDetailsActivity extends AppCompatActivity {
    Context context;
    TextView product_name, product_price, product_seller,
            product_desc, tv_cartCount, tv_favoriteCount, product_option_label;
    Button btn_buy;


    LinearLayout colorsAria, options_aria, similar_products_lieanr;

    LayoutInflater layoutInflater;

    String c_color, c_option;
    private SliderLayout mDemoSlider;
    String p_id, p_title, p_price, p_image, p_desc, p_store_name, p_url;
    FragmentManager fm;

    PhotoView photo_view;
    RelativeLayout boxShow;
    ImageView window_close;
    ToggleButton Toggle_favorite, Toggle_addToCart;
    Toolbar toolbar;
    FrameLayout toolBar_content;


    ArrayList<Favorites_model> product_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        this.context = this;
        initViews();


        Bundle bundle = getIntent().getExtras();

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fm = getSupportFragmentManager();
        product_details = new ArrayList<>();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("التفاصيل");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set up tool bar icons
        setupToolBarIcons(p_title, p_url);


        if (bundle != null) {
            p_id = bundle.getString("id");
            p_title = bundle.getString("title");


            p_image = bundle.getString("image");
            p_price = bundle.getString("price");
            p_desc = bundle.getString("desc");
            p_store_name = bundle.getString("store_name");


            product_name.setText(p_title);
            product_price.setText(p_price + " $");
            product_seller.setText(p_store_name);
            product_desc.setText(p_desc);
        }
        window_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.FadeOut).repeat(0).duration(100).playOn(boxShow);
                boxShow.setVisibility(View.INVISIBLE);
            }
        });


        new getProductDetails(bundle.getString("id")).execute();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("product_id", p_id);
        new StoreProducts(context, Api.similar_products, StringRequest.Method.POST, hashMap, similar_products_lieanr, null).execute();


        //set Toggle switching
        Cursor CartitemInDb = CartTable.getItemByPId(p_id);
        if (CartitemInDb.getCount() == 1) {
            Toggle_addToCart.setChecked(true);
        }


        Cursor FavitemInDb = FavoritesTable.getItemByPId(p_id);
        if (FavitemInDb.getCount() == 1) {
            Toggle_favorite.setChecked(true);
        }



        Toggle_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    FavoritesTable.insert(p_id, p_title, p_price, p_image);
                    Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();


                } else {
                    FavoritesTable.deleteItemByPId(p_id);
                    Toast.makeText(context, "تمت الازالة من المفضلة", Toast.LENGTH_SHORT).show();


                }


                setupToolBarIcons(p_title, p_url);
            }
        });



        Toggle_addToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    long insert = CartTable.insert(Integer.parseInt(p_id), p_title, "smartbazaar", p_price, p_image, c_color, c_option);

                    if (insert != -1) {


                        Toast.makeText(context, "تمت اضافة المنتج الى السلة", Toast.LENGTH_LONG).show();


                    }


                } else {

                    CartTable.deleteItemByPId(p_id);
                    Toast.makeText(context, "تمت الازالة من السلة", Toast.LENGTH_SHORT).show();

                }

                setupToolBarIcons(p_title, p_url);
            }
        });

    }


    @Override
    public void onBackPressed() {

        int visible = boxShow.getVisibility();

        if (visible == View.VISIBLE) {
            boxShow.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();

        }
    }


    private void setupToolBarIcons(String title, String url) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Bundle toolBarFragData = new Bundle();
        toolBarFragData.putString("title", title);
        toolBarFragData.putString("url", url);
        ToolBarIcons toolBarIconsFrag = new ToolBarIcons();
        toolBarIconsFrag.setArguments(toolBarFragData);
        fragmentTransaction.replace(R.id.toolbar_content, toolBarIconsFrag);
        fragmentTransaction.commit();
    }

    private void initViews() {
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_seller = findViewById(R.id.seller_name);
        product_desc = findViewById(R.id.product_desc);
        tv_cartCount = findViewById(R.id.tv_cart_count);
        tv_favoriteCount = findViewById(R.id.tv_favorite_count);
        mDemoSlider = findViewById(R.id.slider);

        Toggle_addToCart = findViewById(R.id.addToCart);
        btn_buy = findViewById(R.id.buy_now);
        colorsAria = findViewById(R.id.colors_aria);
        options_aria = findViewById(R.id.options_aria);
        similar_products_lieanr = findViewById(R.id.similar_products);
        boxShow = findViewById(R.id.boxShow);
        photo_view = findViewById(R.id.photo_view);
        window_close = findViewById(R.id.window_close);
        Toggle_favorite = findViewById(R.id.Toggle_favorite);
        toolbar = findViewById(R.id.toolbar);
        toolBar_content = findViewById(R.id.toolbar_content);

        product_option_label = findViewById(R.id.product_option_label);
    }

    public class getProductDetails extends AsyncTask<String, String, String> {

        private String p_id;

        public getProductDetails(String p_id) {

            this.p_id = p_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.productsDetails(p_id), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.get("status").equals(true)) {


                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");


                            final String id = jsonObject1.getString("id");

                            final String pname = jsonObject1.getString("title");
                            String pDesc = jsonObject1.getString("description");
                            final String pPrice = jsonObject1.getString("price");
                            String classification_id = jsonObject1.getString("classification_id");
                            String category_id = jsonObject1.getString("category_id");
                            final String image_path = jsonObject1.getString("feature_image");
                            p_url = jsonObject1.getString("url");

                            JSONArray optionsArray = jsonObject1.getJSONArray("options");
                            HashMap<String, String> url_maps = new HashMap<String, String>();

                            JSONArray imagesArray = jsonObject1.getJSONArray("images");

                            for (int img = 0; img < imagesArray.length(); img++) {

                                url_maps.put(String.valueOf(img), imagesArray.getString(img));
                            }

                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                            mDemoSlider.setDuration(4000);


                            for (String name : url_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(context);
                                // initialize a SliderLayout
                                textSliderView
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.CenterInside);


                                textSliderView.bundle(new Bundle());

                                textSliderView.getBundle().putString("image", url_maps.get(name));
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        Picasso.with(context).load(slider.getBundle().getString("image")).into(photo_view);
                                        boxShow.setVisibility(View.VISIBLE);
                                        YoYo.with(Techniques.FadeIn).repeat(0).duration(500).playOn(boxShow);
                                    }
                                });
                                mDemoSlider.addSlider(textSliderView);
                            }

                            //colors
                            for (int option = 0; option < optionsArray.length(); option++) {
                                final JSONObject optionJson = optionsArray.getJSONObject(option);
                                final String color_name = optionJson.getString("color");
                                c_color = optionJson.getString("color");

                                View colorView = layoutInflater.inflate(R.layout.single_color, null, false);
                                final ToggleButton tv_color_name = colorView.findViewById(R.id.color_name);

                                tv_color_name.setTextOff(color_name);
                                tv_color_name.setTextOn(color_name);
                                tv_color_name.setText(optionJson.getString("color"));

                                colorView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//
                                        v.setBackground(Utiles.getDrawable(context, R.drawable.border_blue));

//
                                        try {
                                            c_color = optionJson.getString("color");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                                product_details.add(new Favorites_model(id, image_path, pname, pPrice));
                                colorsAria.addView(colorView);


                                final JSONArray options = optionJson.getJSONArray("options");


                                //options
                                if (options.length() == 0) {
                                    product_option_label.setVisibility(View.INVISIBLE);
                                }
                                for (int i = 0; i < options.length(); i++) {
                                    View optionView = layoutInflater.inflate(R.layout.single_color, null, false);
                                    final ToggleButton tv_option_name = optionView.findViewById(R.id.color_name);
                                    final String option_name = options.getString(i);
                                    tv_option_name.setText(options.getString(i));
                                    c_option = option_name;
                                    tv_option_name.setTextOff(option_name);
                                    tv_option_name.setTextOn(option_name);

                                    optionView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            c_option = option_name;


                                            v.setBackground(Utiles.getDrawable(context, R.drawable.border_blue));
                                        }
                                    });

                                    options_aria.addView(optionView);


                                }


                            }


                            // product_name.setText(pname);
                            product_price.setText(pPrice + " $");
                            product_seller.setText(jsonObject1.getString("store_name"));
                            product_desc.setText(pDesc);


                            btn_buy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

//                                    if (!new User(context).isLogin()) {
//                                        Intent intent = new Intent(context, RegisterOrLoginActivity.class);
//                                        startActivity(intent);
//                                    } else {
//
//                                        Bundle bundle = new Bundle();
//                                        bundle.putBoolean("isSingleProduct", true);
//                                        bundle.putString("p_id", id);
//                                        bundle.putString("color", c_color);
//                                        bundle.putString("option", c_option);
//
//                                        Intent intent = new Intent(context, MakeOrderActivity.class);
//                                        intent.putExtras(bundle);
//                                        startActivity(intent);
//                                    }

                                }
                            });

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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//            getSimilarProducts(similar_products_lieanr, p_id);
        }
    }


    public void getSimilarProducts(final LinearLayout parent, final String product_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Api.similar_products, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            View mView = layoutInflater.inflate(R.layout.single_product, null, false);
                            ImageView product_image = mView.findViewById(R.id.product_image);
                            TextView product_name = mView.findViewById(R.id.product_name);
                            TextView product_price = mView.findViewById(R.id.product_price);
                            TextView tv_discount = mView.findViewById(R.id.tv_discount);
                            ToggleButton Toggle_favorite = mView.findViewById(R.id.Toggle_favorite);
                            LinearLayout rate_linear = mView.findViewById(R.id.rate_linear);


                            final ProgressBar progressBar = mView.findViewById(R.id.progressBar);
                            final String id = jsonObject1.getString("id");
                            final String p_title = jsonObject1.getString("title");
                            final String p_desc = jsonObject1.getString("p_desc");
                            final String p_price = jsonObject1.getString("price");
                            final String p_image = jsonObject1.getString("feature_image");
                            final String p_store_name = jsonObject1.getString("store_name");

                            product_name.setText(jsonObject1.getString("title"));
                            product_price.setText(jsonObject1.getString("price") + " $ ");


                            Picasso.with(context).load(jsonObject1.getString("feature_image")).into(product_image, new Callback() {
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


                                    if (isChecked) {
                                        FavoritesTable.insert(id, p_title, p_price, p_image);
                                        Toast.makeText(context, "تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show();

                                    } else {
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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> hashMap = new HashMap<>();


                hashMap.put("product_id", product_id);

                return hashMap;
            }
        };

        requestQueue.add(stringRequest);

    }
}
