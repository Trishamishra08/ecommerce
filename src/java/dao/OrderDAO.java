package dao;

import model.OrderHeader;
import model.OrderItem;
import dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Add a new order header
    public boolean addOrderHeader(OrderHeader orderHeader) {
        String sql = "INSERT INTO Order_Header(order_date, customer_id, shipping_contact_mech_id, billing_contact_mech_id) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, new java.sql.Date(orderHeader.getOrderDate().getTime()));
            ps.setInt(2, orderHeader.getCustomerId());
            ps.setInt(3, orderHeader.getShippingContactMechId());
            ps.setInt(4, orderHeader.getBillingContactMechId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderHeader.setOrderId(rs.getInt(1));
                    }
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add order item
    public boolean addOrderItem(OrderItem item) {
        String sql = "INSERT INTO Order_Item(order_id, product_id, quantity, status) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getStatus() != null ? item.getStatus() : "Pending");

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        item.setOrderItemSeqId(rs.getInt(1));
                    }
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all order headers
    public List<OrderHeader> getAllOrderHeaders() {
        List<OrderHeader> list = new ArrayList<>();
        String sql = "SELECT * FROM Order_Header";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderHeader order = new OrderHeader();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setShippingContactMechId(rs.getInt("shipping_contact_mech_id"));
                order.setBillingContactMechId(rs.getInt("billing_contact_mech_id"));
                list.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get single order header by ID
    public OrderHeader getOrderHeaderById(int id) {
        String sql = "SELECT * FROM Order_Header WHERE order_id=?";
        OrderHeader order = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = new OrderHeader();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setOrderDate(rs.getDate("order_date"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setShippingContactMechId(rs.getInt("shipping_contact_mech_id"));
                    order.setBillingContactMechId(rs.getInt("billing_contact_mech_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    // Update order header (PUT)
    public boolean updateOrderHeader(OrderHeader orderHeader) {
        String sql = "UPDATE Order_Header SET order_date=?, customer_id=?, shipping_contact_mech_id=?, billing_contact_mech_id=? WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(orderHeader.getOrderDate().getTime()));
            ps.setInt(2, orderHeader.getCustomerId());
            ps.setInt(3, orderHeader.getShippingContactMechId());
            ps.setInt(4, orderHeader.getBillingContactMechId());
            ps.setInt(5, orderHeader.getOrderId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update order item fully (PUT)
    public boolean updateOrderItem(OrderItem item) {
        String sql = "UPDATE Order_Item SET product_id=?, quantity=?, status=? WHERE order_item_seq_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getProductId());
            ps.setInt(2, item.getQuantity());
            ps.setString(3, item.getStatus());
            ps.setInt(4, item.getOrderItemSeqId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete order header and its items
    public boolean deleteOrderHeader(int orderId) {
        String sqlItem = "DELETE FROM Order_Item WHERE order_id=?";
        String sqlHeader = "DELETE FROM Order_Header WHERE order_id=?";
        try (Connection con = DBConnection.getConnection()) {

            try (PreparedStatement psItem = con.prepareStatement(sqlItem)) {
                psItem.setInt(1, orderId);
                psItem.executeUpdate();
            }

            try (PreparedStatement psHeader = con.prepareStatement(sqlHeader)) {
                psHeader.setInt(1, orderId);
                return psHeader.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all items for a particular order
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM Order_Item WHERE order_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemSeqId(rs.getInt("order_item_seq_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setStatus(rs.getString("status"));
                    list.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update order item status only
    public boolean updateOrderItemStatus(int orderItemSeqId, String status) {
        String sql = "UPDATE Order_Item SET status=? WHERE order_item_seq_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderItemSeqId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete single order item
    public boolean deleteOrderItem(int orderItemSeqId) {
        String sql = "DELETE FROM Order_Item WHERE order_item_seq_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderItemSeqId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
