package com.qquique.jamm.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "details")
public class Detail extends Base {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private Header header;

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

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", header=" + header +
                '}';
    }
}
