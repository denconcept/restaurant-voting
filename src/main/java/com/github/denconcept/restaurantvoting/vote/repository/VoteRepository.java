package com.github.denconcept.restaurantvoting.vote.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminResponseVotingResultTo;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = "restaurant")
    Optional<Vote> findWithRestaurantByUserIdAndVoteDate(Integer userId, LocalDate voteDate);

    Optional<Vote> findByUserIdAndVoteDate(Integer userId, LocalDate voteDate);

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByUserIdOrderByVoteDateDesc(Integer userId);

    @Query("""
            select new com.github.denconcept.restaurantvoting.restaurant.to.AdminResponseVotingResultTo(r.id, r.name, count(v))
            from Vote v
            join v.restaurant r
            where v.voteDate = :date
            group by r.id, r.name
            order by count(v) desc
            """)
    List<AdminResponseVotingResultTo> findVotingResultsByDate(@Param("date") LocalDate date);
}