package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Role;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String username) throws SQLException;

    Optional<Role> findById(Long id) throws RuntimeException;
}
