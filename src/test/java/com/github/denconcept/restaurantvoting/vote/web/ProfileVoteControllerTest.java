package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import com.github.denconcept.restaurantvoting.vote.to.VoteRequestTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.NOT_FOUND_RESTAURANT_ID;
import static com.github.denconcept.restaurantvoting.user.UserTestData.USER;
import static com.github.denconcept.restaurantvoting.user.UserTestData.USER_MAIL;
import static com.github.denconcept.restaurantvoting.vote.web.ProfileVoteController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(USER_MAIL)
    void vote() throws Exception {
        VoteRequestTo request = new VoteRequestTo(MCDONALDS.getId());
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote vote = voteRepository.findByUserIdAndVoteDate(USER.getId(), LocalDate.now()).orElseThrow();
        assertEquals(MCDONALDS.getId(), vote.getRestaurant().getId());
        assertEquals(USER.getId(), vote.getUser().getId());
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
}