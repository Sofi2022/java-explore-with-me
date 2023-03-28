package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("select p from ParticipationRequest p inner join p.requester join fetch p.event where p.requester.id = :userId " +
            "and p.event.id = :eventId")
    List<ParticipationRequest> findAllByRequesterIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
