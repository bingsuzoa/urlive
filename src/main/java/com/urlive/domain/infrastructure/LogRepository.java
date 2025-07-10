package com.urlive.domain.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {


    @Query("""
            select FUNCTION('date', l.created_at) as date, count(l) 
            from Log l 
            where shortUrl = :shortUrl 
            and l.created_at between :start and :end
            group by FUNCTION('date', l.created_at)
            order by date
            """
    )
    List<Object[]> findLogByDateRange(
            @Param("shortUrl") String shortUrl,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    @Query("""
            select FUNCTION('date', l.created_at) as date, l.referer, count(l)
            from Log l
            where shortUrl = :shortUrl
            and l.createAt between :start and :end
            group by FUNCTION('date', l.created_at), l.referer
            """)
    List<Object[]> findLogByReferer(
            @Param("shortUrl") String shortUrl,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
