package dao;

import model.Product;
import dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Add new product
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Product(product_name, color, size) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getColor());
            ps.setString(3, product.getSize());

            int affectedRows = ps.executeUpdate();

            // Get generated product_id
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setProductId(rs.getInt(1));
                    }
                }
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setColor(rs.getString("color"));
                p.setSize(rs.getString("size"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get product by ID
    public Product getProductById(int id) {
        String sql = "SELECT * FROM Product WHERE product_id=?";
        Product product = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setProductName(rs.getString("product_name"));
                    product.setColor(rs.getString("color"));
                    product.setSize(rs.getString("size"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Update product
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET product_name=?, color=?, size=? WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getColor());
            ps.setString(3, product.getSize());
            ps.setInt(4, product.getProductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete product
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM Product WHERE product_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
