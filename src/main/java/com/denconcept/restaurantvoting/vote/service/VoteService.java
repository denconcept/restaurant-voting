package com.denconcept.restaurantvoting.vote.service;

import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.denconcept.restaurantvoting.user.model.User;
import com.denconcept.restaurantvoting.user.repository.UserRepository;
import com.denconcept.restaurantvoting.vote.model.Vote;
import com.denconcept.restaurantvoting.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

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
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndVoteDate(userId, today);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id = " + restaurantId));
        if (voteOptional.isPresent()) {
            if (!LocalTime.now().isBefore(DEADLINE)) {
                throw new VotingDeadlineExceededException("You can't re-vote after " + DEADLINE);
            }
            Vote vote = voteOptional.get();
            vote.setRestaurant(restaurant);
            voteRepository.save(vote);
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Vote vote = new Vote(today, user, restaurant);
        voteRepository.save(vote);
    }
}