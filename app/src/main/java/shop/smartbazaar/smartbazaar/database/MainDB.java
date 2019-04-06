package shop.smartbazaar.smartbazaar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainDB extends SQLiteOpenHelper {


    private static final int version = 11;
    public static final String db_name = "smartbazaar";
    public static final String Cart_table = "cart";
    public static final String Favorites_table = "favorites";
    public static final String Slider_table = "slider";
    public static final String Products_table = "products";
    public static final String Product_images = "product_images";
    public static final String Classification_table = "classification";
    public static  SQLiteDatabase sqliteWrite;

    public MainDB(Context context) {
        super(context, db_name,null, version);
        sqliteWrite = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table_cart = "CREATE TABLE " + this.Cart_table +
                " (`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
                " `p_id` INTEGER,"+
                " `title` TEXT,"+
                " `seller` TEXT,"+
                " `qtn` INTEGER,"+
                " `total_price` TEXT," +
                "`image` TEXT,`color` TEXT,`option` TEXT)";


        String create_favorite_table = "CREATE TABLE "+this.Favorites_table +
                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "`p_id` INTEGER,"+
                "title TEXT,"+
                "price TEXT,"+
                "image TEXT)";


        String create_slider_table = "CREATE TABLE " + this.Slider_table +
                " (`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " `title` TEXT, " +
                " `description` TEXT, " +
                " `link` TEXT, " +
                " `image` TEXT, " +
                " `classification_id` TEXT, " +
                " `category_id` TEXT, " +
                " `sub_category_id` TEXT, " +
                " `brand_id` TEXT);";


        String create_products_table = "CREATE TABLE " + this.Products_table+
                
                "( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " `p_id` INTEGER NOT NULL, " +
                " `title` TEXT, " +
                " `description` TEXT, " +
                " `price` INTEGER, " +
                " `classification_id` TEXT, " +
                " `category_id` TEXT, " +
                " `sub_category_id` TEXT, " +
                " `rating` INTEGER, " +
                " `store_name` TEXT, " +
                " `feature_image` TEXT " +
                ");";


        String create_product_images = "CREATE TABLE "+ this.Product_images +
                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " `p_id` INTEGER, " +
                " `image` TEXT " +
                ");";

        String create_classification_table = "CREATE TABLE " + this.Classification_table +
                " (`id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " `c_id` INTEGER, " +
                " `name` TEXT, " +
                " `image` TEXT " +
                ");";
        db.execSQL(create_slider_table);
        db.execSQL(create_table_cart);
        db.execSQL(create_favorite_table);
        db.execSQL(create_products_table);
        db.execSQL(create_product_images);
        db.execSQL(create_classification_table);
        Log.v("sajjadiq","db created");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + this.Cart_table);
        db.execSQL("DROP TABLE IF EXISTS " + this.Favorites_table);
        db.execSQL("DROP TABLE IF EXISTS " + this.Slider_table);
        db.execSQL("DROP TABLE IF EXISTS " + this.Products_table);
        db.execSQL("DROP TABLE IF EXISTS " + this.Product_images);
        db.execSQL("DROP TABLE IF EXISTS " + this.Classification_table);

        onCreate(db);

    }
}
