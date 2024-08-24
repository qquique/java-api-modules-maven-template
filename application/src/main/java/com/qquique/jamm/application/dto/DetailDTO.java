package com.qquique.jamm.application.dto;

public class DetailDTO {
    private Long id;
    private String description;
    private Long headerId;

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

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    @Override
    public String toString() {
        return "DetailDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", headerId=" + headerId +
                '}';
    }
}
