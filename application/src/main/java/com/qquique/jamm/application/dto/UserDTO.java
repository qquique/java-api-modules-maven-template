package com.qquique.jamm.application.dto;

import java.util.Date;

public class UserDTO {
    private Long id;
    private String username;
    private String password;


    private Date lastDateCreated;
    private Date lastDateModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastDateModified() {
        return lastDateModified;
    }

    public Date getLastDateCreated() {
        return lastDateCreated;
    }

    public void setLastDateCreated(Date lastDateCreated) {
        this.lastDateCreated = lastDateCreated;
    }

    public void setLastDateModified(Date lastDateModified) {
        this.lastDateModified = lastDateModified;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastDateCreated=" + lastDateCreated +
                ", lastDateModified=" + lastDateModified +
                '}';
    }
}

