package hub.event.users.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
