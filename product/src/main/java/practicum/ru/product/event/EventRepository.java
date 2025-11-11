package practicum.ru.product.event;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findByInitiatorId(Long initiatorId);

    default List<Event> findEvents(String text,
                                   List<Long> categories,
                                   Boolean paid,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Boolean onlyAvailable) {

        Specification<Event> spec = Specification.where(null);

        if (text != null && !text.trim().isEmpty()) {
            String searchText = "%" + text.toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("annotation")), searchText),
                            cb.like(cb.lower(root.get("description")), searchText)
                    )
            );
        }

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").get("id").in(categories));
        }

        if (paid != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("paid"), paid));
        }

        if (rangeStart != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        if (rangeEnd != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        if (Boolean.TRUE.equals(onlyAvailable)) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit"))
            );
        }

        spec = spec.and((root, query, cb) -> cb.equal(root.get("state"), Status.PUBLISHED));
        return findAll(spec, Sort.by(Sort.Direction.ASC, "eventDate"));
    }

//    @Query("SELECT e FROM Event e WHERE " +
//            "e.state = 'PUBLISHED'")
//    List<Event> findEvents();

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
}
