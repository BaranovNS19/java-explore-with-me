package practicum.ru.product.event;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
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

    List<Event> findByCategoryId(Long categoryId);


    default List<Event> findEventsByAdmin(List<Long> users,
                                          List<Status> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd) {

        Specification<Event> spec = Specification.where(null);

        if (users != null && !users.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("initiator").get("id").in(users));
        }

        if (states != null && !states.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("state").in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").get("id").in(categories));
        }

        if (rangeStart != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        return findAll(spec);
    }


    @Query("SELECT e FROM Event e")
    List<Event> findAllEvents();

    @Query(value = "SELECT e.* FROM events e " +
            "WHERE (:text IS NULL OR " +
            "       e.annotation ILIKE CONCAT('%', :text, '%') OR " +
            "       e.description ILIKE CONCAT('%', :text, '%')) AND " +
            "      (COALESCE(:categories) IS NULL OR e.category_id IN (:categories)) AND " +
            "      (:paid IS NULL OR e.paid = :paid) AND " +
            "      (:rangeStart IS NULL OR e.event_date >= :rangeStart) AND " +
            "      (:rangeEnd IS NULL OR e.event_date <= :rangeEnd) AND " +
            "      (:onlyAvailable = false OR e.confirmed_requests < e.participant_limit) AND " +
            "      e.state = 'PUBLISHED'",
            nativeQuery = true)
    List<Event> findEvents(@Param("text") String text,
                           @Param("categories") List<Long> categories,
                           @Param("paid") Boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           @Param("onlyAvailable") Boolean onlyAvailable);
}
