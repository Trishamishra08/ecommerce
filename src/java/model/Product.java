package model;

public class Product {

    private int productId;
    private String productName;
    private String color;
    private String size;

    public Product() {}

    public Product(int productId, String productName, String color, String size) {
        this.productId = productId;
        this.productName = productName;
        this.color = color;
        this.size = size;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
