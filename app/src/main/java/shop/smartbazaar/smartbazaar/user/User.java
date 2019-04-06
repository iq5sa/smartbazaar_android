package shop.smartbazaar.smartbazaar.user;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    public SharedPreferences sharedPreferences;
    public static String id;
    public static String name;
    public static String email;
    public static String mobile;
    public static String access_toekn;


    public User(Context context) {
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

    }

    public boolean isLogin() {


        boolean islogin = sharedPreferences.getBoolean("isLogin", false);


        return islogin;
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",false);
        editor.apply();


    }


    public void saveLang(String lang){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",lang);
        editor.apply();
    }

    public String getLang(){

        String lang = sharedPreferences.getString("lang","");

        return lang;
    }

    public void saveUserData(String id, String name, String email, String mobile, String token,String area,String gender) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("access_token", token);
        editor.putString("area", area);
        editor.putString("gender", gender);
        editor.putBoolean("isLogin",true);


        editor.apply();

    }


    public  String getId() {

        return this.sharedPreferences.getString("id","");
    }

    public  String getName() {
        return this.sharedPreferences.getString("name","");
    }


    public  String getEmail() {
        return this.sharedPreferences.getString("email","");
    }

    public  String getMobile() {
        return this.sharedPreferences.getString("mobile","");
    }

    public String getAccess_toekn() {
        return this.sharedPreferences.getString("access_token","");
    }



    public void saveShippingAddress(String address){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", address);

        editor.apply();
    }

    public String getArea(){
        return this.sharedPreferences.getString("area","");
    }
    public String getAddress(){
        return this.sharedPreferences.getString("address","");
    }
}
