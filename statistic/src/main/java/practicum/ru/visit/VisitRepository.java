package practicum.ru.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practicum.ru.visit.dto.VisitCountDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("""
            SELECT new practicum.ru.visit.dto.VisitCountDto(
                MIN(v.id),
                MIN(v.app),
                v.uri,
                MIN(v.ip),
                MIN(v.timestamp),
                COUNT(v))
            FROM Visit v
            WHERE v.timestamp BETWEEN :start AND :end
            GROUP BY v.uri
            ORDER BY COUNT(v) DESC
            """)
    List<VisitCountDto> findUniqueUrisWithVisitCount(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

    @Query("""
            SELECT new practicum.ru.visit.dto.VisitCountDto(
                MIN(v.id),
                MIN(v.app),
                v.uri,
                MIN(v.ip),
                MIN(v.timestamp),
                COUNT(DISTINCT v.ip))
            FROM Visit v
            WHERE v.timestamp BETWEEN :start AND :end
            GROUP BY v.uri
            ORDER BY COUNT(DISTINCT v.ip) DESC
            """)
    List<VisitCountDto> findUniqueUrisWithVisitStats(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end);

    @Query("""
            SELECT new practicum.ru.visit.dto.VisitCountDto(
                MIN(v.id),
                MIN(v.app),
                v.uri,
                MIN(v.ip),
                MIN(v.timestamp),
                COUNT(DISTINCT v.ip))
            FROM Visit v
            WHERE v.timestamp BETWEEN :startDate AND :endDate
              AND v.uri IN :uris
            GROUP BY v.uri
            ORDER BY COUNT(DISTINCT v.ip) DESC
            """)
    List<VisitCountDto> findUniqueUrisByUrisWithVisitCount(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate,
                                                           @Param("uris") List<String> uris);
}
