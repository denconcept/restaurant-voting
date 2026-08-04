package com.github.denconcept.restaurantvoting.vote.service;

import com.github.denconcept.restaurantvoting.common.error.IllegalRequestDataException;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.user.model.User;
import com.github.denconcept.restaurantvoting.user.repository.UserRepository;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import com.github.denconcept.restaurantvoting.vote.to.VoteTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.TODAY;

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
        Optional<Vote> existingVote = voteRepository.findByUserIdAndVoteDate(userId, TODAY);
        if (existingVote.isPresent()) {
            throw new IllegalRequestDataException("Today's vote already exists");
        }
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        User user = userRepository.getExisted(userId);
        Vote vote = new Vote(TODAY, user, restaurant);
        voteRepository.save(vote);
        log.info("User {} voted for restaurant {}", userId, restaurantId);
    }

    @Transactional(readOnly = true)
    public VoteTo getTodayVote(Integer userId) {
        Vote vote = voteRepository.findByUserIdAndVoteDate(userId, TODAY)
                .orElseThrow(() -> new NotFoundException("Today's vote not found"));
        Restaurant restaurant = vote.getRestaurant();
        return new VoteTo(vote.getVoteDate(), restaurant.getId(), restaurant.getName());
    }

    @Transactional(readOnly = true)
    public List<VoteTo> getAll(Integer userId) {
        return voteRepository.findAllByUserIdOrderByVoteDateDesc(userId).stream()
                .map(vote -> new VoteTo(vote.getVoteDate(), vote.getRestaurant().getId(), vote.getRestaurant().getName()))
                .toList();
    }

    @Transactional
    public void revote(Integer userId, Integer restaurantId) {
        Vote existingVote = voteRepository.findByUserIdAndVoteDate(userId, TODAY)
                .orElseThrow(() -> new NotFoundException("Today's vote not found"));
        checkVotingDeadline();
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        existingVote.setRestaurant(restaurant);
        log.info("User {} changed vote to restaurant {}", userId, restaurantId);
    }

    private void checkVotingDeadline() {
        if (!LocalTime.now().isBefore(DEADLINE)) {
            log.warn("Attempt to change vote after {}", DEADLINE);
            throw new VotingDeadlineExceededException("You can't re-vote after " + DEADLINE);
        }
    }
}