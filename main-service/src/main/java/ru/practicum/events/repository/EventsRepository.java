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


    @Query("select e from Event e join e.initiator join e.category where e.initiator.id in :userIds and " +
            "e.state in :states and e.category.id in :categories and e.eventDate between :rangeStart and :rangeEnd")
    Page<Event> findAllWithAllParameters(@Param("userIds") List<Long> userIds, @Param("states") List<State> states,
                                         @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e join e.category where e.category.id in :categories and e.state in :states and " +
            "e.eventDate between:rangeStart and:rangeEnd")
    Page<Event> findAllEventsWithoutIdList(@Param("categories") List<Long> categories, @Param("states") List<State> states,
                                           @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e join e.initiator join e.category where e.initiator.id = :userId and e.id = :eventId")
    List<Event> findAllByUser(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select e from Event e join e.initiator join e.category where e.initiator.id = :userId")
    Page<Event> findAllByUserWithPage(@Param("userId") Long userId, PageRequest pageRequest);

    @Query("select e from Event e where e.id = :id and e.state ='PUBLISHED'")
    Optional<Event> findEventByIdAndStatePublished(@Param("id") Long eventId);


    @Query("select e from Event e join e.category where lower(e.annotation) like lower(:text) or lower(e.description) " +
            "like lower(:text) and e.category.id in :categoriesIds and e.eventDate between :start and " +
            ":end and e.state = 'PUBLISHED' order by e.eventDate asc")
    Page<Event> findAllEventsFilteredWithCategoriesAndDateTimeAsc(PageRequest pageRequest, @Param("text") String text,
                                                                  @Param("categoriesIds")
    List<Long> categoriesIds, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("select e from Event e join e.category where lower(e.annotation) like lower(:text) or lower(e.description) " +
            "like lower(:text) and e.category.id in :categoriesIds and e.eventDate between :start and " +
            ":end and e.state = 'PUBLISHED' order by e.views asc")
    Page<Event> findAllEventsFilteredWithCategoriesAndViewsAsc(PageRequest pageRequest, @Param("text") String text,
                                                               @Param("categoriesIds") List<Long> categoriesIds,
                                                                  @Param("start") LocalDateTime start, @Param("end")
                                                               LocalDateTime end);

    @Query("select e from Event e where lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text) " +
            "and e.paid = false and e.eventDate between :start and :end and e.state = 'PUBLISHED' order by e.eventDate asc")
    Page<Event> findAllEventsFilteredWithPageWithoutCategoryEventDateAsc(PageRequest pageRequest, @Param("text") String text,
                                                             @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select e from Event e where lower(e.annotation) like lower(:text) or lower(e.description) like lower(:text)" +
            " and e.paid = false and e.eventDate between :start and :end and e.state = 'PUBLISHED' order by e.views asc")
    Page<Event> findAllEventsFilteredWithPageWithoutCategoryViewsAsc(PageRequest pageRequest, @Param("text") String text,
                                                                         @Param("start") LocalDateTime start,
                                                                     @Param("end") LocalDateTime end);

    @Query("select e from Event e join e.category where e.category.id in :categoriesIds")
    Page<Event> findAllByIds(PageRequest pageRequest, List<Long> categoriesIds);


    @Query("select e from Event e join e.category where e.category.id = :catId")
    Optional<List<Event>> findAllByCategoryId(Long catId);
}
