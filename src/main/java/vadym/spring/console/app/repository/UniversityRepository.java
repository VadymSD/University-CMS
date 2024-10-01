package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.University;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByUniversityName(String name) throws SQLException;
}
