package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpDeliveryClient {

    private static final String BASE = "http://localhost:8080/courier-delivery-server";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public HttpDeliveryClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private String enc(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    // Універсальний GET-запит для отримання списків чи даних
    protected <T> List<T> getList(String endpoint, Class<T> clazz) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Server error: " + response.statusCode() + " - " + response.body());
        }

        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.readValue(response.body(), listType);
    }

    // Універсальний POST-запит із параметрами (x-www-form-urlencoded)
    protected String post(String endpoint, Map<String, String> params) throws IOException, InterruptedException {
        StringBuilder body = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                body.append("&");
            }
            body.append(enc(entry.getKey()))
                    .append("=")
                    .append(enc(entry.getValue()));
            first = false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + endpoint))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Server error: " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }

    // --- Методи отримання списків (GET) ---
    public List<Client> getClients() throws IOException, InterruptedException {
        return getList("/clients", Client.class);
    }

    public List<Courier> getCouriers() throws IOException, InterruptedException {
        return getList("/couriers", Courier.class);
    }

    public List<Shipment> getShipments() throws IOException, InterruptedException {
        return getList("/shipments", Shipment.class);
    }

    public List<DeliveryOrder> getOrders() throws IOException, InterruptedException {
        return getList("/delivery-orders", DeliveryOrder.class);
    }

    // --- Методи додавання (POST: action=add) ---
    public String addClient(String fullName, String phone, String email, String address) throws IOException, InterruptedException {
        return post("/clients", Map.of(
                "action", "add",
                "fullName", fullName,
                "phone", phone,
                "email", email,
                "address", address
        ));
    }

    public String addCourier(String fullName, String phone, String vehicleType, String status) throws IOException, InterruptedException {
        return post("/couriers", Map.of(
                "action", "add",
                "fullName", fullName,
                "phone", phone,
                "vehicleType", vehicleType,
                "status", status
        ));
    }

    public String addShipment(String trackingNumber, double weight, String description, double declaredValue) throws IOException, InterruptedException {
        return post("/shipments", Map.of(
                "action", "add",
                "trackingNumber", trackingNumber,
                "weight", String.valueOf(weight),
                "description", description,
                "declaredValue", String.valueOf(declaredValue)
        ));
    }

    public String addOrder(long clientId, long courierId, long shipmentId, String orderDate, double totalCost) throws IOException, InterruptedException {
        return post("/delivery-orders", Map.of(
                "action", "add",
                "clientId", String.valueOf(clientId),
                "courierId", String.valueOf(courierId),
                "shipmentId", String.valueOf(shipmentId),
                "orderDate", orderDate,
                "totalCost", String.valueOf(totalCost)
        ));
    }

    // --- Методи редагування (POST: action=update) ---
    public String updateClient(long id, String fullName, String phone, String email, String address) throws IOException, InterruptedException {
        return post("/clients", Map.of(
                "action", "update",
                "id", String.valueOf(id),
                "fullName", fullName,
                "phone", phone,
                "email", email,
                "address", address
        ));
    }

    // --- Методи видалення через POST (action=delete) ---
    public String deleteClient(long id) throws IOException, InterruptedException {
        return post("/clients", Map.of(
                "action", "delete",
                "id", String.valueOf(id)
        ));
    }

    public String deleteCourier(long id) throws IOException, InterruptedException {
        return post("/couriers", Map.of(
                "action", "delete",
                "id", String.valueOf(id)
        ));
    }

    public String deleteShipment(long id) throws IOException, InterruptedException {
        return post("/shipments", Map.of(
                "action", "delete",
                "id", String.valueOf(id)
        ));
    }

    public String deleteOrder(long id) throws IOException, InterruptedException {
        return post("/delivery-orders", Map.of(
                "action", "delete",
                "id", String.valueOf(id)
        ));
    }
}