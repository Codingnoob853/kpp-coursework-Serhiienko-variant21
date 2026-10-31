package ua.edu.nupp.kpp.server;

public class DeliveryOrder {
    private Long id;
    private long clientId;
    private long courierId;
    private long shipmentId;
    private String orderDate;
    private String status;
    private double totalCost;

    public DeliveryOrder() {}

    public DeliveryOrder(Long id, long clientId, long courierId, long shipmentId, String orderDate, String status, double totalCost) {
        this.id = id;
        this.clientId = clientId;
        this.courierId = courierId;
        this.shipmentId = shipmentId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalCost = totalCost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public long getClientId() { return clientId; }
    public void setClientId(long clientId) { this.clientId = clientId; }

    public long getCourierId() { return courierId; }
    public void setCourierId(long courierId) { this.courierId = courierId; }

    public long getShipmentId() { return shipmentId; }
    public void setShipmentId(long shipmentId) { this.shipmentId = shipmentId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
}