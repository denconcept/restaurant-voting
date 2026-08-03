package com.github.denconcept.restaurantvoting.vote.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.vote.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate voteDate);
}