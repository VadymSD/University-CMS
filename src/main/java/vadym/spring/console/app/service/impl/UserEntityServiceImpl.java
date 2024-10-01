package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.RoleRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.dto.UserDto;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.service.UserEntityService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(UserEntityServiceImpl.class);

    @Autowired
    public UserEntityServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<UserEntity> findByName(String name) throws RuntimeException {
        try {
            logger.info("UserEntity with name {} found", name);
            return userRepository.findByUsername(name);
        } catch (SQLException e) {
            logger.error("Error while finding user with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) throws RuntimeException {
        try {
            logger.info("UserEntity with id {} found", id);
            return userRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding user with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserEntity> findAll() throws RuntimeException {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> findAllUnassignedUsers() {
        return userRepository.findAll().stream()
                .filter(userEntity -> userEntity.getStudent() == null && userEntity.getTeacher() == null)
                .toList();
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) throws RuntimeException {
        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(RuntimeException::new);

        UserEntity user = UserEntity.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(role)
                .build();

        userRepository.save(user);

        return userDto;
    }

    @Override
    @Transactional
    public void update(UserDto userDto) throws RuntimeException {
        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(RuntimeException::new);

        UserEntity user = UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(role)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        userRepository.deleteById(id);
    }
}
