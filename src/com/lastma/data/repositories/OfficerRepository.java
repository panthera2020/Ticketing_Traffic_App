package com.lastma.data.repositories;

import com.lastma.data.models.Officer;

import java.util.List;

public interface OfficerRepository {
    Officer save(Officer officer);

    long count();

    Officer findById(int id);

    List<Officer> findAll();

    void delete(int officerId);

    void deleteAll();

    boolean existById(int officerId);
}
