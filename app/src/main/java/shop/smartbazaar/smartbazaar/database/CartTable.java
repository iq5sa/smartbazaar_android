package shop.smartbazaar.smartbazaar.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class CartTable {



    public static long insert(int p_id,String title,String seller,String total_price,String image,String color,String option){

        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;
        ContentValues contentValues = new ContentValues();

        contentValues.put("p_id",p_id);
        contentValues.put("title",title);
        contentValues.put("seller",seller);
        contentValues.put("qtn",1);
        contentValues.put("total_price",total_price);
        contentValues.put("image",image);
        contentValues.put("color",color);
        contentValues.put("option",option);

        long insert = sqLiteDatabase.insert(MainDB.Cart_table,null,contentValues);

        //@return the row ID of the newly inserted row, or -1 if an error occurred
        return insert;
    }


    public static int deleteItemById(String id){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        int cursor = sqLiteDatabase.delete(MainDB.Cart_table,"id=?",new String[]{id});

        return cursor;

    }
    public static Cursor getItemByPId(String p_id){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MainDB.Cart_table + " WHERE p_id="+p_id,null);

        return cursor;

    }

    public static int deleteItemByPId(String id){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        int cursor = sqLiteDatabase.delete(MainDB.Cart_table,"p_id=?",new String[]{id});

        return cursor;

    }
    public static Cursor getAllItems(){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
                MainDB.Cart_table +
                " ORDER BY id DESC",null);

        return cursor;

    }


    public static int  updateQtn(int qtn,String id){
        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;

        ContentValues contentValues = new ContentValues();

        contentValues.put("qtn",qtn);

       return sqLiteDatabase.update(MainDB.Cart_table,contentValues,"id=?",new String[]{id});
    }

    public static String getItemById(String id,int column){

        SQLiteDatabase sqLiteDatabase = MainDB.sqliteWrite;


        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MainDB.Cart_table + " WHERE id="+id,null);

         cursor.moveToFirst();
         if (cursor !=null)
            return cursor.getString(column);

            else
                return "null";
    }

}
