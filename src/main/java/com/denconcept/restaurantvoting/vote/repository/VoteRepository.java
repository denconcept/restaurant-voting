package com.denconcept.restaurantvoting.vote.repository;

import com.denconcept.restaurantvoting.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
