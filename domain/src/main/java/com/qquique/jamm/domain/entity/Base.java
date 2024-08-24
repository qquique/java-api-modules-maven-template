package com.qquique.jamm.domain.entity;

import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public class Base {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_date_created")
    private Date lastDateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_date_modified")
    private Date lastDateModified;

    public Date getLastDateCreated() {
        return lastDateCreated;
    }

    public void setLastDateCreated(Date lastDateCreated) {
        this.lastDateCreated = lastDateCreated;
    }

    public Date getLastDateModified() {
        return lastDateModified;
    }

    public void setLastDateModified(Date lastDateModified) {
        this.lastDateModified = lastDateModified;
    }

    @PrePersist
    public void prePersist() {
        this.lastDateCreated = new Date();
        this.lastDateModified = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastDateModified = new Date();
    }
}
