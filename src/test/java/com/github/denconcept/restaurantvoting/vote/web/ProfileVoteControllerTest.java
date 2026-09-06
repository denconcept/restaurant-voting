package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import com.github.denconcept.restaurantvoting.vote.to.VoteRequestTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.*;
import static com.github.denconcept.restaurantvoting.user.UserTestData.USER;
import static com.github.denconcept.restaurantvoting.user.UserTestData.USER_MAIL;
import static com.github.denconcept.restaurantvoting.vote.web.ProfileVoteController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        mockCurrentDateTime(NOW_BEFORE_DEADLINE);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void vote() throws Exception {
        performFirstVote();
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void revoteBeforeDeadline() throws Exception {
        performFirstVote();
        VoteRequestTo secondRequest = new VoteRequestTo(BURGER_KING.getId());
        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Vote vote = voteRepository.findByUserIdAndVoteDate(
                        USER.getId(),
                        NOW_BEFORE_DEADLINE.toLocalDate())
                .orElseThrow();
        assertEquals(BURGER_KING.getId(), vote.getRestaurant().getId());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void revoteAfterDeadline() throws Exception {
        mockCurrentDateTime(NOW_AFTER_DEADLINE);
        performFirstVote();
        VoteRequestTo secondRequest = new VoteRequestTo(BURGER_KING.getId());
        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        Vote vote = voteRepository.findByUserIdAndVoteDate(
                        USER.getId(),
                        NOW_AFTER_DEADLINE.toLocalDate())
                .orElseThrow();
        assertEquals(MCDONALDS.getId(), vote.getRestaurant().getId());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void voteForNotFoundRestaurant() throws Exception {
        VoteRequestTo request = new VoteRequestTo(NOT_FOUND_RESTAURANT_ID);
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void voteUnauthorized() throws Exception {
        mockMvc.perform(post(REST_URL).param("restaurantId", String.valueOf(MCDONALDS.getId())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private void performFirstVote() throws Exception {
        VoteRequestTo request = new VoteRequestTo(MCDONALDS.getId());
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote vote = voteRepository.findByUserIdAndVoteDate(USER.getId(), LocalDate.now(clock)).orElseThrow();
        assertEquals(MCDONALDS.getId(), vote.getRestaurant().getId());
        assertEquals(USER.getId(), vote.getUser().getId());
    }

    private void mockCurrentDateTime(LocalDateTime dateTime) {
        ZoneId zone = ZoneId.systemDefault();
        given(clock.instant()).willReturn(dateTime.atZone(zone).toInstant());
        given(clock.getZone()).willReturn(zone);
    }
}