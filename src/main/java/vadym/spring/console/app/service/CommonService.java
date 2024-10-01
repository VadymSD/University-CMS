package vadym.spring.console.app.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T, D> {
    Optional<T> findById(Long id);

    Optional<T> findByName(String name);

    List<T> findAll();

    D save(D dto);

    void update(D dto);

    void delete(Long id);
}