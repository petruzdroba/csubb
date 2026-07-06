package org.zdroba.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="employees")
@Where(clause = "is_deleted = false")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private BigDecimal salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "phone")
    private String phone;

    @Version
    private Integer version;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;


    public int getId() { return id; }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public Department getDepartment() { return department; }

    public Integer getDepartmentId() {
        return department.getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void softDelete(String username) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
