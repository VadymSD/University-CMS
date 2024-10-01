package vadym.spring.console.app.helpers;

import org.springframework.stereotype.Component;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.FacultyRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.dto.TeacherDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Faculty;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TeacherHelper {
    public TeacherDto buildTeacherDto(Teacher teacher) {
        Set<Long> coursesIds = getCoursesIds(teacher);
        UserEntity user = teacher.getUser();
        Faculty faculty = teacher.getFaculty();

        TeacherDto.TeacherDtoBuilder builder = TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .surname(teacher.getSurname())
                .coursesIds(coursesIds);

        if (user != null) {
            builder.userId(user.getId());
        }
        if (faculty != null) {
            builder.facultyId(faculty.getId());
        }

        return builder.build();
    }

    public Teacher buildTeacher(TeacherDto teacherDto, CourseRepository courseRepository,
                                FacultyRepository facultyRepository, UserRepository userRepository) {
        List<Course> courses = getCourses(teacherDto, courseRepository);

        Optional<Faculty> faculty = facultyRepository.findById(teacherDto.getFacultyId());
        Optional<UserEntity> user = userRepository.findById(teacherDto.getUserId());

        Teacher.TeacherBuilder teacherBuilder = Teacher.builder()
                .id(teacherDto.getId())
                .firstName(teacherDto.getFirstName())
                .surname(teacherDto.getSurname())
                .courses(courses);

        faculty.ifPresent(teacherBuilder::faculty);

        user.ifPresent(teacherBuilder::user);

        return teacherBuilder.build();
    }

    private Set<Long> getCoursesIds(Teacher teacher) {
        return teacher.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
    }

    private List<Course> getCourses(TeacherDto teacherDto, CourseRepository courseRepository) {
        return teacherDto.getCoursesIds().stream()
                .map(courseRepository::findById)
                .flatMap(Optional::stream)
                .toList();
    }
}
