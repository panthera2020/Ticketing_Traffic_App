package com.lastma.data.repositories;

import com.lastma.data.models.Challenge;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRepositoryImpl implements ChallengeRepository {
    private List<Challenge> challenges = new ArrayList<>();
    private int numberOfChallenges = 0;

    @Override
    public Challenge save(Challenge challenge) {
        if (existById(challenge.getId())) {
            update(challenge);
            return challenge;
        }
        challenge.setId(++numberOfChallenges);
        challenges.add(challenge);
        return challenge;
    }

    @Override
    public long count() {
        return challenges.size();
    }

    @Override
    public Challenge findById(int id) {
        for (Challenge challenge : challenges) {
            if (challenge.getId() == id) {
                return challenge;
            }
        }
        throw new IllegalArgumentException("Challenge with id " + id + " not found");
    }

    private void update(Challenge challenge) {
        for (int index = 0; index < challenges.size(); index++) {
            if (challenges.get(index).getId() == challenge.getId()) {
                challenges.set(index, challenge);
                break;
            }
        }
    }

    @Override
    public List<Challenge> findAll() {
        if (count() == 0) throw new IllegalArgumentException("No Challenges saved");
        return challenges;
    }

    @Override
    public void delete(int id) {
        Challenge foundChallenge = findById(id);
        challenges.remove(foundChallenge);
    }

    @Override
    public void deleteAll() {
        challenges.clear();
        numberOfChallenges = 0;
    }

    @Override
    public boolean existById(int id) {
        for (Challenge challenge : challenges) {
            if (challenge.getId() == id) {
                return true;
            }
        }
        return false;
    }
}