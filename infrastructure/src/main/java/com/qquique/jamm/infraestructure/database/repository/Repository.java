package com.qquique.jamm.infrastructure.database.repository;

import java.util.Optional;
import java.util.List;

public interface Repository<T> {
    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T model);

    void deleteById(Long id);
}

