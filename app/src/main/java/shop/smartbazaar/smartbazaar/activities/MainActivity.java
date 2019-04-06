package shop.smartbazaar.smartbazaar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import shop.smartbazaar.smartbazaar.R;
import shop.smartbazaar.smartbazaar.database.CartTable;
import shop.smartbazaar.smartbazaar.database.FavoritesTable;
import shop.smartbazaar.smartbazaar.database.MainDB;
import shop.smartbazaar.smartbazaar.fragments.CartFragment;
import shop.smartbazaar.smartbazaar.fragments.FavoriteFragment;
import shop.smartbazaar.smartbazaar.fragments.HomeFragment;
import shop.smartbazaar.smartbazaar.fragments.MyAccountFragment;
import shop.smartbazaar.smartbazaar.fragments.MyOrdersFragment;
import shop.smartbazaar.smartbazaar.fragments.SearchFragment;
import shop.smartbazaar.smartbazaar.public_classes.BottomMenuHelper;
import shop.smartbazaar.smartbazaar.public_classes.CustomTypefaceSpan;
import shop.smartbazaar.smartbazaar.public_classes.Utiles;
import shop.smartbazaar.smartbazaar.user.User;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    BottomNavigationView bottomNavigationView;

    FrameLayout content_fragment;
    FragmentManager fm;

    NavigationView navigationView;
    ActionBar actionbar;
    Context context;
    ImageView actionBarLogo;

    RelativeLayout progressBarArea;
    VideoView videoView;
    Bundle bundle;
    int menuItemId = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        initViews();
        initSplashVideo();
        initHeaderNavigationView();

        bundle = getIntent().getExtras();
        fm = getSupportFragmentManager();
        progressBarArea.setOnClickListener(null);


        new MainDB(this);


        if (FavoritesTable.getAllItems().getCount() !=0){
            BottomMenuHelper.showBadge(this, bottomNavigationView, R.id.favorite_tab, String.valueOf(FavoritesTable.getAllItems().getCount()));

        }else {
            BottomMenuHelper.removeBadge(bottomNavigationView,R.id.favorite_tab);
        }

        if (CartTable.getAllItems().getCount() !=0){
            BottomMenuHelper.showBadge(this, bottomNavigationView, R.id.cart_tab, String.valueOf(CartTable.getAllItems().getCount()));

        }else {

            BottomMenuHelper.removeBadge(bottomNavigationView,R.id.cart_tab);
        }


        Menu m = navigationView.getMenu();


        for (int i = 0; i < m.size(); i++) {

            MenuItem mi = m.getItem(i);


            SpannableString s = new SpannableString(mi.getTitle());
            s.setSpan(new CustomTypefaceSpan("", Utiles.Main_font(this, "kufi-r")), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mi.setTitle(s);

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionbar.setTitle("");
        }




        //adding home page by default
        Utiles.ReplaceFragment(fm,new HomeFragment(),null);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.myorders:
                        Utiles.ReplaceFragment(fm, new MyOrdersFragment(), null);
                        mDrawerLayout.closeDrawers();

                        break;

                    case R.id.favorite:
                        Utiles.ReplaceFragment(fm, new FavoriteFragment(), null);
                        mDrawerLayout.closeDrawers();

                        break;

                    case R.id.face:
                        context.startActivity(Utiles.getOpenFacebookIntent(context, "Smart.Bazaar.iq", "https://www.facebook.com/Smart.Bazaar.iq/?ref=br_rs"));
                        mDrawerLayout.closeDrawers();

                        break;


                    case R.id.tube:
                        startActivity(Utiles.getOpenYoutubeIntent("https://www.youtube.com/user/souqchannel"));
                        mDrawerLayout.closeDrawers();
                        break;


                    case R.id.settings:
                        Intent GotoSettings = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(GotoSettings);
                }

                return true;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    case R.id.home_tab:
                        actionbar.setTitle("");



                        if (menuItem.getItemId() !=menuItemId){
                            Utiles.ReplaceFragment(fm, new HomeFragment(), null);

                        }

                        menuItemId = menuItem.getItemId();
                        break;

                    case R.id.favorite_tab:



                        if (menuItemId != menuItem.getItemId()){
                            Utiles.ReplaceFragment(fm, new FavoriteFragment(), null);
                        }

                        menuItemId = menuItem.getItemId();

                        actionbar.setTitle(R.string.favorite);


                        break;
                    case R.id.cart_tab:


                        if (menuItemId !=menuItem.getItemId()){
                            Utiles.ReplaceFragment(fm, new CartFragment(), null);
                        }


                        menuItemId = menuItem.getItemId();

                        actionbar.setTitle(R.string.cart);


                        break;

                    case R.id.search_tab:

                       if (menuItemId != menuItem.getItemId()){
                           Utiles.ReplaceFragment(fm, new SearchFragment(), null);
                       }

                        menuItemId = menuItem.getItemId();
                        actionbar.setTitle(R.string.search);

                        break;
                    case R.id.myaccount_tab:


                        if (!new User(context).isLogin()) {

                            Intent intent = new Intent(context, RegisterOrLoginActivity.class);
                            startActivity(intent);
                        } else {

                            if (menuItemId !=menuItem.getItemId()){
                                Utiles.ReplaceFragment(fm, new MyAccountFragment(), null);
                            }

                            menuItemId = menuItem.getItemId();


                        }

                        actionbar.setTitle(R.string.myAccount);

                        break;

                }

                return true;

            }
        });


    }

    private void initHeaderNavigationView() {
        View headerView = navigationView.getHeaderView(0);
        TextView hello_text = headerView.findViewById(R.id.hello_text);
        TextView tv_mobile = headerView.findViewById(R.id.mobile);
        LinearLayout registerArea = headerView.findViewById(R.id.registerArea);


        //this for header navigation view
        if (new User(this).isLogin()) {
            registerArea.setVisibility(View.INVISIBLE);
            tv_mobile.setText(new User(this).getMobile());
            tv_mobile.setVisibility(View.VISIBLE);
        }
        if (new User(context).isLogin()) {
            hello_text.setText(hello_text.getText().toString() + " " + new User(this).getName());

        }

    }

    private void initSplashVideo() {
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+ R.raw.introw));
        videoView.requestFocus();
        videoView.start();

    }

    private void initViews() {

        navigationView = findViewById(R.id.navigationView);
        content_fragment = findViewById(R.id.content_fragment);
        actionBarLogo = findViewById(R.id.actionBarLogo);
        progressBarArea = findViewById(R.id.progressBarArea);
        videoView = findViewById(R.id.videoView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomBar);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                break;


        }
        return super.onOptionsItemSelected(item);
    }


    public void btn_register(View view) {

        Intent intent = new Intent(context, RegisterOrLoginActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawers();


    }

    public void btn_login(View view) {
        Intent intent = new Intent(context, RegisterOrLoginActivity.class);
        startActivity(intent);
        mDrawerLayout.closeDrawers();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }




    @Override
    public void onBackPressed() {

        super.finish();
//        int count = fm.getBackStackEntryCount();
//
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            fm.popBackStack();
//        }
    }



}
