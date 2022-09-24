package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {

    List<Statistic> findAllByTimeAfterAndTimeBeforeAndUrl(LocalDateTime start, LocalDateTime end, String uris);

    @Query("SELECT s FROM Statistic s WHERE s.id in (SELECT max(ss.id) FROM Statistic ss GROUP BY ss.ip)")
    List<Statistic> findWithUniqueIp(LocalDateTime start, LocalDateTime end, String uri);
}
