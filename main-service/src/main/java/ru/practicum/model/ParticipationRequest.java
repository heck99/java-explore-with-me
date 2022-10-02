package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "participation_request")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_request_id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;

    @ManyToOne()
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestState status;

    @Column(name = "created")
    private LocalDateTime created;
}
