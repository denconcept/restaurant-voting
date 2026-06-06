package com.denconcept.restaurantvoting.vote.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteController {

    public static final String REST_URL = "/api/profile/votes";
}