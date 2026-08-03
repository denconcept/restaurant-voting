package com.github.denconcept.restaurantvoting.vote.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.NOT_FOUND_RESTAURANT_ID;
import static com.github.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.TODAY;
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
        mockMvc.perform(post(REST_URL).param("restaurantId", String.valueOf(MCDONALDS.getId())))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote vote = voteRepository.findByUserIdAndVoteDate(USER.getId(), TODAY).orElseThrow();
        assertEquals(MCDONALDS.getId(), vote.getRestaurant().getId());
        assertEquals(USER.getId(), vote.getUser().getId());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void voteForNotFoundRestaurant() throws Exception {
        mockMvc.perform(post(REST_URL).param("restaurantId", String.valueOf(NOT_FOUND_RESTAURANT_ID)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void voteUnauthorized() throws Exception {
        mockMvc.perform(post(REST_URL).param("restaurantId", String.valueOf(MCDONALDS.getId())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}