package com.urlive.domain.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"urls"})
    Optional<User> findUserWithUrlsById(@Param("id") Long id);

    @Query("select u from User u join fetch u.country where u.id = :id")
    Optional<User> findUserWithCountry(@Param("id") Long id);

    @Query("select u from User u join fetch u.country where u.phoneNumber = :phoneNumber")
    Optional<User> findUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
