package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DeliveryOrderServlet", value = "/delivery-orders")
public class DeliveryOrderServlet extends HttpServlet {

    private final DeliveryOrderService deliveryOrderService = new DeliveryOrderService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        try {
            List<DeliveryOrder> orders = deliveryOrderService.getAll();
            writeJson(resp, orders);
        } catch (Exception e) {
            writeError(resp, 500, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                DeliveryOrder order = new DeliveryOrder();
                populateOrderFromRequest(req, order);

                deliveryOrderService.add(order);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"DeliveryOrder added successfully\"}");

            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                DeliveryOrder order = new DeliveryOrder();
                order.setId(id);
                populateOrderFromRequest(req, order);

                deliveryOrderService.update(order);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"DeliveryOrder updated successfully\"}");

            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                deliveryOrderService.delete(id);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"DeliveryOrder deleted successfully\"}");

            } else {
                writeError(resp, 400, "Invalid or missing 'action' parameter");
            }
        } catch (IllegalArgumentException e) {
            writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            writeError(resp, 500, e.getMessage());
        }
    }

    private void populateOrderFromRequest(HttpServletRequest req, DeliveryOrder order) {
        if (req.getParameter("clientId") != null) {
            order.setClientId(Long.parseLong(req.getParameter("clientId")));
        }
        if (req.getParameter("courierId") != null) {
            order.setCourierId(Long.parseLong(req.getParameter("courierId")));
        }
        if (req.getParameter("shipmentId") != null) {
            order.setShipmentId(Long.parseLong(req.getParameter("shipmentId")));
        }
        if (req.getParameter("orderDate") != null) {
            order.setOrderDate(req.getParameter("orderDate"));
        }
        if (req.getParameter("status") != null) {
            order.setStatus(req.getParameter("status"));
        }
        if (req.getParameter("totalCost") != null) {
            order.setTotalCost(Double.parseDouble(req.getParameter("totalCost")));
        }
        if (req.getParameter("f1") != null) {
            order.setF1(Double.parseDouble(req.getParameter("f1")));
        }
        if (req.getParameter("f2") != null) {
            order.setF2(Double.parseDouble(req.getParameter("f2")));
        }
        if (req.getParameter("f3") != null) {
            order.setF3(Double.parseDouble(req.getParameter("f3")));
        }
        if (req.getParameter("riskIndex") != null) {
            order.setRiskIndex(Double.parseDouble(req.getParameter("riskIndex")));
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        mapper.writeValue(out, data);
        out.flush();
    }

    private void writeError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.write("{\"error\":\"" + message + "\"}");
        out.flush();
    }

    private void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}