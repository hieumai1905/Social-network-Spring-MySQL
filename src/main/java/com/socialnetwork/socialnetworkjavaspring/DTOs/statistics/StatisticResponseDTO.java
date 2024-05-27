package com.socialnetwork.socialnetworkjavaspring.DTOs.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticResponseDTO {
    private Map<Month, Integer> newUserByMonth;
    private Map<Month, Integer> newPostsByMonth;
    private Integer totalUsers;
    private Integer totalPosts;
    private Integer totalReports;
}
