package com.socialnetwork.socialnetworkjavaspring.services.statistics;

import com.socialnetwork.socialnetworkjavaspring.DTOs.statistics.StatisticResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostInteractRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class StatisticsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IPostInteractRepository postInteractRepository;

    public StatisticResponseDTO getStatisticByYear(Integer year) {
        StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO();
        List<User> users = userRepository.findAll();
        List<Post> posts = postRepository.findAll();
        List<PostInteract> postInteracts = postInteractRepository.findAllByType(InteractType.REPORT);
        statisticResponseDTO.setTotalPosts(posts.size());
        statisticResponseDTO.setTotalUsers(users.size());
        statisticResponseDTO.setTotalReports(postInteracts.size());

        Map<Month, Integer> newUserByMonth =  new TreeMap<>();
        Map<Month, Integer> newPostsByMonth =  new TreeMap<>();
        for (Month month : Month.values()) {
            newUserByMonth.put(month, 0);
            newPostsByMonth.put(month, 0);
        }

        for (User user : users) {
            LocalDate registerDate = user.getRegisterAt().toInstant()
                    .atZone(ZoneId.of(DateTimeUtils.TIMEZONE_VN.getID())).toLocalDate();
            if (registerDate.getYear() == year) {
                Month month = registerDate.getMonth();
                newUserByMonth.put(month, newUserByMonth.getOrDefault(month, 0) + 1);
            }
        }

        for (Post post : posts) {
            LocalDate postDate = post.getCreateAt().toInstant()
                    .atZone(ZoneId.of(DateTimeUtils.TIMEZONE_VN.getID())).toLocalDate();
            if (postDate.getYear() == year) {
                Month month = postDate.getMonth();
                newPostsByMonth.put(month, newPostsByMonth.getOrDefault(month, 0) + 1);
            }
        }

        statisticResponseDTO.setNewUserByMonth(newUserByMonth);
        statisticResponseDTO.setNewPostsByMonth(newPostsByMonth);

        return statisticResponseDTO;
    }

}
