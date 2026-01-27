package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long tableId;
    private List<MenuItem> menuItems;
    private LocalDateTime date;
    private OrderStatus status;

    public Order(Long tableId, List<MenuItem> menuItems) {
        this.id=null;
        this.tableId = tableId;
        this.menuItems = menuItems;
        this.date = LocalDateTime.now();
        this.status = OrderStatus.PREPARING;
    }

    public Order(Long id, Long tableId, List<MenuItem> menuItems, LocalDateTime date, OrderStatus status) {
        this.id = id;
        this.tableId = tableId;
        this.menuItems = menuItems;
        this.date = date;
        this.status = status;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
