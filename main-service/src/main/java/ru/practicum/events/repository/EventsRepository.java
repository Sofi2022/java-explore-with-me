package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.Event;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
}
