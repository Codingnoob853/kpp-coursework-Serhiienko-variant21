package ua.edu.nupp.kpp.client;

public class Courier {
    private Long id;
    private String fullName;
    private String phone;
    private String vehicleType;
    private String status;

    public Courier() {}

    public Courier(Long id, String fullName, String phone, String vehicleType, String status) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}