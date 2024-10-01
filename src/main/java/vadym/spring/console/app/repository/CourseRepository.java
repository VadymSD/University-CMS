package vadym.spring.console.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    String SELECT_COURSES_BY_TEACHER_NAME =  "SELECT courses.* FROM courses " +
            "JOIN teacher_courses ON courses.course_id = teacher_courses.course_id " +
            "JOIN teachers ON teacher_courses.teacher_id = teachers.teacher_id " +
            "WHERE teachers.first_name like %:name%";

    String SELECT_COURSES_BY_STUDENT_ID = "SELECT courses.* FROM courses " +
            "JOIN group_courses ON courses.course_id = group_courses.course_id " +
            "JOIN groups ON group_courses.group_id = groups.group_id " +
            "JOIN students ON groups.group_id = students.group_id " +
            "WHERE students.student_id = :id";

    String SELECT_COURSES_BY_TEACHER_ID = "SELECT courses.* FROM courses " +
            "JOIN teacher_courses ON courses.course_id = teacher_courses.course_id " +
            "JOIN teachers ON teacher_courses.teacher_id = teachers.teacher_id " +
            "WHERE teachers.teacher_id = :id";

    Optional<Course> findByCourseName(String name);

    Optional<Course> findById(Long id);

    @Query(value = SELECT_COURSES_BY_TEACHER_NAME, nativeQuery = true)
    List<Course> findCourseByTeachersName(@Param("name") String name);

    @Query(value = SELECT_COURSES_BY_STUDENT_ID, nativeQuery = true)
    List<Course> findCourseByStudentId(@Param("id") Long id);

    @Query(value = SELECT_COURSES_BY_TEACHER_ID, nativeQuery = true)
    List<Course> findCourseByTeacherId(@Param("id") Long id);
}
