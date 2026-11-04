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

@WebServlet(name = "ClientServlet", value = "/clients")
public class ClientServlet extends HttpServlet {

    private final ClientService clientService = new ClientService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setAccessControlHeaders(resp);
        try {
            List<Client> clients = clientService.getAll();
            writeJson(resp, clients);
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
                Client client = new Client();
                populateClientFromRequest(req, client);

                clientService.add(client);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Client added successfully\"}");

            } else if ("update".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                Client client = new Client();
                client.setId(id);
                populateClientFromRequest(req, client);

                clientService.update(client);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Client updated successfully\"}");

            } else if ("delete".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                clientService.delete(id);
                writeJson(resp, "{\"status\":\"success\",\"message\":\"Client deleted successfully\"}");

            } else {
                writeError(resp, 400, "Invalid or missing 'action' parameter");
            }
        } catch (IllegalArgumentException e) {
            writeError(resp, 400, e.getMessage());
        } catch (Exception e) {
            writeError(resp, 500, e.getMessage());
        }
    }

    private void populateClientFromRequest(HttpServletRequest req, Client client) {
        if (req.getParameter("fullName") != null) {
            client.setFullName(req.getParameter("fullName"));
        }
        if (req.getParameter("phone") != null) {
            client.setPhone(req.getParameter("phone"));
        }
        if (req.getParameter("email") != null) {
            client.setEmail(req.getParameter("email"));
        }
        if (req.getParameter("address") != null) {
            client.setAddress(req.getParameter("address"));
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