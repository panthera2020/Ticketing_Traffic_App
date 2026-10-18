package com.lastma.data.repositories;

import com.lastma.data.models.Challenge;

import java.util.List;

public interface ChallengeRepository {
    Challenge save(Challenge challenge);

    long count();

    Challenge findById(int id);

    List<Challenge> findAll();

    void delete(int challengeId);

    void deleteAll();

    boolean existById(int challengeId);

}