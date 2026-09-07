package com.github.denconcept.restaurantvoting.vote.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = "restaurant")
    Optional<Vote> findWithRestaurantByUserIdAndVoteDate(Integer userId, LocalDate voteDate);

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate voteDate);

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByUserIdOrderByVoteDateDesc(Integer userId);

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByVoteDate(LocalDate date);
}