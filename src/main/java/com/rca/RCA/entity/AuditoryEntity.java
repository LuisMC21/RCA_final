package com.rca.RCA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
@MappedSuperclass

public class AuditoryEntity {

    @Column(name = "tx_uniqueIdentifier", length = 40)
    private String uniqueIdentifier;
    @Column(name = "tx_status", length = 40)
    private String status;
    @Column(name="tx_create_at")
    private LocalDateTime createAt;
    @Column(name="tx_update_at")
    private LocalDateTime updateAt;
    @Column(name="tx_delete_at")
    private LocalDateTime deleteAt;

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}

