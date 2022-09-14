package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne()
    @JoinColumn(name = "initiator_id", referencedColumnName = "user_id")
    private User initiator;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published")
    private LocalDateTime published;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "title")
    private String title;
}
