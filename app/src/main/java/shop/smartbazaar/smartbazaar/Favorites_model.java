package shop.smartbazaar.smartbazaar;

public class Favorites_model {


    public String id;
    public String image;
    public String title;
    public String price;


    public Favorites_model(String id,String image, String title, String price) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.price = price;
    }



    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getId(){
        return id;
    }
}
