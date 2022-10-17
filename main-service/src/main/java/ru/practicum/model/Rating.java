package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "event_id")
    private Integer eventId;

}
