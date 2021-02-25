package microservices.user.repository;

import microservices.user.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByUsername(String username); //ez alapján úgymond autómatikusan legenerálta nekünk az implementációt

    //List<User> findAllById(Long id);

    @Query("SELECT u.roleId FROM User u WHERE u.username = ?1")
    User findRoleByUsername(String username);
}
