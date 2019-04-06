package shop.smartbazaar.smartbazaar.api;

public class Api {

    public static final String SiteUrl = "https://Smartbazaar.shop/";
    public static final String classifications = SiteUrl + "api/classifications";
    public static final String categoriesInClassifications = SiteUrl + "api/categories";
    public static final String getSubCategories = SiteUrl + "api/sub-categories";
    public static final String brands= SiteUrl + "api/get-all-brands";
    public static final String lastProducts = SiteUrl + "api/latest/products";
    public static final String productsBy = SiteUrl + "api/get-products-in";
    public static final String login = SiteUrl + "api/auth/login";
    public static final String getAreas = SiteUrl + "api/buyer/areas-and-shipment-prices";
    public static final String makeOrder = SiteUrl + "api/buyer/make-order";
    public static final String getSlider = SiteUrl + "api/sliders";
    public static final String getLastProductsDiscounts = SiteUrl + "api/latest/discount/products";
    public static final String register = SiteUrl + "api/auth/signup";
    public static final String check_code = SiteUrl + "api/verify/user";
    public static final String getProductWithType = SiteUrl + "api/get-products-with-type";
    public static final String similar_products = SiteUrl + "api/get-similar-products";
    public static final String getOrders = SiteUrl + "api/buyer/get-my-orders";
    public static final String productsWeOffer = SiteUrl + "api/offers/products";

    public static  String productsDetails(String id){

        return  SiteUrl + "api/get-product-details?id="+id;
    }

    public static String search(String title,String page){


        return SiteUrl + "api/search-product?title="+title+"&page="+page;
    }

}
