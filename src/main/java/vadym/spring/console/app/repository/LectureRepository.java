package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Lecture;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    String SELECT_LECTURES_BY_TEACHERS_NAME = "SELECT lectures.* FROM lectures " +
            "JOIN teachers ON lectures.teacher_id = teachers.teacher_id " +
            "WHERE teachers.first_name LIKE  %:teacherName%";

    String SELECT_LECTURES_BY_STUDENT_ID = "SELECT lectures.* FROM lectures " +
            "JOIN group_lectures ON lectures.lecture_id = group_lectures.lecture_id " +
            "JOIN groups ON group_lectures.group_id = groups.group_id " +
            "JOIN students ON groups.group_id = students.group_id " +
            "WHERE students.student_id = :id";

    Optional<Lecture> findByLectureName(String name) throws SQLException;

    @Query(value = SELECT_LECTURES_BY_TEACHERS_NAME, nativeQuery = true)
    List<Lecture> findLectureByTeacherName(@Param("teacherName") String teacherName);

    @Query(value = SELECT_LECTURES_BY_STUDENT_ID, nativeQuery = true)
    List<Lecture> findLecturesByStudentId(@Param("id") Long id);

    List<Lecture> findLecturesByTeacherId(Long id);

    List<Lecture> findLecturesByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Lecture> findById(Long id);
}
