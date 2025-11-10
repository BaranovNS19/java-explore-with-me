package practicum.ru.product.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long initiatorId);

//    @Query("SELECT e FROM Event e WHERE " +
//            "(:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
//            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND " +
//            "(:categories IS NULL OR e.category.id IN :categories) AND " +
//            "(:paid IS NULL OR e.paid = :paid) AND " +
//            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
//            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) AND " +
//            "(:onlyAvailable = false OR e.confirmedRequests < e.participantLimit) AND " +
//            "e.state = practicum.ru.product.event.Status.PUBLISHED " +  // Используем enum напрямую
//            "ORDER BY " +
//            "CASE WHEN :sort = 'VIEWS' THEN e.views END DESC, " +
//            "CASE WHEN :sort != 'VIEWS' THEN e.eventDate END ASC")
//    List<Event> findEvents(@Param("text") String text,
//                           @Param("categories") List<Long> categories,
//                           @Param("paid") Boolean paid,
//                           @Param("rangeStart") LocalDateTime rangeStart,
//                           @Param("rangeEnd") LocalDateTime rangeEnd,
//                           @Param("onlyAvailable") Boolean onlyAvailable,
//                           @Param("sort") String sort);

    @Query("SELECT e FROM Event e WHERE " +
            "e.state = 'PUBLISHED'")
    List<Event> findEvents();

//    @Query("SELECT e FROM Event e WHERE " +
//            "(:users IS NULL OR e.initiator.id IN :users) AND " +
//            "(:states IS NULL OR e.state IN :states) AND " +
//            "(:categories IS NULL OR e.category.id IN :categories) AND " +
//            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
//            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
//    List<Event> findEventsByAdmin(@Param("users") List<Long> users,
//                                  @Param("states") List<String> states,
//                                  @Param("categories") List<Long> categories,
//                                  @Param("rangeStart") LocalDateTime rangeStart,
//                                  @Param("rangeEnd") LocalDateTime rangeEnd);

    @Query("SELECT e FROM Event e WHERE " +
            "(:users IS NULL OR e.initiator.id IN :users) AND " +
            "(:states IS NULL OR e.state IN :states) AND " +  // states должен быть List<Status>
            "(:categories IS NULL OR e.category.id IN :categories) AND " +
            "(:rangeStart IS NULL OR e.eventDate >= :rangeStart) AND " +
            "(:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findEventsByAdmin(@Param("users") List<Long> users,
                                  @Param("states") List<Status> states,  // ← Изменить на List<Status>
                                  @Param("categories") List<Long> categories,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd);

    List<Event> findByCategoryId(Long categoryId);
}
