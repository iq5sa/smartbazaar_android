package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import shop.smartbazaar.smartbazaar.activities.MakeOrderActivity;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.activities.ProductDetailsActivity;
import shop.smartbazaar.smartbazaar.activities.RegisterOrLoginActivity;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.user.User;

public class CartFragment extends Fragment {

    LinearLayout cart_parent;

    double totalPriceInCart = 0;
    Button btn_makeOrder;
    FragmentManager fm;
    Context context;
    LinearLayout process_area_linear,noItem_linear;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getFragmentManager();
        context = getContext();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View custom_view = inflater.inflate(R.layout.cartlayout, container, false);


        cart_parent = custom_view.findViewById(R.id.cart_parent);
        btn_makeOrder = custom_view.findViewById(R.id.btn_makeOrder);
        process_area_linear = custom_view.findViewById(R.id.process_area);
        noItem_linear = custom_view.findViewById(R.id.noItemsItems_area);



        btn_makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new User(context).isLogin()){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isSingleProduct",false);

                    Intent intent = new Intent(context, MakeOrderActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{

                    Intent intent = new Intent(context, RegisterOrLoginActivity.class);
                    context.startActivity(intent);
                }

            }
        });
        final TextView tv_totalPriceIncart = custom_view.findViewById(R.id.totalPriceIncart);

        final Cursor allItemCart = CartTable.getAllItems();
        if (allItemCart.getCount() == 0){
            process_area_linear.setVisibility(View.INVISIBLE);
            noItem_linear.setVisibility(View.VISIBLE);
        }


        while (allItemCart.moveToNext()) {

            final View item_view = inflater.inflate(R.layout.single_cart_item, null, false);

            TextView product_name = item_view.findViewById(R.id.product_name);
            final TextView product_qtn = item_view.findViewById(R.id.product_qtn);
            TextView decrease = item_view.findViewById(R.id.decrease);
            TextView increase = item_view.findViewById(R.id.increase);
            final TextView totalPrice = item_view.findViewById(R.id.totalPrice);
            ImageView product_image = item_view.findViewById(R.id.product_image);
            Button btn_delete_item = item_view.findViewById(R.id.btn_delete_item);

            final double product_price = Double.parseDouble(allItemCart.getString(5));
            product_name.setText(allItemCart.getString(2));
            product_qtn.setText(allItemCart.getString(4));
            final String item_id = allItemCart.getString(0);

            //total products price المجموع الكلي للمنتج
            // الكمية مضروبه بسعر المتج الاصلي
            double TotalPrice = Double.parseDouble(CartTable.getItemById(item_id, 4)) * product_price;
            totalPrice.setText(String.valueOf(TotalPrice) + " $");

            // جمع الاسعار
            totalPriceInCart = totalPriceInCart + TotalPrice;

            Picasso.with(getContext()).load(allItemCart.getString(6)).into(product_image);
            final String p_id = allItemCart.getString(1);
            item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",p_id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }
            });
            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int lastQtn = Integer.parseInt(CartTable.getItemById(item_id, 4));

                    CartTable.updateQtn(lastQtn + 1, item_id);
                    String newQtn = CartTable.getItemById(item_id, 4);

                    double TotalPrice = Integer.parseInt(CartTable.getItemById(item_id, 4)) * product_price;

                    product_qtn.setText(newQtn);
                    totalPrice.setText(String.valueOf(TotalPrice) + " $");
                    totalPriceInCart = totalPriceInCart + product_price;
                    tv_totalPriceIncart.setText(String.valueOf(totalPriceInCart) + " $");


                }
            });


            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //--


                    int lastQtn = Integer.parseInt(CartTable.getItemById(item_id, 4));

                    if (lastQtn > 1) {
                        CartTable.updateQtn(lastQtn - 1, item_id);

                    }


                    String newQtn = CartTable.getItemById(item_id, 4);


                    product_qtn.setText(newQtn);


                    double TotalPrice = Integer.parseInt(CartTable.getItemById(item_id, 4)) * product_price;

                    totalPrice.setText(String.valueOf(TotalPrice) + " $");


                    if (lastQtn > 1){
                        totalPriceInCart = totalPriceInCart - product_price;

                    }


                    tv_totalPriceIncart.setText(String.valueOf(totalPriceInCart) + " $");




                }
            });


            btn_delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    CartTable.deleteItemById(item_id);
                    Toast.makeText(context, "تم حذف المنتج من السلة", Toast.LENGTH_SHORT).show();
                    cart_parent.removeView(item_view);
                    totalPriceInCart = totalPriceInCart - product_price;
                    tv_totalPriceIncart.setText(String.valueOf(totalPriceInCart) + " $");

                }
            });
            cart_parent.addView(item_view);

        }


        tv_totalPriceIncart.setText(String.valueOf(totalPriceInCart) + " $");
        return custom_view;
    }



}
