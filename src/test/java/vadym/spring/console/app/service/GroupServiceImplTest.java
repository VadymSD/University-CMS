package vadym.spring.console.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.StudentRepository;
import vadym.spring.console.app.dto.GroupDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.helpers.GroupHelper;
import vadym.spring.console.app.service.impl.GroupServiceImpl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {
    @MockBean
    GroupRepository groupRepository;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    GroupHelper groupHelper;

    @MockBean
    StudentService studentService;

    @MockBean
    CourseService courseService;

    @Autowired
    GroupServiceImpl groupServiceImpl;

    @Test
    void shouldReturnListOfGroupsWithLessOrEqualStudent() {
        List<Group> groups = generateListGroups();
        int numberOfStudents = 2;

        try {
            when(groupRepository.findGroupsWithLessOrEqualStudents(numberOfStudents)).thenReturn(groups);
            List<Group> groupList = groupServiceImpl.findGroupsWithLessOrEqualStudents(numberOfStudents);
            assertEquals(groups, groupList);
            verify(groupRepository).findGroupsWithLessOrEqualStudents(numberOfStudents);
        } catch (SQLException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    void shouldReturnListOfGroupsAssignedToCourse() {
        List<Group> groups = generateListGroups();
        String name = "Math";

        try {
            when(groupRepository.findGroupsAssignedToCourseByName(name)).thenReturn(groups);
            List<Group> groupList = groupServiceImpl.findGroupsAssignedToCourseByName(name);
            assertEquals(groups, groupList);
            verify(groupRepository).findGroupsAssignedToCourseByName(name);
        } catch (SQLException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    void shouldBeUpdated() {
        Group group = getGroupEntity();
        group.setCourses(Collections.singletonList(new Course()));
        group.setStudents(Collections.singletonList(new Student()));

        GroupDto groupDto = GroupDto.builder()
                .id(group.getId())
                .name(group.getGroupName())
                .build();

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(groupHelper.buildGroup(groupDto, courseRepository, studentRepository)).thenReturn(group);

        groupServiceImpl.update(groupDto);
        verify(groupRepository).save(group);
    }

    @Test
    void shouldBeDeleted() {
        Group group = spy(getGroupEntity());
        group.setCourses(Collections.singletonList(new Course()));
        group.setStudents(Collections.singletonList(new Student()));

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));

        groupServiceImpl.delete(group.getId());
        verify(groupRepository).deleteById(group.getId());
    }

    private List<Group> generateListGroups() {
        return IntStream.rangeClosed(1, 5)
                .mapToObj(i -> Group.builder().groupName("AA" + i).build())
                .collect(Collectors.toList());
    }

    private Group getGroupEntity() {
        return Group.builder().id((long) 1).groupName("AA-BB").build();
    }
}
