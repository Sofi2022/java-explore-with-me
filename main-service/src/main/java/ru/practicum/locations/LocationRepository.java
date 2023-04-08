package ru.practicum.locations;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.locations.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
