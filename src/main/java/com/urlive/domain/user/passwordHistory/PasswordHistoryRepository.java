package com.urlive.domain.user.passwordHistory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    @Query("select ph from PasswordHistory ph where ph.user.id = :userId order by ph.createdAt DESC")
    List<PasswordHistory> findRecentHistories(@Param("userId") Long userId, Pageable pageable);
}
