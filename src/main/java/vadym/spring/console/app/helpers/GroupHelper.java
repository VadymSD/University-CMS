package vadym.spring.console.app.helpers;

import org.springframework.stereotype.Component;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.StudentRepository;
import vadym.spring.console.app.dto.GroupDto;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.entity.Group;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GroupHelper {
    public GroupDto buildGroupDto(Group group) {
        Set<Long> studentsIds = getStudentsIds(group);
        Set<Long> coursesIds = getCoursesIds(group);

        return GroupDto.builder()
                .id(group.getId())
                .name(group.getGroupName())
                .coursesIds(coursesIds)
                .studentsIds(studentsIds)
                .build();
    }

    public Group buildGroup(GroupDto groupDto, CourseRepository courseRepository, StudentRepository studentRepository) {
        List<Course> courses = getCourses(groupDto, courseRepository);
        List<Student> students = getStudents(groupDto, studentRepository);

        Group group =  Group.builder()
                .id(groupDto.getId())
                .groupName(groupDto.getName())
                .courses(courses)
                .students(students)
                .build();

        return group;
    }

    private Set<Long> getStudentsIds(Group group) {
        return group.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet());
    }

    private Set<Long> getCoursesIds(Group group) {
        return group.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
    }

    private List<Student> getStudents(GroupDto groupDto, StudentRepository studentRepository) {
        return groupDto.getStudentsIds().stream()
                .map(studentRepository::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    private List<Course> getCourses(GroupDto groupDto, CourseRepository courseRepository) {
        return groupDto.getCoursesIds().stream()
                .map(courseRepository::findById)
                .flatMap(Optional::stream)
                .toList();
    }
}
