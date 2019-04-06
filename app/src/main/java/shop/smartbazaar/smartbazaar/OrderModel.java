package shop.smartbazaar.smartbazaar;

public class OrderModel {

    public String id;
    public String quantity;
    public String color;
    public String option;


    public OrderModel(String id, String quantity, String color, String option) {
        this.id = id;
        this.quantity = quantity;
        this.color = color;
        this.option = option;
    }


    public String getId() {
        return id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getColor() {
        return color;
    }

    public String getOption() {
        return option;
    }
}
