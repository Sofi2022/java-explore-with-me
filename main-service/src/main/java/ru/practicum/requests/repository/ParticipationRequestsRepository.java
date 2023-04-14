package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("SELECT p FROM ParticipationRequest p " +
            "WHERE p.requester.id = :userId " +
            "AND p.event.id = :eventId")
    List<ParticipationRequest> findAllByRequesterIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);


    @Query("SELECT p FROM ParticipationRequest p " +
            "JOIN FETCH p.event e " +
            "WHERE p.requester.id =:userId " +
            "AND e.initiator.id <> :userId")
    List<ParticipationRequest> findAllByRequesterIdInForeignEvents(@Param("userId") Long userId);

    @Query("SELECT p FROM ParticipationRequest p " +
            "JOIN FETCH p.event e " +
            "WHERE e.initiator.id =:userId " +
            "AND e.id = :eventId")
    List<ParticipationRequest> findAllUserRequestsInEvent(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("SELECT p FROM ParticipationRequest p " +
            "JOIN FETCH p.event e " +
            "WHERE p.requester.id =:requesterId " +
            "AND e.id = :eventId " +
            "AND p.state = 'CONFIRMED'")
    Optional<ParticipationRequest> findParticipationRequestByRequesterIdAndEventId(@Param("requesterId")Long requesterId,
                                                                                   @Param("eventId")Long eventId);


    @Query("SELECT p FROM ParticipationRequest p " +
            "JOIN FETCH p.event e " +
            "WHERE e.id = :eventId " +
            "AND p.state = 'CONFIRMED'")
    List<ParticipationRequest> findConfirmedRequestsByEventId(@Param("eventId") Long eventId);
}
