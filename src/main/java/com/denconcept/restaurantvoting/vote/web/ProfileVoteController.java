package com.denconcept.restaurantvoting.vote.web;

import com.denconcept.restaurantvoting.app.AuthUser;
import com.denconcept.restaurantvoting.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileVoteController {

    public static final String REST_URL = "/api/profile/votes";
    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void vote(@AuthenticationPrincipal AuthUser authUser, @RequestParam Integer restaurantId) {
        voteService.vote(authUser.id(), restaurantId);
    }
}