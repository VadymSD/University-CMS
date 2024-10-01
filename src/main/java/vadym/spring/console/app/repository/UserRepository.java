package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.UserEntity;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username) throws SQLException;

    Optional<UserEntity> findById(Long id) throws RuntimeException;
}
