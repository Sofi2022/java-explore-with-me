package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("select new  ru.practicum.server.model.ViewState(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in (:uris) " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewState> findAllByTimestampBetween(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);

    @Query("select new  ru.practicum.server.model.ViewState(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between :start and :end " +
            "and e.uri in (:uris) " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewState> findAllByTimestampBetweenUnique(@Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end,
                                              @Param("uris") List<String> uris);
}
