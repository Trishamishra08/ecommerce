package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.OrderDAO;
import model.OrderHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/order")
public class OrderController extends HttpServlet {

    private OrderDAO orderDAO = new OrderDAO();

    // GET /order -> fetch all orders
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<OrderHeader> orders = orderDAO.getAllOrderHeaders();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h3>Orders:</h3>");
        out.println("<ul>");
        for (OrderHeader o : orders) {
            out.println("<li>Order ID: " + o.getOrderId() +
                        " | Customer ID: " + o.getCustomerId() +
                        " | Order Date: " + o.getOrderDate() +
                        " | Shipping Contact ID: " + o.getShippingContactMechId() +
                        " | Billing Contact ID: " + o.getBillingContactMechId() +
                        "</li>");
        }
        out.println("</ul>");
    }

    // POST /order -> add new order
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            OrderHeader oh = new OrderHeader();

            oh.setCustomerId(Integer.parseInt(req.getParameter("customer_id")));

            String dateStr = req.getParameter("order_date"); // e.g., "2025-11-28"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDate = sdf.parse(dateStr);
            oh.setOrderDate(orderDate);

            oh.setShippingContactMechId(Integer.parseInt(req.getParameter("shipping_contact_mech_id")));
            oh.setBillingContactMechId(Integer.parseInt(req.getParameter("billing_contact_mech_id")));

            boolean saved = orderDAO.addOrderHeader(oh);

            if (saved) {
                out.println("Order placed successfully! Order ID: " + oh.getOrderId());
            } else {
                out.println("Failed to place order.");
            }

        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("Error: " + e.getMessage());
        }
    }

    // PUT /order -> update order header (body: x-www-form-urlencoded)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            // Read raw body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse key=value pairs
            String body = sb.toString();
            String[] params = body.split("&");

            int orderId = 0, customerId = 0, shippingId = 0, billingId = 0;
            Date orderDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");

switch (key) {
    case "order_id":
        orderId = Integer.parseInt(value);
        break;
    case "customer_id":
        customerId = Integer.parseInt(value);
        break;
    case "order_date":
        orderDate = sdf.parse(value);
        break;
    case "shipping_contact_mech_id":
        shippingId = Integer.parseInt(value);
        break;
    case "billing_contact_mech_id":
        billingId = Integer.parseInt(value);
        break;
}

                }
            }

            OrderHeader oh = new OrderHeader();
            oh.setOrderId(orderId);
            oh.setCustomerId(customerId);
            oh.setOrderDate(orderDate);
            oh.setShippingContactMechId(shippingId);
            oh.setBillingContactMechId(billingId);

            boolean updated = orderDAO.updateOrderHeader(oh);

            if (updated) {
                out.println("Order updated successfully! Order ID: " + orderId);
            } else {
                out.println("Failed to update order.");
            }

        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("Error: " + e.getMessage());
        }
    }

    // DELETE /order?id=1 -> delete order header + items
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id")); // ?id=1
            boolean deleted = orderDAO.deleteOrderHeader(id);

            if (deleted) {
                out.println("Order deleted successfully!");
            } else {
                out.println("Failed to delete order.");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("Error: " + e.getMessage());
        }
    }
}
