package shop.smartbazaar.smartbazaar;

public class Cities_model {

    public int area_id;
    public String area_name;
    public String normal_shipment_price;
    public String express_shipment_price;


    public Cities_model(int area_id, String area_name, String normal_shipment_price, String express_shipment_price) {
        this.area_id = area_id;
        this.area_name = area_name;
        this.normal_shipment_price = normal_shipment_price;
        this.express_shipment_price = express_shipment_price;
    }


    public int getArea_id() {
        return area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public String getNormal_shipment_price() {
        return normal_shipment_price;
    }

    public String getExpress_shipment_price() {
        return express_shipment_price;
    }
}
