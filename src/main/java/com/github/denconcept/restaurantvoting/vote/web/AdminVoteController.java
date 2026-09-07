package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.restaurant.to.AdminResponseVotingResultTo;
import com.github.denconcept.restaurantvoting.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminVoteController {

    public static final String REST_URL = "/api/admin/voting-results";
    private final VoteService voteService;
    private final Clock clock;

    @GetMapping
    public List<AdminResponseVotingResultTo> getTodayResults() {
        return voteService.getResultsByDate(LocalDate.now(clock));
    }
}