package model;

import java.util.Date;
import java.util.List;

public class OrderHeader {

    private int orderId;
    private Date orderDate; // java.util.Date for flexibility
    private int customerId;
    private int shippingContactMechId;
    private int billingContactMechId;

    // Optional: for returning items inside order details API
    private List<OrderItem> orderItems;

    public OrderHeader() {}

    // --- Getters and Setters ---
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShippingContactMechId() {
        return shippingContactMechId;
    }

    public void setShippingContactMechId(int shippingContactMechId) {
        this.shippingContactMechId = shippingContactMechId;
    }

    public int getBillingContactMechId() {
        return billingContactMechId;
    }

    public void setBillingContactMechId(int billingContactMechId) {
        this.billingContactMechId = billingContactMechId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
