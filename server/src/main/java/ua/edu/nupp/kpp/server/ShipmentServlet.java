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

@WebServlet(name = "ShipmentServlet", value = "/shipments")
public class ShipmentServlet extends HttpServlet {

    private final ShipmentService shipmentService = new ShipmentService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        try {
            List<Shipment> shipments = shipmentService.getAll();
            writeJson(resp, shipments);
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
                Shipment shipment = new Shipment();
                populateShipmentFromRequest(req, shipment);

                shipmentService.add(shipment);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Shipment added successfully\"}");

            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                Shipment shipment = new Shipment();
                shipment.setId(id);
                populateShipmentFromRequest(req, shipment);

                shipmentService.update(shipment);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Shipment updated successfully\"}");

            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                shipmentService.delete(id);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Shipment deleted successfully\"}");

            } else {
                writeError(resp, 400, "Invalid or missing 'action' parameter");
            }
        } catch (IllegalArgumentException e) {
            writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            writeError(resp, 500, e.getMessage());
        }
    }

    private void populateShipmentFromRequest(HttpServletRequest req, Shipment shipment) {
        if (req.getParameter("trackingNumber") != null) {
            shipment.setTrackingNumber(req.getParameter("trackingNumber"));
        }
        if (req.getParameter("weight") != null) {
            shipment.setWeight(Double.parseDouble(req.getParameter("weight")));
        }
        if (req.getParameter("description") != null) {
            shipment.setDescription(req.getParameter("description"));
        }
        if (req.getParameter("declaredValue") != null) {
            shipment.setDeclaredValue(Double.parseDouble(req.getParameter("declaredValue")));
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