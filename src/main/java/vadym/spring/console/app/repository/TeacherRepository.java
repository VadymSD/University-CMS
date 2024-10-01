package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Teacher;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    String SELECT_TEACHERS_BY_COURSE_NAME = "SELECT teachers.* FROM teachers " +
            "JOIN teacher_courses ON teachers.teacher_id = teacher_courses.teacher_id " +
            "JOIN courses ON teacher_courses.course_id = courses.course_id WHERE courses.course_name = :courseName";

    Optional<Teacher> findByFirstName(String name) throws SQLException;

    @Query(value = SELECT_TEACHERS_BY_COURSE_NAME,
            nativeQuery = true)
    List<Teacher> findTeachersAssignedToCourseByName(@Param("courseName") String courseName) throws SQLException;

    Optional<Teacher> findById(Long id);
}
