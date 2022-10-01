package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Integer id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(name = "compilations_events",
    joinColumns = @JoinColumn(name = "compilation_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
}
