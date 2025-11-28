package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.ProductDAO;
import model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

@WebServlet("/product")
public class ProductController extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();

    // GET /product -> fetch all products
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> list = productDAO.getAllProducts();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h3>Products:</h3>");
        out.println("<ul>");
        for (Product p : list) {
            out.println("<li>" + p.getProductId() + ": " + p.getProductName() + " | " + p.getColor() + " | " + p.getSize() + "</li>");
        }
        out.println("</ul>");
    }

    // POST /product -> add new product
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product p = new Product();
        p.setProductName(req.getParameter("product_name"));
        p.setColor(req.getParameter("color"));
        p.setSize(req.getParameter("size"));

        boolean saved = productDAO.addProduct(p);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (saved) {
            out.println("Product added successfully!");
        } else {
            out.println("Failed to add product.");
        }
    }

    // PUT /product -> update product (body: x-www-form-urlencoded)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read raw body
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        // Parse key=value pairs from body
        String body = sb.toString(); // e.g., product_id=1&product_name=Shirt&color=Red&size=M
        String[] params = body.split("&");

        int id = 0;
        String productName = "";
        String color = "";
        String size = "";

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                switch (key) {
                    case "product_id": id = Integer.parseInt(value); break;
                    case "product_name": productName = value; break;
                    case "color": color = value; break;
                    case "size": size = value; break;
                }
            }
        }

        Product p = new Product();
        p.setProductId(id);
        p.setProductName(productName);
        p.setColor(color);
        p.setSize(size);

        boolean updated = productDAO.updateProduct(p);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (updated) {
            out.println("Product updated successfully!");
        } else {
            out.println("Failed to update product.");
        }
    }

    // DELETE /product?id=1 -> delete product
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean deleted = productDAO.deleteProduct(id);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (deleted) {
            out.println("Product deleted successfully!");
        } else {
            out.println("Failed to delete product.");
        }
    }
}
