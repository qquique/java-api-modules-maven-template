package com.qquique.jamm.domain.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="headers")
public class Header extends Base {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Detail> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Header{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", details=" + details +
                '}';
    }
}
