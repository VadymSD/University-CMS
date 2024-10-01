package vadym.spring.console.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.UniversityRepository;
import vadym.spring.console.app.dto.UniversityDto;
import vadym.spring.console.app.entity.University;
import vadym.spring.console.app.service.UniversityService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;

    Logger logger = LoggerFactory.getLogger(UniversityServiceImpl.class);

    @Autowired
    public UniversityServiceImpl(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Override
    public Optional<University> findById(Long id) {
        try {
            logger.info("University with ID {} found", id);
            return universityRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding university with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<University> findByName(String name) {
        try {
            logger.info("University with name {} found", name);
            return universityRepository.findByUniversityName(name);
        } catch (SQLException e) {
            logger.error("Error while finding university with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<University> findAll() {
        return universityRepository.findAll();
    }

    @Override
    public UniversityDto save(UniversityDto universityDto) throws RuntimeException {
        University university = University.builder()
                .id(universityDto.getId())
                .universityName(universityDto.getName())
                .location(universityDto.getLocation())
                .build();

        universityRepository.save(university);

        return universityDto;
    }

    @Override
    public void update(UniversityDto universityDto) throws RuntimeException {
        University university = University.builder()
                .id(universityDto.getId())
                .universityName(universityDto.getName())
                .location(universityDto.getLocation())
                .build();

        universityRepository.save(university);
    }

    @Override
    public void delete(Long id) throws RuntimeException {
        universityRepository.deleteById(id);
    }
}


