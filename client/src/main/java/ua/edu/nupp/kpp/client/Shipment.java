package ua.edu.nupp.kpp.client;

public class Shipment {
    private Long id;
    private String trackingNumber;
    private double weight;
    private String description;
    private double declaredValue;

    public Shipment() {}

    public Shipment(Long id, String trackingNumber, double weight, String description, double declaredValue) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.weight = weight;
        this.description = description;
        this.declaredValue = declaredValue;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDeclaredValue() { return declaredValue; }
    public void setDeclaredValue(double declaredValue) { this.declaredValue = declaredValue; }
}