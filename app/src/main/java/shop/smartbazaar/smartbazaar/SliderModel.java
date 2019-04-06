package shop.smartbazaar.smartbazaar;

public class SliderModel {

    public String id ;
    public String title;
    public String description;
    public String link;
    public String image;
    public String classification_id;
    public String category_id;
    public String sub_category_id;
    public String brand_id;

    public SliderModel(String id, String title, String description, String link, String image, String classification_id, String category_id, String sub_category_id, String brand_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.classification_id = classification_id;
        this.category_id = category_id;
        this.sub_category_id = sub_category_id;
        this.brand_id = brand_id;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getClassification_id() {
        return classification_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }
}

