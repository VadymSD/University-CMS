package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.StudentRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.dto.StudentDto;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.service.StudentService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Student> findByName(String name) throws RuntimeException {
        try {
            logger.info("Student with name {} found", name);
            return studentRepository.findByFirstName(name);
        } catch (SQLException e) {
            logger.error("Error while finding student with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        try {
            logger.info("Student with id {} found", id);
            return studentRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding student with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findAllStudents(String name) throws RuntimeException {
        if(name!= null) {
            try {
                logger.info("Students assigned to group {} found", name);
                return studentRepository.findStudentsAssignedToGroupByName(name);
            } catch (SQLException e) {
                logger.error("Error while finding students assigned to group {}", name, e);
                return Collections.emptyList();
            }
        }

        return studentRepository.findAll();
    }

    @Override
    public List<Student> findAll() throws RuntimeException {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findStudentsAssignedToGroupByName(String name) throws RuntimeException {
        try {
            logger.info("Students assigned to group {} found", name);
            return studentRepository.findStudentsAssignedToGroupByName(name);
        } catch (SQLException e) {
            logger.error("Error while finding students assigned to group {}", name, e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public StudentDto save(StudentDto studentDto) throws RuntimeException {
        studentRepository.save(buildStudent(studentDto));

        return studentDto;
    }

    @Override
    @Transactional
    public void update(StudentDto studentDto) throws RuntimeException {
        studentRepository.save(buildStudent(studentDto));
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Student addStudentToGroup(Long studentId, Long groupId) throws RuntimeException {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Student> student = studentRepository.findById(studentId);

        if(group.isPresent() && student.isPresent()) {
            student.get().setGroup(group.get());
            studentRepository.save(student.get());
            return student.get();
        }

        throw new NotFoundException("Group or student not found");
    }

    @Override
    @Transactional
    public Student removeStudentFromGroup(Long studentId) throws RuntimeException {
        Optional<Student> student = studentRepository.findById(studentId);

        if(student.isPresent()) {
            student.get().setGroup(null);
            studentRepository.save(student.get());
            return student.get();
        }

        throw new NotFoundException("Student not found");
    }

    private Student buildStudent(StudentDto studentDto) {
        Student.StudentBuilder studentBuilder = Student.builder()
                .id(studentDto.getId())
                .firstName(studentDto.getFirstName())
                .surname(studentDto.getSurname());

        if (studentDto.getUserId() != null) {
            Optional<UserEntity> user = userRepository.findById(studentDto.getUserId());
            user.ifPresent(studentBuilder::user);
        }

        if(studentDto.getGroupId() != null) {
            Optional<Group> group = groupRepository.findById(studentDto.getGroupId());
            group.ifPresent(studentBuilder::group);
        }

        return studentBuilder.build();
    }
}
