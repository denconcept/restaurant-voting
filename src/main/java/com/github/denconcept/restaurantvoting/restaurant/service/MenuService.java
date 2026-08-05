package com.github.denconcept.restaurantvoting.restaurant.service;

import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminRestaurantMenuVoteTo;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemTo;
import com.github.denconcept.restaurantvoting.restaurant.to.RestaurantMenuTo;
import com.github.denconcept.restaurantvoting.vote.model.Vote;
import com.github.denconcept.restaurantvoting.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    public static final String MENUS_CACHE = "userMenus";
    public static final String ADMIN_MENUS_CACHE = "adminMenus";

    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;

    @Cacheable(MENUS_CACHE)
    @Transactional(readOnly = true)
    public List<RestaurantMenuTo> getRestaurantsWithMenu(LocalDate menuDate) {
        log.info("Get restaurants with menu for {}", menuDate);
        return menuRepository.findAllByMenuDate(menuDate).stream()
                .map(menu -> {
                    Restaurant restaurant = menu.getRestaurant();
                    return new RestaurantMenuTo(restaurant.getId(), restaurant.getName(), toMenuItems(menu));
                })
                .toList();
    }

    @Cacheable(ADMIN_MENUS_CACHE)
    @Transactional(readOnly = true)
    public List<AdminRestaurantMenuVoteTo> getRestaurantsWithMenuAndVotes(LocalDate menuDate) {
        log.info("Get restaurants with menu and votes for {}", menuDate);
        List<Vote> votes = voteRepository.findAllByVoteDate(menuDate);
        Map<Integer, Long> voteCounts = votes.stream()
                .collect(Collectors.groupingBy(vote -> vote.getRestaurant().getId(), Collectors.counting()));
        return menuRepository.findAllByMenuDate(menuDate).stream()
                .map(menu -> {
                    Restaurant restaurant = menu.getRestaurant();
                    return new AdminRestaurantMenuVoteTo(restaurant.getId(), restaurant.getName(),
                            voteCounts.getOrDefault(restaurant.getId(), 0L), menu.getId(), toMenuItems(menu));
                })
                .toList();
    }

    private List<MenuItemTo> toMenuItems(Menu menu) {
        return menu.getMenuItems().stream()
                .map(item -> new MenuItemTo(item.getName(), item.getPrice()))
                .toList();
    }
}