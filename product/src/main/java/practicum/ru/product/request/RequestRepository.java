package practicum.ru.product.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);
}
