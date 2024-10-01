package vadym.spring.console.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.FacultyRepository;
import vadym.spring.console.app.dto.FacultyDto;
import vadym.spring.console.app.entity.Faculty;
import vadym.spring.console.app.service.FacultyService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        try {
            logger.info("Faculty with ID {} found", id);
            return facultyRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding faculty with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Faculty> findByName(String name) {
        try {
            logger.info("Faculty with name {} found", name);
            return facultyRepository.findByFacultyName(name);
        } catch (SQLException e) {
            logger.error("Error while finding faculty with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public FacultyDto save(FacultyDto facultyDto) throws RuntimeException {
        Faculty faculty = Faculty.builder()
                .id(facultyDto.getId())
                .facultyName(facultyDto.getName())
                .head(facultyDto.getDescription())
                .build();

        facultyRepository.save(faculty);

        return facultyDto;
    }

    @Override
    public void update(FacultyDto facultyDto) throws RuntimeException {
        Faculty faculty = Faculty.builder()
                .id(facultyDto.getId())
                .facultyName(facultyDto.getName())
                .head(facultyDto.getDescription())
                .build();

        facultyRepository.save(faculty);
    }

    @Override
    public void delete(Long id) throws RuntimeException {
        facultyRepository.deleteById(id);
    }
}


