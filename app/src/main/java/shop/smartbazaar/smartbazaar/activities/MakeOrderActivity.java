package shop.smartbazaar.smartbazaar.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shop.smartbazaar.smartbazaar.Cities_model;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.MainActivity;
import shop.smartbazaar.smartbazaar.adapters.Cities_adapter;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.fragments.HomeFragment;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;

public class MakeOrderActivity extends AppCompatActivity {

    Context context;
    Spinner sp_shpping_type, sp_area;


    LayoutInflater layoutInflater;

    ArrayList<Cities_model> citiesArrayList;


    Button btn_makeOrder;
    FragmentManager fm;
    EditText edt_address;
    ActionBar actionBar;

    RelativeLayout parent_layout;

    Bundle bundle;

    TextView tv_normal_ship,tv_express_ship;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);


        this.context = this;
        bundle = getIntent().getExtras();
        parent_layout = findViewById(R.id.parent);
        citiesArrayList = new ArrayList<>();
        fm = getSupportFragmentManager();
        sp_shpping_type = findViewById(R.id.shipping_type);
        sp_area = findViewById(R.id.sp_area);
        btn_makeOrder = findViewById(R.id.btn_makeOrder);
        edt_address = findViewById(R.id.edt_address);
        tv_normal_ship = findViewById(R.id.tv_normal_ship);
        tv_express_ship = findViewById(R.id.tv_express_ship);

        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                parent_layout.getWindowVisibleDisplayFrame(r);

                int heightDiff = parent_layout.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 500) { // if more than 100 pixels, its probably a keyboard...

                    btn_makeOrder.setVisibility(View.INVISIBLE);


                } else {
                    btn_makeOrder.setVisibility(View.VISIBLE);


                }

            }
        });


        sp_shpping_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position ==1){
                    tv_express_ship.setVisibility(View.INVISIBLE);
                    tv_normal_ship.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.DropOut).repeat(0).duration(1000).playOn(tv_normal_ship);
                }else if(position ==2){
                    tv_normal_ship.setVisibility(View.INVISIBLE);
                    tv_express_ship.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.DropOut).repeat(0).duration(1000).playOn(tv_express_ship);
                }else {
                    tv_express_ship.setVisibility(View.INVISIBLE);
                    tv_normal_ship.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAreas();


        btn_makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (sp_shpping_type.getSelectedItemPosition() ==0){
                    Toast.makeText(context, "اختر نوع الشحن", Toast.LENGTH_SHORT).show();
                }else if (sp_area.getSelectedItemPosition() ==0){
                    Toast.makeText(context, "اختر المنطقة", Toast.LENGTH_SHORT).show();
                }else {


                    // cart item
                    if (bundle != null) {
                        boolean isSingleProduct = bundle.getBoolean("isSingleProduct");

                        if (!isSingleProduct) {
                            final Cursor allItemCart = CartTable.getAllItems();

                            String address = edt_address.getText().toString();


                            JSONArray secondOrderData = new JSONArray();

                            while (allItemCart.moveToNext()) {
                                JSONObject jsonObject = new JSONObject(); // order item row

                                String product_id = allItemCart.getString(1);

                                try {
                                    jsonObject.put("product_id", product_id);
                                    jsonObject.put("quantity", allItemCart.getString(4));
                                    jsonObject.put("color", allItemCart.getString(7));
                                    if (allItemCart.getString(8) == null) {
                                        jsonObject.put("option", "");

                                    } else {
                                        jsonObject.put("option", allItemCart.getString(8));

                                    }

                                    secondOrderData.put(jsonObject);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            JSONObject firstOrderData = new JSONObject(); //first order data
                            try {
                                String aria_id = sp_area.getSelectedView().getTag().toString();
                                String shipment_type = String.valueOf(sp_area.getSelectedItemPosition() + 1);
                                firstOrderData.put("shipment_area", aria_id);
                                firstOrderData.put("shipment_type", shipment_type);
                                firstOrderData.put("address", address);
                                firstOrderData.put("type", 2);

                                firstOrderData.put("orders", secondOrderData);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (edt_address.getText().toString().equals("")) {
                                Toast.makeText(context, "الرجاء كتابة العنوان الخاص بك", Toast.LENGTH_SHORT).show();
                            } else {
                                makeNewOrder(firstOrderData);

                            }

                        } else {

                            //single item selected
                            String address = edt_address.getText().toString();
                            String product_id = bundle.getString("p_id");
                            String color = bundle.getString("color");
                            String option = bundle.getString("option");


                            try {
                                JSONArray orders_array = new JSONArray();


                                JSONObject order_item = new JSONObject();
                                order_item.put("product_id", product_id);
                                order_item.put("quantity", 1);
                                order_item.put("color", color);
                                if (option == null) {

                                    order_item.put("option", "");
                                } else {
                                    order_item.put("option", option);

                                }
                                orders_array.put(order_item);

                                JSONObject firstOrderData = new JSONObject(); //first order data
                                String aria_id = sp_area.getSelectedView().getTag().toString();
                                String shipment_type = String.valueOf(sp_area.getSelectedItemPosition() + 1);
                                try {
                                    firstOrderData.put("shipment_area", aria_id);
                                    firstOrderData.put("shipment_type", shipment_type);
                                    firstOrderData.put("address", address);
                                    firstOrderData.put("type", 2);

                                    firstOrderData.put("orders", orders_array);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (edt_address.getText().toString().equals("")) {
                                    Toast.makeText(context, "الرجاء كتابة العنوان الخاص بك", Toast.LENGTH_SHORT).show();
                                } else {

                                    makeNewOrder(firstOrderData);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }


                } //end of spinner condition
            }
        });


        final ArrayList<String> shpping_type_array = new ArrayList<>();
        shpping_type_array.add("اختر نوع الشحن");
        shpping_type_array.add("شحن عادي");
        shpping_type_array.add("شحن اكسبريس");
        ArrayAdapter spinner_adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, shpping_type_array);


        sp_shpping_type.setAdapter(spinner_adapter);




    }



    private void getAreas() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, Api.getAreas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    citiesArrayList.add(new Cities_model(0,"اختر المنطقة","2","3"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int area_id = jsonObject1.getInt("area_id");
                        String shipping_name = jsonObject1.getString("name");
                        String normal_shipment_price = jsonObject1.getString("normal_shipment_price");
                        String express_shipment_price = jsonObject1.getString("normal_shipment_price");


                        citiesArrayList.add(new Cities_model(area_id,shipping_name,normal_shipment_price,express_shipment_price));



                    }


                    Cities_adapter cities_adapter = new Cities_adapter(context, citiesArrayList);

                    sp_area.setAdapter(cities_adapter);


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




    private void makeNewOrder(final JSONObject body) {


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.makeOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("sajjadiq", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(context, "العملية تمت بنجاح شكرا لاختيارك سمارت بازار", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    } else {

                        String message = jsonObject.getString("message");
                        Log.e("sajjadiq",message);
                        Toast.makeText(context, "حدث خطأ غير متوقع راسل الدعم الفني !", Toast.LENGTH_SHORT).show();
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
            public byte[] getBody() throws AuthFailureError {

                return body.toString().getBytes();

            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Authorization", "Bearer " + new User(context).getAccess_toekn());

                return params;

            }
        };

        requestQueue.add(stringRequest);


    }
}
