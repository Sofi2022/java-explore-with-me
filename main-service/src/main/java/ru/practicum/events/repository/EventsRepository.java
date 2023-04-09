package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepository extends CustomEventRepository, JpaRepository<Event, Long> {


    @Query("SELECT e FROM Event e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id IN :userIds " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> findAllWithAllParameters(@Param("userIds") List<Long> userIds, @Param("states") List<State> states,
                                         @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE e.category.id IN :categories " +
            "AND e.state IN :states " +
            "AND e.eventDate BETWEEN:rangeStart AND:rangeEnd")
    Page<Event> findAllEventsWithoutIdList(@Param("categories") List<Long> categories, @Param("states") List<State> states,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id = :userId " +
            "AND e.id = :eventId")
    List<Event> findAllByUser(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("SELECT e FROM Event e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id = :userId")
    Page<Event> findAllByUserWithPage(@Param("userId") Long userId, PageRequest pageRequest);

    @Query("SELECT e FROM Event e " +
            "WHERE e.id = :id " +
            "AND e.state ='PUBLISHED'")
    Optional<Event> findEventByIdAndStatePublished(@Param("id") Long eventId);


    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) OR LOWER(e.description) " +
            "LIKE LOWER(:text) " +
            "AND e.category.id IN :categoriesIds " +
            "AND e.eventDate BETWEEN :start AND :end " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.eventDate ASC")
    Page<Event> findAllEventsFilteredWithCategoriesAndDateTimeAsc(PageRequest pageRequest, @Param("text") String text,
                                                                  @Param("categoriesIds")
    List<Long> categoriesIds, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) " +
            "OR LOWER(e.description) LIKE LOWER(:text) " +
            "AND e.category.id IN :categoriesIds " +
            "AND e.eventDate BETWEEN :start AND :end " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.views ASC")
    Page<Event> findAllEventsFilteredWithCategoriesAndViewsAsc(PageRequest pageRequest, @Param("text") String text,
                                                               @Param("categoriesIds") List<Long> categoriesIds,
                                                                  @Param("start") LocalDateTime start, @Param("end")
                                                               LocalDateTime end);

    @Query("SELECT e FROM Event e " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) " +
            "OR LOWER(e.description) LIKE LOWER(:text) " +
            "AND e.paid = FALSE " +
            "AND e.eventDate BETWEEN :start AND :end " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.eventDate ASC")
    Page<Event> findAllEventsFilteredWithPageWithoutCategoryEventDateAsc(PageRequest pageRequest, @Param("text") String text,
                                                             @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT e FROM Event e " +
            "WHERE LOWER(e.annotation) LIKE LOWER(:text) " +
            "OR LOWER(e.description) LIKE LOWER(:text)" +
            " AND e.paid = FALSE " +
            "AND e.eventDate BETWEEN :start AND :end " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.views ASC")
    Page<Event> findAllEventsFilteredWithPageWithoutCategoryViewsAsc(PageRequest pageRequest, @Param("text") String text,
                                                                         @Param("start") LocalDateTime start,
                                                                     @Param("end") LocalDateTime end);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE e.category.id IN :categoriesIds")
    Page<Event> findAllByIds(PageRequest pageRequest, List<Long> categoriesIds);

    @Query("SELECT e FROM Event e " +
            "WHERE e.id = :eventId " +
            "AND e.initiator.id = :userId")
    Optional<Event> findByIdAndInitId(@Param("eventId") Long eventId, @Param("userId") Long userId);


    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE e.category.id = :catId")
    Optional<List<Event>> findAllByCategoryId(Long catId);
}
