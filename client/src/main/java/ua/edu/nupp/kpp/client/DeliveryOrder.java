package ua.edu.nupp.kpp.client;

public class DeliveryOrder {
    private long id;
    private long clientId;
    private long courierId;
    private long shipmentId;
    private String orderDate;
    private String status;
    private double totalCost;
    private double f1;
    private double f2;
    private double f3;
    private double riskIndex;

    public DeliveryOrder() {}

    public DeliveryOrder(long id, long clientId, long courierId, long shipmentId,
                         String orderDate, String status, double totalCost,
                         double f1, double f2, double f3, double riskIndex) {
        this.id = id;
        this.clientId = clientId;
        this.courierId = courierId;
        this.shipmentId = shipmentId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalCost = totalCost;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.riskIndex = riskIndex;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    public double getF1() { return f1; }
    public void setF1(double f1) { this.f1 = f1; }

    public double getF2() { return f2; }
    public void setF2(double f2) { this.f2 = f2; }

    public double getF3() { return f3; }
    public void setF3(double f3) { this.f3 = f3; }

    public double getRiskIndex() { return riskIndex; }
    public void setRiskIndex(double riskIndex) { this.riskIndex = riskIndex; }
}