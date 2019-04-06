package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.activities.RegisterOrLoginActivity;
import shop.smartbazaar.smartbazaar.api.Api;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;

public class SearchFragment  extends Fragment {


    Context context;
    EditText edt_search;
    LinearLayout search_result_linear;

    FragmentManager fm;
    ScrollView scrollView;
    int scrollYPos = 0;
    int total_pages = 0;
    int current_page = 1;

    ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

        fm = getFragmentManager();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.search_layout,null,false);


        edt_search = mView.findViewById(R.id.edt_search);
        search_result_linear = mView.findViewById(R.id.search_result_linear);
        scrollView = mView.findViewById(R.id.scrollView);
        progressBar = mView.findViewById(R.id.progressBar);


        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    final String search_string = edt_search.getText().toString();

                    Search_post(search_string,String.valueOf(current_page));

                    scrollView.getViewTreeObserver()
                            .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                                @Override
                                public void onScrollChanged() {
                                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {


                                        if (total_pages >= current_page){
                                            current_page = current_page + 1;
                                            Search_post(search_string,String.valueOf(current_page));


                                        }
                                    }
                                }
                            });


                    return  true;
                }

                return false;
            }
        });




        return mView;
    }



    public void Search_post (String title,String page){



            progressBar.setVisibility(View.VISIBLE);
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Api.search(title, page), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        total_pages = jsonObject.getInt("total_pages");

                        progressBar.setVisibility(View.INVISIBLE);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length();i++){

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String p_title = jsonObject1.getString("title");
                            final String p_id = jsonObject1.getString("id");
                            final String title = jsonObject1.getString("title");
                            final String price = jsonObject1.getString("price");
                            final String image = jsonObject1.getString("feature_image");
                            final String desc = jsonObject1.getString("description");
                            final String store_name = jsonObject1.getString("store_name");


                            View view = LayoutInflater.from(context).inflate(R.layout.single_search_result,null,false);
                            TextView tv_title = view.findViewById(R.id.title);
                            tv_title.setText(p_title);
                            view.setTag(p_id);


                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", p_id);
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
                            search_result_linear.addView(view);
                        }



                        scrollYPos = scrollView.getScrollY();



                    }catch (JSONException e){

                        e.getStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(stringRequest);







    }
}
