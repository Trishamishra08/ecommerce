package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.CustomerDAO;
import model.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

@WebServlet("/customer")
public class CustomerController extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();

    // GET /customer -> fetch all customers
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> list = customerDAO.getAllCustomers();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h3>Customers:</h3>");
        out.println("<ul>");
        for (Customer c : list) {
            out.println("<li>" + c.getCustomerId() + ": " + c.getFirstName() + " " + c.getLastName() + "</li>");
        }
        out.println("</ul>");
    }

    // POST /customer -> add new customer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Customer c = new Customer();
        c.setFirstName(req.getParameter("first_name"));
        c.setLastName(req.getParameter("last_name"));

        boolean saved = customerDAO.addCustomer(c);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (saved) {
            out.println("Customer added successfully!");
        } else {
            out.println("Failed to add customer.");
        }
    }

    // PUT /customer -> update customer (body: x-www-form-urlencoded)
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
        String body = sb.toString();
        String[] params = body.split("&");

        int id = 0;
        String firstName = "";
        String lastName = "";

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                if (key.equals("customer_id")) id = Integer.parseInt(value);
                if (key.equals("first_name")) firstName = value;
                if (key.equals("last_name")) lastName = value;
            }
        }

        Customer c = new Customer();
        c.setCustomerId(id);
        c.setFirstName(firstName);
        c.setLastName(lastName);

        boolean updated = customerDAO.updateCustomer(c);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (updated) {
            out.println("Customer updated successfully!");
        } else {
            out.println("Failed to update customer.");
        }
    }

    // DELETE /customer?id=1 -> delete customer
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean deleted = customerDAO.deleteCustomer(id);

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        if (deleted) {
            out.println("Customer deleted successfully!");
        } else {
            out.println("Failed to delete customer.");
        }
    }
}
