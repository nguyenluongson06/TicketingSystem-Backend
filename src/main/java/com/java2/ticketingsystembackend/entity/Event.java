package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "events")
@Getter @Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime timeStart;

    @Column(nullable = false)
    private LocalDateTime timeEnd;

    private String place;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, name = "max_quantity")
    private Integer maxQuantity;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE", name = "is_public")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "thumbnail")
    private String thumbnail;

    public boolean isPublic() {
        return isPublic;
    }
}

