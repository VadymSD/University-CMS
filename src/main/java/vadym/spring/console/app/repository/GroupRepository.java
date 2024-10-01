package vadym.spring.console.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vadym.spring.console.app.entity.Group;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    String FIND_GROUPS_WITH_LESS_OR_EQUAL_STUDENTS =
            "SELECT g.id groupName FROM Group g JOIN g.students s GROUP BY g.id HAVING COUNT(s.id) <= :studentCount";

    String SELECT_GROUPS_BY_COURSE_NAME = "SELECT groups.* FROM groups " +
            "JOIN group_courses ON groups.group_id = group_courses.group_id " +
            "JOIN courses ON group_courses.course_id = courses.course_id WHERE courses.course_name like %:courseName%";

    Optional<Group> findByGroupName(String name) throws SQLException;

    Optional<Group> findById(Long id);

    @Query(FIND_GROUPS_WITH_LESS_OR_EQUAL_STUDENTS)
    List<Group> findGroupsWithLessOrEqualStudents(@Param("studentCount") int studentCount) throws SQLException;

    @Query(value = SELECT_GROUPS_BY_COURSE_NAME,
            nativeQuery = true)
    List<Group> findGroupsAssignedToCourseByName(@Param("courseName") String courseName) throws SQLException;
}
