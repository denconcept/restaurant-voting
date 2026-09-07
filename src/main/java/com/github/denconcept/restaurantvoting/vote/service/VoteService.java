package com.github.denconcept.restaurantvoting.vote.service;

import com.github.denconcept.restaurantvoting.common.error.IllegalRequestDataException;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminResponseVotingResultTo;
import com.github.denconcept.restaurantvoting.user.model.User;
import com.github.denconcept.restaurantvoting.user.repository.UserRepository;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import com.github.denconcept.restaurantvoting.vote.to.VoteTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private static final LocalTime DEADLINE = LocalTime.of(11, 0);
    private final Clock clock;

    @Transactional
    public void vote(Integer userId, Integer restaurantId) {
        LocalDate today = LocalDate.now(clock);
        Optional<Vote> existingVote = voteRepository.findByUserIdAndVoteDate(userId, today);
        if (existingVote.isPresent()) {
            throw new IllegalRequestDataException("Today's vote already exists");
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalRequestDataException("Restaurant not found"));
        User user = userRepository.getReferenceById(userId);
        Vote vote = new Vote(today, user, restaurant);
        voteRepository.save(vote);
        log.info("User {} voted for restaurant {}", userId, restaurantId);
    }

    @Transactional(readOnly = true)
    public Optional<VoteTo> getByDate(Integer userId, LocalDate date) {
        return voteRepository.findWithRestaurantByUserIdAndVoteDate(userId, date)
                .map(vote -> {
                    Restaurant restaurant = vote.getRestaurant();
                    return new VoteTo(
                            vote.getVoteDate(),
                            restaurant.getId(),
                            restaurant.getName()
                    );
                });
    }

    @Transactional(readOnly = true)
    public List<VoteTo> getAll(Integer userId) {
        return voteRepository.findAllByUserIdOrderByVoteDateDesc(userId).stream()
                .map(this::createTo)
                .toList();
    }

    @Transactional
    public void revote(Integer userId, Integer restaurantId) {
        LocalDateTime now = LocalDateTime.now(clock);
        Vote existingVote = voteRepository.findByUserIdAndVoteDate(userId, now.toLocalDate())
                .orElseThrow(() -> new NotFoundException("Today's vote not found"));
        checkVotingDeadline(now.toLocalTime());
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalRequestDataException("Restaurant not found"));
        existingVote.setRestaurant(restaurant);
        log.info("User {} changed vote to restaurant {}", userId, restaurantId);
    }

    @Transactional(readOnly = true)
    public List<AdminResponseVotingResultTo> getResultsByDate(LocalDate date) {
        log.info("Get voting results for date {}", date);
        return voteRepository.findVotingResultsByDate(date);
    }

    private VoteTo createTo(Vote vote) {
        Restaurant restaurant = vote.getRestaurant();
        return new VoteTo(vote.getVoteDate(), restaurant.getId(), restaurant.getName());
    }

    private void checkVotingDeadline(LocalTime currentTime) {
        if (!currentTime.isBefore(DEADLINE)) {
            log.warn("Attempt to change vote after {}", DEADLINE);
            throw new VotingDeadlineExceededException("You can't re-vote after " + DEADLINE);
        }
    }
}