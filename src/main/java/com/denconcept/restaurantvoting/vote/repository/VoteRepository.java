package com.denconcept.restaurantvoting.vote.repository;

import com.denconcept.restaurantvoting.common.BaseRepository;
import com.denconcept.restaurantvoting.vote.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate voteDate);
}