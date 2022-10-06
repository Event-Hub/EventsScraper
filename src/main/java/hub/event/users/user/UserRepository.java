package hub.event.users.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    // raczej do wywalenia
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.filters WHERE u.username = ?1")
    User findByUsernameWithFilters(String username);


    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.filters",
    countQuery = "SELECT COUNT(*) FROM User")
    Page<User> findAllWithFilters(Pageable pageable);
}
