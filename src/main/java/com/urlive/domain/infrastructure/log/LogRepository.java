package com.urlive.domain.infrastructure.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query(value = """
            select DATE(created_at) as date, count(*) as visitCount
            from Log
            where short_url = :shortUrl 
            and created_at between :start and :end
            group by date
            order by date
            """, nativeQuery = true
    )
    List<Object[]> findLogsByDateRange(
            @Param("shortUrl") String shortUrl,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    @Query(value = """
            select DATE(created_at) as date, referer, count(*) as visitCount
            from Log
            where short_url = :shortUrl 
            and created_at between :start and :end
            group by date, referer
            order by date
            """, nativeQuery = true)
    List<Object[]> findLogsByReferer(
            @Param("shortUrl") String shortUrl,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            select DATE(created_at) as date, device, count(*) as visitCount
            from Log
            where short_url = :shortUrl 
            and created_at between :start and :end
            group by date, device
            order by date
            """, nativeQuery = true)
    List<Object[]> findLogsByDevice(
            @Param("shortUrl") String shortUrl,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
