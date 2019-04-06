package shop.smartbazaar.smartbazaar.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Classification_table {

    public static boolean insert(String c_id,String name,String image){

        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;
        ContentValues contentValues = new ContentValues();

        contentValues.put("c_id",c_id);
        contentValues.put("name",name);
        contentValues.put("image",image);


//        int count = getItemByPId(p_id).getCount();
//        if (count ==0){
//
//            sqLiteDatabase.insert(MainDB.Favorites_table,null,contentValues);
//
//
//        }else {
//            deleteItemByPId(p_id);
//        }

        return true;
    }


}
