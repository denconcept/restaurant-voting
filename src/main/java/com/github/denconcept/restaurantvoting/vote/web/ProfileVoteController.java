package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.app.AuthUser;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import com.github.denconcept.restaurantvoting.vote.service.VoteService;
import com.github.denconcept.restaurantvoting.vote.to.VoteTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.github.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.TODAY;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileVoteController {

    public static final String REST_URL = "/api/profile/votes";
    private final VoteService voteService;
    private final VoteRepository voteRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurantId) {
        voteService.vote(authUser.id(), restaurantId);
    }

    @GetMapping
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser) {
        Vote vote = voteRepository.findByUserIdAndVoteDate(authUser.id(), TODAY)
                .orElseThrow(() -> new NotFoundException("Today's vote not found"));
        Restaurant restaurant = vote.getRestaurant();
        return new VoteTo(vote.getVoteDate(), restaurant.getId(), restaurant.getName());
    }
}