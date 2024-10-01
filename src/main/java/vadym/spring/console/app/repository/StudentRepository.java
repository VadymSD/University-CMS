package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    String SELECT_STUDENTS_BY_GROUP_NAME = "SELECT students.* FROM students " +
            "JOIN groups ON students.group_id = groups.group_id WHERE groups.group_name like %:groupName%";

    Optional<Student> findByFirstName(String name) throws SQLException;

    @Query(value = SELECT_STUDENTS_BY_GROUP_NAME,
            nativeQuery = true)
    List<Student> findStudentsAssignedToGroupByName(@Param("groupName") String groupName) throws SQLException;

    Optional<Student> findById(Long id);
}