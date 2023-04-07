package ru.practicum.events.repository;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventSort;
import ru.practicum.state.State;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CustomEventRepositoryImpl implements CustomEventRepository {

    private final EntityManager entityManager;

    @Override
    public List<Event> searchByParamsPublic(
            String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable, EventSort sort, Integer from, Integer size) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);

        Root<Event> rootEvent = criteriaQuery.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(rootEvent.get("state"), State.PUBLISHED));

        if (text != null) {
            String search = text.toLowerCase();
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(rootEvent.get("annotation")), "%" + search + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(rootEvent.get("description")), "%" + search + "%")
            ));
        }

        if (paid != null) {
            predicates.add(criteriaBuilder.equal(rootEvent.get("paid"), paid));
        }

        if (categories != null && categories.size() > 0) {
            CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(rootEvent.get("category"));
            for (Long catId : categories) {
                inClause.value(catId);
            }
            predicates.add(inClause);
        }

        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.le(rootEvent.get("confirmedRequests"), rootEvent.get("participantLimit")),
                    criteriaBuilder.le(rootEvent.get("participantLimit"), 0)
            ));
        }

        if (sort != null) {
            if (sort.equals(EventSort.EVENT_DATE)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootEvent.get("eventDate")));
            } else if (sort.equals(EventSort.VIEWS)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootEvent.get("views")));
            }
        }

            predicates.add(criteriaBuilder.greaterThan(rootEvent.get("eventDate"), rangeStart));

            predicates.add(criteriaBuilder.lessThan(rootEvent.get("eventDate"), rangeEnd));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(from).setMaxResults(size).getResultList();
    }
}
