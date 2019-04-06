package shop.smartbazaar.smartbazaar.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import shop.smartbazaar.smartbazaar.Favorites_model;
import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.adapters.Favorites_adapter;

public class FavoriteFragment extends Fragment {


    Context context;
    LayoutInflater layoutInflater;
    RecyclerView favorite_recycler;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FragmentManager fm;
    Cursor favorites;
    Favorites_adapter favorites_adapter;
    ActionBar actionBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.context = getContext();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("المفضلة");
        fm = getFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.favorite_layout, null, false);
        favorite_recycler = mView.findViewById(R.id.favorites_recycler);
        favorite_recycler.setHasFixedSize(true);
        RelativeLayout empty_area = mView.findViewById(R.id.empty_area);

        favorite_recycler.setLayoutManager(new LinearLayoutManager(context));

        final ArrayList<Favorites_model> arrayList = new ArrayList<>();


        favorites = FavoritesTable.getAllItems();


        if (favorites.getCount() == 0) {
            empty_area.setVisibility(View.VISIBLE);
        }

        while (favorites.moveToNext()) {
            arrayList.add(new Favorites_model(favorites.getString(1), favorites.getString(4), favorites.getString(2), favorites.getString(3)));

        }
        favorites_adapter = new Favorites_adapter(arrayList, context,fm);

        favorite_recycler.setAdapter(favorites_adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

                Log.v("sajjadiq", "moved");
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                arrayList.remove(viewHolder.getAdapterPosition());


                favorites_adapter.notifyDataSetChanged();

                String item_id = viewHolder.itemView.getTag().toString();

                FavoritesTable.deleteItemByPId(item_id);

                Toast.makeText(context, "تم حذف المنتج من المفضلة", Toast.LENGTH_SHORT).show();


            }
        }).attachToRecyclerView(favorite_recycler);


//
//            item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id",v.getTag().toString());
//                    Utiles.ReplaceFragment(fm,new ProductDetails(),bundle);
//
//                }
//            });
//
//
//            ic_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "asdasd", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//
//            YoYo.with(Techniques.Bounce).duration(1000).repeat(0).playOn(item);
//            favorite_viewGroup.addView(item);
//        }

        return mView;
    }
}
