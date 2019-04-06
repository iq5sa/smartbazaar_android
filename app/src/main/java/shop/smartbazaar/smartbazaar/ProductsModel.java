package shop.smartbazaar.smartbazaar;

import java.util.ArrayList;

public class ProductsModel {

    public String id;
    public String title;
    public String description;
    public String price;
    public String discount;
    public String classification_id;
    public String category_id;
    public String sub_category_id;
    public String rating;
    public String feature_image;
    public String views;
    public String certified;
    public String code;
    public String discount_end_date;
    public String video;
    public String pay_with;

    public String brand_id;
    public String brand_name;
    public String shipment;
    public String store_name;

    public String option;
    public String color;
    public ArrayList<String> images;

    public String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getRating() {
        return rating;
    }


    public String getFeature_image() {
        return feature_image;
    }


    public String getOption() {
        return option;
    }


    public String getUrl() {
        return url;
    }


    public ProductsModel(String id, String title, String description, String price, String discount, String classification_id, String category_id, String sub_category_id, String rating, String feature_image, String views, String certified, String code, String discount_end_date, String video, String pay_with, String brand_id, String brand_name, String shipment, String option,String color, String url, String store_name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.classification_id = classification_id;
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
        this.rating = rating;
        this.feature_image = feature_image;
        this.views = views;
        this.certified = certified;
        this.code = code;
        this.discount_end_date = discount_end_date;
        this.video = video;
        this.pay_with = pay_with;
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.shipment = shipment;
        this.option = option;
        this.color = color;
        this.images = images;
        this.url = url;
        this.store_name = store_name;
    }
}
