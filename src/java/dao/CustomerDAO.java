package dao;

import model.Customer;
import dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Add new customer
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customer(first_name, last_name) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getFirstName() != null ? c.getFirstName() : "");
            ps.setString(2, c.getLastName() != null ? c.getLastName() : "");

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Get the auto-generated customer_id
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setCustomerId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update customer
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customer SET first_name=?, last_name=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getFirstName() != null ? c.getFirstName() : "");
            ps.setString(2, c.getLastName() != null ? c.getLastName() : "");
            ps.setInt(3, c.getCustomerId());

            int affectedRows = ps.executeUpdate();
            System.out.println("Rows updated: " + affectedRows); // Debug
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete customer
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customer WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            System.out.println("Rows deleted: " + affectedRows); // Debug
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
