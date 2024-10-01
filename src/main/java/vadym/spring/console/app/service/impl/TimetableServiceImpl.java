package vadym.spring.console.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.TimetableRepository;
import vadym.spring.console.app.dto.TimetableDto;
import vadym.spring.console.app.entity.Timetable;
import vadym.spring.console.app.service.TimetableService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository timetableRepository;

    Logger logger = LoggerFactory.getLogger(TimetableServiceImpl.class);

    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    @Override
    public Optional<Timetable> findById(Long id) {
        try {
            logger.info("Timetable with ID {} found", id);
            return timetableRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding timetable with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Timetable> findByName(String name) {
        try {
            logger.info("Timetable with name {} found", name);
            return timetableRepository.findByTimetableName(name);
        } catch (SQLException e) {
            logger.error("Error while finding timetable with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Timetable> findAll() {
        return timetableRepository.findAll();
    }

    @Override
    public TimetableDto save(TimetableDto timetableDto) throws RuntimeException {
        Timetable timetable = Timetable.builder()
                .id(timetableDto.getId())
                .timetableName(timetableDto.getName())
                .type(timetableDto.getType())
                .startDate(timetableDto.getStartDate())
                .build();

        timetableRepository.save(timetable);

        return timetableDto;
    }

    @Override
    public void update(TimetableDto timetableDto) throws RuntimeException {
        Timetable timetable = Timetable.builder()
                .id(timetableDto.getId())
                .timetableName(timetableDto.getName())
                .type(timetableDto.getType())
                .startDate(timetableDto.getStartDate())
                .build();

        timetableRepository.save(timetable);
    }

    @Override
    public void delete(Long id) throws RuntimeException {
        timetableRepository.deleteById(id);
    }
}


