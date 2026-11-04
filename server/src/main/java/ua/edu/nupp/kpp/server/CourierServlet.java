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

@WebServlet(name = "CourierServlet", value = "/couriers")
public class CourierServlet extends HttpServlet {

    private final CourierService courierService = new CourierService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        try {
            List<Courier> couriers = courierService.getAll();
            writeJson(resp, couriers);
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
                Courier courier = new Courier();
                populateCourierFromRequest(req, courier);

                courierService.add(courier);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Courier added successfully\"}");

            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                Courier courier = new Courier();
                courier.setId(id);
                populateCourierFromRequest(req, courier);

                courierService.update(courier);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Courier updated successfully\"}");

            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                courierService.delete(id);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Courier deleted successfully\"}");

            } else {
                writeError(resp, 400, "Invalid or missing 'action' parameter");
            }
        } catch (IllegalArgumentException e) {
            writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            writeError(resp, 500, e.getMessage());
        }
    }

    private void populateCourierFromRequest(HttpServletRequest req, Courier courier) {
        if (req.getParameter("fullName") != null) {
            courier.setFullName(req.getParameter("fullName"));
        }
        if (req.getParameter("phone") != null) {
            courier.setPhone(req.getParameter("phone"));
        }
        if (req.getParameter("vehicleType") != null) {
            courier.setVehicleType(req.getParameter("vehicleType"));
        }
        if (req.getParameter("status") != null) {
            courier.setStatus(req.getParameter("status"));
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