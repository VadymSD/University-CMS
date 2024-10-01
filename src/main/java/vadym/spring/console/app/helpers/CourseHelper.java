package vadym.spring.console.app.helpers;

import org.springframework.stereotype.Component;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.dto.CourseDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Teacher;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseHelper {
    public CourseDto buildCourseDto(Course course) {
        Set<Long> groupsIds = getGroupsIds(course);
        Set<Long> teachersIds = getTeachersIds(course);

        return CourseDto.builder()
                .id(course.getId())
                .name(course.getCourseName())
                .description(course.getDescription())
                .groupsIds(groupsIds)
                .teachersIds(teachersIds)
                .build();
    }

    public Course buildCourse(CourseDto courseDto, GroupRepository groupRepository, TeacherRepository teacherRepository) {
        List<Teacher> teachers = getTeachers(courseDto, teacherRepository);
        List<Group> groups = getGroups(courseDto, groupRepository);

        return Course.builder()
                .id(courseDto.getId())
                .courseName(courseDto.getName())
                .description(courseDto.getDescription())
                .groups(groups)
                .teachers(teachers)
                .build();
    }

    private Set<Long> getTeachersIds(Course course) {
       return course.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toSet());
    }

    private Set<Long> getGroupsIds(Course course) {
        return course.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toSet());
    }

    private List<Teacher> getTeachers(CourseDto courseDto, TeacherRepository teacherRepository) {
        return courseDto.getTeachersIds().stream()
                .map(teacherRepository::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    private List<Group> getGroups(CourseDto courseDto, GroupRepository groupRepository) {
        return courseDto.getGroupsIds().stream()
                .map(groupRepository::findById)
                .flatMap(Optional::stream)
                .toList();
    }
}
