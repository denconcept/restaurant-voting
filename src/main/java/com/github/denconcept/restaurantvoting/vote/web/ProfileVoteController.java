package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.app.AuthUser;
import com.github.denconcept.restaurantvoting.vote.service.VoteService;
import com.github.denconcept.restaurantvoting.vote.to.VoteRequestTo;
import com.github.denconcept.restaurantvoting.vote.to.VoteTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileVoteController {

    public static final String REST_URL = "/api/profile/votes";
    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteRequestTo voteRequestTo) {
        voteService.vote(authUser.id(), voteRequestTo.restaurantId());
    }

    @GetMapping("/today")
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser) {
        return voteService.getTodayVote(authUser.id());
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return voteService.getAll(authUser.id());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revote(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteRequestTo voteRequestTo) {
        voteService.revote(authUser.id(), voteRequestTo.restaurantId());
    }
}