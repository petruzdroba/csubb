package org.example.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long driverId;
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String pickupAddress;
    private String destinationAddress;
    private String clientName;

    public Order() {
    }

    public Order(Long id, Long driverId, Status status, LocalDateTime startDate, LocalDateTime endDate, String pickupAddress, String destinationAddress, String clientName) {
        this.id = id;
        this.driverId = driverId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.clientName = clientName;
    }

    public Order(String pickupAddress, String destinationAddress, String clientName) {

        this.driverId = null;
        this.status = Status.PENDING;
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.now();
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.clientName = clientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
