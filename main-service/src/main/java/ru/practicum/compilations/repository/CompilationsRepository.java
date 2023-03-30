package ru.practicum.compilations.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<Compilation, Long> {

//    @Query("select c from Compilation c left join compilation_events where c.pinned=true")
//    List<Compilation> findAllPinnedTrue(PageRequest request);
//
//    @Query("select c from Compilation c where c.pinned=false")
//    List<Compilation> findAllPinnedFalse(PageRequest request);

    List<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
