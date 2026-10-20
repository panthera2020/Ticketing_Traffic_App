package com.lastma.data.repositories;

import com.lastma.data.models.Officer;

import java.util.ArrayList;
import java.util.List;

public class OfficerRepositoryImpl implements OfficerRepository {
    private static List<Officer> officers = new ArrayList<>();
    private int nextId = 0;

    @Override
    public Officer save(Officer officer) {
        if (existById(officer.getId())) {
            update(officer);
            return officer;
        }
        officer.setId(++nextId);
        officers.add(officer);
        return officer;
    }

    private void update(Officer officer) {
        for (int i = 0; i < officers.size(); i++) {
            if (officers.get(i).getId() == officer.getId()) {
                officers.set(i, officer);
                return;
            }
        }
    }

    @Override
    public long count() {
        return officers.size();
    }

    @Override
    public Officer findById(int id) {
        for (Officer officer : officers) {
            if (officer.getId() == id) {
                return officer;
            }
        }
        return null;
    }

    @Override
    public List<Officer> findAll() {
        if (officers.isEmpty()) return null;
        return new ArrayList<>(officers);
    }

    @Override
    public void delete(int id) {
        Officer foundOfficer = findById(id);
        officers.remove(foundOfficer);
    }

    @Override
    public void deleteAll() {
        officers.clear();
        nextId = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Officer officer : officers) {
            if (officer.getId() == id) return true;
        }
        return false;
    }

}
