package com.qquique.jamm.application.dto;

import java.util.List;

public class HeaderDTO {
    private Long id;
    private String description;
    private List<DetailDTO> details;

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

    public List<DetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDTO> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "HeaderDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", details=" + details +
                '}';
    }
}

