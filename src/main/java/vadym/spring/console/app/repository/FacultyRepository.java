package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Faculty;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyName(String name) throws SQLException;

    Optional<Faculty> findById(Long id);
}
