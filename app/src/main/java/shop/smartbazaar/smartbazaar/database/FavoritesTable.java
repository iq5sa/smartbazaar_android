package shop.smartbazaar.smartbazaar.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoritesTable {

    public static boolean insert(String p_id,String title,String price,String image){

        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;
        ContentValues contentValues = new ContentValues();

        contentValues.put("p_id",p_id);
        contentValues.put("title",title);
        contentValues.put("price",price);
        contentValues.put("image",image);


        int count = getItemByPId(p_id).getCount();
        if (count ==0){

            sqLiteDatabase.insert(MainDB.Favorites_table,null,contentValues);


        }else {
            //deleteItemByPId(p_id);
        }

        return true;
    }


    public static String getItemById(String id,int column){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ MainDB.Favorites_table +" WHERE id="+id,null);
        if (cursor !=null)
            return cursor.getString(column);

        else
            return "null";

    }

    public static int deleteItemByPId(String pid){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        int cursor = sqLiteDatabase.delete(MainDB.Favorites_table,"p_id=?",new String[]{pid});

        return cursor;

    }
    public static int deleteItemById(String id){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        int cursor = sqLiteDatabase.delete(MainDB.Favorites_table,"id=?",new String[]{id});

        return cursor;

    }
    public static Cursor getItemByPId(String pid){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ MainDB.Favorites_table +" WHERE `p_id`="+pid,null);


        return cursor;
    }

    public static Cursor getAllItems(){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                MainDB.Favorites_table +
                " ORDER BY id DESC",null);

        return cursor;

    }


}
