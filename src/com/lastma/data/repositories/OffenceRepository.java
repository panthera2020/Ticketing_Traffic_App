package com.lastma.data.repositories;

import com.lastma.data.models.Offence;

import java.util.List;

public interface OffenceRepository {
    Offence save(Offence offence);

    long count();

    Offence findById(int id);

    List<Offence> findAll();

    void delete(int offenceId);

    void deleteAll();

    boolean existById(int offenceId);

}