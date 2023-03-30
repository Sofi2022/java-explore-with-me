package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

@Repository
public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("select p from ParticipationRequest p where p.requester.id = :userId " +
            "and p.event.id = :eventId")
    List<ParticipationRequest> findAllByRequesterIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);


    @Query("select p from ParticipationRequest p join fetch p.event e where p.requester.id =:userId " +
            "and e.initiator.id <> :userId")
    List<ParticipationRequest> findAllByRequesterIdInForeignEvents(@Param("userId") Long userId);

    @Query("select p from ParticipationRequest p join fetch p.event e where e.initiator.id =:userId and e.id = :eventId")
    List<ParticipationRequest> findAllUserRequestsInEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
