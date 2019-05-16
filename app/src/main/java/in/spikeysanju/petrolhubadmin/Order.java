package in.spikeysanju.petrolhubadmin;

public class Order {
    private String address,quantity,price,uid,name,status;

    public Order(){}
    public Order(String address, String quantity, String price, String uid, String name, String status) {
        this.address = address;
        this.quantity = quantity;
        this.price = price;
        this.uid = uid;
        this.name = name;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
