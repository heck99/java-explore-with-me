package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Integer id;

    @Column(name = "pinned")
    Boolean pinned;

    @Column(name = "title")
    String title;

    @ManyToMany
    @JoinTable(name = "compilations_events",
    joinColumns = @JoinColumn(name = "compilation_id"),
    inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<Event> events;
}
