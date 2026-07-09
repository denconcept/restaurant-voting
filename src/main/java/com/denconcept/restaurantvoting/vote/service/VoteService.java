package com.denconcept.restaurantvoting.vote.service;

import com.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.denconcept.restaurantvoting.user.model.User;
import com.denconcept.restaurantvoting.user.repository.UserRepository;
import com.denconcept.restaurantvoting.vote.model.Vote;
import com.denconcept.restaurantvoting.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private static final LocalTime DEADLINE = LocalTime.of(11, 0);
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void vote(Integer userId, Integer restaurantId) {
        LocalDate today = LocalDate.now();
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Optional<Vote> existingVote = voteRepository.findByUserIdAndVoteDate(userId, today);
        if (existingVote.isPresent()) {
            checkVotingDeadline();
            existingVote.get().setRestaurant(restaurant);
            log.info("User {} changed vote to restaurant {}", userId, restaurantId);
            return;
        }
        User user = userRepository.getExisted(userId);
        Vote vote = new Vote(today, user, restaurant);
        voteRepository.save(vote);
        log.info("User {} voted for restaurant {}", userId, restaurantId);
    }

    private void checkVotingDeadline() {
        if (!LocalTime.now().isBefore(DEADLINE)) {
            log.warn("Attempt to change vote after {}", DEADLINE);
            throw new VotingDeadlineExceededException("You can't re-vote after " + DEADLINE);
        }
    }
}