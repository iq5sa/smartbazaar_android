package shop.smartbazaar.smartbazaar.public_classes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import shop.smartbazaar.smartbazaar.R;


public class Utiles {



    public static final String[] cities = {"أربيل","الأنبار","بابل","بغداد","البصرة","دهوك",
            "القادسية","ديالى","ذي قار","السليمانية","صلاح الدين","كركوك","كربلاء","المثنى","ميسان","النجف","نينوى","واسط"};

    public static final Drawable getDrawable(Context context, int id) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return ContextCompat.getDrawable(context, id);
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
    public static String EnNumbersToAr(String numbers) {


        numbers = numbers.replace("0", "٠").replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "٤").replace("5", "٥").replace("6", "٦").replace("7", "٧").replace("8", "٨").replace("9", "٩");
        return numbers;
    }

    public static void restartApp(Activity context){
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage( context.getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(i);
        context.recreate();


    }
    public static final String getDate(long time_stamp_server) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());

        return formatter.format(time_stamp_server * 1000L);
    }

    public static final String getTime(long time_stamp_server) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getDefault());


        return formatter.format(time_stamp_server * 1000L);
    }

    public static final String getAmPm(long time_stamp_server) {
        SimpleDateFormat formatter = new SimpleDateFormat("a", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getDefault());


        return formatter.format(time_stamp_server * 1000L);

    }

    public static final String getAmPmArabic(long time_stamp_server) {
        SimpleDateFormat formatter = new SimpleDateFormat("a", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getDefault());

        String result = formatter.format(time_stamp_server * 1000L);


        return result;

    }


    public static Typeface Main_font(Context context, @NonNull String key) {

        Typeface font;
        if (key.equals("kufi-b")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/droidkufi_bold.ttf");


        } else if (key.equals("kufi-r")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/droidkufi_regular.ttf");


        } else if (key.equals("m")) {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/ktvfont.woff");


        } else {
            font = Typeface.createFromAsset(context.getAssets(), "fonts/Droid-Req.ttf");
        }


        return font;

    }






    public static void ReplaceFragment(FragmentManager fm, Fragment fragment, Bundle bundle) {

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        fragmentTransaction.replace(R.id.content_fragment, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }


    static public String Udid(Context context) {

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }


    public static Intent share(String shareBody,String subject){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        return sharingIntent;
    }


    public static boolean isArLang() {
        if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean playOnlineAudio(MediaPlayer mediaPlayer, String url_file) {

        mediaPlayer.reset();
        try {


            mediaPlayer.setDataSource(url_file);


            mediaPlayer.prepareAsync();


            return true;
        } catch (IOException e) {

            return false;
        }


    }

    public static boolean isOnline(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    static public SharedPreferences preferences(Context cont) {
        SharedPreferences sharedpreferences = cont.getSharedPreferences("private_settings", Context.MODE_PRIVATE);
        return sharedpreferences;
    }







    static public void changestatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //arg color (R.color.test)
        //arg activity (this or activity)
        //window.setStatusBarColor(ContextCompat.getColor(activity, color));
    }


    static public String getArDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE yyyy/MM/dd", new Locale("ar"));

        Date date = new Date();

        // System.out.println(format.format(date));

        return format.format(date);
    }

    static public void setAppDirection(Context context,String lang) {
        Locale locale2 = new Locale(lang);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        context.getResources().updateConfiguration(
                config2, context.getResources().getDisplayMetrics());


    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean delete_file(String path) {

        File file = new File(path);

        boolean delete = file.delete();

        return delete;
    }


    public static Intent getOpenFacebookIntent(Context context, String id, String url) {
        String fb_url = url;

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + id));
        } catch (PackageManager.NameNotFoundException e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(fb_url));
        }

    }


    public static Intent getOpenTwitterIntent(Context context, String id, String url) {
        String fb_url = url;

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=" + id));
        } catch (PackageManager.NameNotFoundException e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(fb_url));
        }

    }


    public static Intent getOpenYoutubeIntent(String url) {

        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse(url));
            return intent;
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            return intent;
        }
    }

    public static String getDeviceId(Context context) {
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return device_id;
    }

    public static Intent openInsta(String url) {
        Intent intent = null;


        try {
            Uri uri = Uri.parse(url);

            intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setPackage("com.instagram.android");

            return intent;
        } catch (ActivityNotFoundException e) {

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            return intent;


        }

    }
}
