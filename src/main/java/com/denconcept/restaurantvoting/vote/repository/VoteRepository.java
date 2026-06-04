package com.denconcept.restaurantvoting.vote.repository;

import com.denconcept.restaurantvoting.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate voteDate);
}