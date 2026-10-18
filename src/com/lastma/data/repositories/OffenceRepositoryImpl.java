package com.lastma.data.repositories;

import com.lastma.data.models.Offence;

import java.util.ArrayList;
import java.util.List;

public class OffenceRepositoryImpl implements OffenceRepository {
    private List<Offence> offences = new ArrayList<>();
    private int numberOfOffences = 0;

    @Override
    public Offence save(Offence offence) {
        if (existById(offence.getId())) {
            update(offence);
            return offence;
        }
        offence.setId(++numberOfOffences);
        offences.add(offence);
        return offence;
    }

    @Override
    public long count() {
        return offences.size();
    }

    @Override
    public Offence findById(int id) {
        for (Offence offence : offences) {
            if (offence.getId() == id) {
                return offence;
            }
        }
        throw new IllegalArgumentException("Offence with id " + id + " not found");
    }

    private void update(Offence offence) {
        for (int index = 0; index < offences.size(); index++) {
            if (offences.get(index).getId() == offence.getId()) {
                offences.set(index, offence);
                break;
            }
        }
    }

    @Override
    public List<Offence> findAll() {
        if (count() == 0) throw new IllegalArgumentException("No Offences saved");
        return offences;
    }

    @Override
    public void delete(int id) {
        Offence foundOffence = findById(id);
        offences.remove(foundOffence);
    }

    @Override
    public void deleteAll() {
        offences.clear();
        numberOfOffences = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Offence offence : offences) {
            if (offence.getId() == id) {
                return true;
            }
        }
        return false;
    }
}