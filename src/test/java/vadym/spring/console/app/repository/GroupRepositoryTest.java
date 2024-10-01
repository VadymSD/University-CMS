package vadym.spring.console.app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import vadym.spring.console.app.entity.Group;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        GroupRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class GroupRepositoryTest {

    @Autowired
    private GroupRepository dao;

    @Test
    void findGroupsWithLessOrEqualStudentsTest() {
        int studentCount = 30;

        try {
            List<Group> groupList = dao.findGroupsWithLessOrEqualStudents(studentCount);

            assertNotNull(groupList);
        } catch (SQLException e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    void findGroupsAssignedToCourseByNameTest() {
        String name = "Math";

        try {
            List<Group> groupList = dao.findGroupsAssignedToCourseByName(name);

            assertNotNull(groupList);
        } catch (SQLException e) {
            fail("Unexpected Exception");
        }
    }
}
