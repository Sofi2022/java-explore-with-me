package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id in :userIds order by u.id desc")
    List<User> findAllByIdContains(@Param ("userIds") List<Long> userIds);

    @Query("select u from User u where u.id in :userIds order by u.id desc")
    Page<User> findAllByIdContainsWithPage(PageRequest pageRequest, @Param ("userIds") List<Long> userIds);

    @Query("select u from User u where u.name like :name")
    Optional<List<User>> findByName(String name);
}
