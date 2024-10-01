package vadym.spring.console.app.helpers;

import org.springframework.stereotype.Component;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.dto.LectureDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Lecture;
import vadym.spring.console.app.entity.Teacher;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LectureHelper {
    public LectureDto buildLectureDto(Lecture lecture) {
        Set<Long> groupIds = getGroupIds(lecture);

        LectureDto.LectureDtoBuilder lectureDtoBuilder =
                LectureDto.builder()
                .id(lecture.getId())
                .name(lecture.getLectureName())
                .groupIds(groupIds)
                .date(lecture.getDate())
                .startTime(lecture.getStartTime())
                .endTime(lecture.getEndTime())
                .room(lecture.getRoom());

        if(lecture.getCourse() != null) {
            lectureDtoBuilder.courseId(lecture.getCourse().getId());
        }

        if(lecture.getTeacher() != null) {
            lectureDtoBuilder.teacherId(lecture.getTeacher().getId());
        }

        return lectureDtoBuilder.build();
    }

    public Lecture buildLecture(LectureDto lectureDto, GroupRepository groupRepository,
                                CourseRepository courseRepository, TeacherRepository teacherRepository) {

        Set<Group> groups = getGroups(lectureDto, groupRepository);

        Lecture.LectureBuilder lectureBuilder =
                Lecture.builder()
                .id(lectureDto.getId())
                .lectureName(lectureDto.getName())
                .groups(groups)
                .date(lectureDto.getDate())
                .startTime(lectureDto.getStartTime())
                .endTime(lectureDto.getEndTime())
                .room(lectureDto.getRoom());

        if(lectureDto.getCourseId() != null) {
            Optional<Course> course = courseRepository.findById(lectureDto.getCourseId());
            course.ifPresent(lectureBuilder::course);
        }

        if(lectureDto.getTeacherId() != null) {
            Optional<Teacher> teacher = teacherRepository.findById(lectureDto.getTeacherId());
            teacher.ifPresent(lectureBuilder::teacher);
        }

        return lectureBuilder.build();
    }

    private Set<Group> getGroups(LectureDto lectureDto, GroupRepository groupRepository) {
        return lectureDto.getGroupIds().stream()
                .map(groupRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    private Set<Long> getGroupIds(Lecture lecture) {
        return lecture.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toSet());
    }
}
